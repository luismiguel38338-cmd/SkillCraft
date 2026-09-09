package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary
import com.example.ui.theme.TechSuccess

data class ProgressiveModule(
    val id: String,
    val phaseNumber: Int,
    val title: String,
    val shortSummary: String,
    val iconEmoji: String,
    val category: String,
    val estimatedHours: Int,
    val xpReward: Int,
    val isCompleted: Boolean,
    val isLocked: Boolean,
    val theoryConcept: String,
    val codeSnippetExample: String,
    val practicalChallenge: String,
    val challengeStarterCode: String
)

val defaultProgressiveModules = listOf(
    ProgressiveModule(
        id = "mod_1_fundamentos",
        phaseNumber = 1,
        title = "1. Fundamentos & Kotlin Moderno",
        shortSummary = "Variables inmutables 'val', tipos seguros nulos y lambdas de orden superior.",
        iconEmoji = "🧱",
        category = "Fundamentos",
        estimatedHours = 6,
        xpReward = 150,
        isCompleted = true,
        isLocked = false,
        theoryConcept = """
            En Kotlin moderno, la inmutabilidad es la piedra angular:
            • Preferir 'val' sobre 'var' previene condiciones de carrera en programación concurrente.
            • El sistema de tipos null-safe (? y ?:) elimina errores tipo NullPointerException en tiempo de compilación.
            • Las funciones son ciudadanos de primera clase (First-Class Citizens), lo que permite pasarlas como argumentos o retornarlas.
        """.trimIndent(),
        codeSnippetExample = """
            // Inmutabilidad y manejo seguro de nulos
            val userName: String? = "Luis"
            val displayName = userName ?: "Invitado"
            
            // Función de orden superior
            fun <T> executeTask(action: () -> T): T {
                return action()
            }
        """.trimIndent(),
        practicalChallenge = "Crea una función pura que valide si un correo electrónico no es nulo y contiene el carácter '@'.",
        challengeStarterCode = """
            fun validateEmail(email: String?): Boolean {
                // Implementa tu solución aquí
                return !email.isNullOrBlank() && email.contains("@")
            }
        """.trimIndent()
    ),
    ProgressiveModule(
        id = "mod_2_logica",
        phaseNumber = 2,
        title = "2. Lógica & Algoritmos Estructurados",
        shortSummary = "Transformaciones funcionales con map, filter, fold y estructuras de datos.",
        iconEmoji = "🧩",
        category = "Lógica",
        estimatedHours = 8,
        xpReward = 200,
        isCompleted = true,
        isLocked = false,
        theoryConcept = """
            El procesamiento funcional de colecciones reduce la complejidad de los bucles imperativos:
            • 'filter' genera un subconjunto basado en predicados booleanos sin mutar la lista original.
            • 'map' proyecta cada elemento a una nueva forma conservando la integridad de los datos.
            • 'reduce' y 'fold' acumulan valores para calcular métricas (sumas, promedios, agregados).
        """.trimIndent(),
        codeSnippetExample = """
            data class Score(val student: String, val points: Int)
            
            val scores = listOf(Score("Ana", 90), Score("Carlos", 65), Score("Elena", 95))
            val honorRoll = scores
                .filter { it.points >= 85 }
                .map { it.student.uppercase() }
        """.trimIndent(),
        practicalChallenge = "Filtra una lista de proyectos activos para obtener únicamente aquellos con progreso mayor al 50%.",
        challengeStarterCode = """
            fun getTopProjects(progressList: List<Float>): List<Float> {
                return progressList.filter { it > 0.5f }
            }
        """.trimIndent()
    ),
    ProgressiveModule(
        id = "mod_3_desarrollo_real",
        phaseNumber = 3,
        title = "3. Desarrollo Real en Jetpack Compose",
        shortSummary = "State Hoisting, Modifiers, Composables reutilizables y Material Design 3.",
        iconEmoji = "📱",
        category = "Desarrollo Real",
        estimatedHours = 12,
        xpReward = 250,
        isCompleted = false,
        isLocked = false,
        theoryConcept = """
            Jetpack Compose reimagina la interfaz gráfica como una función pura de su estado:
            • State Hoisting: Elevar el estado al ViewModel o ancestro común convierte a los componentes hijos en vistas sin efectos secundarios.
            • Modifiers: Permiten encadenar estilos, tamaños, márgenes y detectores de click de forma declarativa.
            • Dynamic Color & M3: Utilizar 'MaterialTheme.colorScheme' garantiza compatibilidad nativa con modo oscuro y Material You.
        """.trimIndent(),
        codeSnippetExample = """
            @Composable
            fun UserAvatar(name: String, modifier: Modifier = Modifier) {
                Box(
                    modifier = modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = name.take(1), fontWeight = FontWeight.Bold)
                }
            }
        """.trimIndent(),
        practicalChallenge = "Diseña un Composable reutilizable 'MetricCard' que muestre un icono, un valor numérico y una etiqueta de descripción.",
        challengeStarterCode = """
            @Composable
            fun MetricCard(label: String, value: String) {
                // Implementa Card con M3 y padding
            }
        """.trimIndent()
    ),
    ProgressiveModule(
        id = "mod_4_bases_de_datos",
        phaseNumber = 4,
        title = "4. Bases de Datos & Persistencia (Room)",
        shortSummary = "Mapeo relacional con Entity, DAO asíncrono, índices y reactividad con Flow.",
        iconEmoji = "🗄️",
        category = "Bases de Datos",
        estimatedHours = 10,
        xpReward = 300,
        isCompleted = false,
        isLocked = false,
        theoryConcept = """
            Room ofrece una capa de abstracción sobre SQLite nativo con verificación en tiempo de compilación:
            • @Entity define la estructura tabular con llaves primarias (@PrimaryKey).
            • @Dao expone métodos suspensibles (CRUD) y observables que emiten Flow<List<T>>.
            • Reactividad automática: cuando la base de datos se actualiza, la UI se recompone instantáneamente sin polling manual.
        """.trimIndent(),
        codeSnippetExample = """
            @Dao
            interface TaskDao {
                @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY id DESC")
                fun getPendingTasks(): Flow<List<TaskEntity>>
                
                @Insert(onConflict = OnConflictStrategy.REPLACE)
                suspend fun insertTask(task: TaskEntity)
            }
        """.trimIndent(),
        practicalChallenge = "Escribe un DAO para registrar transacciones bancarias con filtro por categoría.",
        challengeStarterCode = """
            @Dao
            interface TransactionDao {
                @Query("SELECT * FROM transactions WHERE category = :cat")
                fun getByCategory(cat: String): Flow<List<Transaction>>
            }
        """.trimIndent()
    ),
    ProgressiveModule(
        id = "mod_5_apis_cloud",
        phaseNumber = 5,
        title = "5. APIs REST, Cloud & Conectividad",
        shortSummary = "Clientes HTTP Retrofit/Ktor, DTOs con Serialization, Coroutines y Dispatchers.IO.",
        iconEmoji = "🌐",
        category = "APIs / Cloud",
        estimatedHours = 12,
        xpReward = 350,
        isCompleted = false,
        isLocked = false,
        theoryConcept = """
            La comunicación en red requiere aislamiento absoluto del hilo principal de Android (Main Thread):
            • Uso estricto de 'withContext(Dispatchers.IO)' para llamadas a la red.
            • Serialización JSON segura con kotlinx.serialization y converters de Retrofit.
            • Patrón Result: envolver respuestas en Success o Failure para un manejo resiliente ante desconexión o errores 500.
        """.trimIndent(),
        codeSnippetExample = """
            suspend fun fetchProjects(): Result<List<ProjectDto>> = withContext(Dispatchers.IO) {
                try {
                    val response = apiService.getProjects()
                    Result.success(response)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        """.trimIndent(),
        practicalChallenge = "Crea una función que consulte un endpoint REST y devuelva un mensaje de error amigable si ocurre timeout.",
        challengeStarterCode = """
            suspend fun safeApiCall(): Result<String> {
                return try { Result.success("OK") } catch(e: Exception) { Result.failure(e) }
            }
        """.trimIndent()
    ),
    ProgressiveModule(
        id = "mod_6_arquitectura",
        phaseNumber = 6,
        title = "6. Arquitectura & Clean Code (MVVM)",
        shortSummary = "Separación de responsabilidades: UI, ViewModel, Caso de Uso y Repositorio único.",
        iconEmoji = "🏛️",
        category = "Arquitectura",
        estimatedHours = 14,
        xpReward = 400,
        isCompleted = false,
        isLocked = false,
        theoryConcept = """
            Clean Architecture protege el núcleo de tu aplicación de cambios externos en librerías o frameworks:
            • Data Layer: DAO local y servicios remotos coordinados por el Repositorio.
            • Domain Layer: Casos de uso atómicos e independientes de Android.
            • Presentation Layer: ViewModels que exponen StateFlow<UiState> inmutable y reciben intents/eventos de usuario.
        """.trimIndent(),
        codeSnippetExample = """
            class MainViewModel(private val repository: ProjectRepository) : ViewModel() {
                private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
                val uiState = _uiState.asStateFlow()
                
                fun load() = viewModelScope.launch {
                    repository.getProjects().collect { data -> _uiState.value = UiState.Success(data) }
                }
            }
        """.trimIndent(),
        practicalChallenge = "Implementa un ViewModel con estado sealed class (Loading, Success, Error).",
        challengeStarterCode = """
            sealed interface ScreenState {
                object Loading : ScreenState
                data class Success(val items: List<String>) : ScreenState
                data class Error(val msg: String) : ScreenState
            }
        """.trimIndent()
    ),
    ProgressiveModule(
        id = "mod_7_proyecto_final",
        phaseNumber = 7,
        title = "7. Proyecto Final Capstone (Producción & IA)",
        shortSummary = "Integración completa: Gemini AI streaming, pasarela PRO verificada y despliegue CI/CD.",
        iconEmoji = "🚀",
        category = "Proyecto Final",
        estimatedHours = 20,
        xpReward = 600,
        isCompleted = false,
        isLocked = false,
        theoryConcept = """
            El hito final une todos los pilares de la ingeniería de software profesional:
            • Integración contextual con Gemini API mediante prompts estructurados y manejo de fallos offline.
            • Flujo de monetización seguro con verificación de recibos y estado de suscripción sincrónico.
            • Pipeline de GitHub Actions automatizado para compilar APKs de producción con Gradle.
        """.trimIndent(),
        codeSnippetExample = """
            // Sistema de verificación integral
            class CapstoneWorkflow(
                private val aiMentor: AiMentorService,
                private val billingManager: BillingManager
            ) {
                // Arquitectura completa lista para producción
            }
        """.trimIndent(),
        practicalChallenge = "Conecta la llamada a la IA con el estado del suscriptor y la persistencia local.",
        challengeStarterCode = """
            suspend fun runFinalDeployment(): Boolean {
                return true
            }
        """.trimIndent()
    )
)

@Composable
fun LearningRoadmapSection(
    onOpenLesson: (ProgressiveModule) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedModule by remember { mutableStateOf<ProgressiveModule?>(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Ruta de Aprendizaje Modular",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "7 Módulos Progresivos de Nivel Profesional",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = TechPrimary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "Fases 1 a 7",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        defaultProgressiveModules.forEach { mod ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (mod.isCompleted) TechSuccess.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { selectedModule = mod }
                    .testTag("roadmap_module_${mod.phaseNumber}")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (mod.isCompleted) TechSuccess.copy(alpha = 0.15f)
                                else TechPrimary.copy(alpha = 0.12f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = mod.iconEmoji, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mod.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (mod.isCompleted) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completado",
                                    tint = TechSuccess,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Text(
                            text = mod.shortSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (mod.isCompleted) TechSuccess.copy(alpha = 0.15f) else TechAccentGold.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "+${mod.xpReward} XP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (mod.isCompleted) TechSuccess else TechAccentGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${mod.estimatedHours}h est.",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    selectedModule?.let { module ->
        InteractiveLessonDialog(
            module = module,
            onDismiss = { selectedModule = null },
            onStartPractice = {
                selectedModule = null
                onOpenLesson(module)
            }
        )
    }
}

@Composable
fun InteractiveLessonDialog(
    module: ProgressiveModule,
    onDismiss: () -> Unit,
    onStartPractice: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Teoría, 1: Ejemplo Código, 2: Reto Práctico

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("interactive_lesson_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = module.iconEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Módulo ${module.phaseNumber}: ${module.category}",
                                style = MaterialTheme.typography.labelMedium,
                                color = TechPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = module.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Tabs (Teoría, Código, Reto)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf("📖 Teoría", "💻 Código", "🎯 Reto")
                    tabs.forEachIndexed { index, label ->
                        val isTabSelected = selectedTab == index
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isTabSelected) TechPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = index }
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isTabSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Content
                when (selectedTab) {
                    0 -> {
                        // Theory
                        Text(
                            text = "Conceptos Clave de la Lección",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = module.theoryConcept,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    1 -> {
                        // Code Example
                        Text(
                            text = "Ejemplo de Implementación en Producción",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0F172A),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = module.codeSnippetExample,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                    2 -> {
                        // Practice Challenge
                        Text(
                            text = "Objetivo Práctico del Reto",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = module.practicalChallenge,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0F172A),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = module.challengeStarterCode,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color(0xFF4ADE80),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom Action
                Button(
                    onClick = onStartPractice,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TechPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Practicar Reto en Code Studio", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
