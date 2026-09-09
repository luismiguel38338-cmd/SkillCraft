package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "primary_user",
    val name: String = "Luis Miguel",
    val email: String = "luismiguel@dev.com",
    val title: String = "Desarrollador Junior",
    val level: Int = 3,
    val currentXp: Int = 1250,
    val xpToNextLevel: Int = 2000,
    val streakDays: Int = 5,
    val completedProjectsCount: Int = 2,
    val totalTimeHours: Int = 18,
    val isPro: Boolean = false,
    val targetTrack: String = "Desarrollador Android & Full Stack",
    val assessedSkillLevel: String = "Intermedio (Nivel 3)",
    val avatarUrl: String = ""
)

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String,
    val title: String,
    val shortDescription: String,
    val detailedDescription: String,
    val category: String, // Móvil, Web FullStack, IA & ML, Backend, Cloud & DevOps
    val difficulty: String, // Principiante, Intermedio, Avanzado, Pro
    val estimatedHours: Int,
    val xpReward: Int,
    val isProOnly: Boolean = false,
    val tags: String, // comma-separated e.g. "Kotlin, Compose, Room, Clean Arch"
    val iconCategory: String, // code, mobile, cloud, ai, web
    val gradientColorIndex: Int = 0,
    val totalTasks: Int,
    val completedTasks: Int = 0,
    val status: String = "AVAILABLE", // AVAILABLE, IN_PROGRESS, COMPLETED
    val enrolledCount: Int = 1420,
    val rating: Float = 4.9f
) {
    val progressPercent: Float
        get() = if (totalTasks > 0) completedTasks.toFloat() / totalTasks.toFloat() else 0f
}

@Entity(tableName = "project_tasks")
data class ProjectTask(
    @PrimaryKey val id: String,
    val projectId: String,
    val orderIndex: Int,
    val title: String,
    val summary: String,
    val instructions: String,
    val starterCodeHint: String = "",
    val expectedOutput: String = "",
    val xpReward: Int = 50,
    val isCompleted: Boolean = false,
    val userSubmissionNotes: String = "",
    val aiReviewFeedback: String = "",
    val aiScore: Int = 0 // 0 to 10
)

@Entity(tableName = "achievements")
data class UserAchievement(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconName: String, // rocket, brain, trophy, star, fire, code
    val isUnlocked: Boolean = false,
    val xpReward: Int = 100,
    val progress: Int = 1,
    val maxProgress: Int = 1
)

@Entity(tableName = "daily_challenges")
data class DailyChallenge(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val xpReward: Int = 80,
    val isCompleted: Boolean = false
)

@Entity(tableName = "certificates")
data class Certificate(
    @PrimaryKey val id: String,
    val projectId: String,
    val projectTitle: String,
    val studentName: String,
    val issueDate: String,
    val verificationCode: String,
    val skillsAcquired: String, // comma-separated
    val scoreGrade: String = "Excelente (9.8/10)"
)

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorRole: String,
    val projectTitle: String,
    val content: String,
    val codeSnippet: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timeAgo: String = "Hace 2 horas",
    val isLiked: Boolean = false,
    val tag: String = "Showcase"
)

@Entity(tableName = "chat_messages")
data class AiChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val relatedProjectId: String? = null,
    val isCodeReview: Boolean = false
)
