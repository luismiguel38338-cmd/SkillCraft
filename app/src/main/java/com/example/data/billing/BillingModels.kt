package com.example.data.billing

enum class BillingEnvironment(val displayName: String, val description: String) {
    TEST_SANDBOX(
        displayName = "Sandbox (Modo de Pruebas)",
        description = "Simula respuestas bancarias y de pasarela sin cargos reales. Ideal para probar validación y estados de error."
    ),
    PRODUCTION_GOOGLE_PLAY(
        displayName = "Google Play Store (Producción)",
        description = "Requiere SKU configurado y publicado en Google Play Console con firma de release y cuenta de comerciante."
    )
}

enum class SubscriptionPlan(
    val id: String,
    val title: String,
    val billingCycleText: String,
    val monthlyEquivalent: String,
    val totalBilledText: String,
    val badge: String?,
    val isRecommended: Boolean,
    val basePriceUsd: Double
) {
    MONTHLY(
        id = "skillcraft_pro_monthly",
        title = "Plan Mensual Flexible",
        billingCycleText = "Facturación mensual",
        monthlyEquivalent = "$14.99 USD",
        totalBilledText = "$14.99 USD facturados cada mes",
        badge = null,
        isRecommended = false,
        basePriceUsd = 14.99
    ),
    ANNUAL(
        id = "skillcraft_pro_annual",
        title = "Plan Anual Pro",
        billingCycleText = "Facturación anual recurrente",
        monthlyEquivalent = "$7.99 USD / mes",
        totalBilledText = "$95.88 USD facturados anualmente",
        badge = "Ahorra 47%",
        isRecommended = true,
        basePriceUsd = 95.88
    )
}

enum class SandboxTestOutcome(val title: String, val description: String) {
    VERIFIED_SUCCESS(
        title = "Aprobación Exitosa Verificada",
        description = "Simula pago aprobado y firma criptográfica válida del servidor."
    ),
    BANK_DECLINED(
        title = "Tarjeta Rechazada por Banco",
        description = "Simula fondos insuficientes o rechazo de emisor para verificar manejo de error."
    ),
    PENDING_CONFIRMATION(
        title = "Confirmación Bancaria Pendiente (2FA / OXXO / SEPA)",
        description = "Simula transacción en espera de validación bancaria externa (NO activa PRO)."
    ),
    USER_CANCELLED(
        title = "Cancelación por el Usuario",
        description = "Simula cuando el usuario cierra la pasarela antes de confirmar el pago."
    ),
    NETWORK_TIMEOUT(
        title = "Fallo de Red / Timeout de Pasarela",
        description = "Simula timeout de conexión para comprobar la robustez ante caídas."
    )
}

sealed class PaymentState {
    object Idle : PaymentState()

    data class Processing(
        val stepTitle: String,
        val stepDetail: String,
        val progress: Float
    ) : PaymentState()

    data class Pending(
        val referenceCode: String,
        val message: String,
        val estimatedHours: Int = 24
    ) : PaymentState()

    data class Success(
        val orderId: String,
        val purchaseToken: String,
        val planTitle: String,
        val purchaseDate: String,
        val expiryDate: String,
        val isRestored: Boolean = false
    ) : PaymentState()

    data class Cancelled(
        val reason: String
    ) : PaymentState()

    data class Error(
        val errorCode: String,
        val errorMessage: String,
        val resolutionAction: String
    ) : PaymentState()
}

data class StoredPurchaseRecord(
    val orderId: String,
    val purchaseToken: String,
    val planId: String,
    val purchaseTimestamp: Long,
    val expiryTimestamp: Long,
    val isValidatedByServer: Boolean,
    val environment: BillingEnvironment
)
