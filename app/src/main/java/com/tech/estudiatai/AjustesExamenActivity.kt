package com.tech.estudiatai

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.tech.estudiatai.ui.theme.AppTheme

class AjustesExamenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("TITULO") ?: "EXAMEN"
        val block = intent.getIntExtra("BLOCK_ID", 0)
        val topic = intent.getIntExtra("TOPIC_ID", 0)

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        setContent {
            AppTheme {
                SettingsScreen(
                    title,
                    block,
                    topic,
                    onFinish = { finish() }
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(tituloTema: String, block: Int, topic: Int, onFinish: () -> Unit) {
    var examDuration by remember { mutableStateOf(TextFieldValue("3")) }
    var questionCount by remember { mutableStateOf(TextFieldValue("10")) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val additionalInfo =
        "- Cuestionario tipo test con cuatro opciones y una única respuesta\n" +
                "- Las respuestas correctas valen 1 punto.\n" +
                "- Las respuestas incorrectas se penalizan con 1/3 del valor de una respuesta correcta.\n" +
                "- Las respuestas en blanco no penalizan.\n" +
                "- El examen se corregirá al finalizar, mostrando los resultados, o al pasar el tiempo establecido.\n"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background//Color(0xFFF5F5F5)  // Fondo más claro para el Scaffold
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra superior con título y botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onFinish() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Salir", tint = MaterialTheme.colorScheme.onSecondary)
                }

                // Tema del examen
                Text(
                    text = tituloTema,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {  }) {
                    // Dejo esto vacío porque así dejo el texto de arriba centrado
                    // ...
                }
            }

            // Configurar el examen
            Text(
                text = "Configura los ajustes del examen:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            var isError by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }
            // Campo para configurar la duración del examen
            OutlinedTextField(
                value = examDuration,
                onValueChange = {
                     //examDuration = it
                        newValue ->
                    val textValue = newValue.text // Extraer el texto ingresado
                    val intValue = textValue.toIntOrNull() // Convertir a número si es posible

                    if (textValue.isEmpty()) {
                        // Permitir que el usuario borre
                        examDuration = newValue
                        isError = false
                        errorMessage = ""
                    } else if (intValue != null && intValue in 1..30) {
                        // Si el valor es válido
                        examDuration = newValue
                        isError = false
                        errorMessage = ""
                    } else {
                        // Si el número es inválido, error
                        isError = true
                        errorMessage = "El tiempo máximo son 30 minutos"
                    }
                                },
                label = { Text("Duración del examen (minutos)") },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Icono de timer") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError, // Muestra el borde rojo si hay error
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color.White
                )
            )

            if (isError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            var isErrorQuestion by remember { mutableStateOf(false) }
            var errorMessageQuestion by remember { mutableStateOf("") }
            // Campo para configurar el número de preguntas
            OutlinedTextField(
                value = questionCount,
                onValueChange = { newValue ->
                    val textValue = newValue.text // Extraer el texto ingresado
                    val intValue = textValue.toIntOrNull() // Convertir a número si es posible

                    if (textValue.isEmpty()) {
                        // Permitir que el usuario borre
                        questionCount = newValue
                        isErrorQuestion = false
                        errorMessageQuestion = ""
                    } else if (intValue != null && intValue in 1..15) {
                        // Si el valor es válido
                        questionCount = newValue
                        isErrorQuestion = false
                        errorMessageQuestion = ""
                    } else {
                        // Si el número es inválido, error
                        isErrorQuestion = true
                        errorMessageQuestion = "El número de preguntas debe ser entre 1 y 15"
                    }
                },
                label = { Text("Número de preguntas") },
                leadingIcon = {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Icono de preguntas")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isErrorQuestion, // Muestra el borde rojo si hay error
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = if (isErrorQuestion) MaterialTheme.colorScheme.error else Color(0xFFCCCCCC)
                )
            )

            if (isErrorQuestion) {
                Text(
                    text = errorMessageQuestion,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            // Para enviar el botón al final
            //Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val intent = Intent(context, ExamenActivity::class.java)
                    intent.putExtra("TITULO", tituloTema)
                    intent.putExtra("EXAM_DURATION", examDuration.text)
                    intent.putExtra("QUESTION_COUNT", questionCount.text)
                    intent.putExtra("BLOCK_ID", block)
                    intent.putExtra("TOPIC_ID", topic)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Información adicional
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)//CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "A tener en cuenta:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = additionalInfo,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}
