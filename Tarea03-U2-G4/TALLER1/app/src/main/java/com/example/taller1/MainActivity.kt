package com.example.taller1
// Paquete donde está almacenada esta clase. Sirve para organizar el código en módulos.

// -------------------------------------------------------------
//  IMPORTACIONES NECESARIAS
// -------------------------------------------------------------

import android.os.Bundle
// Clase usada para recibir información del estado al crear una Activity.

import androidx.activity.ComponentActivity
// Clase base para actividades que usan Jetpack Compose.

import androidx.activity.compose.setContent
// Permite definir la UI de la Activity usando funciones @Composable.

import androidx.activity.enableEdgeToEdge
// Permite que el contenido ocupe toda la pantalla, incluyendo áreas del sistema.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
// Contiene elementos básicos de diseño como Box, Column, Row, Spacer, padding, etc.

import androidx.compose.material3.*
// Componentes Material Design 3 (Text, Surface, Theme, etc.).

import androidx.compose.runtime.Composable
// Anotación necesaria para declarar funciones que generan UI en Compose.

import androidx.compose.ui.Alignment
// Alinea contenido dentro de Box, Column o Row.

import androidx.compose.ui.Modifier
// Modifiers permiten cambiar tamaño, color, padding, etc.

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
// Brush sirve para degradados. Color para colores personalizados.

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
// Propiedades de texto: tipo de letra, peso, alineación, etc.

import androidx.compose.ui.tooling.preview.Preview
// Permite crear previews del composable en Android Studio.

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// dp = densidad independiente, sp = tamaño de letra.


/* =============================================================
                    MAIN ACTIVITY
   ============================================================= */

class MainActivity : ComponentActivity() {

    // Método que se ejecuta al crear la Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // Permite que la app utilice toda la pantalla, eliminando márgenes del sistema (barras)

        setContent {
            // setContent define la UI usando Jetpack Compose

            Taller1Theme {
                // Aplica el tema visual personalizado con colores y tipografía

                Surface(color = MaterialTheme.colorScheme.background) {
                    // Surface es un contenedor visual que respeta el tema Material.
                    // Aquí se establece el color de fondo.

                    PantallaUCE()
                    // Llamada a la función composable que dibuja la pantalla principal
                }
            }
        }
    }
}


/* =============================================================
                   PANTALLA PRINCIPAL
   ============================================================= */

@Composable
fun PantallaUCE() {

    // Se crea un degradado vertical (de arriba hacia abajo)
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF990000),  // Rojo vino
            Color(0xFF001F4D),  // Azul oscuro
            Color.White         // Blanco
        )
    )

    // Box permite superponer elementos y centrar contenido
    Box(
        modifier = Modifier
            .fillMaxSize()       // Ocupa toda la pantalla
            .background(gradient) // Aplica el fondo con el degradado
            .padding(24.dp),     // Espaciado interno para que no pegue a los bordes
        contentAlignment = Alignment.Center // Centra todo el contenido dentro del Box
    ) {

        // Column ordena elementos uno debajo del otro
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centra los hijos horizontalmente
            verticalArrangement = Arrangement.Center            // Centra verticalmente los elementos
        ) {

            // ------------------------------
            // TITULO PRINCIPAL
            // ------------------------------
            Text(
                text = "Dispositivos Móviles",
                color = Color.White,             // Texto blanco
                fontSize = 36.sp,                // Tamaño grande
                fontWeight = FontWeight.ExtraBold, // Texto muy grueso
                textAlign = TextAlign.Center,    // Centrado
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Espacio vertical entre elementos

            // ------------------------------
            // SUBTÍTULO
            // ------------------------------
            Text(
                text = "Grupo N° 4",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF4444), // Rojo claro
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ------------------------------
            // CUADRO DE NOMBRES
            // ------------------------------
            Box(
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.15f), // Blanco translúcido
                        shape = MaterialTheme.shapes.medium // Forma redondeada del tema
                    )
                    .padding(16.dp)
            ) {

                Text(
                    text = """
                        - Byron Condolo
                        - Pamela Fernández
                        - Marielena González
                        - Angelo Lascano
                        - Ruth Rosero
                        - Joan Santamaria
                        - Dennis Trujillo
                    """.trimIndent(),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp // Espaciado entre líneas
                )
            }
        }
    }
}


/* =============================================================
                 PREVIEW PARA ANDROID STUDIO
   ============================================================= */

@Preview(showBackground = true)
@Composable
fun PantallaUCEPreview() {
    Taller1Theme {
        PantallaUCE()
        // Esto muestra la pantalla en el Preview del IDE
    }
}


/* =============================================================
                    TEMA PERSONALIZADO
   ============================================================= */

@Composable
fun Taller1Theme(
    darkTheme: Boolean = false, // Permite activar modo oscuro si se desea
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme() // Colores automáticos para modo oscuro
        else -> lightColorScheme()      // Colores automáticos para modo claro
    }

    // MaterialTheme aplica colores, tipografía y forma a toda la UI
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}


/* =============================================================
                        TIPOGRAFÍA
   ============================================================= */

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Fuente por defecto
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,                // Tamaño del texto grande estándar
        lineHeight = 24.sp,              // Altura de línea (espaciado vertical)
        letterSpacing = 0.5.sp           // Espaciado entre letras
    )
)
