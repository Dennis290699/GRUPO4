package com.example.lemonade

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.lemonade.ui.theme.AppTheme
import java.util.Locale

// -----------------------------------------------------------
//  MAIN ACTIVITY
// -----------------------------------------------------------
class MainActivity : ComponentActivity() {

    companion object {
        var localeState = mutableStateOf(Locale("es"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val locale by localeState

            AppTheme {
                LemonadeApp(
                    locale = locale,
                    changeLanguage = { code -> localeState.value = Locale(code) }
                )
            }
        }
    }
}

// -----------------------------------------------------------
//  HELPER PARA STRINGRESOURCE SEGUN IDIOMA
// -----------------------------------------------------------
@Composable
fun stringResourceByLocale(id: Int, locale: Locale): String {
    val context = LocalContext.current
    val config = Configuration(context.resources.configuration).apply { setLocale(locale) }
    val localizedContext = context.createConfigurationContext(config)
    return localizedContext.resources.getString(id)
}

// -----------------------------------------------------------
//  UI PRINCIPAL
// -----------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemonadeApp(
    locale: Locale,
    changeLanguage: (String) -> Unit
) {

    var currentStep by remember { mutableStateOf(1) }
    var squeezeCount by remember { mutableStateOf(0) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResourceByLocale(R.string.app_name, locale),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Button(onClick = { showLanguageDialog = true }) {
                        Text("🌐")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->

        if (showLanguageDialog) {
            LanguageDialog(
                onDismiss = { showLanguageDialog = false },
                changeLanguage = changeLanguage
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
        ) {
            when (currentStep) {

                1 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_select,
                    drawableResourceId = R.drawable.lemon_tree,
                    contentDescriptionResourceId = R.string.lemon_tree_content_description,
                    onImageClick = {
                        currentStep = 2
                        squeezeCount = (2..4).random()
                    },
                    locale = locale
                )

                2 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_squeeze,
                    drawableResourceId = R.drawable.lemon_squeeze,
                    contentDescriptionResourceId = R.string.lemon_content_description,
                    onImageClick = {
                        squeezeCount--
                        if (squeezeCount == 0) currentStep = 3
                    },
                    locale = locale
                )

                3 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_drink,
                    drawableResourceId = R.drawable.lemon_drink,
                    contentDescriptionResourceId = R.string.lemonade_content_description,
                    onImageClick = { currentStep = 4 },
                    locale = locale
                )

                4 -> LemonTextAndImage(
                    textLabelResourceId = R.string.lemon_empty_glass,
                    drawableResourceId = R.drawable.lemon_restart,
                    contentDescriptionResourceId = R.string.empty_glass_content_description,
                    onImageClick = { currentStep = 1 },
                    locale = locale
                )
            }
        }
    }
}

// -----------------------------------------------------------
//  COMPONENTE IMAGEN + TEXTO
// -----------------------------------------------------------
@Composable
fun LemonTextAndImage(
    textLabelResourceId: Int,
    drawableResourceId: Int,
    contentDescriptionResourceId: Int,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = onImageClick,
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Image(
                    painter = painterResource(drawableResourceId),
                    contentDescription = stringResourceByLocale(contentDescriptionResourceId, locale),
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.button_image_width))
                        .height(dimensionResource(R.dimen.button_image_height))
                        .padding(dimensionResource(R.dimen.button_interior_padding))
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_vertical)))

            Text(
                text = stringResourceByLocale(textLabelResourceId, locale),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// -----------------------------------------------------------
//  DIÁLOGO DE IDIOMAS
// -----------------------------------------------------------
@Composable
fun LanguageDialog(
    onDismiss: () -> Unit,
    changeLanguage: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Selecciona un idioma") },
        text = {
            Column {
                Button(onClick = { changeLanguage("es"); onDismiss() }) {
                    Text("Español 🇪🇸")
                }
                Button(onClick = { changeLanguage("en"); onDismiss() }) {
                    Text("English 🇺🇸")
                }
                Button(onClick = { changeLanguage("fr"); onDismiss() }) {
                    Text("Français 🇫🇷")
                }
            }
        }
    )
}

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
