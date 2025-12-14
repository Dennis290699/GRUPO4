package com.example.electronicazytron.vista

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onValidar: (String, String) -> Boolean
) {
    Scaffold {
        BodyContent(onValidar)
    }
}

@Composable
fun BodyContent(
    onValidar: (String, String) -> Boolean
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

    // Estado para mostrar el popup
    var showDialog by remember { mutableStateOf(false) }
    var mensajeDialog by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ingrese Su Nombre:")
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
        )

        Text("Ingrese Su Apellido:")
        TextField(
            value = apellido,
            onValueChange = { apellido = it },
        )

        Button(onClick = {
            val esValido = onValidar(nombre, apellido)
            if (esValido) {
                mensajeDialog = "¡Credenciales correctas!"
            } else {
                mensajeDialog = "Nombre o apellido incorrectos"
            }
            showDialog = true
        }) {
            Text("Ingresar")
        }
    }

    // Popup de alerta
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Login") },
            text = { Text(mensajeDialog) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    LoginScreen { _, _ -> true }
}
