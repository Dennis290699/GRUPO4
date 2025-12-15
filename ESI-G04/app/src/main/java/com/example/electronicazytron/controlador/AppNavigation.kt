package com.example.electronicazytron.controlador


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.electronicazytron.modelo.Usuario
import com.example.electronicazytron.vistas.LoginScreen
import com.example.electronicazytron.vistas.ProductScreen


import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.electronicazytron.modelo.ProductoViewModel
import com.example.electronicazytron.modelo.UsuarioViewModel

import com.example.electronicazytron.vista.InsertProductScreen
import com.example.electronicazytron.vistas.HomeScreen
import com.example.electronicazytron.vistas.RegistrarScreen
import com.example.electronicazytron.vistas.UpdateProductScreen
import java.security.MessageDigest

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val productoViewModel: ProductoViewModel = viewModel()

    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home" // <-- inicia desde HomeScreen
    ) {
        composable("home") {
            HomeScreen(
                onLogin = {
                    navController.navigate("login") //<-- Redireccion de boton al login
                },
                onRegistrar={
                    navController.navigate("insertUser") //<-- Redirreccion del boton al registro de usuarios
                }
            )
        }

        composable("login") {
            LoginScreen { nombre, apellido, password ->
                val usuario = Usuario(nombre, apellido)
                val valido = usuarioViewModel.validar(usuario)

                if (valido) {
                    val hash = hashString(password)
                    // Navegar a productos pasando nombre, password y hash
                    navController.navigate("productos?nombre=$nombre&password=$password&hash=$hash") {
                        popUpTo("login") { inclusive = true }
                    }
                }

                valido
            }
        }

        composable(
            route = "productos?nombre={nombre}&password={password}&hash={hash}",
            arguments = listOf(
                navArgument("nombre") { defaultValue = "" },
                navArgument("password") { defaultValue = "" },
                navArgument("hash") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            val hash = backStackEntry.arguments?.getString("hash") ?: ""
            ProductScreen(productoViewModel, navController, nombre, password, hash)
        }

        composable("updateProduct/{codigo}") { backStackEntry ->
            val codigo = backStackEntry.arguments?.getString("codigo")
            if (codigo != null) {
                UpdateProductScreen(codigo, productoViewModel, navController)
            }
        }

        composable("insertProduct") {
            InsertProductScreen(productoViewModel, navController)
        }

        composable("insertUser") {
            RegistrarScreen(usuarioViewModel, navController)
        }

    }
}

fun hashString(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
