package com.example.data.local

import com.example.data.model.*

object DatabaseInitializer {

    val defaultUserProfile = UserProfile(
        id = "primary_user",
        name = "Luis Miguel",
        email = "luismiguel@dev.com",
        title = "Desarrollador Junior",
        level = 3,
        currentXp = 1350,
        xpToNextLevel = 2000,
        streakDays = 5,
        completedProjectsCount = 2,
        totalTimeHours = 24,
        isPro = false,
        targetTrack = "Desarrollador Android & Full Stack",
        assessedSkillLevel = "Nivel 3 - Intermedio",
        avatarUrl = ""
    )

    fun getInitialProjects(): List<Project> = listOf(
        Project(
            id = "proj_android_finance",
            title = "FinanzApp: Gestor Financiero con Jetpack Compose",
            shortDescription = "Crea una app móvil nativa moderna con persistencia local Room, gráficos y arquitectura M3.",
            detailedDescription = "Construirás una aplicación completa para el control de ingresos y egresos personales. Aprenderás state management con StateFlow, patrones Clean MVVM, bases de datos Room con migraciones seguras y animaciones en Jetpack Compose.",
            category = "Móvil",
            difficulty = "Intermedio",
            estimatedHours = 12,
            xpReward = 450,
            isProOnly = false,
            tags = "Kotlin, Jetpack Compose, Room, MVVM, Material 3",
            iconCategory = "mobile",
            gradientColorIndex = 0,
            totalTasks = 4,
            completedTasks = 2,
            status = "IN_PROGRESS",
            enrolledCount = 2410,
            rating = 4.9f
        ),
        Project(
            id = "proj_ai_assistant",
            title = "Copiloto IA Empresarial con Gemini API & RAG",
            shortDescription = "Diseña un asistente de inteligencia artificial con recuperación de documentos y chat contextual.",
            detailedDescription = "Aprenderás a interactuar con los modelos Gemini de Google, gestionar prompts estructurados, procesar respuestas en streaming e implementar un motor RAG (Retrieval-Augmented Generation) para responder sobre bases de conocimiento privadas.",
            category = "IA & ML",
            difficulty = "Avanzado",
            estimatedHours = 16,
            xpReward = 700,
            isProOnly = true,
            tags = "Gemini API, Python/Kotlin, Embeddings, RAG, REST API",
            iconCategory = "ai",
            gradientColorIndex = 1,
            totalTasks = 5,
            completedTasks = 0,
            status = "AVAILABLE",
            enrolledCount = 1890,
            rating = 5.0f
        ),
        Project(
            id = "proj_backend_jwt",
            title = "API REST de Alta Disponibilidad & JWT Auth",
            shortDescription = "Desarrolla un backend robusto con autenticación tokenizada, middlewares de seguridad y caché.",
            detailedDescription = "Construye una API profesional con endpoints RESTful, hashing criptográfico de contraseñas con BCrypt, emisión y rotación de tokens JWT, rate limiting y pruebas unitarias automáticas.",
            category = "Backend",
            difficulty = "Intermedio",
            estimatedHours = 10,
            xpReward = 400,
            isProOnly = false,
            tags = "Ktor/Node.js, JWT, PostgreSQL, Redis, Docker",
            iconCategory = "cloud",
            gradientColorIndex = 2,
            totalTasks = 4,
            completedTasks = 4,
            status = "COMPLETED",
            enrolledCount = 3120,
            rating = 4.8f
        ),
        Project(
            id = "proj_web_ecommerce",
            title = "E-Commerce Reactivo con Catálogo & Carrito",
            shortDescription = "Plataforma de ventas con carrito en tiempo real, checkout y filtros avanzados de productos.",
            detailedDescription = "Diseña una tienda digital de vanguardia con Server-Side Rendering, gestión global de estado de compras, checkout con validación de tarjetas e integración de webhook de pagos.",
            category = "Web FullStack",
            difficulty = "Intermedio",
            estimatedHours = 14,
            xpReward = 500,
            isProOnly = false,
            tags = "TypeScript, React, TailwindCSS, State Management",
            iconCategory = "web",
            gradientColorIndex = 3,
            totalTasks = 4,
            completedTasks = 4,
            status = "COMPLETED",
            enrolledCount = 4250,
            rating = 4.9f
        ),
        Project(
            id = "proj_frontend_dashboard",
            title = "Dashboard Analítico con Métricas en Vivo",
            shortDescription = "Panel de control con métricas interactivas, modo oscuro y gráficos reactivos.",
            detailedDescription = "Tu primer proyecto frontend profesional. Aprende diseño responsive, jerarquía visual, manipulación de datasets en memoria y visualización de datos con tarjetas interactivas y animaciones.",
            category = "Frontend",
            difficulty = "Principiante",
            estimatedHours = 6,
            xpReward = 250,
            isProOnly = false,
            tags = "Compose/HTML, CSS Grid, Charting, Responsive UI",
            iconCategory = "code",
            gradientColorIndex = 4,
            totalTasks = 3,
            completedTasks = 0,
            status = "AVAILABLE",
            enrolledCount = 5120,
            rating = 4.7f
        ),
        Project(
            id = "proj_cloud_microservices",
            title = "Arquitectura de Microservicios con Kafka & Kubernetes",
            shortDescription = "Sistema distribuido de procesamiento de eventos con tolerancia a fallos y despliegue CI/CD.",
            detailedDescription = "Proyecto exclusivo de nivel Pro donde diseñarás una arquitectura guiada por eventos (EDA) utilizando Apache Kafka, orquestación de contenedores con Kubernetes y monitoreo con Prometheus.",
            category = "Cloud & DevOps",
            difficulty = "Pro",
            estimatedHours = 20,
            xpReward = 950,
            isProOnly = true,
            tags = "Kubernetes, Kafka, Microservices, CI/CD, DevOps",
            iconCategory = "cloud",
            gradientColorIndex = 5,
            totalTasks = 5,
            completedTasks = 0,
            status = "AVAILABLE",
            enrolledCount = 980,
            rating = 4.95f
        )
    )

    fun getInitialTasks(): List<ProjectTask> = listOf(
        // Tasks for FinanzApp
        ProjectTask(
            id = "task_fin_1",
            projectId = "proj_android_finance",
            orderIndex = 1,
            title = "Diseño de la Arquitectura & Entidades Room",
            summary = "Define la estructura de base de datos con TransactionEntity y CategoryEntity.",
            instructions = "1. Crea la data class TransactionEntity con @PrimaryKey(autoGenerate = true).\n2. Incluye campos: id, title, amount, type (EXPENSE/INCOME), category, dateTimestamp.\n3. Implementa TransactionDao con funciones Flow<List<TransactionEntity>> e insert/delete.\n4. Configura el AppDatabase con exportSchema = false.",
            starterCodeHint = "@Entity(tableName = \"transactions\")\ndata class TransactionEntity(\n    @PrimaryKey(autoGenerate = true) val id: Long = 0,\n    val title: String,\n    val amount: Double,\n    val isExpense: Boolean\n)",
            expectedOutput = "Base de datos Room compilada correctamente con queries reactivos Flow.",
            xpReward = 100,
            isCompleted = true,
            userSubmissionNotes = "Implementé las entidades y el Dao con Flow y Coroutines usando Room 2.7.0.",
            aiReviewFeedback = "¡Excelente implementación! Estructura limpia y uso correcto de Flow para programación reactiva.",
            aiScore = 10
        ),
        ProjectTask(
            id = "task_fin_2",
            projectId = "proj_android_finance",
            orderIndex = 2,
            title = "ViewModel con StateFlow & Cálculo de Balances",
            summary = "Gestiona el estado de la UI y calcula saldo total, ingresos y gastos.",
            instructions = "1. Inyecta el TransactionRepository en el FinanceViewModel.\n2. Expón un StateFlow<FinanceUiState> con saldo, lista de movimientos y filtros.\n3. Implementa validaciones para evitar montos negativos o títulos vacíos.",
            starterCodeHint = "data class FinanceUiState(\n    val totalBalance: Double = 0.0,\n    val transactions: List<TransactionEntity> = emptyList(),\n    val isLoading: Boolean = false\n)",
            expectedOutput = "ViewModel reactivo con StateFlow emitiendo cálculos de balance en tiempo real.",
            xpReward = 100,
            isCompleted = true,
            userSubmissionNotes = "StateFlow implementado con stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Initial).",
            aiReviewFeedback = "Muy buen manejo de WhileSubscribed para ahorrar recursos en ciclo de vida Android.",
            aiScore = 9
        ),
        ProjectTask(
            id = "task_fin_3",
            projectId = "proj_android_finance",
            orderIndex = 3,
            title = "Interfaz de Usuario en Compose con Tarjeta de Balance",
            summary = "Construye la pantalla principal con Scaffold, tarjeta de saldo y lista dinámica.",
            instructions = "1. Diseña un Card con degradado que muestre el saldo general destacado.\n2. Añade un FloatingActionButton para registrar nuevo movimiento.\n3. Crea un LazyColumn para renderizar cada transacción con swipe-to-delete o diálogo de confirmación.\n4. Aplica colores semánticos (verde para ingresos, rojo para egresos).",
            starterCodeHint = "@Composable\nfun BalanceHeroCard(balance: Double, income: Double, expense: Double) {\n    Card(shape = RoundedCornerShape(24.dp)) { ... }\n}",
            expectedOutput = "Pantalla moderna con diseño responsivo Material 3 y scroll fluido.",
            xpReward = 120,
            isCompleted = false,
            userSubmissionNotes = "",
            aiReviewFeedback = "",
            aiScore = 0
        ),
        ProjectTask(
            id = "task_fin_4",
            projectId = "proj_android_finance",
            orderIndex = 4,
            title = "Gráficos de Gastos por Categoría & Exportación",
            summary = "Integra un gráfico circular o de barras y genera resumen mensual.",
            instructions = "1. Agrupa las transacciones por categoría y calcula porcentajes.\n2. Dibuja un gráfico con Canvas o biblioteca de charts.\n3. Agrega animación de entrada al visualizar los datos.\n4. Realiza pruebas en modo oscuro y claro.",
            starterCodeHint = "Canvas(modifier = Modifier.size(200.dp)) {\n    // drawArc para cada segmento de categoría\n}",
            expectedOutput = "Visualizador interactivo de métricas de gasto con desglose porcentual.",
            xpReward = 130,
            isCompleted = false,
            userSubmissionNotes = "",
            aiReviewFeedback = "",
            aiScore = 0
        ),

        // Tasks for Gemini AI Assistant
        ProjectTask(
            id = "task_ai_1",
            projectId = "proj_ai_assistant",
            orderIndex = 1,
            title = "Configuración del Cliente Gemini REST & Auth Segura",
            summary = "Conecta con el modelo gemini-3.5-flash mediante Retrofit y BuildConfig.",
            instructions = "1. Configura Retrofit con el endpoint oficial de Gemini v1beta.\n2. Extrae la API Key de forma segura mediante BuildConfig.\n3. Define los data models para Content, Part y GenerateContentRequest.\n4. Implementa timeout de 60 segundos con OkHttpClient.",
            starterCodeHint = "interface GeminiService {\n    @POST(\"v1beta/models/gemini-3.5-flash:generateContent\")\n    suspend fun generate(...) : GenerateContentResponse\n}",
            expectedOutput = "Petición POST exitosa con respuesta textual de Gemini.",
            xpReward = 120,
            isCompleted = false
        ),
        ProjectTask(
            id = "task_ai_2",
            projectId = "proj_ai_assistant",
            orderIndex = 2,
            title = "Ingeniería de Prompts y System Instructions",
            summary = "Define la personalidad experta del mentor y formato de salida estructurado.",
            instructions = "1. Añade SystemInstruction para orientar a la IA como tutor pedagógico.\n2. Configura temperature a 0.7 para balancear precisión y creatividad.\n3. Maneja casos de respuesta vacía o errores de conectividad con fallbacks elegantes.",
            starterCodeHint = "val systemInstruction = Content(parts = listOf(Part(\"Eres un mentor senior de software...\")))",
            expectedOutput = "Respuestas adaptadas al perfil pedagógico del estudiante.",
            xpReward = 140,
            isCompleted = false
        )
    )

    fun getInitialAchievements(): List<UserAchievement> = listOf(
        UserAchievement(
            id = "ach_first_task",
            title = "Primer Paso de Código",
            description = "Completa tu primera tarea de un proyecto real.",
            iconName = "rocket",
            isUnlocked = true,
            xpReward = 100,
            progress = 1,
            maxProgress = 1
        ),
        UserAchievement(
            id = "ach_streak_3",
            title = "Racha Imparable (5 Días)",
            description = "Aprende y programa durante 5 días consecutivos.",
            iconName = "fire",
            isUnlocked = true,
            xpReward = 150,
            progress = 5,
            maxProgress = 5
        ),
        UserAchievement(
            id = "ach_ai_mentor",
            title = "Aprendiz de la IA",
            description = "Consulta dudas y pide revisión de código a tu mentor IA.",
            iconName = "brain",
            isUnlocked = true,
            xpReward = 120,
            progress = 1,
            maxProgress = 1
        ),
        UserAchievement(
            id = "ach_project_master",
            title = "Creador de Proyectos",
            description = "Finaliza 2 proyectos de software completos con verificación.",
            iconName = "trophy",
            isUnlocked = true,
            xpReward = 300,
            progress = 2,
            maxProgress = 2
        ),
        UserAchievement(
            id = "ach_community_star",
            title = "Colaborador Activo",
            description = "Comparte un proyecto o solución con la comunidad de estudiantes.",
            iconName = "star",
            isUnlocked = false,
            xpReward = 150,
            progress = 0,
            maxProgress = 1
        ),
        UserAchievement(
            id = "ach_pro_architect",
            title = "Arquitecto Pro",
            description = "Desbloquea el nivel Pro y completa un proyecto de microservicios o IA avanzada.",
            iconName = "code",
            isUnlocked = false,
            xpReward = 500,
            progress = 0,
            maxProgress = 1
        )
    )

    fun getInitialDailyChallenges(): List<DailyChallenge> = listOf(
        DailyChallenge(
            id = "ch_1",
            title = "Pregunta al Mentor IA",
            description = "Haz una consulta técnica sobre arquitectura o bugs a tu mentor.",
            category = "IA",
            xpReward = 60,
            isCompleted = true
        ),
        DailyChallenge(
            id = "ch_2",
            title = "Avanza una Tarea del Proyecto",
            description = "Completa un paso del proyecto en progreso hoy.",
            category = "Código",
            xpReward = 90,
            isCompleted = false
        ),
        DailyChallenge(
            id = "ch_3",
            title = "Revisa el Foro de la Comunidad",
            description = "Deja feedback o felicita a otro estudiante en sus publicaciones.",
            category = "Social",
            xpReward = 50,
            isCompleted = false
        )
    )

    fun getInitialCertificates(): List<Certificate> = listOf(
        Certificate(
            id = "cert_backend_jwt_01",
            projectId = "proj_backend_jwt",
            projectTitle = "API REST de Alta Disponibilidad & JWT Auth",
            studentName = "Luis Miguel",
            issueDate = "15 de Agosto, 2026",
            verificationCode = "SKILL-2026-BEND-9842",
            skillsAcquired = "Autenticación JWT, Hashing Criptográfico, APIs RESTful, Middleware de Seguridad, Pruebas Unitarias",
            scoreGrade = "Sobresaliente (9.9/10)"
        ),
        Certificate(
            id = "cert_web_ecom_02",
            projectId = "proj_web_ecommerce",
            projectTitle = "E-Commerce Reactivo con Catálogo & Carrito",
            studentName = "Luis Miguel",
            issueDate = "28 de Agosto, 2026",
            verificationCode = "SKILL-2026-ECOM-4419",
            skillsAcquired = "Gestión de Estado Global, Reactividad en Tiempo Real, Integración de Pasarelas, UI Responsiva",
            scoreGrade = "Excelente (9.7/10)"
        )
    )

    fun getInitialCommunityPosts(): List<CommunityPost> = listOf(
        CommunityPost(
            id = "post_1",
            authorName = "Valeria Morales",
            authorRole = "Estudiante FullStack • Nivel 4",
            projectTitle = "FinanzApp con Jetpack Compose",
            content = "¡Acabo de terminar el módulo de gráficos de gastos! El mentor IA me sugirió usar Canvas con animaciones de degradado y el resultado visual quedó genial. ¿Qué les parece?",
            codeSnippet = "Canvas(modifier = Modifier.size(180.dp)) {\n  drawArc(\n    brush = Brush.sweepGradient(colors = listOf(Cyan, Indigo)),\n    startAngle = -90f, sweepAngle = animatedAngle,\n    useCenter = false, style = Stroke(width = 24.dp.toPx())\n  )\n}",
            likesCount = 38,
            commentsCount = 12,
            timeAgo = "Hace 3 horas",
            isLiked = false,
            tag = "Proyecto Terminado"
        ),
        CommunityPost(
            id = "post_2",
            authorName = "Carlos Mendoza",
            authorRole = "Ingeniero IA • Nivel 5 (Plan Pro)",
            projectTitle = "Copiloto IA Empresarial con Gemini",
            content = "Logré reducir la latencia de respuesta del mentor conectando el endpoint streaming de Gemini 3.5. La revisión automática de código ahora evalúa sintaxis y complejidad ciclomática al instante.",
            codeSnippet = "RetrofitClient.service.generateContentStream(apiKey, request)\n  .byteStream().bufferedReader().forEachLine { chunk ->\n    emitChunkToUi(chunk)\n  }",
            likesCount = 54,
            commentsCount = 19,
            timeAgo = "Hace 6 horas",
            isLiked = true,
            tag = "Consejo Técnico"
        ),
        CommunityPost(
            id = "post_3",
            authorName = "Elena Rojas",
            authorRole = "Desarrolladora Móvil • Nivel 2",
            projectTitle = "Dashboard Analítico",
            content = "Mi primer proyecto en SkillCraft completado y certificado. La ruta paso a paso hace que no te sientas abrumado porque cada tarea tiene instrucciones claras y el mentor te saca de dudas.",
            codeSnippet = "",
            likesCount = 29,
            commentsCount = 7,
            timeAgo = "Ayer",
            isLiked = false,
            tag = "Logro Desbloqueado"
        )
    )

    fun getInitialChatMessages(): List<AiChatMessage> = listOf(
        AiChatMessage(
            role = "assistant",
            content = "¡Hola Luis Miguel! 👋 Soy tu Mentor Personal de IA en SkillCraft. Mi misión es evaluar tu nivel, guiarte paso a paso en proyectos reales, explicarte tareas complejas, resolver tus dudas técnicas y revisar tu código. ¿En qué proyecto o habilidad deseas enfocarte hoy?",
            timestamp = System.currentTimeMillis() - 600000
        )
    )
}
