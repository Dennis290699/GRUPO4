package com.example.diceroller
// Paquete donde está la aplicación (organiza el proyecto)


// ------------------------------------------------------------
// IMPORTACIONES
// ------------------------------------------------------------

import android.content.Context
// Necesario para trabajar con actualización de locales (idiomas)

import android.content.res.Configuration
// Permite crear una configuración personalizada del sistema

import android.os.Bundle
// Estado de la Activity

import androidx.activity.ComponentActivity
// Activity que soporta Jetpack Compose

import androidx.activity.compose.setContent
// Permite cargar Compose como interfaz principal

import androidx.compose.foundation.Image
// Permite mostrar imágenes en la UI

import androidx.compose.foundation.layout.*
// Row, Column, Spacer, tamaño, padding…

import androidx.compose.material3.*
// Componentes de Material 3: Text, Button, Surface, DropdownMenu, IconButton…

import androidx.compose.runtime.*
// Estados, remember, variables reactivas

import androidx.compose.ui.Alignment
// Alineaciones (center, start)

import androidx.compose.ui.Modifier
// Permite modificar tamaño, padding, etc.

import androidx.compose.ui.platform.LocalContext
// Permite acceder al contexto actual de Android dentro de Compose

import androidx.compose.ui.res.painterResource
// Cargar imágenes desde /drawable

import androidx.compose.ui.res.stringResource
// Cargar textos desde strings.xml (para internacionalización)

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// dp = densidad independiente, sp = tamaño del texto

import com.example.diceroller.ui.theme.DiceRollerTheme
// Tema personalizado de la app

import java.util.Locale
// Para gestionar idiomas manualmente (inglés, español, francés)



// ============================================================
//                   MAIN ACTIVITY
// ============================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga la UI dentro del tema de la app
        setContent {
            DiceRollerTheme {

                // Controlador de idioma (cambia idioma dinámicamente)
                LanguageController()
            }
        }
    }
}



// ============================================================
//                CONTROL DE IDIOMA GLOBAL
// ============================================================

@Composable
fun LanguageController() {

    // Estado que almacena el idioma actual seleccionado
    var lang by remember { mutableStateOf("en") } // idioma por defecto → inglés

    // Contexto base (sin modificar)
    val base = LocalContext.current

    // Crea un contexto nuevo con el idioma seleccionado
    // Se vuelve a crear cuando "lang" cambia → recomposición
    val localized = remember(lang) { base.updateLocale(lang) }

    /*
     * CompositionLocalProvider permite REEMPLAZAR valores globales
     * en toda la jerarquía composable. Aquí reemplaza el contexto
     * para que todos los stringResource usen el nuevo idioma.
     */
    CompositionLocalProvider(LocalContext provides localized) {

        Surface(modifier = Modifier.fillMaxSize()) {

            // Renderiza la app y le envía la función para cambiar idioma
            DiceRollerApp { lang = it }
        }
    }
}



// ============================================================
//        FUNCIÓN PARA CAMBIAR EL IDIOMA DEL CONTEXTO
// ============================================================

/**
 * Extensión de Context para cambiar manualmente el idioma del sistema
 * dentro del contexto de la app (sin cambiar Android completo).
 */
fun Context.updateLocale(language: String): Context {

    val locale = Locale(language)  // Crea la nueva Locale (en, es, fr)
    Locale.setDefault(locale)       // Ajusta idioma global dentro de la app

    val config = Configuration(resources.configuration)
    config.setLocale(locale)       // Aplica la nueva configuración de idioma

    // Crea un contexto nuevo basado en esta configuración
    return createConfigurationContext(config)
}



// ============================================================
//               UI PRINCIPAL DE LA APLICACIÓN
// ============================================================

@Composable
fun DiceRollerApp(onLangSelect: (String) -> Unit) {

    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()                // ocupa toda la pantalla
            .wrapContentSize(Alignment.Center), // contenido centrado
        onLangSelect = onLangSelect       // callback para cambiar el idioma
    )
}



// ============================================================
//       PANTALLA PRINCIPAL CON DADO + BOTÓN + IDIOMAS
// ============================================================

@Composable
fun DiceWithButtonAndImage(
    modifier: Modifier = Modifier,
    onLangSelect: (String) -> Unit        // Función que recibe el idioma seleccionado
) {

    // Estado del número del dado → inicia en 1
    var result by remember { mutableStateOf(1) }

    // Estado del menú desplegable (abierto o cerrado)
    var expanded by remember { mutableStateOf(false) }

    // Selección de imagen según el número actual del dado
    val image = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    // Texto traducido desde strings.xml según el idioma actual
    val resultText = when (result) {
        1 -> stringResource(R.string.dice_one)
        2 -> stringResource(R.string.dice_two)
        3 -> stringResource(R.string.dice_three)
        4 -> stringResource(R.string.dice_four)
        5 -> stringResource(R.string.dice_five)
        else -> stringResource(R.string.dice_six)
    }


    // ------------------------------------------------------------
    //   CONTENEDOR PRINCIPAL DE LA INTERFAZ
    // ------------------------------------------------------------
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Imagen del dado según el número
        Image(
            painter = painterResource(image),
            contentDescription = result.toString()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Texto traducido (One, Dos, Trois…)
        Text(resultText, fontSize = 22.sp)

        Spacer(modifier = Modifier.height(10.dp))

        // Botón que genera un número aleatorio del 1 al 6
        Button(onClick = { result = (1..6).random() }) {
            Text(stringResource(R.string.roll), fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))


        // ------------------------------------------------------------
        //   BOTÓN DE CAMBIO DE IDIOMA (MENÚ DESPLEGABLE)
        // ------------------------------------------------------------

        Box {
            // Botón redondo con icono de globo de idioma
            IconButton(onClick = { expanded = true }) {
                Image(
                    painter = painterResource(R.drawable.ic_language),
                    contentDescription = "Language",
                    modifier = Modifier.size(28.dp)
                )
            }

            // Menú desplegable bajo el botón
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                // ---- Opción 1: Inglés ----
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.english)) },
                    onClick = {
                        expanded = false
                        onLangSelect("en")   // Cambia idioma a inglés
                    }
                )

                // ---- Opción 2: Español ----
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.spanish)) },
                    onClick = {
                        expanded = false
                        onLangSelect("es")   // Cambia idioma a español
                    }
                )

                // ---- Opción 3: Francés ----
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.french)) },
                    onClick = {
                        expanded = false
                        onLangSelect("fr")   // Cambia idioma a francés
                    }
                )
            }
        }
    }
}
