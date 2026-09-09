package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary
import com.example.ui.theme.TechSuccess

data class OnboardingStep(
    val stepNumber: Int,
    val tag: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val highlights: List<String>
)

@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit,
    onStartAssessment: () -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }

    val steps = listOf(
        OnboardingStep(
            stepNumber = 1,
            tag = "Metodología Activa",
            title = "Aprende Creando Software Real",
            subtitle = "Olvídate de tutoriales pasivos. En SkillCraft construyes aplicaciones completas de la industria desde el día uno.",
            icon = Icons.Default.Terminal,
            accentColor = TechPrimary,
            highlights = listOf(
                "Proyectos Android con Jetpack Compose & Room",
                "Integración de LLMs y Agentes RAG con Gemini",
                "APIs de Alta Disponibilidad & Microservicios Cloud"
            )
        ),
        OnboardingStep(
            stepNumber = 2,
            tag = "Mentoría 24/7",
            title = "Tutor de IA Contextualizado",
            subtitle = "Tu mentor personal impulsado por los modelos Gemini te guía paso a paso según el código exacto de tu proyecto.",
            icon = Icons.Default.SmartToy,
            accentColor = TechSecondary,
            highlights = listOf(
                "Explicaciones pedagógicas y descomposición de tareas",
                "Code Reviews automáticos con retroalimentación y nota",
                "Depuración de errores y simulación de entrevistas técnicas"
            )
        ),
        OnboardingStep(
            stepNumber = 3,
            tag = "Progresión & Gamificación",
            title = "Rutas Adaptadas a tus Metas",
            subtitle = "Sube de nivel, mantén tu racha diaria de código y desbloquea insignias que demuestran tu evolución.",
            icon = Icons.Default.Timeline,
            accentColor = TechAccentGold,
            highlights = listOf(
                "Diagnóstico inicial de experiencia y disponibilidad",
                "Rachas de fuego diarias y retos de código con XP",
                "Galería de logros y árbol de habilidades interactivo"
            )
        ),
        OnboardingStep(
            stepNumber = 4,
            tag = "Acreditación Oficial",
            title = "Certificados Verificables con QR",
            subtitle = "Al finalizar cada proyecto real, obtén un diploma digital con hash único y código QR listo para tu portafolio.",
            icon = Icons.Default.Verified,
            accentColor = TechSuccess,
            highlights = listOf(
                "Código criptográfico único de verificación online",
                "Desglose de competencias técnicas adquiridas",
                "Compatible para compartir en LinkedIn, GitHub y CV"
            )
        )
    )

    val currentStep = steps[currentStepIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("onboarding_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Skip button & Step Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = currentStep.accentColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = currentStep.tag.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentStep.accentColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("skip_onboarding_button")
                    ) {
                        Text("Omitir", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Animated Hero Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    currentStep.accentColor.copy(alpha = 0.35f),
                                    currentStep.accentColor.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .border(2.dp, currentStep.accentColor.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = currentStep.icon,
                        contentDescription = currentStep.title,
                        tint = currentStep.accentColor,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Title and Subtitle
                Text(
                    text = currentStep.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentStep.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Bullet points
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        currentStep.highlights.forEach { item ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = currentStep.accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Step dots indicator
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.forEachIndexed { index, _ ->
                        val isSelected = index == currentStepIndex
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(6.dp)
                                .width(if (isSelected) 22.dp else 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isSelected) currentStep.accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (currentStepIndex > 0) {
                        OutlinedButton(
                            onClick = { currentStepIndex-- },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Anterior", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Atrás")
                        }
                    }

                    if (currentStepIndex < steps.size - 1) {
                        Button(
                            onClick = { currentStepIndex++ },
                            modifier = Modifier
                                .weight(if (currentStepIndex > 0) 1.5f else 1f)
                                .height(48.dp)
                                .testTag("onboarding_next_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TechPrimary),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Siguiente", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = {
                                onDismiss()
                                onStartAssessment()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("onboarding_finish_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TechSuccess),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Iniciar con Diagnóstico IA", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
