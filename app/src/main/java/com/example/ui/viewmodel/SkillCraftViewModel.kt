package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.billing.*
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.SkillCraftRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SkillCraftViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SkillCraftRepository
    val billingManager = BillingManager()

    init {
        val database = AppDatabase.getInstance(application)
        repository = SkillCraftRepository(database.skillCraftDao())
        viewModelScope.launch {
            repository.initializeDataIfEmpty()
            // Verify subscription status upon opening the application
            verifySubscriptionOnAppStart()
        }
    }

    val billingEnvironment: StateFlow<BillingEnvironment> = billingManager.billingEnvironment
    val paymentState: StateFlow<PaymentState> = billingManager.paymentState
    val selectedBillingPlan: StateFlow<SubscriptionPlan> = billingManager.selectedPlan
    val selectedSandboxOutcome: StateFlow<SandboxTestOutcome> = billingManager.selectedSandboxOutcome

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

    // Game 400 Levels Mode State
    private val _gameUnlockedLevel = MutableStateFlow(1)
    val gameUnlockedLevel: StateFlow<Int> = _gameUnlockedLevel.asStateFlow()

    private val _gameCompletedLevels = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val gameCompletedLevels: StateFlow<Map<Int, Int>> = _gameCompletedLevels.asStateFlow()

    fun completeGameLevel(level: Int, stars: Int, xp: Int) {
        viewModelScope.launch {
            val updated = _gameCompletedLevels.value.toMutableMap()
            val prevStars = updated[level] ?: 0
            updated[level] = maxOf(prevStars, stars)
            _gameCompletedLevels.value = updated

            val nextLevel = (level + 1).coerceAtMost(400)
            if (nextLevel > _gameUnlockedLevel.value) {
                _gameUnlockedLevel.value = nextLevel
            }

            repository.awardXp(xp)
            _userFeedbackNotice.value = "¡Nivel $level completado! Byte celebra tu logro: +$xp XP."
        }
    }

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

    fun askMentor(
        question: String,
        relatedProjectId: String? = null,
        mode: com.example.data.remote.MentorPedagogicalMode = com.example.data.remote.MentorPedagogicalMode.EXPLANATION
    ) {
        if (question.isBlank()) return
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                repository.askMentor(question, relatedProjectId, mode)
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

    // Billing & Subscription Management (Strict Verification Flow - No Demo Bypass)
    fun selectBillingPlan(plan: SubscriptionPlan) {
        billingManager.selectPlan(plan)
    }

    fun setBillingEnvironment(env: BillingEnvironment) {
        billingManager.setEnvironment(env)
    }

    fun setSandboxOutcome(outcome: SandboxTestOutcome) {
        billingManager.setSandboxOutcome(outcome)
    }

    fun resetPaymentState() {
        billingManager.resetPaymentState()
    }

    fun startSubscriptionCheckout() {
        viewModelScope.launch {
            val result = billingManager.startCheckout()
            when (result) {
                is PaymentState.Success -> {
                    repository.activateVerifiedPro(
                        orderId = result.orderId,
                        purchaseToken = result.purchaseToken,
                        planTitle = result.planTitle
                    )
                    _userFeedbackNotice.value = "🎉 ¡Suscripción PRO confirmada por el proveedor! Beneficios activados."
                }
                is PaymentState.Pending -> {
                    // Critical: Keep PRO locked until payment confirmation is finalized by the provider
                    _userFeedbackNotice.value = "⚠️ Transacción en verificación bancaria. PRO se mantendrá bloqueado hasta confirmación definitiva."
                }
                is PaymentState.Error -> {
                    _userFeedbackNotice.value = "❌ No se pudo completar el cobro: ${result.errorMessage}"
                }
                is PaymentState.Cancelled -> {
                    _userFeedbackNotice.value = "ℹ️ Operación cancelada por el usuario."
                }
                else -> Unit
            }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            val result = billingManager.restorePurchases()
            if (result is PaymentState.Success) {
                repository.activateVerifiedPro(
                    orderId = result.orderId,
                    purchaseToken = result.purchaseToken,
                    planTitle = result.planTitle
                )
                _userFeedbackNotice.value = "✅ Suscripción PRO restaurada y validada con éxito."
            } else if (result is PaymentState.Error) {
                _userFeedbackNotice.value = "ℹ️ ${result.errorMessage}"
            }
        }
    }

    private suspend fun verifySubscriptionOnAppStart() {
        // Enforce subscription verification when the application starts or user profile loads
        val isLocallyPro = userProfile.firstOrNull()?.isPro == true
        val isProviderActive = billingManager.isSubscriptionActive()

        // If local profile had PRO marked without an active provider subscription or was expired, lock PRO
        if (isLocallyPro && !isProviderActive && billingManager.billingEnvironment.value == BillingEnvironment.PRODUCTION_GOOGLE_PLAY) {
            repository.deactivatePro()
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
