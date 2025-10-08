package com.tech.estudiatai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
class MainActivity : ComponentActivity() {
    //private var isDarkTheme = mutableStateOf(false) // Estado global para alternar el tema

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            // Pasa el estado actual del tema a EstudiaTAITheme
            AppTheme { //darkTheme = isDarkTheme.value
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background) //Por fin color del fondo
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                        //isDarkTheme = isDarkTheme.value,
                        //onToggleTheme = { isDarkTheme.value = !isDarkTheme.value } // Alterna el tema
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    //isDarkTheme: Boolean, // Estado del tema
    //onToggleTheme: () -> Unit // Callback para alternar el tema
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GreetingText()
            Spacer(modifier = Modifier.height(32.dp))
            ButtonsSection()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aplicación open-source. Basado en la convocatoria 2024.",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSecondary
            )
            //Spacer(modifier = Modifier.height(16.dp))
            // Para versiones futuras:
            //ToggleThemeButton(isDarkTheme = isDarkTheme, onToggleTheme = onToggleTheme)
        }
    }
}

@Composable
// Queda pendiente:
fun ToggleThemeButton(isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    Button(
        onClick = onToggleTheme,
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) Color.White else Color.DarkGray
        )
    ) {
        Text(
            text = if (isDarkTheme) "☀\uFE0F" else "\uD83C\uDF19",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GreetingText() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "¡Hola! \uD83D\uDC4B ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = "¿Qué vas a estudiar hoy?",
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary

        )
    }
}

@Composable
fun ButtonsSection() {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        CustomButton("\uD83D\uDCDA " + context.getString(R.string.b1),
            onClick = {
                showToast(context)
            })
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton("\uD83D\uDCBD " + context.getString(R.string.b2),
            onClick = {
                val intent = Intent(context, B2IndiceActivity::class.java)
                context.startActivity(intent)
            })
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton("\uD83E\uDDD1\u200D\uD83D\uDCBB " + context.getString(R.string.b3),
            onClick = {
                val intent = Intent(context, B3IndiceActivity::class.java)
                context.startActivity(intent)
            })
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton("\uD83E\uDD16 " + context.getString(R.string.b4),
            onClick = {
                val intent = Intent(context, B4IndiceActivity::class.java)
                context.startActivity(intent)
            })
        //Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = "Apoya el desarrollo ❤️",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        KoFiAndCoffeeButtons() // reutiliza tu composable existente
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton("Sobre la app",
            onClick = {
                val intent = Intent(context, ReadMeActivity::class.java)
                context.startActivity(intent)
            })
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    AppTheme{
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Color de fondo del botón
                contentColor = MaterialTheme.colorScheme.surface // Color del texto
            )
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun showToast(context: android.content.Context) {
    Toast.makeText(context, "¡Pronto estará disponible!", Toast.LENGTH_SHORT).show()
}