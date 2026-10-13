package com.example.recipebox.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Orange = Color(0xFFE67E22)
private val Cream = Color(0xFFFFF8F0)
private val DarkBrown = Color(0xFF59321E)

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Logo y nombre de la aplicación
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Restaurant,
                    contentDescription = "Logo de RecipeBox",
                    modifier = Modifier.size(70.dp),
                    tint = Orange
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "RecipeBox",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBrown
                )

                Text(
                    text = "Offline",
                    fontSize = 16.sp,
                    color = Orange
                )
            }

            // Mensaje principal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Bienvenido a tu cocina!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBrown,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Guarda, organiza y descubre tus recetas favoritas en un solo lugar.",
                    fontSize = 17.sp,
                    color = Color(0xFF805E48),
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "¡Tus recetas siempre contigo, incluso sin conexión a Internet!",
                    fontSize = 15.sp,
                    color = Orange,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            // Botón para continuar
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Comenzar a cocinar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen(onStartClick = {})
    }
}
