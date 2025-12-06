package com.example.lemonade
// Paquete donde se encuentra el código. Organiza el proyecto y evita conflictos de nombres.

// -----------------------------------------------------------
//  IMPORTACIONES (cada una con su propósito)
// -----------------------------------------------------------

import android.content.res.Configuration
// Clase que representa la configuración de recursos del dispositivo (idioma, orientación, etc.).
// Se usa para crear un contexto con un Locale específico y obtener strings en otro idioma.

import android.os.Bundle
// Clase que contiene el estado de la Activity cuando se crea; se recibe en onCreate.

import androidx.activity.ComponentActivity
// Activity base que facilita el uso de Jetpack Compose como UI principal.

import androidx.activity.compose.setContent
// Función que establece la UI Compose de la Activity dentro de onCreate.

import androidx.activity.enableEdgeToEdge
// Habilita que la UI utilice toda la pantalla (incluye zonas del sistema como barra de estado).

import androidx.compose.foundation.Image
// Composable para mostrar imágenes (drawables).

import androidx.compose.foundation.background
// Modifier que permite pintar un fondo (color) a un elemento Compose.

import androidx.compose.foundation.layout.*
// Contiene Column, Row, Box, Spacer, padding, fillMaxSize, etc. (layout básico).

import androidx.compose.foundation.shape.RoundedCornerShape
// Clase para crear formas redondeadas (usada en botones).

import androidx.compose.material3.*
// Importa componentes Material 3: Scaffold, Surface, Button, AlertDialog, Text, etc.

import androidx.compose.material3.ButtonDefaults
// Permite customizar colores y estilos por defecto de los botones.

import androidx.compose.material3.CenterAlignedTopAppBar
// TopAppBar centrado (Material3) — barra superior con título centrado.

import androidx.compose.material3.ExperimentalMaterial3Api
// Marca experimental para usar APIs de Material3 que aún no son estables.

import androidx.compose.material3.Scaffold
// Contenedor principal que soporta topBar, bottomBar, floatingActionButton, etc.

import androidx.compose.material3.Surface
// Contenedor visual que respeta el tema (color, elevación).

import androidx.compose.material3.Text
// Composable básico para mostrar texto.

import androidx.compose.material3.TopAppBarDefaults
// Provee colores y estilos por defecto para TopAppBar.

import androidx.compose.runtime.*
// Importa APIs reactivas de Compose: remember, mutableStateOf, by, Composable, etc.

import androidx.compose.ui.Alignment
// Alineaciones (CenterHorizontally, CenterVertically, etc.).

import androidx.compose.ui.Modifier
// Objeto para modificar composables (padding, size, fillMaxSize...).

import androidx.compose.ui.platform.LocalContext
// Provee el Context actual de Android dentro de un composable.

import androidx.compose.ui.res.dimensionResource
// Carga dimensiones definidas en res/values/dimens.xml (p. ej. tamaños y paddings).

import androidx.compose.ui.res.painterResource
// Carga imágenes desde res/drawable como Painter para el composable Image.

import androidx.compose.ui.text.font.FontWeight
// Define peso de fuente (Bold, Normal, etc.).

import androidx.compose.ui.tooling.preview.Preview
// Anotación para que Android Studio muestre una preview del composable.

import com.example.lemonade.ui.theme.AppTheme
// Tema personalizado de la app (colores, tipografías, etc.).

import java.util.Locale
// Clase Java para representar configuraciones regionales/idiomas (es, en, fr, ...).

// -----------------------------------------------------------
//  MAIN ACTIVITY (punto de entrada)
// -----------------------------------------------------------

class MainActivity : ComponentActivity() {

    companion object {
        // Estado compartido (mutableStateOf) que guarda el Locale actual de la app.
        // Usado desde Compose: cuando cambia this value, recompondrá quienes lo observen.
        // Se inicializa con Locale("es") -> español por defecto.
        var localeState = mutableStateOf(Locale("es"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita que la interfaz ocupe toda la pantalla (edge-to-edge).
        enableEdgeToEdge()

        // setContent es donde se define la UI Compose para la Activity.
        setContent {
            // Captura el valor actual del locale desde el estado compartido.
            val locale by localeState

            // Aplica el tema de la app (colores, tipografías, etc.).
            AppTheme {
                // Llama al composable principal y le pasa:
                // - el Locale actual
                // - una función para cambiar el idioma (actualiza localeState)
                LemonadeApp(
                    locale = locale,
                    changeLanguage = { code -> localeState.value = Locale(code) }
                )
            }
        }
    }
}

// -----------------------------------------------------------
//  HELPER: stringResourceByLocale
// -----------------------------------------------------------

/**
 * Devuelve un string (resources) para un Locale dado.
 *
 * Motivo: los stringResource normales usan el LocalContext actual; si queremos
 * mostrar texto en otro idioma sin reiniciar la Activity, creamos un context
 * con la Configuration con la Locale deseada y pedimos el string ahí.
 *
 * @param id ID del recurso string (R.string.xxx)
 * @param locale Locale requerido (ej: Locale("en"))
 * @return Texto traducido según ese Locale
 */
@Composable
fun stringResourceByLocale(id: Int, locale: Locale): String {
    // Obtiene el contexto actual (Activity) dentro de Compose
    val context = LocalContext.current

    // Clona la configuración actual y aplica el Locale solicitado
    val config = Configuration(context.resources.configuration).apply { setLocale(locale) }

    // Crea un contexto nuevo "localizado" usando la configuración anterior
    val localizedContext = context.createConfigurationContext(config)

    // Retorna el string usando ese contexto localizado
    return localizedContext.resources.getString(id)
}

// -----------------------------------------------------------
//  UI PRINCIPAL: LemonadeApp
// -----------------------------------------------------------

/**
 * Composable principal que controla el flujo del juego Lemonade:
 * pasos (1 a 4), lógica de exprimir (squeezeCount) y diálogo para cambiar idioma.
 *
 * @param locale Locale actual que se usará para mostrar strings traducidos.
 * @param changeLanguage Callback que acepta un código de idioma (ej: "es") y lo aplica.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemonadeApp(
    locale: Locale,
    changeLanguage: (String) -> Unit
) {
    // currentStep controla el paso actual del juego:
    // 1 = escoger limón del árbol
    // 2 = exprimir limón (varias presiones)
    // 3 = beber la limonada
    // 4 = vaso vacío / reiniciar
    var currentStep by remember { mutableStateOf(1) }

    // squeezeCount: cuántas veces hay que "exprimir" el limón en el paso 2.
    // Se determina aleatoriamente cuando se pasa del paso 1 al 2.
    var squeezeCount by remember { mutableStateOf(0) }

    // Controla si se muestra el diálogo para cambiar idioma
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Scaffold provee la estructura base: topBar + contenido principal.
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Título de la app obtenido en el idioma seleccionado
                    Text(
                        text = stringResourceByLocale(R.string.app_name, locale),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Botón que abre el diálogo de selección de idioma
                    Button(onClick = { showLanguageDialog = true }) {
                        Text("🌐") // emoji como label del botón
                    }
                },
                // Color del contenedor de la AppBar según el tema
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        // innerPadding: padding que Scaffold reserva para evitar solapamiento con la topBar.

        // Si showLanguageDialog true, mostramos el diálogo que permite cambiar idioma
        if (showLanguageDialog) {
            LanguageDialog(
                onDismiss = { showLanguageDialog = false },
                changeLanguage = changeLanguage
            )
        }

        // Surface que contiene el contenido principal (fondo y layout)
        Surface(
            modifier = Modifier
                .fillMaxSize() // Ocupa toda la pantalla
                .padding(innerPadding) // Respeta el padding del Scaffold
                .background(MaterialTheme.colorScheme.tertiaryContainer), // Fondo
        ) {
            // Lógica por pasos: muestra un LemonTextAndImage para cada paso
            when (currentStep) {

                // -----------------------------
                // Paso 1: Seleccionar limón del árbol
                // -----------------------------
                1 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_select,
                    drawableResourceId = R.drawable.lemon_tree,
                    contentDescriptionResourceId = R.string.lemon_tree_content_description,
                    onImageClick = {
                        // Al pulsar: avanzamos al paso 2 y calculamos
                        // cuántas veces hay que exprimir (2 a 4 presiones aleatorias).
                        currentStep = 2
                        squeezeCount = (2..4).random()
                    },
                    locale = locale
                )

                // -----------------------------
                // Paso 2: Exprimir el limón (varias pulsaciones)
                // -----------------------------
                2 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_squeeze,
                    drawableResourceId = R.drawable.lemon_squeeze,
                    contentDescriptionResourceId = R.string.lemon_content_description,
                    onImageClick = {
                        // Cada pulsación decrementa squeezeCount.
                        // Cuando llega a 0, avanzamos al paso 3 (beber).
                        squeezeCount--
                        if (squeezeCount == 0) currentStep = 3
                    },
                    locale = locale
                )

                // -----------------------------
                // Paso 3: Beber la limonada
                // -----------------------------
                3 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_drink,
                    drawableResourceId = R.drawable.lemon_drink,
                    contentDescriptionResourceId = R.string.lemonade_content_description,
                    onImageClick = { currentStep = 4 }, // Avanza al paso 4 al pulsar
                    locale = locale
                )

                // -----------------------------
                // Paso 4: Vaso vacío (reiniciar)
                // -----------------------------
                4 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_empty_glass,
                    drawableResourceId = R.drawable.lemon_restart,
                    contentDescriptionResourceId = R.string.empty_glass_content_description,
                    onImageClick = { currentStep = 1 }, // Reinicia el flujo
                    locale = locale
                )
            }
        }
    }
}

// -----------------------------------------------------------
//  COMPONENTE REUTILIZABLE: LemonTextAndImage
// -----------------------------------------------------------

/**
 * Componente que muestra:
 * - una imagen dentro de un botón (para capturar clicks)
 * - un texto debajo que describe la acción
 *
 * Parámetros:
 * @param textLabelResourceId ID del string que describe el paso (R.string.*)
 * @param drawableResourceId ID del drawable a mostrar (R.drawable.*)
 * @param contentDescriptionResourceId ID del string para contentDescription (accesibilidad)
 * @param onImageClick Callback que se ejecuta al pulsar la imagen/botón
 * @param modifier Modificador Compose (por defecto, ninguno)
 * @param locale Locale actual para cargar los strings traducidos
 */
@Composable
fun LemonTextAndImage(
    textLabelResourceId: Int,
    drawableResourceId: Int,
    contentDescriptionResourceId: Int,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale
) {
    // Box para permitir apilar y centrar el contenido si fuera necesario
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Columna centrada con la imagen (dentro de botón) y el texto inferior
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Botón que contiene la imagen. Usamos Button para obtener feedback visual
            Button(
                onClick = onImageClick,
                // Esquinas redondeadas; valores vienen de res/values/dimens.xml
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                // Color de fondo del botón tomado del tema (tertiaryContainer)
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Image(
                    // Carga la imagen desde drawable (p.ej. R.drawable.lemon_tree)
                    painter = painterResource(drawableResourceId),
                    // Content description para accesibilidad, cargado en el idioma actual
                    contentDescription = stringResourceByLocale(contentDescriptionResourceId, locale),
                    // Dimensiones y padding interior definidos en dimens.xml
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.button_image_width))
                        .height(dimensionResource(R.dimen.button_image_height))
                        .padding(dimensionResource(R.dimen.button_interior_padding))
                )
            }

            // Espacio vertical entre la imagen y el texto (valor en dimens.xml)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_vertical)))

            // Texto descriptivo del paso, cargado en el Locale actual
            Text(
                text = stringResourceByLocale(textLabelResourceId, locale),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// -----------------------------------------------------------
//  DIÁLOGO PARA CAMBIAR IDIOMA
// -----------------------------------------------------------

/**
 * Diálogo modal sencillo que permite seleccionar entre Español, Inglés y Francés.
 *
 * @param onDismiss Se llama para cerrar el diálogo (sin cambiar idioma).
 * @param changeLanguage Callback que acepta el código de idioma ("es","en","fr") y
 *                       aplica el cambio en el estado global (localeState).
 */
@Composable
fun LanguageDialog(
    onDismiss: () -> Unit,
    changeLanguage: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // qué hacer si el usuario pulsa fuera del diálogo
        confirmButton = {}, // no usamos confirmButton (usamos botones dentro del texto)
        title = { Text("Selecciona un idioma") }, // título fijo (podría localizarse también)
        text = {
            Column {
                // Botón para Español: llama changeLanguage con "es" y cierra el diálogo
                Button(onClick = { changeLanguage("es"); onDismiss() }) {
                    Text("Español 🇪🇸")
                }
                // Inglés
                Button(onClick = { changeLanguage("en"); onDismiss() }) {
                    Text("English 🇺🇸")
                }
                // Francés
                Button(onClick = { changeLanguage("fr"); onDismiss() }) {
                    Text("Français 🇫🇷")
                }
            }
        }
    )
}

// -----------------------------------------------------------
//  PREVIEW (Android Studio)
// -----------------------------------------------------------

/**
 * Preview para Android Studio: muestra cómo se vería la UI
 * con locale = Locale("es"). Útil para diseño sin ejecutar la app.
 */
@Preview
@Composable
fun LemonPreview() {
    AppTheme {
        LemonadeApp(
            locale = Locale("es"),
            changeLanguage = {}
        )
    }
}
