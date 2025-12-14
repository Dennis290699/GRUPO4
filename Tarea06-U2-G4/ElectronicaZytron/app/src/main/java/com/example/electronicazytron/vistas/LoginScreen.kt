package com.example.electronicazytron.vista

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onValidar: (String, String) -> Unit
) {
    Scaffold {
        BodyContent(onValidar)
    }
}

@Composable
fun BodyContent(
    onValidar: (String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(top = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
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
            onValidar(nombre, apellido)
        }) {
            Text("Ingresar")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
   LoginScreen { _, _ -> }
}
