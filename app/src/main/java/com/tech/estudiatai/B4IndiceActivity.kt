package com.tech.estudiatai

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.estudiatai.ui.theme.AppTheme

class B4IndiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Color de fondo del tema
                ) {
                    B4IndiceButtons()
                }
            }
        }
    }
}

@Composable
fun B4IndiceButtons() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showDropdown by remember { mutableStateOf(false) }

    // Lista de temas deshabilitados (índices de los botones 7 y 10)
    val disabledTopics = listOf(5, 6, 9)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .windowInsetsPadding(WindowInsets.navigationBars) // Ajusta según la barra de navegación
            .windowInsetsPadding(WindowInsets.statusBars) // Ajusta según la barra de estado
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior con título y botones de acción
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { (context as? Activity)?.finish() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = MaterialTheme.colorScheme.onSecondary, contentDescription = "Salir")

            }

            Text(
                text = "Índice",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSecondary
            )

            IconButton(onClick = { }) { /* Espaciador para centrar el título */ }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = context.getString(R.string.b4),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Datos de los botones
        val buttonData = listOf(
            context.getString(R.string.b4t1) to R.raw.b4t1apuntes,
            context.getString(R.string.b4t2) to R.raw.b4t2apuntes,
            context.getString(R.string.b4t3) to R.raw.b4t3apuntes,
            context.getString(R.string.b4t4) to R.raw.b4t4apuntes,
            context.getString(R.string.b4t5) to R.raw.b4t5apuntes,
            context.getString(R.string.b4t6) to R.raw.b4t6apuntes,
            context.getString(R.string.b4t7) to R.raw.b4t7apuntes,
            context.getString(R.string.b4t8) to R.raw.b4t8apuntes,
            context.getString(R.string.b4t9) to R.raw.b4t9apuntes,
            context.getString(R.string.b4t10) to R.raw.b4t10apuntes
        )

        buttonData.forEachIndexed { index, (label, resourceId) ->
            val isSelected = selectedIndex == index
            val isDisabled = disabledTopics.contains(index) // Comprobar si el tema está deshabilitado

            Button(
                shape = RoundedCornerShape(20),
                onClick = {
                    if (!isDisabled) { // Solo permitir clic si no está deshabilitado
                        if (isSelected) {
                            showDropdown = !showDropdown
                        } else {
                            selectedIndex = index
                            showDropdown = true
                        }
                    }else{
                        Toast.makeText(context, "¡Pronto estará disponible!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(60.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))

            ) {
                Text(label, modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.surface)
            }

            // Menú desplegable con animación
            AnimatedVisibility(visible = showDropdown && isSelected) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(context, ApuntesActivity::class.java)
                                intent.putExtra("HTML_RESOURCE_ID", resourceId)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Apuntes Icon",
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text("Apuntes", color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }

                        Button(
                            onClick = {
                                val intent = Intent(context, AjustesExamenActivity::class.java)
                                intent.putExtra("TITULO", label)
                                intent.putExtra("BLOCK_ID", 4)
                                intent.putExtra("TOPIC_ID", index)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Examen Icon",
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text("Examen", color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    }
                }

                // Ajuste de desplazamiento si el dropdown se abre cerca del final
                LaunchedEffect(showDropdown) {
                    if (showDropdown && isSelected) {
                        val scrollOffset = (index + 1) * 100 // Asegurar visibilidad del menú desplegable
                        scrollState.animateScrollTo(scrollOffset, animationSpec = tween(durationMillis = 300))
                    }
                }
            }
        }
    }
}
