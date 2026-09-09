package com.example.data.billing

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class BillingManager {

    private val _billingEnvironment = MutableStateFlow(BillingEnvironment.TEST_SANDBOX)
    val billingEnvironment: StateFlow<BillingEnvironment> = _billingEnvironment.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    private val _selectedPlan = MutableStateFlow(SubscriptionPlan.ANNUAL)
    val selectedPlan: StateFlow<SubscriptionPlan> = _selectedPlan.asStateFlow()

    private val _selectedSandboxOutcome = MutableStateFlow(SandboxTestOutcome.VERIFIED_SUCCESS)
    val selectedSandboxOutcome: StateFlow<SandboxTestOutcome> = _selectedSandboxOutcome.asStateFlow()

    private val _lastVerifiedPurchase = MutableStateFlow<StoredPurchaseRecord?>(null)
    val lastVerifiedPurchase: StateFlow<StoredPurchaseRecord?> = _lastVerifiedPurchase.asStateFlow()

    fun setEnvironment(env: BillingEnvironment) {
        _billingEnvironment.value = env
        _paymentState.value = PaymentState.Idle
    }

    fun selectPlan(plan: SubscriptionPlan) {
        _selectedPlan.value = plan
    }

    fun setSandboxOutcome(outcome: SandboxTestOutcome) {
        _selectedSandboxOutcome.value = outcome
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
    }

    suspend fun startCheckout(): PaymentState {
        val plan = _selectedPlan.value
        val environment = _billingEnvironment.value

        if (environment == BillingEnvironment.PRODUCTION_GOOGLE_PLAY) {
            _paymentState.value = PaymentState.Processing(
                stepTitle = "Conectando con Google Play Billing",
                stepDetail = "Consultando catálogo de productos '${plan.id}' en Google Play Store...",
                progress = 0.35f
            )
            delay(1200)

            // En entorno de ejecución no vinculado a una cuenta de comerciante activa en Google Play Console:
            val errorState = PaymentState.Error(
                errorCode = "PLAY_CONSOLE_CATALOG_UNCONFIGURED",
                errorMessage = "Para procesar compras reales de Google Play Store, debes registrar el SKU '${plan.id}' en Google Play Console (Monetización > Suscripciones) y subir un paquete firmado a una pista de prueba interna o producción.",
                resolutionAction = "Cambia al modo Sandbox para probar el flujo completo con validación y todos los estados de respuesta."
            )
            _paymentState.value = errorState
            return errorState
        }

        // Environment: TEST_SANDBOX
        // Multi-stage realistic processing without handling or storing raw credit card details
        _paymentState.value = PaymentState.Processing(
            stepTitle = "Iniciando Pasarela Segura",
            stepDetail = "Negociando canal seguro TLS 1.3 con pasarela certificada PCI-DSS Nivel 1...",
            progress = 0.25f
        )
        delay(750)

        _paymentState.value = PaymentState.Processing(
            stepTitle = "Procesando Transacción",
            stepDetail = "Comunicando con entidad emisora para la orden del ${plan.title} (${plan.monthlyEquivalent})...",
            progress = 0.60f
        )
        delay(850)

        _paymentState.value = PaymentState.Processing(
            stepTitle = "Verificación Criptográfica",
            stepDetail = "Validando firma digital del token de compra en servidores seguros...",
            progress = 0.88f
        )
        delay(700)

        val outcome = _selectedSandboxOutcome.value
        val finalState: PaymentState = when (outcome) {
            SandboxTestOutcome.VERIFIED_SUCCESS -> {
                val cal = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val purchaseDate = dateFormat.format(cal.time)

                if (plan == SubscriptionPlan.ANNUAL) {
                    cal.add(Calendar.YEAR, 1)
                } else {
                    cal.add(Calendar.MONTH, 1)
                }
                val expiryDate = dateFormat.format(cal.time)
                val orderId = "GPA." + (1000..9999).random() + "-" + (1000..9999).random() + "-" + (10000..99999).random()
                val token = "tok_" + UUID.randomUUID().toString().replace("-", "").take(24)

                val record = StoredPurchaseRecord(
                    orderId = orderId,
                    purchaseToken = token,
                    planId = plan.id,
                    purchaseTimestamp = System.currentTimeMillis(),
                    expiryTimestamp = cal.timeInMillis,
                    isValidatedByServer = true,
                    environment = BillingEnvironment.TEST_SANDBOX
                )
                _lastVerifiedPurchase.value = record

                PaymentState.Success(
                    orderId = orderId,
                    purchaseToken = token,
                    planTitle = plan.title,
                    purchaseDate = purchaseDate,
                    expiryDate = expiryDate,
                    isRestored = false
                )
            }

            SandboxTestOutcome.BANK_DECLINED -> {
                PaymentState.Error(
                    errorCode = "CARD_DECLINED_INSUFFICIENT_FUNDS",
                    errorMessage = "La entidad bancaria emisora rechazó la operación por fondos insuficientes o bloqueo temporal de compras por internet.",
                    resolutionAction = "Consulta con tu banco emisor o prueba con otro método de pago."
                )
            }

            SandboxTestOutcome.PENDING_CONFIRMATION -> {
                PaymentState.Pending(
                    referenceCode = "PND-" + (100000..999999).random(),
                    message = "Tu banco ha puesto la transacción en revisión de seguridad adicional (3DSecure diferido o pago en efectivo). Tu plan PRO NO se desbloqueará hasta que la entidad bancaria emita la confirmación de pago.",
                    estimatedHours = 24
                )
            }

            SandboxTestOutcome.USER_CANCELLED -> {
                PaymentState.Cancelled(
                    reason = "Has cancelado la operación en la ventana de pago antes de confirmar la compra."
                )
            }

            SandboxTestOutcome.NETWORK_TIMEOUT -> {
                PaymentState.Error(
                    errorCode = "HTTP_TIMEOUT_504",
                    errorMessage = "No se pudo establecer conexión con el servidor de autorizaciones de Google Play en el tiempo límite. No se realizó ningún cargo a tu cuenta.",
                    resolutionAction = "Verifica tu conexión de red y reintenta la operación."
                )
            }
        }

        _paymentState.value = finalState
        return finalState
    }

    suspend fun restorePurchases(): PaymentState {
        _paymentState.value = PaymentState.Processing(
            stepTitle = "Restaurando Suscripciones",
            stepDetail = "Consultando el historial de órdenes de compra vinculadas a tu cuenta...",
            progress = 0.5f
        )
        delay(1100)

        val active = _lastVerifiedPurchase.value
        val finalState: PaymentState = if (active != null && active.expiryTimestamp > System.currentTimeMillis()) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val expiry = dateFormat.format(Date(active.expiryTimestamp))
            val purchase = dateFormat.format(Date(active.purchaseTimestamp))
            val plan = if (active.planId == SubscriptionPlan.ANNUAL.id) SubscriptionPlan.ANNUAL else SubscriptionPlan.MONTHLY

            PaymentState.Success(
                orderId = active.orderId,
                purchaseToken = active.purchaseToken,
                planTitle = plan.title,
                purchaseDate = purchase,
                expiryDate = expiry,
                isRestored = true
            )
        } else {
            PaymentState.Error(
                errorCode = "NO_ACTIVE_SUBSCRIPTION_FOUND",
                errorMessage = "No se encontraron suscripciones activas vinculadas a esta cuenta.",
                resolutionAction = "Asegúrate de estar utilizando la misma cuenta de Google con la que realizaste la compra original."
            )
        }

        _paymentState.value = finalState
        return finalState
    }

    fun cancelSubscription() {
        _lastVerifiedPurchase.value = null
        _paymentState.value = PaymentState.Idle
    }

    fun isSubscriptionActive(): Boolean {
        val purchase = _lastVerifiedPurchase.value ?: return false
        return purchase.isValidatedByServer && purchase.expiryTimestamp > System.currentTimeMillis()
    }
}
