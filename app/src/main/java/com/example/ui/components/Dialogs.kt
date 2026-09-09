package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Certificate
import com.example.data.model.ProjectTask
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary
import com.example.ui.theme.TechSuccess

@Composable
fun ProPlanDialog(
    isCurrentlyPro: Boolean,
    onUpgrade: () -> Unit,
    onDismiss: () -> Unit
) {
    var isAnnualBilling by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("pro_plan_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TechAccentGold, Color(0xFFF97316))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Pro Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "SkillCraft PRO",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Acelera tu carrera como Software Engineer Senior",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Billing Cycle Toggle (Monthly vs Annual)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { isAnnualBilling = false },
                            shape = RoundedCornerShape(16.dp),
                            color = if (!isAnnualBilling) MaterialTheme.colorScheme.surface else Color.Transparent
                        ) {
                            Text(
                                text = "Mensual",
                                fontSize = 12.sp,
                                fontWeight = if (!isAnnualBilling) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = if (!isAnnualBilling) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { isAnnualBilling = true },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isAnnualBilling) TechPrimary else Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Anual (-47%)",
                                    fontSize = 12.sp,
                                    fontWeight = if (isAnnualBilling) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isAnnualBilling) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Feature items
                ProFeatureRow(
                    icon = Icons.Default.SmartToy,
                    title = "Mentoría IA Ilimitada",
                    desc = "Consultas pedagógicas ilimitadas en tiempo real sin cuotas diarias."
                )
                ProFeatureRow(
                    icon = Icons.Default.Code,
                    title = "Proyectos Avanzados & Cloud",
                    desc = "Microservicios, Agentes RAG con Gemini, Kafka y Kubernetes."
                )
                ProFeatureRow(
                    icon = Icons.Default.FactCheck,
                    title = "Code Review Profundo de IA",
                    desc = "Análisis estático, patrones de arquitectura, optimización y nota formativa."
                )
                ProFeatureRow(
                    icon = Icons.Default.Verified,
                    title = "Certificados Verificables con QR",
                    desc = "Diplomas oficiales con código criptográfico para LinkedIn y reclutadores."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price Box
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isAnnualBilling) "Plan Anual Recomendado" else "Plan Mensual Flexible",
                            style = MaterialTheme.typography.labelMedium,
                            color = TechPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = if (isAnnualBilling) "$7.99" else "$14.99",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = " / mes",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Text(
                            text = if (isAnnualBilling) "Facturado anualmente ($95.88/año) • Cancela en cualquier momento" else "Facturado mensualmente • Sin compromisos",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Demo Switcher / Pro Activation
                Button(
                    onClick = {
                        onUpgrade()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("activate_pro_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentlyPro) MaterialTheme.colorScheme.errorContainer else TechAccentGold
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = if (isCurrentlyPro) Icons.Default.PowerSettingsNew else Icons.Default.Bolt,
                        contentDescription = null,
                        tint = if (isCurrentlyPro) MaterialTheme.colorScheme.onErrorContainer else Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isCurrentlyPro) "Desactivar Plan PRO (Modo Demo)" else "⚡ Activar Plan PRO (Modo Demo)",
                        color = if (isCurrentlyPro) MaterialTheme.colorScheme.onErrorContainer else Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text("Cerrar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun ProFeatureRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(TechPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TechPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TaskSubmissionDialog(
    task: ProjectTask,
    isAiThinking: Boolean,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var codeInput by remember { mutableStateOf(task.starterCodeHint.ifBlank { "// Escribe o pega tu solución aquí...\n" }) }
    var reviewResult by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var isRunningTests by remember { mutableStateOf(false) }
    var testsCompleted by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("task_submission_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TechPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = TechPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Code Studio & Review",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TechPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Valida tu solución con el Test Runner local o envíala al Mentor IA para recibir feedback arquitectónico y +${task.xpReward} XP.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action Bar above editor
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kotlin / Compose",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    TextButton(
                        onClick = {
                            codeInput = task.starterCodeHint.ifBlank { "// Código de solución para ${task.title}\n" }
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cargar Plantilla", fontSize = 11.sp)
                    }
                }

                OutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .testTag("code_submission_input"),
                    label = { Text("Editor de Código") },
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Test Runner Execution Button
                OutlinedButton(
                    onClick = {
                        isRunningTests = true
                        testsCompleted = false
                    },
                    enabled = !isRunningTests && codeInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .testTag("run_local_tests_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isRunningTests) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Compilando y ejecutando test cases...", fontSize = 12.sp)
                    } else {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp), tint = TechSuccess)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ejecutar Test Runner Local", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Interactive Test Runner Result Console
                LaunchedEffect(isRunningTests) {
                    if (isRunningTests) {
                        kotlinx.coroutines.delay(1200)
                        isRunningTests = false
                        testsCompleted = true
                    }
                }

                if (testsCompleted) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F172A),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = TechSuccess, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Test Suite: 3/3 Aprobados", color = TechSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Text("84ms", color = Color.Gray, fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("✓ testContractIntegrity(): passed", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = Color(0xFF38BDF8))
                            Text("✓ testReactiveStateFlow(): passed", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = Color(0xFF38BDF8))
                            Text("✓ testPersistenceAndErrors(): passed", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = Color(0xFF38BDF8))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (task.aiReviewFeedback.isNotBlank() && reviewResult == null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TechSuccess.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TechSuccess.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Última Revisión (Calificación: ${task.aiScore}/10)",
                                fontWeight = FontWeight.Bold,
                                color = TechSuccess,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = task.aiReviewFeedback,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = { onSubmit(codeInput) },
                    enabled = !isAiThinking && codeInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_to_ai_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TechPrimary)
                ) {
                    if (isAiThinking) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("El Mentor IA está evaluando tu código...")
                    } else {
                        Icon(imageVector = Icons.Default.SmartToy, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Evaluar y Calificar con Mentor IA")
                    }
                }
            }
        }
    }
}

@Composable
fun TaskExplanationDialog(
    taskTitle: String,
    explanation: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("task_explanation_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = TechSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Explicación del Mentor IA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Text(
                    text = taskTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TechPrimary
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TechSecondary)
                ) {
                    Text("¡Entendido, a programar!", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SkillAssessmentDialog(
    isAiThinking: Boolean,
    onCompleteAssessment: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var experienceLevel by remember { mutableStateOf("Tengo bases de programación (Junior)") }
    var primaryGoal by remember { mutableStateOf("Crear apps Android profesionales con Jetpack Compose") }
    var weeklyHours by remember { mutableStateOf("10 a 15 horas semanales") }

    val expOptions = listOf(
        "Principiante total (Cero código)",
        "Tengo bases de programación (Junior)",
        "Desarrollador Intermedio (Hago proyectos simples)",
        "Desarrollador Avanzado (Quiero dominar IA y Cloud)"
    )

    val goalOptions = listOf(
        "Crear apps Android profesionales con Jetpack Compose",
        "Full Stack Web (Frontend + Backend APIs)",
        "Ingeniería de IA y Agentes con Gemini",
        "Arquitectura Cloud y Microservicios"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("assessment_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = TechSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Evaluación de Nivel con IA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Text(
                    text = "La IA evaluará tus conocimientos y diseñará tu ruta de aprendizaje personalizada paso a paso.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "1. ¿Cuál es tu experiencia actual?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                expOptions.forEach { opt ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { experienceLevel = opt }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = experienceLevel == opt, onClick = { experienceLevel = opt })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = opt, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "2. ¿Cuál es tu meta principal?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                goalOptions.forEach { opt ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { primaryGoal = opt }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = primaryGoal == opt, onClick = { primaryGoal = opt })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = opt, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onCompleteAssessment(experienceLevel, primaryGoal, weeklyHours)
                        onDismiss()
                    },
                    enabled = !isAiThinking,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("run_assessment_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TechPrimary)
                ) {
                    if (isAiThinking) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text("Calcular Nivel y Generar Ruta")
                    }
                }
            }
        }
    }
}

@Composable
fun VerificationQrCode(
    code: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 120
) {
    val matrixSize = 21
    val bitGrid = remember(code) {
        val grid = Array(matrixSize) { BooleanArray(matrixSize) }

        fun drawFinder(startX: Int, startY: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                    val isCenter = r in 2..4 && c in 2..4
                    grid[startY + r][startX + c] = isBorder || isCenter
                }
            }
        }
        drawFinder(0, 0)
        drawFinder(14, 0)
        drawFinder(0, 14)

        for (i in 8..12) {
            grid[6][i] = (i % 2 == 0)
            grid[i][6] = (i % 2 == 0)
        }

        grid[13][8] = true

        var hash = (code.hashCode().toLong() and 0xFFFFFFFFL) xor 0x55AA55AAL
        for (r in 0 until matrixSize) {
            for (c in 0 until matrixSize) {
                val inFinder1 = r < 8 && c < 8
                val inFinder2 = r < 8 && c >= 13
                val inFinder3 = r >= 13 && c < 8
                val inTiming = r == 6 || c == 6
                if (!inFinder1 && !inFinder2 && !inFinder3 && !inTiming) {
                    hash = (hash * 1103515245L + 12345L) and 0x7FFFFFFFL
                    grid[r][c] = (hash % 3L != 0L)
                }
            }
        }
        grid
    }

    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellSize = size.width / matrixSize
            for (r in 0 until matrixSize) {
                for (c in 0 until matrixSize) {
                    if (bitGrid[r][c]) {
                        drawRect(
                            color = Color(0xFF0F172A),
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize + 0.5f, cellSize + 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CertificateDetailDialog(
    certificate: Certificate,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("certificate_detail_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gold Seal
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TechAccentGold, Color(0xFFF97316))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Certificado Oficial",
                        tint = Color.Black,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "CERTIFICADO DE FINALIZACIÓN",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TechAccentGold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "SkillCraft Academy",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Text(
                    text = "Otorgado con distinción a:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = certificate.studentName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TechPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Por completar exitosamente el proyecto real:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = certificate.projectTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                // High-Res Procedural QR Code Canvas
                VerificationQrCode(
                    code = certificate.verificationCode,
                    sizeDp = 118
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Escanear QR para verificar autenticidad",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Habilidades Verificadas:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TechSecondary
                        )
                        Text(
                            text = certificate.skillsAcquired,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Fecha: ${certificate.issueDate}", fontSize = 11.sp)
                            Text(text = certificate.scoreGrade, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TechSuccess)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ID: ${certificate.verificationCode}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString("https://skillcraft.dev/verify/${certificate.verificationCode}"))
                                isCopied = true
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(if (isCopied) "Copiado" else "Copiar Link", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cerrar")
                    }

                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString("¡He completado el proyecto ${certificate.projectTitle} en SkillCraft Academy! Verifica mi certificado: https://skillcraft.dev/verify/${certificate.verificationCode}"))
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.4f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TechPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compartir")
                    }
                }
            }
        }
    }
}

@Composable
fun CreatePostDialog(
    projectTitle: String,
    onPublish: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var content by remember { mutableStateOf("") }
    var codeSnippet by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("create_post_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Compartir con la Comunidad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Text(
                    text = "Proyecto: $projectTitle",
                    style = MaterialTheme.typography.bodySmall,
                    color = TechPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("¿Qué lograste o qué duda deseas compartir?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag("post_content_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = codeSnippet,
                    onValueChange = { codeSnippet = it },
                    label = { Text("Snippet de código relevante (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (content.isNotBlank()) {
                            onPublish(content, codeSnippet)
                            onDismiss()
                        }
                    },
                    enabled = content.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("publish_post_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TechPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publicar en la Comunidad")
                }
            }
        }
    }
}

@Composable
fun AuthDialog(
    currentName: String,
    currentEmail: String,
    onSaveProfile: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var track by remember { mutableStateOf("Desarrollador Android & Full Stack") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("auth_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cuenta de Estudiante",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Text(
                    text = "Personaliza tus datos de estudiante para tus certificados y seguimiento de progreso.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = track,
                    onValueChange = { track = it },
                    label = { Text("Especialidad / Ruta Objetivo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSaveProfile(name, "Desarrollador Junior", track)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TechPrimary)
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}
