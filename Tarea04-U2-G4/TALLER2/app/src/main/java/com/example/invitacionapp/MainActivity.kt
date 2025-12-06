package com.example.invitacionapp
// Paquete donde se almacena este archivo. Sirve para organizar el proyecto.


// -------------------------------------------------------------
// IMPORTACIONES NECESARIAS PARA LA UI Y LOS ICONOS
// -------------------------------------------------------------

import android.os.Bundle
// Proporciona el estado de la Activity.

import androidx.activity.ComponentActivity
// Actividad base que permite usar Jetpack Compose.

import androidx.activity.compose.setContent
// Permite escribir la UI dentro de setContent usando funciones @Composable.

import androidx.compose.foundation.Image
// Composable para mostrar imágenes.

import androidx.compose.foundation.layout.*
// Contiene Row, Column, Box, Spacer, tamaño, padding, etc.

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// Permiten que una columna sea desplazable (scroll vertical).

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
// Colección de íconos predeterminados (email, location, phone, etc.)

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
// Elementos de Material 3: Icon, Surface, Text, Theme.

import androidx.compose.runtime.Composable
// Marca funciones que generan UI.

import androidx.compose.ui.Alignment
// Alineación de elementos (center, start, etc.)

import androidx.compose.ui.Modifier
// Modifiers para cambiar apariencia, tamaño, color, padding, etc.

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
// Tipos de color e íconos vectoriales.

import androidx.compose.ui.res.painterResource
// Permite cargar imágenes desde /res/drawable.

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
// Muestra vista previa en Android Studio.

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// dp = densidad independiente, sp = tamaño de texto.


// ============================================================
//                     MAIN ACTIVITY
// ============================================================

class MainActivity : ComponentActivity() {

    // Método principal que se ejecuta al iniciar la Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Aquí empieza la interfaz construida con Compose.

            Surface(
                modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
                color = MaterialTheme.colorScheme.background // Fondo del tema Material
            ) {
                ChristmasAppContent() // Llama al composable principal de la app
            }
        }
    }
}


// ============================================================
//            COMPOSABLE PRINCIPAL DE LA APLICACIÓN
// ============================================================

@Composable
fun ChristmasAppContent() {

    Column(
        modifier = Modifier
            .fillMaxSize() // La columna ocupa toda la pantalla
            .padding(40.dp) // Separación de los bordes
            .verticalScroll(rememberScrollState()), // Permite scroll vertical
        horizontalAlignment = Alignment.CenterHorizontally // Centra todo horizontalmente
    ) {

        // --------------------------------------------------------
        // IMAGEN DE NAVIDAD
        // --------------------------------------------------------
        Image(
            painter = painterResource(id = R.drawable.navidad), // Imagen en drawable
            contentDescription = "Imagen de Navidad",
            modifier = Modifier
                .size(280.dp)      // Tamaño cuadrado
                .padding(bottom = 30.dp) // Espacio debajo de la imagen
        )


        // --------------------------------------------------------
        // CONTENEDOR DE INFORMACIÓN (FECHA, LUGAR, EMAIL, ETC.)
        // --------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth() // Columna de ancho completo
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Fila con icono y texto: FECHA
            InfoRow(icon = Icons.Default.CalendarToday, text = "Lunes 25 de diciembre del 2025")
            Spacer(modifier = Modifier.height(15.dp))

            // Ubicación
            InfoRow(icon = Icons.Default.LocationOn, text = "Universidad Central Del Ecuador")
            Spacer(modifier = Modifier.height(15.dp))

            // Email
            InfoRow(icon = Icons.Default.Email, text = "christmas@mail.com")
            Spacer(modifier = Modifier.height(15.dp))

            // Teléfono
            InfoRow(icon = Icons.Default.Phone, text = "09234567812")
            Spacer(modifier = Modifier.height(15.dp))


            // --------------------------------------------------------
            // TÍTULO "Compartir en:"
            // --------------------------------------------------------
            Text(
                text = "Compartir en:",
                fontSize = 20.sp, // Letras más grandes
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 20.dp)
            )


            // --------------------------------------------------------
            // FILA DE ICONOS DE REDES SOCIALES
            // --------------------------------------------------------
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp), // Espaciado entre iconos
                modifier = Modifier.padding(top = 20.dp)
            ) {

                // Icono de compartir genérico
                SocialIcon(
                    icon = Icons.Default.Share,
                    description = "Compartir",
                    color = Color(0xFF666666)
                )

                // Facebook
                SocialIconCustom(
                    painter = painterResource(id = R.drawable.facebook),
                    description = "Facebook",
                    color = Color(0xFF1877F2)
                )

                // Twitter
                SocialIconCustom(
                    painter = painterResource(id = R.drawable.twitter),
                    description = "Twitter",
                    color = Color(0xFF1DA1F2)
                )

                // WhatsApp
                SocialIconCustom(
                    painter = painterResource(id = R.drawable.whatsapp),
                    description = "WhatsApp",
                    color = Color(0xFF25D366)
                )
            }
        }
    }
}


// ============================================================
//            FILA DE INFORMACIÓN (ICONO + TEXTO)
// ============================================================

@Composable
fun InfoRow(icon: ImageVector, text: String) {

    Row(
        verticalAlignment = Alignment.CenterVertically, // Icono centrado con el texto
        modifier = Modifier.fillMaxWidth(), // La fila ocupa todo el ancho
        horizontalArrangement = Arrangement.Start // Alinea a la izquierda
    ) {

        Icon(
            imageVector = icon, // Icono vectorial (Material Icons)
            contentDescription = null, // No necesario para accesibilidad en este caso
            tint = Color(0xFF2E7D32), // Verde oscuro personalizado
            modifier = Modifier
                .size(45.dp)      // Tamaño del icono
                .padding(end = 16.dp) // Espacio entre icono y texto
        )

        Text(
            text = text,
            fontSize = 18.sp,
            color = Color(0xFF333333), // Gris oscuro
            fontWeight = FontWeight.Normal
        )
    }
}


// ============================================================
//          ICONO VECTORIAL (POR EJEMPLO EL DE COMPARTIR)
// ============================================================

@Composable
fun SocialIcon(icon: ImageVector, description: String, color: Color) {

    Icon(
        imageVector = icon, // Ícono vectorial
        contentDescription = description, // Etiqueta accesible
        tint = color, // Color del ícono (Facebook, Twitter, etc.)
        modifier = Modifier.size(42.dp) // Tamaño uniforme
    )
}


// ============================================================
//          ICONO PERSONALIZADO (PNG O SVG DEL DRAWABLE)
// ============================================================

@Composable
fun SocialIconCustom(
    painter: androidx.compose.ui.graphics.painter.Painter, // Imagen desde drawable
    description: String,
    color: Color
) {

    Icon(
        painter = painter, // Imagen personalizada
        contentDescription = description,
        tint = color, // Tinte del color de la red social
        modifier = Modifier.size(42.dp)
    )
}


// ============================================================
//                       PREVIEW
// ============================================================

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChristmasAppContent()
    // Vista previa para Android Studio sin necesidad de ejecutar la app
}
