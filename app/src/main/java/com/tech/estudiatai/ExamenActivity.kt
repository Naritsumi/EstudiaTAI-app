package com.tech.estudiatai

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.tech.estudiatai.ui.theme.AppTheme
import com.google.gson.JsonObject
import kotlinx.coroutines.delay

// Clases necesarias para poder leer la estructura json
data class Topic(
    val topicId: Int,
    val questions: List<Question>
)

data class Question(
    val questionId: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

class ExamenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val blockId = intent.getIntExtra("BLOCK_ID", 0)
        val topicId = intent.getIntExtra("TOPIC_ID", 0) + 1

        // Cargar preguntas desde el archivo JSON
        val questions = loadQuestionsFromAssets(blockId, topicId)
        val title = intent.getStringExtra("TITULO") ?: "EXAMEN"

        var timer = intent.getStringExtra("EXAM_DURATION")?.toInt() ?: 0
        timer *= 60
        val questionCount = intent.getStringExtra("QUESTION_COUNT")?.toInt() ?: 0

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Color de fondo del tema
                ) {
                    Examen(questions, title, timer, questionCount)
                }
            }
        }
    }

    /*
    private fun loadQuestionsFromAssets(blockId: Int, topicId: Int): List<Question> {
        return try {
            val inputStream = assets.open("questions.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Block>>() {}.type
            val blocks: List<Block> = Gson().fromJson(reader, type)

            // Filtrar el bloque específico
            val selectedBlock = blocks.find { it.blockId == blockId } ?: return emptyList()

            // Filtrar el tema específico dentro de ese bloque
            val selectedTopic = selectedBlock.topics.find { it.topicId == topicId } ?: return emptyList()

            // Retornar solo las preguntas de ese tema
            return selectedTopic.questions

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}*/

    private fun loadQuestionsFromAssets(blockId: Int, topicId: Int): List<Question> {
        return try {
            val inputStream = assets.open("block${blockId}/topic${topicId}.json")
            val reader = InputStreamReader(inputStream)

            // Leer como JsonObject
            val jsonObject = Gson().fromJson(reader, JsonObject::class.java)
            val questionsJson = jsonObject.getAsJsonArray("questions")

            // Convertir solo el array "questions" a List<Question>
            val type = object : TypeToken<List<Question>>() {}.type
            Gson().fromJson(questionsJson, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    @Composable
    fun Examen(
        questions: List<Question>,
        tituloTema: String,
        timer: Int,
        questionCount: Int
    ) {
        val context = LocalContext.current

        var currentQuestionIndex by remember { mutableIntStateOf(0) }
        var showResult by remember { mutableStateOf(false) }
        var correctAnswersCount by remember { mutableIntStateOf(0) }
        var tiempo by remember { mutableStateOf(timer) }
        var isPaused by remember { mutableStateOf(false) }
        var shouldExit by remember { mutableStateOf(false) }

        val userAnswers by remember { mutableStateOf(mutableListOf<Pair<Question, Int>>()) }
        var showFinalResults by remember { mutableStateOf(false) }

        // Estado para mantener las preguntas aleatorias seleccionadas una única vez
        var randomQuestions by remember { mutableStateOf<List<Question>>(emptyList()) }

        if (questions.isNotEmpty() && randomQuestions.isEmpty()) {
            // Mezclar las preguntas y tomar las `questionCount` preguntas aleatorias
            randomQuestions = questions.shuffled().take(questionCount)
        }

        val currentQuestion = randomQuestions[currentQuestionIndex]

        // Formatear el tiempo transcurrido
        val minutes = tiempo / 60
        val seconds = tiempo % 60
        val elapsedTimeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

        LaunchedEffect(tiempo, isPaused) {
            while (tiempo > 0 && !isPaused && !shouldExit) {
                delay(1000L) // Espera 1 segundo
                tiempo-- // Decrementa el tiempo

                if (tiempo == 0) {
                    // Cuando el tiempo llegue a 0, revisar las preguntas no contestadas
                    for (i in userAnswers.size until randomQuestions.size) {
                        // Marca las preguntas no contestadas con -1
                        userAnswers.add(Pair(randomQuestions[i], -1))
                    }

                    showResult = true
                }
            }
        }


        if (shouldExit) {
            (context as Activity).finish()
        } else if (!showResult && !showFinalResults) {
            QuestionScreen(
                question = currentQuestion,
                currentQuestionIndex = currentQuestionIndex,
                totalQuestions = randomQuestions.size,
                elapsedTime = elapsedTimeFormatted,
                isPaused = isPaused,
                onAnswerSelected = { selectedAnswerIndex ->
                    userAnswers.add(
                        Pair(
                            currentQuestion,
                            selectedAnswerIndex
                        )
                    ) // Almacenamos la respuesta
                    if (selectedAnswerIndex == currentQuestion.correctAnswer) correctAnswersCount++
                    if (currentQuestionIndex < randomQuestions.size - 1) {
                        currentQuestionIndex++
                    } else {
                        showResult = true
                    }
                },
                onPauseResume = { isPaused = !isPaused },
                examTitle = tituloTema,
                onExitConfirmed = { shouldExit = true },
                onRestartConfirmed = {
                    currentQuestionIndex = 0
                    correctAnswersCount = 0
                    tiempo = timer
                    userAnswers.clear()
                    showResult = false
                    showFinalResults = false
                },
                onFinishExam = { showFinalResults = true }
            )
        } else {
            // Aseguramos que pasamos las respuestas junto con las preguntas aleatorias a ResultScreen
            ResultScreen(correctAnswersCount, randomQuestions.size, userAnswers,
                randomQuestions = randomQuestions,
                onRestart = {
                    currentQuestionIndex = 0
                    correctAnswersCount = 0
                    tiempo = timer
                    userAnswers.clear()
                    showResult = false
                    showFinalResults = false
                },
                onFinish = {
                    finish()
                })
        }
    }

    @Composable
    fun QuestionScreen(
        question: Question,
        currentQuestionIndex: Int,
        totalQuestions: Int,
        elapsedTime: String,
        isPaused: Boolean,
        onAnswerSelected: (Int) -> Unit,
        onPauseResume: () -> Unit,
        onExitConfirmed: () -> Unit,
        onRestartConfirmed: () -> Unit,
        onFinishExam: () -> Unit,
        examTitle: String
    ) {

        var showExitDialog by remember { mutableStateOf(false) }
        var showRestartDialog by remember { mutableStateOf(false) }

        if (showExitDialog) {
            AlertDialogCustom(
                onDismissRequest = { showExitDialog = false },
                onConfirmation = {
                    showExitDialog = false
                    onExitConfirmed()
                },
                dialogTitle = "Salir",
                dialogText = "¿Seguro que quieres salir del examen? No podrás visualizar las estadísticas."
            )
        }

        if (showRestartDialog) {
            AlertDialogCustom(
                onDismissRequest = { showRestartDialog = false },
                onConfirmation = {
                    showRestartDialog = false
                    onRestartConfirmed()
                },
                dialogTitle = "Reiniciar",
                dialogText = "¿Seguro que quieres reiniciar el examen?"
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Fondo más claro y moderno
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Barra superior con título y botones de acción
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Salir",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Text(
                        text = examTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    IconButton(onClick = { showRestartDialog = true }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Reiniciar",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                // Información de la pregunta y tiempo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pregunta ${currentQuestionIndex + 1} de $totalQuestions",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = elapsedTime,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                // Enunciado de la pregunta
                Text(
                    text = question.question,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )

                // Opciones de respuesta
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    question.options.forEachIndexed { index, option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAnswerSelected(index) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Letra de la opción (A, B, C...) con un círculo de fondo
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${('A' + index)}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.surface
                                    )
                                }

                                // Texto de la opción
                                Text(
                                    text = option,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .weight(1f)
                                )
                            }
                        }
                    }
                }

                // Botones adicionales
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp), // Más separación con las opciones
                    verticalArrangement = Arrangement.spacedBy(12.dp), // Espaciado uniforme
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedButton(
                        onClick = {
                            // Registrar la pregunta como no contestada
                            onAnswerSelected(-1) // -1 indica que la pregunta no ha sido contestada
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Gray), // Borde para diferenciarlo
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary)
                    ) {
                        Text(
                            "No contestar",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }

                    Button(
                        onClick = { onFinishExam() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            "Terminar examen",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ResultScreen(
        correctAnswers: Int,
        totalQuestions: Int,
        userAnswers: List<Pair<Question, Int>>,
        randomQuestions: List<Question>,
        onRestart: () -> Unit,
        onFinish: () -> Unit
    ) {
        val incorrectAnswers = totalQuestions - correctAnswers
        var grade = correctAnswers - (0.33 * incorrectAnswers)

        if (grade < 0) {
            grade = 0.0
        }

        val context = LocalContext.current as? Activity

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 30.dp)
                .verticalScroll(scrollState), // Habilitamos scroll vertical
            verticalArrangement = Arrangement.spacedBy(8.dp)  // Reducir espacio entre los elementos
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onFinish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Salir",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Text(
                    text = "¡Resultados del examen!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSecondary
                )
                IconButton(onClick = { onRestart() }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Reiniciar",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            // Resumen de resultados
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Respuestas correctas: $correctAnswers de $totalQuestions",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Nota: ${"%.2f".format(grade)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            // Lista de respuestas corregidas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                randomQuestions.forEach { question ->  // Usamos randomQuestions en lugar de todas las preguntas
                    // Buscar la respuesta correspondiente de las preguntas
                    val userAnswer =
                        userAnswers.find { it.first.questionId == question.questionId }?.second
                            ?: -1
                    val isCorrect = userAnswer == question.correctAnswer
                    val isBlank = userAnswer == -1
                    val resultText = when {
                        isCorrect -> "✅ Correcta"
                        isBlank -> "❓ No contestada"
                        else -> "❌ Incorrecta"
                    }
                    val correctOption = question.options[question.correctAnswer]

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isCorrect -> Color(0xFFE0F7FA) // Azul claro para correctas
                                isBlank -> Color(0xFFE0E0E0) // Gris para no contestadas
                                else -> Color(0xFFFFEBEE) // Rojo claro para incorrectas
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = question.question,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (!isBlank) {
                                Text(
                                    text = "Tu respuesta: ${question.options[userAnswer]}",
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Text(
                                text = "Respuesta correcta: $correctOption",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = resultText,
                                fontSize = 15.sp,
                                color = when {
                                    isCorrect -> MaterialTheme.colorScheme.secondary
                                    isBlank -> Color.LightGray
                                    else -> MaterialTheme.colorScheme.error
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AlertDialogCustom(
        onDismissRequest: () -> Unit,
        onConfirmation: () -> Unit,
        dialogTitle: String,
        dialogText: String,
    ) {
        AlertDialog(
            title = {
                Text(
                    text = dialogTitle,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            },
            text = {
                Text(
                    text = dialogText,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = TextStyle(
                        fontSize = 18.sp // Aumenta el tamaño de la fuente para el texto
                    )
                )
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("SÍ")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("NO")
                }
            }
        )
    }
}
