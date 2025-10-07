package com.tech.estudiatai

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tech.estudiatai.ui.theme.AppTheme
import androidx.core.net.toUri


class ReadMeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReadMeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ReadMeScreen(modifier: Modifier = Modifier) {
    // Permite desplazamiento en caso de que el contenido sea largo
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        IconButton(onClick = { (context as? Activity)?.finish() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Salir", tint = MaterialTheme.colorScheme.onSecondary)
        }
        Text(
            text = "¡Bienvenido a EstudiaTAI!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = """
                Esta es una aplicación desarrollada como proyecto personal y hobby para ayudar a la gente que quiere prepararse la oposición, ya que yo también he pasado por ahí y sé lo difícil que es. 
                
                Estoy trabajando constantemente para mejorarla y añadir nuevas funcionalidades. Espero que disfrutes usándola tanto como yo disfruto desarrollándola.

                Por favor, recuerda que esta es una aplicación en constante desarrollo y habrá actualizaciones frecuentes para añadir temario y mejorar la aplicación.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )

        KoFiAndCoffeeButtons()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = """
                ¡Gracias por tu apoyo! :) 
                
                Si tienes algún comentario, sugerencia, encuentras algún problema o información errada, no dudes en contactarme.                                
            """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
        
        EmailButton()

        Text(
            text = """
                IMPORTANTE: Los apuntes aportados en esta aplicación NO están sujetos a derechos de autor.                 
            """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
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
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón Ko-fi
        IconButton(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://ko-fi.com/tech_racoon".toUri() 
                )
                context.startActivity(intent)
            },
            modifier = Modifier
                .width(180.dp)
                .height(60.dp)
                .shadow(5.dp, shape = RoundedCornerShape(25.dp)),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Image para tratar el icono como png, cosa que icon no hace
                Image(
                    painter = painterResource(id = R.drawable.kofi_symbol),
                    contentDescription = "Ko-fi",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apóyame en Ko-fi")
            }
        }

        // Botón Buy Me a Coffee
        IconButton(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://buymeacoffee.com/tech_racoon".toUri()
                )
                context.startActivity(intent)
            },
            modifier = Modifier
                .width(180.dp)
                .height(60.dp)
                .shadow(5.dp, shape = RoundedCornerShape(25.dp)),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Lo dejo como icon porque me gusta el toque negro que le agrega
                Icon(
                    painter = painterResource(id = R.drawable.bmc_logo),
                    contentDescription = "Buy Me a Coffee",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invítame a un café")
            }
        }
    }
}

@SuppressLint("UseKtx")
@Composable
fun EmailButton() {
    val context = LocalContext.current

    // Envolvemos el botón en un Column para centrarlo horizontalmente
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("techracoon.group@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "EstudiaTAI")
                }
                val chooser = Intent.createChooser(intent, "Selecciona una aplicación de correo")
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(chooser)
                }
            },
            modifier = Modifier
                .width(180.dp)
                .height(60.dp)
                .shadow(5.dp, shape = RoundedCornerShape(25.dp)),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Email, contentDescription = "Enviar correo", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("¡Escríbeme!")
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun Preview2() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReadMeScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
*/
