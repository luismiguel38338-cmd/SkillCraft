package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.SkillCraftRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SkillCraftViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SkillCraftRepository

    init {
        val database = AppDatabase.getInstance(application)
        repository = SkillCraftRepository(database.skillCraftDao())
        viewModelScope.launch {
            repository.initializeDataIfEmpty()
        }
    }

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allProjects: StateFlow<List<Project>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeProject: StateFlow<Project?> = repository.activeProject
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val achievements: StateFlow<List<UserAchievement>> = repository.achievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyChallenges: StateFlow<List<DailyChallenge>> = repository.dailyChallenges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val certificates: StateFlow<List<Certificate>> = repository.certificates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityPosts: StateFlow<List<CommunityPost>> = repository.communityPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<AiChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProjectId = MutableStateFlow<String?>("proj_android_finance")
    val selectedProjectId: StateFlow<String?> = _selectedProjectId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedProject: StateFlow<Project?> = combine(allProjects, _selectedProjectId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedProjectTasks: StateFlow<List<ProjectTask>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getProjectTasks(id) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _userFeedbackNotice = MutableStateFlow<String?>(null)
    val userFeedbackNotice: StateFlow<String?> = _userFeedbackNotice.asStateFlow()

    fun dismissNotice() {
        _userFeedbackNotice.value = null
    }

    fun selectProject(projectId: String) {
        _selectedProjectId.value = projectId
    }

    fun enrollInProject(projectId: String) {
        viewModelScope.launch {
            repository.enrollInProject(projectId)
            _selectedProjectId.value = projectId
            _userFeedbackNotice.value = "¡Te has inscrito al proyecto! La IA adaptó tu ruta de tareas."
        }
    }

    fun askMentor(question: String, relatedProjectId: String? = null) {
        if (question.isBlank()) return
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                repository.askMentor(question, relatedProjectId)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun explainTask(taskId: String, onExplanationReady: (String) -> Unit) {
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                val explanation = repository.explainTaskWithAi(taskId)
                onExplanationReady(explanation)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun submitTaskForReview(taskId: String, submissionText: String, onReviewed: (Int, String) -> Unit) {
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                val (score, feedback) = repository.submitTaskForReview(taskId, submissionText)
                _userFeedbackNotice.value = "¡Tarea aprobada con $score/10! +XP otorgado a tu perfil."
                onReviewed(score, feedback)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun runLevelAssessment(experience: String, goal: String, hours: String, onFinished: (String, String) -> Unit) {
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                val summary = "Experiencia previa: $experience. Meta profesional: $goal. Disponibilidad semanal: $hours horas."
                val (level, roadmap) = repository.runLevelAssessment(summary)
                _userFeedbackNotice.value = "Evaluación completada: Nivel asignado $level"
                onFinished(level, roadmap)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun toggleLikePost(postId: String) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
        }
    }

    fun createCommunityPost(content: String, projectTitle: String, codeSnippet: String) {
        viewModelScope.launch {
            repository.createCommunityPost(content, projectTitle, codeSnippet)
            _userFeedbackNotice.value = "¡Publicación compartida con la comunidad!"
        }
    }

    fun upgradeToPro() {
        viewModelScope.launch {
            repository.upgradeToPro()
            _userFeedbackNotice.value = "🎉 ¡Felicidades! Eres miembro SkillCraft PRO."
        }
    }

    fun toggleProPlanDemo() {
        viewModelScope.launch {
            val isPro = repository.toggleProPlanDemo()
            _userFeedbackNotice.value = if (isPro) {
                "🌟 Modo DEMO: Plan PRO activado con éxito. Proyectos avanzados desbloqueados."
            } else {
                "ℹ️ Modo DEMO: Has vuelto al Plan Estándar."
            }
        }
    }

    fun completeDailyChallenge(challengeId: String) {
        viewModelScope.launch {
            val completed = repository.completeDailyChallenge(challengeId)
            if (completed) {
                _userFeedbackNotice.value = "🔥 ¡Reto completado! +80 XP sumados a tu progreso y racha protegida."
            }
        }
    }

    fun updateProfile(name: String, title: String, track: String) {
        viewModelScope.launch {
            repository.updateProfileInfo(name, title, track)
            _userFeedbackNotice.value = "Perfil actualizado correctamente."
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
            _userFeedbackNotice.value = "Historial con el mentor reiniciado."
        }
    }
}
