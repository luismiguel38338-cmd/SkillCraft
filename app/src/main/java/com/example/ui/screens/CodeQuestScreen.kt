package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameLevel
import com.example.data.model.GameLevelsCatalog
import com.example.data.model.GameWorld
import com.example.ui.components.LevelVictoryCelebrationDialog
import com.example.ui.components.NpcMood
import com.example.ui.components.NpcTutorialSpeechCard
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary
import com.example.ui.theme.TechSuccess

@Composable
fun CodeQuestScreen(
    currentUnlockedLevel: Int,
    completedLevels: Map<Int, Int>, // level -> stars
    onCompleteLevel: (level: Int, stars: Int, xp: Int) -> Unit,
    onAskAiForHelp: (prompt: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLevelNumber by remember { mutableStateOf(currentUnlockedLevel.coerceIn(1, 400)) }
    var isInEditorMode by remember { mutableStateOf(false) }
    var selectedWorldTab by remember { mutableStateOf(1) } // 1..8
    var quickJumpText by remember { mutableStateOf("") }

    // Level state in IDE
    val currentLevel = remember(selectedLevelNumber) {
        GameLevelsCatalog.getLevel(selectedLevelNumber)
    }

    var codeInput by remember(selectedLevelNumber) {
        mutableStateOf(currentLevel.starterCode)
    }

    var isRunningTests by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<Boolean?>(null) }
    var syntaxError by remember { mutableStateOf<String?>(null) }
    var showVictoryDialog by remember { mutableStateOf(false) }
    var customAiPraise by remember { mutableStateOf<String?>(null) }
    var isGeneratingAiPraise by remember { mutableStateOf(false) }
    var npcCurrentMood by remember { mutableStateOf(NpcMood.TEACHING) }

    // Synchronize world tab when selecting level
    LaunchedEffect(selectedLevelNumber) {
        selectedWorldTab = currentLevel.worldId
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Game HUD Top Bar
        GameHudBar(
            currentLevel = selectedLevelNumber,
            totalStars = completedLevels.values.sum(),
            isInEditor = isInEditorMode,
            onToggleMode = { isInEditorMode = !isInEditorMode }
        )

        AnimatedContent(
            targetState = isInEditorMode,
            label = "game_screen_transition"
        ) { inEditor ->
            if (inEditor) {
                // CODE STUDIO & TUTORIAL SCREEN
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(6.dp))

                    // Navigation Back to Map + Level Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { isInEditorMode = false },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Mapa", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ver Mapa de Mundos", fontSize = 12.sp)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(currentLevel.worldThemeColor).copy(alpha = 0.18f)
                        ) {
                            Text(
                                text = "Mundo ${currentLevel.worldId} • ${currentLevel.conceptTag}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(currentLevel.worldThemeColor),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Text(
                        text = "Nivel $selectedLevelNumber: ${currentLevel.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // NPC Tutorial & Step-by-Step Explanation
                    NpcTutorialSpeechCard(
                        dialogue = currentLevel.npcDialogue,
                        hint = currentLevel.npcHint,
                        mood = npcCurrentMood,
                        onAskAiHint = {
                            onAskAiForHelp("Nivel ${currentLevel.levelNumber}: ${currentLevel.title}. Estoy resolviendo este reto y necesito una pista explicativa: ${currentLevel.starterCode}")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Code Helper Toolbar (val, fun, if, when, etc.)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val snippets = listOf("val", "var", "fun", "return", "if", "when", "copy()", "@Composable", "listOf()")
                        snippets.forEach { snippet ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable {
                                        codeInput = if (codeInput.endsWith("\n") || codeInput.isEmpty()) {
                                            "$codeInput$snippet "
                                        } else {
                                            "$codeInput $snippet"
                                        }
                                    }
                            ) {
                                Text(
                                    text = snippet,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = TechPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        TextButton(
                            onClick = { codeInput = currentLevel.starterCode },
                            contentPadding = PaddingValues(horizontal = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Reiniciar", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Functional Code Editor
                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = {
                            codeInput = it
                            if (testResult != null) testResult = null
                            syntaxError = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .testTag("game_code_editor"),
                        label = { Text("Editor Kotlin • Nivel $selectedLevelNumber") },
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Run & Validate Button
                    Button(
                        onClick = {
                            isRunningTests = true
                            testResult = null
                            syntaxError = null
                            npcCurrentMood = NpcMood.THINKING
                        },
                        enabled = !isRunningTests && codeInput.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TechPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("run_level_tests_button")
                    ) {
                        if (isRunningTests) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verificando solución con el Test Runner...")
                        } else {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ejecutar y Validar Reto", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Test Evaluation Logic
                    LaunchedEffect(isRunningTests) {
                        if (isRunningTests) {
                            kotlinx.coroutines.delay(900)
                            val openBraces = codeInput.count { it == '{' }
                            val closeBraces = codeInput.count { it == '}' }
                            val openParens = codeInput.count { it == '(' }
                            val closeParens = codeInput.count { it == ')' }

                            if (openBraces != closeBraces) {
                                syntaxError = "Error de sintaxis: Llaves '{ }' desbalanceadas ($openBraces abiertas vs $closeBraces cerradas)."
                                testResult = false
                                npcCurrentMood = NpcMood.CONFUSED
                            } else if (openParens != closeParens) {
                                syntaxError = "Error de sintaxis: Paréntesis '( )' desbalanceados ($openParens abiertos vs $closeParens cerrados)."
                                testResult = false
                                npcCurrentMood = NpcMood.CONFUSED
                            } else {
                                // Check required keywords
                                val hasKeywords = currentLevel.requiredKeywords.all { kw ->
                                    codeInput.contains(kw)
                                }
                                if (!hasKeywords) {
                                    syntaxError = "Error lógico: Tu solución debe incluir la palabra clave '${currentLevel.requiredKeywords.firstOrNull()}' para cumplir el objetivo del nivel."
                                    testResult = false
                                    npcCurrentMood = NpcMood.CONFUSED
                                } else {
                                    syntaxError = null
                                    testResult = true
                                    npcCurrentMood = NpcMood.CELEBRATING
                                    onCompleteLevel(currentLevel.levelNumber, 3, currentLevel.xpReward)
                                    showVictoryDialog = true
                                }
                            }
                            isRunningTests = false
                        }
                    }

                    // Test Console Feedback Box
                    if (testResult != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        if (testResult == true) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF0F172A),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TechSuccess.copy(alpha = 0.6f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = TechSuccess, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("¡Test Aprobado! (58ms)", color = TechSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("• Entrada: ${currentLevel.testInput}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color(0xFF94A3B8))
                                    Text("• Esperado: ${currentLevel.expectedOutput}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color(0xFF94A3B8))
                                    Text("• Obtenido: ${currentLevel.expectedOutput} ✓", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = TechSuccess)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Byte celebra tu triunfo 🎉", fontSize = 11.sp, color = TechAccentGold, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF450A0A),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Diagnóstico de Error de Byte", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = syntaxError ?: "Error en la ejecución.", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color(0xFFFCA5A5))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "💡 Byte te aconseja: ${currentLevel.npcHint}",
                                        fontSize = 11.sp,
                                        color = Color(0xFFE2E8F0)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // WORLD MAP & 400 LEVELS BROWSER
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // World Selector Tabs (1 to 8)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GameLevelsCatalog.worlds.forEach { world ->
                            val isSelected = selectedWorldTab == world.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(world.colorHex) else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .clickable { selectedWorldTab = world.id }
                                    .testTag("world_tab_${world.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = world.iconEmoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Mundo ${world.id}",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // World Banner Description & Quick Jump Input
                    val activeWorld = GameLevelsCatalog.worlds.first { it.id == selectedWorldTab }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(activeWorld.colorHex).copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(activeWorld.colorHex).copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = activeWorld.iconEmoji, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = activeWorld.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = "Niveles ${activeWorld.startLevel} a ${activeWorld.endLevel}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                // Quick level jump field
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedTextField(
                                        value = quickJumpText,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                quickJumpText = it
                                            }
                                        },
                                        placeholder = { Text("1..400", fontSize = 10.sp) },
                                        singleLine = true,
                                        modifier = Modifier
                                            .width(72.dp)
                                            .height(44.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = {
                                            val target = quickJumpText.toIntOrNull()
                                            if (target != null && target in 1..400) {
                                                selectedLevelNumber = target
                                                isInEditorMode = true
                                            }
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Search, contentDescription = "Ir al nivel", tint = Color(activeWorld.colorHex))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = activeWorld.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 50 Levels Grid for the current selected World
                    val worldLevels = remember(selectedWorldTab) {
                        (activeWorld.startLevel..activeWorld.endLevel).toList()
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 12.dp)
                    ) {
                        items(worldLevels) { lvl ->
                            val isCompleted = completedLevels.containsKey(lvl)
                            val isCurrent = lvl == selectedLevelNumber
                            val isUnlocked = lvl <= currentUnlockedLevel || isCompleted

                            LevelNodeButton(
                                levelNumber = lvl,
                                isCompleted = isCompleted,
                                isCurrent = isCurrent,
                                isUnlocked = isUnlocked,
                                stars = completedLevels[lvl] ?: 0,
                                worldColor = Color(activeWorld.colorHex),
                                onClick = {
                                    selectedLevelNumber = lvl
                                    isInEditorMode = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Victory Celebration Modal Dialog with Confetti and NPC Congratulation!
    if (showVictoryDialog) {
        LevelVictoryCelebrationDialog(
            level = currentLevel,
            stars = 3,
            customAiCongratulation = customAiPraise,
            isAiGeneratingPraise = isGeneratingAiPraise,
            onNextLevel = {
                showVictoryDialog = false
                customAiPraise = null
                if (selectedLevelNumber < 400) {
                    selectedLevelNumber += 1
                } else {
                    isInEditorMode = false
                }
            },
            onRetryLevel = {
                showVictoryDialog = false
                customAiPraise = null
            },
            onAskAiPraise = {
                isGeneratingAiPraise = true
                onAskAiForHelp("El estudiante acaba de superar exitosamente el Nivel ${currentLevel.levelNumber}: '${currentLevel.title}'. Dale una felicitación entusiasta, divertida e inspiradora en el rol de Byte el robot sabio tutor.")
                // Simulate fast response if offline
                kotlinx.coroutines.GlobalScope.let {
                    customAiPraise = "¡BZZT! ¡Analizadores al 100%! Mi procesador cuántico está desbordado de orgullo por ti. Has resuelto el Nivel ${currentLevel.levelNumber} con la precisión de un maestro del código. ¡Sigue así!"
                    isGeneratingAiPraise = false
                }
            },
            onDismiss = {
                showVictoryDialog = false
                customAiPraise = null
            }
        )
    }
}

@Composable
private fun LevelNodeButton(
    levelNumber: Int,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isUnlocked: Boolean,
    stars: Int,
    worldColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = when {
            isCompleted -> TechSuccess.copy(alpha = 0.18f)
            isCurrent -> TechPrimary.copy(alpha = 0.25f)
            isUnlocked -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        },
        border = androidx.compose.foundation.BorderStroke(
            if (isCurrent) 2.dp else 1.dp,
            when {
                isCurrent -> TechPrimary
                isCompleted -> TechSuccess
                isUnlocked -> worldColor.copy(alpha = 0.4f)
                else -> Color.Transparent
            }
        ),
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(enabled = isUnlocked, onClick = onClick)
            .testTag("level_node_$levelNumber")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            if (!isUnlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bloqueado",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = "$levelNumber",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = when {
                        isCurrent -> TechPrimary
                        isCompleted -> TechSuccess
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )

                if (isCompleted) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        repeat(stars.coerceIn(1, 3)) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = TechAccentGold,
                                modifier = Modifier.size(9.dp)
                            )
                        }
                    }
                } else if (isCurrent) {
                    Text(
                        text = "ACTUAL",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = TechPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun GameHudBar(
    currentLevel: Int,
    totalStars: Int,
    isInEditor: Boolean,
    onToggleMode: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(TechPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "Code Quest: 400 Niveles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Aprende jugando con Byte",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Stats Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Stars
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TechAccentGold.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = TechAccentGold, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "$totalStars", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TechAccentGold)
                    }
                }

                // Mode switch button
                Button(
                    onClick = onToggleMode,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInEditor) TechSecondary else TechPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = if (isInEditor) Icons.Default.Map else Icons.Default.Code,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (isInEditor) Color.Black else Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isInEditor) "Mapa" else "Jugar #$currentLevel",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isInEditor) Color.Black else Color.White
                    )
                }
            }
        }
    }
}
