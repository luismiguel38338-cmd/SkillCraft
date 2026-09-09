package com.example.data.repository

import com.example.data.local.DatabaseInitializer
import com.example.data.local.SkillCraftDao
import com.example.data.model.*
import com.example.data.remote.AiMentorService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.*

class SkillCraftRepository(
    private val dao: SkillCraftDao,
    private val aiMentorService: AiMentorService = AiMentorService()
) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val allProjects: Flow<List<Project>> = dao.getAllProjects()
    val activeProject: Flow<Project?> = dao.getActiveProject()
    val achievements: Flow<List<UserAchievement>> = dao.getAllAchievements()
    val dailyChallenges: Flow<List<DailyChallenge>> = dao.getDailyChallenges()
    val certificates: Flow<List<Certificate>> = dao.getAllCertificates()
    val communityPosts: Flow<List<CommunityPost>> = dao.getCommunityPosts()
    val chatMessages: Flow<List<AiChatMessage>> = dao.getAllChatMessages()

    fun getProjectTasks(projectId: String): Flow<List<ProjectTask>> {
        return dao.getTasksForProject(projectId)
    }

    suspend fun initializeDataIfEmpty() {
        val currentProfile = dao.getUserProfile().firstOrNull()
        if (currentProfile == null) {
            dao.insertOrUpdateProfile(DatabaseInitializer.defaultUserProfile)
            dao.insertProjects(DatabaseInitializer.getInitialProjects())
            dao.insertTasks(DatabaseInitializer.getInitialTasks())
            dao.insertAchievements(DatabaseInitializer.getInitialAchievements())
            dao.insertChallenges(DatabaseInitializer.getInitialDailyChallenges())
            dao.insertCertificates(DatabaseInitializer.getInitialCertificates())
            dao.insertCommunityPosts(DatabaseInitializer.getInitialCommunityPosts())
            for (msg in DatabaseInitializer.getInitialChatMessages()) {
                dao.insertChatMessage(msg)
            }
        }
    }

    suspend fun enrollInProject(projectId: String) {
        val projects = dao.getAllProjects().firstOrNull() ?: return
        for (p in projects) {
            if (p.id == projectId) {
                dao.updateProject(p.copy(status = "IN_PROGRESS"))
            } else if (p.status == "IN_PROGRESS") {
                dao.updateProject(p.copy(status = "AVAILABLE"))
            }
        }
    }

    suspend fun explainTaskWithAi(taskId: String): String {
        val task = dao.getTaskById(taskId) ?: return "Tarea no encontrada."
        return aiMentorService.explainTask(task.title, task.instructions, task.starterCodeHint)
    }

    suspend fun submitTaskForReview(taskId: String, submissionText: String): Pair<Int, String> {
        val task = dao.getTaskById(taskId) ?: return Pair(0, "Tarea no encontrada.")
        val (score, feedback) = aiMentorService.reviewTask(task.title, task.instructions, submissionText)

        val updatedTask = task.copy(
            isCompleted = true,
            userSubmissionNotes = submissionText,
            aiReviewFeedback = feedback,
            aiScore = score
        )
        dao.updateTask(updatedTask)

        // Award XP to user
        awardXp(task.xpReward)

        // Check project completion
        checkAndAdvanceProjectProgress(task.projectId)

        // Check achievements
        val achievementsList = dao.getAllAchievements().firstOrNull() ?: emptyList()
        val firstTaskAch = achievementsList.find { it.id == "ach_first_task" }
        if (firstTaskAch != null && !firstTaskAch.isUnlocked) {
            dao.updateAchievement(firstTaskAch.copy(isUnlocked = true))
            awardXp(firstTaskAch.xpReward)
        }

        return Pair(score, feedback)
    }

    private suspend fun checkAndAdvanceProjectProgress(projectId: String) {
        val tasks = dao.getTasksForProject(projectId).firstOrNull() ?: return
        val completedCount = tasks.count { it.isCompleted }
        val allProjects = dao.getAllProjects().firstOrNull() ?: return
        val currentProject = allProjects.find { it.id == projectId } ?: return

        val isFullyComplete = completedCount == tasks.size
        val newStatus = if (isFullyComplete) "COMPLETED" else "IN_PROGRESS"

        val updatedProject = currentProject.copy(
            completedTasks = completedCount,
            status = newStatus
        )
        dao.updateProject(updatedProject)

        if (isFullyComplete) {
            // Generate official certificate
            val profile = dao.getUserProfile().firstOrNull()
            val studentName = profile?.name ?: "Luis Miguel"
            val dateFormat = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "ES"))
            val currentDate = dateFormat.format(Date())
            val randomCode = "SKILL-" + (1000..9999).random() + "-" + currentProject.category.take(3).uppercase()

            val newCertificate = Certificate(
                id = "cert_${projectId}_${System.currentTimeMillis()}",
                projectId = projectId,
                projectTitle = currentProject.title,
                studentName = studentName,
                issueDate = currentDate,
                verificationCode = randomCode,
                skillsAcquired = currentProject.tags,
                scoreGrade = "Sobresaliente (9.9/10)"
            )
            dao.insertCertificate(newCertificate)

            // Update user completed projects count and award bonus XP
            awardXp(currentProject.xpReward)
            if (profile != null) {
                dao.insertOrUpdateProfile(
                    profile.copy(
                        completedProjectsCount = profile.completedProjectsCount + 1
                    )
                )
            }
        }
    }

    suspend fun awardXp(amount: Int) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        var newXp = profile.currentXp + amount
        var newLevel = profile.level
        var xpToNext = profile.xpToNextLevel

        while (newXp >= xpToNext) {
            newXp -= xpToNext
            newLevel++
            xpToNext = (xpToNext * 1.35).toInt()
        }

        dao.insertOrUpdateProfile(
            profile.copy(
                currentXp = newXp,
                level = newLevel,
                xpToNextLevel = xpToNext
            )
        )
    }

    suspend fun askMentor(
        question: String,
        relatedProjectId: String? = null,
        mode: com.example.data.remote.MentorPedagogicalMode = com.example.data.remote.MentorPedagogicalMode.EXPLANATION
    ): String {
        // Save user message
        dao.insertChatMessage(
            AiChatMessage(
                role = "user",
                content = question,
                relatedProjectId = relatedProjectId
            )
        )

        var context: String? = null
        if (!relatedProjectId.isNullOrBlank()) {
            val allProjects = dao.getAllProjects().firstOrNull()
            val project = allProjects?.find { it.id == relatedProjectId }
            if (project != null) {
                context = "${project.title} (${project.category}, dificultad ${project.difficulty})"
            }
        }

        val answer = aiMentorService.askMentor(question, context, mode)

        // Save AI response
        dao.insertChatMessage(
            AiChatMessage(
                role = "assistant",
                content = answer,
                relatedProjectId = relatedProjectId
            )
        )

        return answer
    }

    suspend fun runLevelAssessment(answersSummary: String): Pair<String, String> {
        val (levelName, roadmap) = aiMentorService.assessSkillLevel(answersSummary)
        val profile = dao.getUserProfile().firstOrNull()
        if (profile != null) {
            dao.insertOrUpdateProfile(
                profile.copy(
                    assessedSkillLevel = levelName,
                    level = maxOf(profile.level, 3)
                )
            )
        }

        // Add assessment message to chat
        dao.insertChatMessage(
            AiChatMessage(
                role = "assistant",
                content = "🎉 **Evaluación de Nivel Completada**:\n\nTu nivel asignado es **$levelName**.\n\n$roadmap"
            )
        )

        return Pair(levelName, roadmap)
    }

    suspend fun toggleLikePost(postId: String) {
        val posts = dao.getCommunityPosts().firstOrNull() ?: return
        val post = posts.find { it.id == postId } ?: return
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
        dao.updateCommunityPost(post.copy(isLiked = newLiked, likesCount = newCount))
    }

    suspend fun createCommunityPost(content: String, projectTitle: String, codeSnippet: String) {
        val profile = dao.getUserProfile().firstOrNull()
        val authorName = profile?.name ?: "Luis Miguel"
        val authorRole = "${profile?.title ?: "Desarrollador"} • Nivel ${profile?.level ?: 3}"

        val newPost = CommunityPost(
            id = "post_${System.currentTimeMillis()}",
            authorName = authorName,
            authorRole = authorRole,
            projectTitle = projectTitle,
            content = content,
            codeSnippet = codeSnippet,
            likesCount = 1,
            commentsCount = 0,
            timeAgo = "Justo ahora",
            isLiked = true,
            tag = "Proyecto Estudiantil"
        )
        dao.insertCommunityPost(newPost)
    }

    suspend fun activateVerifiedPro(orderId: String, purchaseToken: String, planTitle: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateProfile(profile.copy(isPro = true))

        // Record official activation event in chat messages
        dao.insertChatMessage(
            AiChatMessage(
                role = "assistant",
                content = "🎉 **¡Suscripción PRO Confirmada y Verificada!**\n\nTu orden `$orderId` ($planTitle) fue verificada por la pasarela de pagos. Has desbloqueado:\n- Acceso a proyectos avanzados de Cloud, Microservicios y Agentes con Gemini.\n- Mentoría IA contextual sin cuotas diarias.\n- Pruebas automatizadas y code reviews profundos.\n- Certificados profesionales verificables con código QR."
            )
        )
    }

    suspend fun deactivatePro() {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateProfile(profile.copy(isPro = false))
    }

    suspend fun completeDailyChallenge(challengeId: String): Boolean {
        val challenge = dao.getChallengeById(challengeId) ?: return false
        if (challenge.isCompleted) return false

        dao.updateChallenge(challenge.copy(isCompleted = true))
        awardXp(challenge.xpReward)

        val profile = dao.getUserProfile().firstOrNull()
        if (profile != null) {
            dao.insertOrUpdateProfile(
                profile.copy(
                    streakDays = maxOf(profile.streakDays, 6)
                )
            )
        }
        return true
    }

    suspend fun updateProfileInfo(name: String, title: String, track: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateProfile(
            profile.copy(
                name = name,
                title = title,
                targetTrack = track
            )
        )
    }

    suspend fun clearChat() {
        dao.clearChatMessages()
        dao.insertChatMessage(
            AiChatMessage(
                role = "assistant",
                content = "¡Hola de nuevo! El historial ha sido reiniciado. ¿En qué duda técnica o proyecto de SkillCraft puedo ayudarte hoy?"
            )
        )
    }
}
