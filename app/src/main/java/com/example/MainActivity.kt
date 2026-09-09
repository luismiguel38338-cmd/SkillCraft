package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Certificate
import com.example.data.model.ProjectTask
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SkillCraftViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SkillCraftApp()
            }
        }
    }
}

@Composable
fun SkillCraftApp(viewModel: SkillCraftViewModel = viewModel()) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allProjects by viewModel.allProjects.collectAsStateWithLifecycle()
    val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val dailyChallenges by viewModel.dailyChallenges.collectAsStateWithLifecycle()
    val certificates by viewModel.certificates.collectAsStateWithLifecycle()
    val communityPosts by viewModel.communityPosts.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val selectedProject by viewModel.selectedProject.collectAsStateWithLifecycle()
    val selectedProjectTasks by viewModel.selectedProjectTasks.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val feedbackNotice by viewModel.userFeedbackNotice.collectAsStateWithLifecycle()

    // Billing & Pro Subscription States
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()
    val billingEnvironment by viewModel.billingEnvironment.collectAsStateWithLifecycle()
    val selectedBillingPlan by viewModel.selectedBillingPlan.collectAsStateWithLifecycle()
    val selectedSandboxOutcome by viewModel.selectedSandboxOutcome.collectAsStateWithLifecycle()

    // Game 400 Levels States
    val gameUnlockedLevel by viewModel.gameUnlockedLevel.collectAsStateWithLifecycle()
    val gameCompletedLevels by viewModel.gameCompletedLevels.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(NavTab.HOME) }
    var isProjectDetailOpen by remember { mutableStateOf(false) }

    // Dialogs state
    var showProDialog by remember { mutableStateOf(false) }
    var showAssessmentDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var showOnboardingDialog by rememberSaveable { mutableStateOf(false) }
    var taskToSubmit by remember { mutableStateOf<ProjectTask?>(null) }
    var taskExplanationData by remember { mutableStateOf<Pair<String, String>?>(null) }
    var certificateToView by remember { mutableStateOf<Certificate?>(null) }
    var showCreatePostDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackNotice) {
        feedbackNotice?.let { notice ->
            snackbarHostState.showSnackbar(notice)
            viewModel.dismissNotice()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (!isProjectDetailOpen) {
                SkillCraftTopBar(
                    userProfile = userProfile,
                    onOpenPro = { showProDialog = true },
                    onOpenAssessment = { showAssessmentDialog = true }
                )
            }
        },
        bottomBar = {
            if (!isProjectDetailOpen) {
                SkillCraftBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isProjectDetailOpen && selectedProject != null) {
                ProjectDetailScreen(
                    project = selectedProject!!,
                    tasks = selectedProjectTasks,
                    isUserPro = userProfile?.isPro == true,
                    onBack = { isProjectDetailOpen = false },
                    onEnroll = { id -> viewModel.enrollInProject(id) },
                    onExplainTask = { taskId ->
                        val task = selectedProjectTasks.find { it.id == taskId }
                        viewModel.explainTask(taskId) { explanation ->
                            taskExplanationData = Pair(task?.title ?: "Tarea", explanation)
                        }
                    },
                    onSubmitTask = { task ->
                        taskToSubmit = task
                    },
                    onAskMentorAboutProject = {
                        isProjectDetailOpen = false
                        currentTab = NavTab.AI_MENTOR
                    },
                    onOpenProModal = { showProDialog = true }
                )
            } else {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        NavTab.HOME -> HomeScreen(
                            userProfile = userProfile,
                            activeProject = activeProject,
                            dailyChallenges = dailyChallenges,
                            onOpenProject = { id ->
                                viewModel.selectProject(id)
                                isProjectDetailOpen = true
                            },
                            onOpenAssessment = { showAssessmentDialog = true },
                            onOpenMentor = { currentTab = NavTab.AI_MENTOR },
                            onOpenCatalog = { currentTab = NavTab.CATALOG },
                            onOpenGame = { currentTab = NavTab.GAME },
                            onCompleteDailyChallenge = { id -> viewModel.completeDailyChallenge(id) },
                            onOpenOnboarding = { showOnboardingDialog = true }
                        )

                        NavTab.GAME -> CodeQuestScreen(
                            currentUnlockedLevel = gameUnlockedLevel,
                            completedLevels = gameCompletedLevels,
                            onCompleteLevel = { lvl, stars, xp ->
                                viewModel.completeGameLevel(lvl, stars, xp)
                            },
                            onAskAiForHelp = { prompt ->
                                currentTab = NavTab.AI_MENTOR
                                viewModel.askMentor(prompt, null, com.example.data.remote.MentorPedagogicalMode.HINTS)
                            }
                        )

                        NavTab.CATALOG -> CatalogScreen(
                            projects = allProjects,
                            isUserPro = userProfile?.isPro == true,
                            onSelectProject = { id ->
                                viewModel.selectProject(id)
                                isProjectDetailOpen = true
                            },
                            onOpenProModal = { showProDialog = true }
                        )

                        NavTab.AI_MENTOR -> MentorScreen(
                            chatMessages = chatMessages,
                            activeProject = activeProject,
                            isAiThinking = isAiThinking,
                            onSendMessage = { query, mode ->
                                viewModel.askMentor(query, activeProject?.id, mode)
                            },
                            onClearChat = { viewModel.clearChatHistory() },
                            onOpenAssessment = { showAssessmentDialog = true }
                        )

                        NavTab.COMMUNITY -> CommunityScreen(
                            posts = communityPosts,
                            onToggleLike = { postId -> viewModel.toggleLikePost(postId) },
                            onOpenCreatePost = { showCreatePostDialog = true }
                        )

                        NavTab.PROFILE -> ProfileScreen(
                            userProfile = userProfile,
                            achievements = achievements,
                            certificates = certificates,
                            onOpenProModal = { showProDialog = true },
                            onOpenAuthModal = { showAuthDialog = true },
                            onOpenAssessmentModal = { showAssessmentDialog = true },
                            onViewCertificate = { cert -> certificateToView = cert },
                            onOpenOnboarding = { showOnboardingDialog = true }
                        )
                    }
                }
            }
        }
    }

    // Pro Subscription Checkout Modal (Strict Verification & No Demo Bypass)
    if (showProDialog) {
        ProSubscriptionCheckoutDialog(
            isCurrentlyPro = userProfile?.isPro == true,
            paymentState = paymentState,
            billingEnvironment = billingEnvironment,
            selectedPlan = selectedBillingPlan,
            selectedSandboxOutcome = selectedSandboxOutcome,
            onSelectPlan = { viewModel.selectBillingPlan(it) },
            onSetEnvironment = { viewModel.setBillingEnvironment(it) },
            onSetSandboxOutcome = { viewModel.setSandboxOutcome(it) },
            onStartCheckout = { viewModel.startSubscriptionCheckout() },
            onRestorePurchases = { viewModel.restorePurchases() },
            onResetState = { viewModel.resetPaymentState() },
            onDismiss = { showProDialog = false }
        )
    }

    // AI Skill Assessment Modal
    if (showAssessmentDialog) {
        SkillAssessmentDialog(
            isAiThinking = isAiThinking,
            onCompleteAssessment = { exp, goal, hours ->
                viewModel.runLevelAssessment(exp, goal, hours) { _, _ ->
                    currentTab = NavTab.AI_MENTOR
                }
            },
            onDismiss = { showAssessmentDialog = false }
        )
    }

    // User Profile / Auth Edit Modal
    if (showAuthDialog) {
        AuthDialog(
            currentName = userProfile?.name ?: "Luis Miguel",
            currentEmail = userProfile?.email ?: "luismiguel@dev.com",
            onSaveProfile = { name, title, track ->
                viewModel.updateProfile(name, title, track)
            },
            onDismiss = { showAuthDialog = false }
        )
    }

    // Task Submission Modal
    taskToSubmit?.let { task ->
        TaskSubmissionDialog(
            task = task,
            isAiThinking = isAiThinking,
            onSubmit = { submissionText ->
                viewModel.submitTaskForReview(task.id, submissionText) { _, _ ->
                    taskToSubmit = null
                }
            },
            onDismiss = { taskToSubmit = null }
        )
    }

    // Task AI Explanation Modal
    taskExplanationData?.let { (title, explanation) ->
        TaskExplanationDialog(
            taskTitle = title,
            explanation = explanation,
            onDismiss = { taskExplanationData = null }
        )
    }

    // Certificate View Modal
    certificateToView?.let { cert ->
        CertificateDetailDialog(
            certificate = cert,
            onDismiss = { certificateToView = null }
        )
    }

    // Create Community Post Modal
    if (showCreatePostDialog) {
        CreatePostDialog(
            projectTitle = activeProject?.title ?: "Desarrollo de Software",
            onPublish = { content, codeSnippet ->
                viewModel.createCommunityPost(content, activeProject?.title ?: "SkillCraft", codeSnippet)
            },
            onDismiss = { showCreatePostDialog = false }
        )
    }

    // Interactive Onboarding Dialog
    if (showOnboardingDialog) {
        OnboardingDialog(
            onDismiss = { showOnboardingDialog = false },
            onStartAssessment = {
                showOnboardingDialog = false
                showAssessmentDialog = true
            }
        )
    }
}
