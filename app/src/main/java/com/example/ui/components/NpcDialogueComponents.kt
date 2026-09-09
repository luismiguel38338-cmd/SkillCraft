package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GameLevel
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary
import com.example.ui.theme.TechSuccess
import kotlin.random.Random

enum class NpcMood {
    TEACHING,
    THINKING,
    CELEBRATING,
    HINTING,
    CONFUSED
}

@Composable
fun NpcAvatarWidget(
    mood: NpcMood = NpcMood.TEACHING,
    modifier: Modifier = Modifier,
    sizeDp: Int = 64
) {
    val infiniteTransition = rememberInfiniteTransition(label = "npc_anim")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    val eyeBlink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1f at 0
                1f at 2700
                0.1f at 2850
                1f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "blink"
    )

    val antennaGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "antenna"
    )

    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .offset(y = bounceOffset.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Antenna Stem
            drawLine(
                color = Color(0xFF64748B),
                start = Offset(w * 0.5f, h * 0.28f),
                end = Offset(w * 0.5f, h * 0.10f),
                strokeWidth = w * 0.06f
            )

            // Antenna Orb (Glowing)
            val orbColor = when (mood) {
                NpcMood.CELEBRATING -> Color(0xFFFACC15)
                NpcMood.THINKING -> Color(0xFF38BDF8)
                NpcMood.HINTING -> Color(0xFFA855F7)
                NpcMood.CONFUSED -> Color(0xFFEF4444)
                else -> Color(0xFF10B981)
            }
            drawCircle(
                color = orbColor.copy(alpha = antennaGlow),
                radius = w * 0.10f,
                center = Offset(w * 0.5f, h * 0.09f)
            )

            // Robot Head (Curved Rounded Rect)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                ),
                topLeft = Offset(w * 0.15f, h * 0.26f),
                size = Size(w * 0.70f, h * 0.60f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.20f, w * 0.20f)
            )

            // Screen Visor
            drawRoundRect(
                color = Color(0xFF020617),
                topLeft = Offset(w * 0.22f, h * 0.35f),
                size = Size(w * 0.56f, h * 0.35f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.10f, w * 0.10f)
            )

            // Robot Eyes
            val eyeColor = when (mood) {
                NpcMood.CELEBRATING -> Color(0xFF34D399)
                NpcMood.CONFUSED -> Color(0xFFF87171)
                NpcMood.HINTING -> Color(0xFFFBBF24)
                else -> Color(0xFF38BDF8)
            }

            if (mood == NpcMood.CELEBRATING) {
                // Happy Arc Eyes (^^)
                drawArc(
                    color = eyeColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.30f, h * 0.42f),
                    size = Size(w * 0.14f, h * 0.12f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.05f)
                )
                drawArc(
                    color = eyeColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.56f, h * 0.42f),
                    size = Size(w * 0.14f, h * 0.12f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.05f)
                )
            } else {
                // Normal / Blinking Eyes
                val eyeHeight = (h * 0.12f) * eyeBlink
                drawOval(
                    color = eyeColor,
                    topLeft = Offset(w * 0.32f, h * 0.48f - (eyeHeight / 2)),
                    size = Size(w * 0.11f, eyeHeight)
                )
                drawOval(
                    color = eyeColor,
                    topLeft = Offset(w * 0.57f, h * 0.48f - (eyeHeight / 2)),
                    size = Size(w * 0.11f, eyeHeight)
                )
            }

            // Cheerful Smile (Small green or cyan line)
            drawLine(
                color = eyeColor.copy(alpha = 0.8f),
                start = Offset(w * 0.42f, h * 0.61f),
                end = Offset(w * 0.58f, h * 0.61f),
                strokeWidth = w * 0.04f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            // Neck base
            drawRoundRect(
                color = Color(0xFF475569),
                topLeft = Offset(w * 0.38f, h * 0.86f),
                size = Size(w * 0.24f, h * 0.10f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f, w * 0.04f)
            )
        }
    }
}

@Composable
fun NpcTutorialSpeechCard(
    npcName: String = "Byte el Sabio",
    dialogue: String,
    hint: String,
    mood: NpcMood = NpcMood.TEACHING,
    onAskAiHint: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showHint by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            TechPrimary.copy(alpha = 0.35f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("npc_tutorial_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                NpcAvatarWidget(mood = if (showHint) NpcMood.HINTING else mood, sizeDp = 50)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = npcName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TechPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = TechPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "NPC TUTOR",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = TechPrimary,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Explicación del reto paso a paso",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Speech text bubble
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (showHint) "💡 Pista de Byte: $hint" else dialogue,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (showHint) TechAccentGold else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { showHint = !showHint },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (showHint) Icons.Default.MenuBook else Icons.Default.Lightbulb,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = TechAccentGold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showHint) "Ver Tutorial" else "Pista del NPC",
                        fontSize = 12.sp,
                        color = TechAccentGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                OutlinedButton(
                    onClick = onAskAiHint,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = TechSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Preguntar a IA",
                        fontSize = 12.sp,
                        color = TechSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LevelVictoryCelebrationDialog(
    level: GameLevel,
    stars: Int = 3,
    customAiCongratulation: String? = null,
    isAiGeneratingPraise: Boolean = false,
    onNextLevel: () -> Unit,
    onRetryLevel: () -> Unit,
    onAskAiPraise: () -> Unit,
    onDismiss: () -> Unit
) {
    val confettiColors = listOf(
        Color(0xFFFFD700), Color(0xFF38BDF8), Color(0xFF4ADE80),
        Color(0xFFF43F5E), Color(0xFFA855F7), Color(0xFFFB923C)
    )

    // Animated Star Scales
    val star1Scale = remember { Animatable(0f) }
    val star2Scale = remember { Animatable(0f) }
    val star3Scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        star1Scale.animateTo(1.2f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
        star1Scale.animateTo(1f)
        if (stars >= 2) {
            star2Scale.animateTo(1.2f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
            star2Scale.animateTo(1f)
        }
        if (stars >= 3) {
            star3Scale.animateTo(1.2f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
            star3Scale.animateTo(1f)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("level_victory_dialog")
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Confetti Particles Background Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    val random = Random(42)
                    for (i in 0 until 45) {
                        val cx = random.nextFloat() * size.width
                        val cy = random.nextFloat() * size.height
                        val cSize = random.nextFloat() * 8f + 4f
                        val color = confettiColors[random.nextInt(confettiColors.size)]
                        drawCircle(color = color.copy(alpha = 0.85f), radius = cSize / 2, center = Offset(cx, cy))
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(22.dp)
                        .fillMaxWidth()
                ) {
                    // NPC Byte Celebrating Avatar
                    NpcAvatarWidget(mood = NpcMood.CELEBRATING, sizeDp = 72)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "¡NIVEL ${level.levelNumber} COMPLETADO!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TechAccentGold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = level.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3 Stars Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Estrella 1",
                            tint = TechAccentGold,
                            modifier = Modifier
                                .size(36.dp)
                                .scale(star1Scale.value)
                        )
                        Icon(
                            imageVector = if (stars >= 2) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Estrella 2",
                            tint = if (stars >= 2) TechAccentGold else Color.Gray,
                            modifier = Modifier
                                .size(46.dp)
                                .scale(if (stars >= 2) star2Scale.value else 1f)
                        )
                        Icon(
                            imageVector = if (stars >= 3) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Estrella 3",
                            tint = if (stars >= 3) TechAccentGold else Color.Gray,
                            modifier = Modifier
                                .size(36.dp)
                                .scale(if (stars >= 3) star3Scale.value else 1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // NPC Speech Bubble with Enthusiastic Congratulations!
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = TechPrimary.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TechPrimary.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Byte el Sabio dice:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TechPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = customAiCongratulation ?: level.npcCelebMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rewards Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RewardBadge(icon = Icons.Default.FlashOn, value = "+${level.xpReward} XP", color = TechPrimary)
                        RewardBadge(icon = Icons.Default.MonetizationOn, value = "+${level.coinsReward} 🪙", color = TechAccentGold)
                        RewardBadge(icon = Icons.Default.Diamond, value = "+${level.gemsReward} 💎", color = Color(0xFF38BDF8))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Main Action: Siguiente Nivel
                    Button(
                        onClick = onNextLevel,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TechSuccess),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("next_level_button")
                    ) {
                        Text(
                            text = if (level.levelNumber < 400) "¡Siguiente Nivel ${level.levelNumber + 1}! ➔" else "¡HAS CONQUISTADO EL JUEGO!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary actions: Celebrar con IA / Reintentar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onAskAiPraise,
                            enabled = !isAiGeneratingPraise,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isAiGeneratingPraise) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = TechSecondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Felicitar con IA", fontSize = 11.sp, color = TechSecondary)
                            }
                        }

                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Mapa de Niveles", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
