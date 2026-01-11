package com.example.taller6

import android.content.Context
import android.content.Intent              // Para cambiar de pantalla
import android.os.Bundle
import android.widget.Toast               // Para mostrar mensajes
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation // Oculta la contraseña
import androidx.compose.ui.tooling.preview.Preview
import com.example.taller6.ui.theme.Taller6Theme
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat          // Para formatear fecha
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Define la interfaz usando Jetpack Compose
        setContent {
            Taller6Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        LoginScreen()     // Llama a la pantalla de login
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {

    var usuario by remember { mutableStateOf("") }   // Guarda lo que escribe el usuario
    var password by remember { mutableStateOf("") } // Guarda la contraseña
    val contexto = LocalContext.current              // Contexto para usar Toast e Intents

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Muestra el logo del grupo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo del grupo",
            modifier = Modifier.size(300.dp).padding(bottom = 20.dp)
        )

        // Campo de texto para el usuario
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña (oculta los caracteres)
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de ingreso
        Button(
            onClick = {
                // Verifica que ambos campos estén llenos
                if (usuario.isNotEmpty() && password.isNotEmpty()) {

                    // Guarda usuario, fecha y hora en SharedPreferences
                    guardarIngreso(contexto, usuario)

                    // Mensaje de confirmación
                    Toast.makeText(contexto, "Ingreso registrado", Toast.LENGTH_SHORT).show()

                    // Abre la pantalla del visor PDF
                    val intent = Intent(contexto, PDFActivity::class.java)
                    contexto.startActivity(intent)

                } else {
                    // Mensaje si falta algún dato
                    Toast.makeText(contexto, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("INGRESAR")
        }
    }
}

// Función que guarda el usuario y la fecha en SharedPreferences
fun guardarIngreso(context: Context, nombre: String) {

    // Accede al archivo de preferencias
    val sharedPref = context.getSharedPreferences("PreferenciasTaller6", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    // Obtiene fecha y hora actual
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val fechaHora = sdf.format(Date())

    // Crea una clave única usando el tiempo
    val llave = "registro_${System.currentTimeMillis()}"

    // Guarda el dato en el archivo
    editor.putString(llave, "Usuario: $nombre | Fecha: $fechaHora")
    editor.apply()
}

// Vista previa en Android Studio
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Taller6Theme {
        LoginScreen()
    }
}
