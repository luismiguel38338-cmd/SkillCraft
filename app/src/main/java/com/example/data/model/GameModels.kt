package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_levels_progress")
data class GameLevelProgress(
    @PrimaryKey val levelNumber: Int,
    val stars: Int = 0, // 0 = not completed, 1..3 = completed
    val isUnlocked: Boolean = false,
    val userCode: String = "",
    val completedAt: Long = 0L,
    val attempts: Int = 0
)

data class GameLevel(
    val levelNumber: Int, // 1 to 400+
    val worldId: Int, // 1 to 8
    val worldName: String,
    val worldThemeColor: Long,
    val title: String,
    val conceptTag: String,
    val npcDialogue: String,
    val npcHint: String,
    val npcCelebMessage: String,
    val starterCode: String,
    val requiredKeywords: List<String>,
    val testDescription: String,
    val testInput: String,
    val expectedOutput: String,
    val xpReward: Int = 50,
    val coinsReward: Int = 20,
    val gemsReward: Int = 5
)

data class GameWorld(
    val id: Int,
    val name: String,
    val subtitle: String,
    val iconEmoji: String,
    val startLevel: Int,
    val endLevel: Int,
    val colorHex: Long
)
