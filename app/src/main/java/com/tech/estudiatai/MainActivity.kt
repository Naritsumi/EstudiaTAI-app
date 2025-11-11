package com.tech.estudiatai

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.estudiatai.ui.theme.AppTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    //private var isDarkTheme = mutableStateOf(false) // Estado global para alternar el tema

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var showUpdateDialog by remember { mutableStateOf(false) }

            // Verificar actualizaciones al iniciar
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    val prefs = this@MainActivity.dataStore.data.first()
                    val checkUpdates = prefs[CHECK_UPDATES_KEY] ?: false

                    if (checkUpdates) {
                        val update = UpdateChecker.checkForUpdates(BuildConfig.VERSION_NAME)
                        if (update != null) {
                            showUpdateDialog = true
                        }
                    }
                }
            }

            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )

                    // Actualización
                    if (showUpdateDialog) {
                        UpdateDialog(
                            onDismiss = { showUpdateDialog = false },
                            onUpdate = {
                                // Abro la web directamente
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://estudiatai.es/".toUri()
                                )
                                startActivity(intent)
                                showUpdateDialog = false
                            }
                        )
                    }
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GreetingText()
            Spacer(modifier = Modifier.height(32.dp))
            ButtonsSection()
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Aplicación open-source v${BuildConfig.VERSION_NAME}.\nBasado en la convocatoria 2024.",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun UpdateDialog(
    onDismiss: () -> Unit,
    onUpdate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Info,
                contentDescription = "Actualización disponible"
            )
        },
        title = {
            Text(
                text = "Nueva versión disponible",
                color = Color.Black
            )
        },
        text = {
            Column {
                Text(
                    text = "Hay una nueva versión disponible. Pulsa Aceptar para descargarla desde la web.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Versión actual: ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        },
        confirmButton = {
            Button(onClick = onUpdate) {
                Text("Descargar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Más tarde")
            }
        }
    )
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
        Spacer(modifier = Modifier.height(18.dp))
        CustomButton("Sobre la app",
            onClick = {
                val intent = Intent(context, ReadMeActivity::class.java)
                context.startActivity(intent)
            })
    }
}

@SuppressLint("UseKtx")
@Composable
fun KoFiAndCoffeeButtons() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val buttonModifier = Modifier
            .width(160.dp) // un poco más compacto, mejor equilibrio visual
            .height(56.dp)
            .shadow(5.dp, shape = RoundedCornerShape(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, "https://ko-fi.com/tech_racoon".toUri()))
            },
            modifier = buttonModifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.kofi_symbol),
                contentDescription = "Ko-fi",
                modifier = Modifier.size(20.dp)

            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Ko-fi", fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Button(
            onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, "https://buymeacoffee.com/tech_racoon".toUri()))
            },
            modifier = buttonModifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bmc_logo),
                contentDescription = "Buy Me a Coffee",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Invítame a un café", fontSize = 15.sp)
        }
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


/*@Composable
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
}*/