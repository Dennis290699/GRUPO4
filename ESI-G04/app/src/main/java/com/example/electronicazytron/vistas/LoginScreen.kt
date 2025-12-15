package com.example.electronicazytron.vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onValidar: (String, String, String) -> Boolean
) {
    Scaffold {
        LoginContent(onValidar)
    }
}

@Composable
private fun LoginContent(
    onValidar: (String, String, String) -> Boolean
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 🔹 Estado de error (sin AlertDialog)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inicio de Sesión",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorMessage = null
                    },
                    label = { Text("Nombre") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = apellido,
                    onValueChange = {
                        apellido = it
                        errorMessage = null
                    },
                    label = { Text("Apellido") },
                    leadingIcon = {
                        Icon(Icons.Default.PersonOutline, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (nombre.isBlank() || apellido.isBlank() || password.isBlank()) {
                            errorMessage = "Complete todos los campos"
                            return@Button
                        }

                        // VALIDACIÓN: La contraseña DEBE ser el apellido
                        if (password != apellido) {
                            errorMessage = "La contraseña debe ser igual al apellido"
                            return@Button
                        }

                        val esValido = onValidar(nombre, apellido, password)

                        if (!esValido) {
                            errorMessage = "Usuario no encontrado"
                        }
                        // Si es válido, el NavController navega desde AppNavigation
                    }
                ) {
                    Text("Ingresar")
                }

                // Mensaje de error elegante
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen { _, _, _ -> false }
    }
}
