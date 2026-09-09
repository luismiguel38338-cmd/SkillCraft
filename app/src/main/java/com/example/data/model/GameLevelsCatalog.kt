package com.example.data.model

object GameLevelsCatalog {

    val worlds = listOf(
        GameWorld(
            id = 1,
            name = "Bosque de Variables y Tipos",
            subtitle = "Fundamentos del lenguaje, val, var y seguridad de tipos",
            iconEmoji = "🌲",
            startLevel = 1,
            endLevel = 50,
            colorHex = 0xFF10B981L
        ),
        GameWorld(
            id = 2,
            name = "Mazmorra de la Lógica",
            subtitle = "Condicionales, operadores booleanos y expresiones when",
            iconEmoji = "🏰",
            startLevel = 51,
            endLevel = 100,
            colorHex = 0xFF6366F1L
        ),
        GameWorld(
            id = 3,
            name = "Laberinto de Bucles",
            subtitle = "Colecciones, iteraciones, transformaciones map y filter",
            iconEmoji = "🌀",
            startLevel = 101,
            endLevel = 150,
            colorHex = 0xFF0EA5E9L
        ),
        GameWorld(
            id = 4,
            name = "Cumbre de Funciones",
            subtitle = "Funciones puras, lambdas, parámetros por defecto y orden superior",
            iconEmoji = "⛰️",
            startLevel = 151,
            endLevel = 200,
            colorHex = 0xFFF59E0BL
        ),
        GameWorld(
            id = 5,
            name = "Ciudadela de Clases y OOP",
            subtitle = "Data classes, inmutabilidad, enums y sealed interfaces",
            iconEmoji = "🛡️",
            startLevel = 201,
            endLevel = 250,
            colorHex = 0xFFEC4899L
        ),
        GameWorld(
            id = 6,
            name = "Torre de Jetpack Compose",
            subtitle = "UI declarativa, remember, state hoisting, modifiers y layouts",
            iconEmoji = "📱",
            startLevel = 251,
            endLevel = 300,
            colorHex = 0xFF8B5CF6L
        ),
        GameWorld(
            id = 7,
            name = "Caverna de Persistencia Room",
            subtitle = "Bases de datos SQLite, Entity, DAO y flujos asíncronos Flow",
            iconEmoji = "🗄️",
            startLevel = 301,
            endLevel = 350,
            colorHex = 0xFF14B8A6L
        ),
        GameWorld(
            id = 8,
            name = "Dimensión Cloud & Inteligencia Artificial",
            subtitle = "Coroutines, Dispatchers.IO, Retrofit, Gemini AI y producción",
            iconEmoji = "🚀",
            startLevel = 351,
            endLevel = 400,
            colorHex = 0xFFF43F5EL
        )
    )

    private val levelCache = mutableMapOf<Int, GameLevel>()

    fun getLevel(levelNumber: Int): GameLevel {
        val clamped = levelNumber.coerceIn(1, 400)
        return levelCache.getOrPut(clamped) {
            generateLevel(clamped)
        }
    }

    private fun generateLevel(lvl: Int): GameLevel {
        val world = worlds.firstOrNull { lvl in it.startLevel..it.endLevel } ?: worlds.first()
        val indexInWorld = lvl - world.startLevel + 1 // 1..50

        return when (world.id) {
            1 -> generateWorld1Level(lvl, indexInWorld, world)
            2 -> generateWorld2Level(lvl, indexInWorld, world)
            3 -> generateWorld3Level(lvl, indexInWorld, world)
            4 -> generateWorld4Level(lvl, indexInWorld, world)
            5 -> generateWorld5Level(lvl, indexInWorld, world)
            6 -> generateWorld6Level(lvl, indexInWorld, world)
            7 -> generateWorld7Level(lvl, indexInWorld, world)
            else -> generateWorld8Level(lvl, indexInWorld, world)
        }
    }

    // Mundo 1: Variables y Tipos (1..50)
    private fun generateWorld1Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val titles = listOf(
            "El Despertar de Val: Declarando tu Primera Constante",
            "La Antorcha Mutable: Usando Var con Precaución",
            "Cristal de Vida: Tipos Enteros Int",
            "Manto de Nombres: Cadenas de Texto String",
            "Monedas de Oro: Tipos de Punto Flotante Double",
            "El Orbe de la Verdad: Booleanos true y false",
            "El Escudo Protector: Tipos Nullables (?)",
            "El Puente Seguro: El Operador Elvis (?:)",
            "Llamada Segura: Navegación Segura (?.)",
            "Jefe Menor: El Centinela de los Tipos",
            "Inferencia de Tipos Mágica",
            "Template Strings con Expresiones Simples",
            "Template Strings con Expresiones Complejas",
            "Conversión de Cadena a Entero toInt()",
            "Conversión de Entero a Doble toDouble()",
            "Conversión Segura toIntOrNull()",
            "Declaración Múltiple de Variables",
            "Constantes en Tiempo de Compilación const val",
            "Límites de Tipos: Int.MAX_VALUE",
            "Precisión Numérica con Long",
            "Cálculo de Daño de Ataque",
            "Cálculo de Regeneración de Maná",
            "El Talismán de Caracteres Char",
            "Escapado de Cadenas Especiales",
            "Cadenas Multilínea con TrimIndent",
            "Jefe de Zona: El Guardián de la Memoria",
            "Variables 'lazy' de Inicialización Perezosa",
            "Inicialización Tardía con lateinit var",
            "Comprobación de Inicialización ::var.isInitialized",
            "Tipos Primitivos vs Objetos en Kotlin",
            "Asignaciones Compuestas (+=, -=)",
            "Incrementos y Decrementos (++heroLevel)",
            "Operador Unario Negativo",
            "Manejo de Booleanos Negados (!isSleeping)",
            "Comparación de Referencia (===) vs Valor (==)",
            "Inmutabilidad de Colecciones Base",
            "Propiedades de Solo Lectura",
            "Sobrecarga de Asignación",
            "Conversión de Tipos Explícita",
            "Jefe de Zona: El Mago de la Inmutabilidad",
            "Cálculo de Puntos de Experiencia",
            "Formato Numérico Moneda",
            "Variables Globales vs Variables Locales",
            "Ámbito de Bloques y Sombra de Variables",
            "Tipos Any y Nothing en Kotlin",
            "El Misterio del Tipo Unit",
            "Variables con Getters Personalizados",
            "Variables con Setters Privados",
            "Prueba de Maestría en Tipado",
            "Gran Jefe del Bosque: El Titán de las Variables"
        )

        val title = titles.getOrElse(index - 1) { "Maestría en Variables Parte $index" }

        val starter = when (index) {
            1 -> "// Declara una variable inmutable llamada heroName con el valor 'Heroe'\nval heroName = \"Heroe\""
            2 -> "// Declara una variable mutable llamada health con valor inicial 100\nvar health = 100"
            3 -> "// Declara un entero con tipo explícito\nval maxMana: Int = 250"
            7 -> "// Declara un String que permita valores nulos usando ?\nval shield: String? = null"
            8 -> "// Usa el operador Elvis para asignar un valor por defecto si es nulo\nval weapon = optionalWeapon ?: \"Espada de Madera\""
            else -> "// Nivel $lvl: Resuelve el reto de variables\nval levelCode = $lvl\nval status = \"READY\""
        }

        val required = when (index) {
            1 -> listOf("val", "heroName")
            2 -> listOf("var", "health")
            3 -> listOf("Int", "maxMana")
            7 -> listOf("String?", "shield")
            8 -> listOf("?:")
            else -> listOf("val")
        }

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "Variables",
            npcDialogue = "¡Hola recluta! Soy Byte, tu tutor robot. En este nivel ($lvl) aprenderemos: '$title'. En Kotlin, 'val' crea una referencia que no se puede reasignar. ¡Eso evita incontables bugs!",
            npcHint = "Recuerda que la sintaxis básica es: val nombreVariable: Tipo = valor. ¡Tú puedes!",
            npcCelebMessage = "¡Fabuloso trabajo, aprendiz! Has dominado este secreto del Bosque de Variables. ¡Tu código es impecable!",
            starterCode = starter,
            requiredKeywords = required,
            testDescription = "Verifica que la variable cumpla con la convención de tipos y asignación inmutable.",
            testInput = "val target = $lvl",
            expectedOutput = "ASIGNADO_CORRECTAMENTE",
            xpReward = 40 + (index * 2),
            coinsReward = 15 + index,
            gemsReward = if (index % 10 == 0) 15 else 5
        )
    }

    // Mundo 2: Lógica y Condicionales (51..100)
    private fun generateWorld2Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val title = "Desafío Lógico $index: " + when (index) {
            1 -> "El Portal Condicional if - else"
            2 -> "If como Expresión que Retorna Valor"
            3 -> "El Oráculo when: Reemplazando Switch"
            4 -> "when con Múltiples Casos por Rama"
            5 -> "Rangos de Números con 'in 1..10'"
            6 -> "Operadores Lógicos AND (&&) y OR (||)"
            10 -> "Jefe Menor: El Esfinge de las Decisiones"
            25 -> "when sin Argumentos como Cadena Lógica"
            50 -> "Gran Jefe de la Mazmorra: El Amo de los Algoritmos"
            else -> "Lógica Condicional Fase $index"
        }

        val starter = """
            fun evaluateGate(power: Int): String {
                // Escribe una expresión if/else o when
                return if (power >= 50) "PASSED" else "BLOCKED"
            }
        """.trimIndent()

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "Lógica",
            npcDialogue = "¡Cuidado con las trampas lógicas! Soy Byte. En el nivel $lvl exploramos cómo tomar decisiones en código. En Kotlin, 'if' y 'when' son expresiones poderosas que devuelven resultados.",
            npcHint = "Usa 'when (variable)' o 'if (condicion) valorA else valorB'.",
            npcCelebMessage = "¡Victoria brillante! Tu mente lógica resolvió el dilema sin pestañear. ¡Avanzamos con paso firme!",
            starterCode = starter,
            requiredKeywords = listOf("if", "return"),
            testDescription = "Evalúa que la condición devuelva la respuesta esperada ante valores mayores o menores a 50.",
            testInput = "evaluateGate(75)",
            expectedOutput = "PASSED",
            xpReward = 60 + index,
            coinsReward = 20 + index,
            gemsReward = if (index % 10 == 0) 20 else 6
        )
    }

    // Mundo 3: Bucles y Colecciones (101..150)
    private fun generateWorld3Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val title = "Laberinto de Datos $index: " + when (index) {
            1 -> "Listas Inmutables listOf()"
            2 -> "Listas Mutables mutableListOf()"
            3 -> "Bucle for con Iterador Simple"
            4 -> "Bucle for con Índices withIndex()"
            5 -> "Transformación Mágica con map()"
            6 -> "Filtrado Selectivo con filter()"
            7 -> "Verificación Rápida any() y all()"
            10 -> "Jefe Menor: El Señor de las Matrices"
            25 -> "Agrupación de Datos con groupBy()"
            50 -> "Gran Jefe del Laberinto: El Coloso de las Colecciones"
            else -> "Procesamiento de Listas $index"
        }

        val starter = """
            fun filterActiveSpells(spells: List<String>): List<String> {
                // Filtra los hechizos que contengan más de 4 letras
                return spells.filter { it.length > 4 }
            }
        """.trimIndent()

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "Colecciones",
            npcDialogue = "¡Entrando al Laberinto de Bucles! Byte aquí para recordarte que la programación moderna prefiere operaciones funcionales como map y filter en vez de forzar bucles manuales.",
            npcHint = "La lambda recibe cada elemento como 'it'. Por ejemplo: lista.filter { it > 10 }.",
            npcCelebMessage = "¡Extraordinario! Dominas las colecciones con elegancia digna de un arquitecto sénior.",
            starterCode = starter,
            requiredKeywords = listOf("filter", "return"),
            testDescription = "Comprueba que la lista resultante contenga únicamente elementos válidos.",
            testInput = "listOf('Fuego', 'Luz', 'Relampago')",
            expectedOutput = "[Fuego, Relampago]",
            xpReward = 75 + index,
            coinsReward = 25 + index,
            gemsReward = if (index % 10 == 0) 25 else 7
        )
    }

    // Mundo 4: Funciones y Lambdas (151..200)
    private fun generateWorld4Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val title = "Cumbre Funcional $index: " + when (index) {
            1 -> "Declaración de Funciones fun"
            2 -> "Parámetros con Valores por Defecto"
            3 -> "Llamadas con Argumentos Nombrados"
            4 -> "Funciones de Expresión Única (=)"
            5 -> "Lambdas como Tipos de Primera Clase"
            6 -> "Funciones de Extensión Personalizadas"
            10 -> "Jefe Menor: El Alquimista de Funciones"
            50 -> "Gran Jefe de la Cumbre: El Archimago de las Lambdas"
            else -> "Arquitectura de Funciones $index"
        }

        val starter = """
            fun calculatePower(base: Int, multiplier: Int = 2): Int {
                // Calcula el poder con valor por defecto
                return base * multiplier
            }
        """.trimIndent()

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "Funciones",
            npcDialogue = "¡Hemos ascendido a la Cumbre de Funciones! Byte te enseña: una buena función debe hacer una sola cosa y hacerla extraordinariamente bien.",
            npcHint = "Usa parámetros con valor por defecto: fun test(param: Int = 10): Int = param * 2.",
            npcCelebMessage = "¡Sublime! Tus funciones son puras, modulares y fáciles de testear. ¡Subes de nivel de habilidad!",
            starterCode = starter,
            requiredKeywords = listOf("fun", "return"),
            testDescription = "Verifica la ejecución de la función tanto con parámetros por defecto como personalizados.",
            testInput = "calculatePower(10)",
            expectedOutput = "20",
            xpReward = 90 + index,
            coinsReward = 30 + index,
            gemsReward = if (index % 10 == 0) 30 else 8
        )
    }

    // Mundo 5: Clases y OOP (201..250)
    private fun generateWorld5Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val title = "Ciudadela de Clases $index: " + when (index) {
            1 -> "Modelado con data class"
            2 -> "Inmutabilidad y método copy()"
            3 -> "Enumeraciones con enum class"
            4 -> "Polimorfismo con sealed interface"
            5 -> "El Patrón Singleton con object"
            10 -> "Jefe Menor: El Guardián del Estado"
            50 -> "Gran Jefe de la Ciudadela: El Rey de los Patrones de Diseño"
            else -> "Modelado de Datos $index"
        }

        val starter = """
            data class Player(val id: String, val level: Int) {
                fun levelUp(): Player = copy(level = level + 1)
            }
        """.trimIndent()

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "OOP",
            npcDialogue = "¡Bienvenidos a la Ciudadela! Soy Byte. Las data classes de Kotlin generan automáticamente equals, hashCode, toString y el crucial método copy().",
            npcHint = "Para clonar un objeto con cambios usa: miObjeto.copy(campoModificado = nuevoValor).",
            npcCelebMessage = "¡Maestría arquitectónica! Tus clases son sólidas, inmutables y preparadas para el desarrollo empresarial.",
            starterCode = starter,
            requiredKeywords = listOf("data class", "copy"),
            testDescription = "Comprueba que la data class soporte copia inmutable sin modificar la instancia previa.",
            testInput = "Player('1', 5).levelUp()",
            expectedOutput = "Player(id=1, level=6)",
            xpReward = 110 + index,
            coinsReward = 35 + index,
            gemsReward = if (index % 10 == 0) 35 else 9
        )
    }

    // Mundo 6: Jetpack Compose (251..300)
    private fun generateWorld6Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val title = "Torre Compose $index: " + when (index) {
            1 -> "Tu Primer Composable @Composable"
            2 -> "Preservando Estado con remember"
            3 -> "State Hoisting: Elevar el Estado"
            4 -> "Layouts: Column, Row y Box"
            5 -> "Modifiers: Padding, Size y Clickable"
            10 -> "Jefe Menor: El Artista Declarativo"
            50 -> "Gran Jefe de la Torre: El Diseñador de Sistemas UI"
            else -> "Componente Reactivo $index"
        }

        val starter = """
            @Composable
            fun StatCard(label: String, value: Int) {
                // Diseña un componente con Row o Column y Text
            }
        """.trimIndent()

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "Compose",
            npcDialogue = "¡Estamos en la Torre de Jetpack Compose! La interfaz gráfica ahora es una función matemática de tu estado. Si el estado cambia, la UI se recompone sola.",
            npcHint = "Recuerda usar @Composable antes de la declaración de la función.",
            npcCelebMessage = "¡Fascinante! Tu UI es moderna, reactiva y sigue estrictamente las directrices de Material 3.",
            starterCode = starter,
            requiredKeywords = listOf("@Composable", "fun"),
            testDescription = "Valida que la función composable acepte estado y no genere efectos secundarios no controlados.",
            testInput = "StatCard('HP', 100)",
            expectedOutput = "COMPOSED_SUCCESSFULLY",
            xpReward = 130 + index,
            coinsReward = 40 + index,
            gemsReward = if (index % 10 == 0) 40 else 10
        )
    }

    // Mundo 7: Room y Persistencia (301..350)
    private fun generateWorld7Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val title = "Caverna Room $index: " + when (index) {
            1 -> "Definiendo una Entidad @Entity"
            2 -> "Clave Primaria con @PrimaryKey"
            3 -> "Data Access Object @Dao"
            4 -> "Consultas SQL @Query reactivas con Flow"
            5 -> "Operaciones Suspensibles @Insert y @Delete"
            10 -> "Jefe Menor: El Minero de SQLite"
            50 -> "Gran Jefe de la Caverna: El Custodio de los Datos"
            else -> "Persistencia SQL $index"
        }

        val starter = """
            @Dao
            interface HeroDao {
                @Query("SELECT * FROM heroes WHERE level >= :minLevel")
                fun getEliteHeroes(minLevel: Int): Flow<List<HeroEntity>>
            }
        """.trimIndent()

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = "Room",
            npcDialogue = "¡En las profundidades de la Caverna Room! Byte te guía: Room verifica tus consultas SQL en tiempo de compilación para que nunca tengas errores de sintaxis en runtime.",
            npcHint = "Para devolver flujos reactivos no uses suspend en el método del DAO, retorna directamente Flow<List<T>>.",
            npcCelebMessage = "¡Impresionante! Tus datos están protegidos, indexados y fluyen reactivamente a la UI.",
            starterCode = starter,
            requiredKeywords = listOf("@Dao", "@Query"),
            testDescription = "Comprueba que el DAO exponga consultas reactivas verificadas.",
            testInput = "HeroDao.getEliteHeroes(10)",
            expectedOutput = "QUERY_COMPILED",
            xpReward = 150 + index,
            coinsReward = 45 + index,
            gemsReward = if (index % 10 == 0) 45 else 12
        )
    }

    // Mundo 8: Cloud, Coroutines & AI (351..400)
    private fun generateWorld8Level(lvl: Int, index: Int, world: GameWorld): GameLevel {
        val isFinalBoss = lvl == 400
        val title = if (isFinalBoss) {
            "Nivel 400: La Batalla Final - El Arquitecto Maestro Full-Stack & IA"
        } else {
            "Dimensión Cloud $index: " + when (index) {
                1 -> "Funciones Suspensibles suspend fun"
                2 -> "Cambiando de Hilo con Dispatchers.IO"
                3 -> "Consumo de API con Retrofit y Ktor"
                4 -> "Integración con Gemini API para Asistencia"
                5 -> "StateFlow en ViewModels UDF"
                10 -> "Jefe Menor: El Orquestador Concurrente"
                else -> "Cloud & Inteligencia Artificial $index"
            }
        }

        val starter = if (isFinalBoss) {
            """
                // NIVEL 400: Implementa la arquitectura completa que conecta
                // Room Database, ViewModel, Coroutines y Gemini AI Mentor
                class MasterFullStackCapstone(
                    private val aiService: AiMentorService,
                    private val db: AppDatabase
                ) {
                    suspend fun orchestrateProductionSystem(): Boolean {
                        return true
                    }
                }
            """.trimIndent()
        } else {
            """
                suspend fun fetchCloudData(): Result<String> = withContext(Dispatchers.IO) {
                    try {
                        Result.success("CLOUD_DATA_OK")
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }
            """.trimIndent()
        }

        return GameLevel(
            levelNumber = lvl,
            worldId = world.id,
            worldName = world.name,
            worldThemeColor = world.colorHex,
            title = title,
            conceptTag = if (isFinalBoss) "CAPSTONE" else "Cloud & IA",
            npcDialogue = if (isFinalBoss) {
                "¡HEMOS LLEGADO AL NIVEL 400! 👑 Soy Byte, y he sido testigo de tu evolución desde que declaraste tu primera variable 'val' en el nivel 1. Ahora te enfrentas al reto definitivo: orquestar una aplicación completa de producción con IA."
            } else {
                "¡Estamos en la Dimensión Cloud! Las corrutinas de Kotlin permiten concurrencia ligera sin bloquear la interfaz de usuario. Nunca hagas operaciones pesadas en Dispatchers.Main."
            },
            npcHint = if (isFinalBoss) {
                "Combina ViewModel + Dispatchers.IO + Result<T> + Flow para una arquitectura inquebrantable."
            } else {
                "Usa 'withContext(Dispatchers.IO)' para llamadas a la red o APIs remotas."
            },
            npcCelebMessage = if (isFinalBoss) {
                "¡¡¡ERES UN MAESTRO DE LA PROGRAMACIÓN!!! 🏆🎉 Has conquistado los 400 niveles de SkillCraft. Has demostrado perseverancia, excelencia técnica y mentalidad de artesano del software. ¡El mundo del código es tuyo!"
            } else {
                "¡Espectacular! Conectas la nube y la inteligencia artificial con total destreza. ¡La victoria está cerca!"
            },
            starterCode = starter,
            requiredKeywords = if (isFinalBoss) listOf("suspend", "class") else listOf("suspend", "Dispatchers"),
            testDescription = if (isFinalBoss) "Ejecución del pipeline completo de producción capstone 400." else "Comprueba la llamada asíncrona segura sin excepciones no controladas.",
            testInput = if (isFinalBoss) "MasterFullStackCapstone.orchestrateProductionSystem()" else "fetchCloudData()",
            expectedOutput = if (isFinalBoss) "LEVEL_400_CONQUERED" else "Result.success(CLOUD_DATA_OK)",
            xpReward = if (isFinalBoss) 1000 else 180 + index,
            coinsReward = if (isFinalBoss) 500 else 50 + index,
            gemsReward = if (isFinalBoss) 100 else 15
        )
    }
}
