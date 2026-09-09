package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.billing.*
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary
import com.example.ui.theme.TechSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProSubscriptionCheckoutDialog(
    isCurrentlyPro: Boolean,
    paymentState: PaymentState,
    billingEnvironment: BillingEnvironment,
    selectedPlan: SubscriptionPlan,
    selectedSandboxOutcome: SandboxTestOutcome,
    onSelectPlan: (SubscriptionPlan) -> Unit,
    onSetEnvironment: (BillingEnvironment) -> Unit,
    onSetSandboxOutcome: (SandboxTestOutcome) -> Unit,
    onStartCheckout: () -> Unit,
    onRestorePurchases: () -> Unit,
    onResetState: () -> Unit,
    onDismiss: () -> Unit
) {
    var showAdvancedSettings by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            if (paymentState !is PaymentState.Processing) {
                onResetState()
                onDismiss()
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .padding(vertical = 12.dp)
                .testTag("pro_subscription_checkout_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (billingEnvironment == BillingEnvironment.TEST_SANDBOX) TechSecondary.copy(alpha = 0.2f) else TechAccentGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = if (billingEnvironment == BillingEnvironment.TEST_SANDBOX) "🧪 MODO SANDBOX" else "🛡️ GOOGLE PLAY PROD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (billingEnvironment == BillingEnvironment.TEST_SANDBOX) TechSecondary else TechAccentGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (paymentState !is PaymentState.Processing) {
                                onResetState()
                                onDismiss()
                            }
                        },
                        enabled = paymentState !is PaymentState.Processing
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                // Pro Badge Header
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(TechAccentGold, Color(0xFFEA580C)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "SkillCraft PRO",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Acelera tu camino a Ingeniero de Software Senior con mentoría personalizada y proyectos de arquitectura.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Plan Selector: Monthly vs Annual
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Monthly Option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(enabled = paymentState !is PaymentState.Processing) {
                                    onSelectPlan(SubscriptionPlan.MONTHLY)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPlan == SubscriptionPlan.MONTHLY) TechPrimary else Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Mensual",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedPlan == SubscriptionPlan.MONTHLY) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$14.99 / mes",
                                    fontSize = 11.sp,
                                    color = if (selectedPlan == SubscriptionPlan.MONTHLY) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Annual Option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(enabled = paymentState !is PaymentState.Processing) {
                                    onSelectPlan(SubscriptionPlan.ANNUAL)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPlan == SubscriptionPlan.ANNUAL) TechPrimary else Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Anual",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedPlan == SubscriptionPlan.ANNUAL) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (selectedPlan == SubscriptionPlan.ANNUAL) TechAccentGold else TechAccentGold.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "-47%",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "$7.99 / mes ($95.88/año)",
                                    fontSize = 11.sp,
                                    color = if (selectedPlan == SubscriptionPlan.ANNUAL) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Value propositions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CheckoutBenefit(
                        icon = Icons.Default.SmartToy,
                        title = "Mentor IA sin restricciones",
                        detail = "Explicación de código, refactorización y depuración con Gemini 3.5 Flash."
                    )
                    CheckoutBenefit(
                        icon = Icons.Default.CloudQueue,
                        title = "Proyectos Enterprise Avanzados",
                        detail = "Microservicios, Agentes RAG, Kafka, Kubernetes y Backend con JWT."
                    )
                    CheckoutBenefit(
                        icon = Icons.Default.FactCheck,
                        title = "Code Review Profundo & Pruebas",
                        detail = "Test runner automatizado con retroalimentación arquitectónica."
                    )
                    CheckoutBenefit(
                        icon = Icons.Default.Verified,
                        title = "Certificados Verificables con QR",
                        detail = "Credenciales oficiales con código criptográfico para compartir en LinkedIn."
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Breakdown & Tax Notice
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal de la suscripción:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                if (selectedPlan == SubscriptionPlan.ANNUAL) "$95.88 USD" else "$14.99 USD",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Impuestos aplicables (IVA/Sales Tax):", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Calculados por Google Play en checkout", fontSize = 11.sp, color = TechPrimary, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total estimado:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = selectedPlan.monthlyEquivalent,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = TechPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Condiciones: ${selectedPlan.totalBilledText}. La suscripción se renovará automáticamente al término del período. Puedes cancelar en cualquier momento con 1 clic en Google Play Store > Pagos y suscripciones sin penalizaciones.",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // PCI-DSS / Security Disclaimer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TechSuccess,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Procesamiento seguro certificado PCI-DSS. Esta aplicación no almacena ni procesa directamente números de tarjetas ni CVV.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Payment Status Area
                when (paymentState) {
                    is PaymentState.Idle -> {
                        Button(
                            onClick = onStartCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("checkout_pay_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TechAccentGold),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Suscribirme a SkillCraft PRO",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    is PaymentState.Processing -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    color = TechPrimary,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = paymentState.stepTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = paymentState.stepDetail,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { paymentState.progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = TechPrimary
                                )
                            }
                        }
                    }

                    is PaymentState.Pending -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFEF3C7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.HourglassTop, contentDescription = null, tint = Color(0xFFB45309))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Pago Pendiente de Aprobación Bancaria",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E),
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = paymentState.message,
                                    fontSize = 11.sp,
                                    color = Color(0xFF78350F)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Código de Referencia: ${paymentState.referenceCode}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                                Text(
                                    text = "🛡️ Estado de Seguridad: El Plan PRO permanecerá bloqueado hasta que el proveedor confirme la liquidación de los fondos.",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = onResetState,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Entendido / Volver al Checkout", color = Color(0xFF92400E))
                                }
                            }
                        }
                    }

                    is PaymentState.Success -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = TechSuccess.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TechSuccess),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = TechSuccess,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (paymentState.isRestored) "¡Suscripción PRO Restaurada!" else "¡Suscripción PRO Confirmada y Activa!",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = TechSuccess,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Orden: ${paymentState.orderId}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Plan: ${paymentState.planTitle} • Válido hasta: ${paymentState.expiryDate}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        onResetState()
                                        onDismiss()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = TechSuccess),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Comenzar a Aprender con PRO", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    is PaymentState.Error -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Error en la Transacción [${paymentState.errorCode}]",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = paymentState.errorMessage,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Acción recomendada: ${paymentState.resolutionAction}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = onStartCheckout,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Reintentar Pago", color = Color.White)
                                }
                            }
                        }
                    }

                    is PaymentState.Cancelled -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Transacción Cancelada",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = paymentState.reason,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(onClick = onResetState) {
                                    Text("Volver al Checkout")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Restore Purchases & Advanced Environment Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onRestorePurchases,
                        enabled = paymentState !is PaymentState.Processing
                    ) {
                        Icon(imageVector = Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Restaurar Compras", fontSize = 11.sp)
                    }

                    TextButton(
                        onClick = { showAdvancedSettings = !showAdvancedSettings }
                    ) {
                        Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (showAdvancedSettings) "Ocultar Entorno" else "Entorno / Sandbox", fontSize = 11.sp)
                    }
                }

                // Advanced Testing & Sandbox Controls
                AnimatedVisibility(visible = showAdvancedSettings) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Configuración del Proveedor de Pagos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TechPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Environment Switcher
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                FilterChip(
                                    selected = billingEnvironment == BillingEnvironment.TEST_SANDBOX,
                                    onClick = { onSetEnvironment(BillingEnvironment.TEST_SANDBOX) },
                                    label = { Text("Sandbox (Pruebas)", fontSize = 10.sp) }
                                )
                                FilterChip(
                                    selected = billingEnvironment == BillingEnvironment.PRODUCTION_GOOGLE_PLAY,
                                    onClick = { onSetEnvironment(BillingEnvironment.PRODUCTION_GOOGLE_PLAY) },
                                    label = { Text("Google Play (Producción)", fontSize = 10.sp) }
                                )
                            }

                            if (billingEnvironment == BillingEnvironment.TEST_SANDBOX) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Simular Respuesta del Proveedor:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    SandboxOutcomeRadio(
                                        selected = selectedSandboxOutcome == SandboxTestOutcome.VERIFIED_SUCCESS,
                                        label = "✅ Éxito Criptográfico Verificado",
                                        onClick = { onSetSandboxOutcome(SandboxTestOutcome.VERIFIED_SUCCESS) }
                                    )
                                    SandboxOutcomeRadio(
                                        selected = selectedSandboxOutcome == SandboxTestOutcome.BANK_DECLINED,
                                        label = "❌ Rechazo Bancario (Fondos / Emisor)",
                                        onClick = { onSetSandboxOutcome(SandboxTestOutcome.BANK_DECLINED) }
                                    )
                                    SandboxOutcomeRadio(
                                        selected = selectedSandboxOutcome == SandboxTestOutcome.PENDING_CONFIRMATION,
                                        label = "⏳ Pendiente de Aprobación (2FA / OXXO)",
                                        onClick = { onSetSandboxOutcome(SandboxTestOutcome.PENDING_CONFIRMATION) }
                                    )
                                    SandboxOutcomeRadio(
                                        selected = selectedSandboxOutcome == SandboxTestOutcome.USER_CANCELLED,
                                        label = "🚫 Cancelación de Usuario",
                                        onClick = { onSetSandboxOutcome(SandboxTestOutcome.USER_CANCELLED) }
                                    )
                                    SandboxOutcomeRadio(
                                        selected = selectedSandboxOutcome == SandboxTestOutcome.NETWORK_TIMEOUT,
                                        label = "⚡ Timeout de Red (Error 504)",
                                        onClick = { onSetSandboxOutcome(SandboxTestOutcome.NETWORK_TIMEOUT) }
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "En modo Producción, el flujo consulta los SKUs de Google Play Console. Si la app no está publicada o falta la cuenta de comerciante, reportará el código de error correspondiente.",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckoutBenefit(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(TechPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = TechPrimary, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SandboxOutcomeRadio(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 10.sp)
    }
}
