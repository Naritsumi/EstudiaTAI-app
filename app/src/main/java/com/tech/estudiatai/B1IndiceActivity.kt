package com.tech.estudiatai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tech.estudiatai.ui.theme.AppTheme

class B1IndiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                B1IndiceButtons()
            }
        }
    }
}

@Composable
fun B1IndiceButtons() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFdefbff))
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        // Column to arrange the buttons vertically
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("T1. La Constitución Española de 1978", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }

            /*
            * Pronto estarán disponible los temas restantes
            * */
        }
    }
}
