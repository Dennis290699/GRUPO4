package com.example.diceroller

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga la UI usando Jetpack Compose dentro del tema de la app
        setContent {
            DiceRollerTheme {
                // Controlador que administra los cambios de idioma
                LanguageController()
            }
        }
    }
}

@Composable
fun LanguageController() {
    // Guarda el idioma actual en memoria usando Compose
    var lang by remember { mutableStateOf("en") }   // por defecto inglés

    // Obtiene el contexto base de Android
    val base = LocalContext.current

    // Crea un contexto "localizado" cada vez que cambia el idioma
    val localized = remember(lang) { base.updateLocale(lang) }

    // Reemplaza el LocalContext para que toda la UI use el nuevo idioma
    CompositionLocalProvider(LocalContext provides localized) {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Renderiza la app y pasa callback para cambiar idioma
            DiceRollerApp { lang = it }
        }
    }
}

/* ---- Cambiar idioma ---- */

/*
 * Extensión de Context para aplicar un idioma manualmente.
 * Esto crea un ConfigurationContext con la nueva Locale.
 */
fun Context.updateLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale) // Ajusta el idioma global

    val config = Configuration(resources.configuration)
    config.setLocale(locale)  // Aplica la Locale en el config

    return createConfigurationContext(config)   // Devuelve el contexto con ese idioma
}

/* ---- UI principal ---- */

@Composable
fun DiceRollerApp(onLangSelect: (String) -> Unit) {
    // Pantalla principal del dado
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        onLangSelect = onLangSelect
    )
}

@Composable
fun DiceWithButtonAndImage(
    modifier: Modifier = Modifier,
    onLangSelect: (String) -> Unit
) {
    // Valor actual del dado
    var result by remember { mutableStateOf(1) }

    // Control del estado del menú desplegable
    var expanded by remember { mutableStateOf(false) }

    // Selecciona la imagen del dado según el número
    val image = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    // Texto “Uno, Dos, Three…" según el idioma actual
    val resultText = when (result) {
        1 -> stringResource(R.string.dice_one)
        2 -> stringResource(R.string.dice_two)
        3 -> stringResource(R.string.dice_three)
        4 -> stringResource(R.string.dice_four)
        5 -> stringResource(R.string.dice_five)
        else -> stringResource(R.string.dice_six)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* Imagen del dado */
        Image(
            painter = painterResource(image),
            contentDescription = result.toString()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Texto del resultado ya traducido
        Text(resultText, fontSize = 22.sp)

        Spacer(modifier = Modifier.height(10.dp))

        // Botón para generar un número aleatorio
        Button(onClick = { result = (1..6).random() }) {
            Text(stringResource(R.string.roll), fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))

        /* ---- Botón de menú de idiomas ---- */

        // Box porque el menú necesita un "ancla"
        Box {

            // Botón redondo con icono de idioma
            IconButton(onClick = { expanded = true }) {
                Image(
                    painter = painterResource(R.drawable.ic_language),
                    contentDescription = "Language",
                    modifier = Modifier.size(28.dp)
                )
            }

            // Menú desplegable para escoger idioma
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                // Inglés
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.english)) },
                    onClick = {
                        expanded = false
                        onLangSelect("en")  // Cambia idioma en el controlador
                    }
                )

                // Español
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.spanish)) },
                    onClick = {
                        expanded = false
                        onLangSelect("es")
                    }
                )

                // Francés
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.french)) },
                    onClick = {
                        expanded = false
                        onLangSelect("fr")
                    }
                )
            }
        }
    }
}
