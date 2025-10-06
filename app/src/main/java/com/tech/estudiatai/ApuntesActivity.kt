package com.tech.estudiatai

import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.tech.estudiatai.ui.theme.AppTheme

class ApuntesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val htmlResourceId = intent.getIntExtra("HTML_RESOURCE_ID", -1)

        if (htmlResourceId == -1) {
            // No se pasó un recurso válido, cerramos la actividad
            finish()
            return
        }

        // Por si quiero controlar la salida del usuario dejo esto preparado
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Color de fondo del tema
                ) {
                    ApuntesContent(htmlResourceId, onFinish = { finish() })
                }
            }
        }
    }
}

// Función para cargar el archivo HTML desde los recursos de forma eficiente
fun loadHtmlFromFileApuntes(context: Context, resourceId: Int): String {
    val inputStream = context.resources.openRawResource(resourceId)
    return inputStream.bufferedReader().use { it.readText() }
}

@Composable
fun ApuntesContent(resourceId: Int, onFinish: () -> Unit) {
    val context = LocalContext.current
    val htmlText = loadHtmlFromFileApuntes(context, resourceId) // Cargar archivo HTML dinámico

    // WebView que mostrará el HTML con sus estilos
    Column(modifier = Modifier.padding(20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onFinish() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Salir", tint = MaterialTheme.colorScheme.onSecondary)
            }

            // Tema del examen
            Text(
                text = "Apuntes",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSecondary
            )

            IconButton(onClick = {  }) {
                // Dejo esto vacío porque así dejo el texto de arriba centrado
                // ...
            }
        }
        AndroidView(factory = {
            WebView(context).apply {
                webViewClient = WebViewClient() // Evitar abrir enlaces en el navegador
                loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null)
            }
        }, modifier = Modifier.fillMaxSize())
    }
}