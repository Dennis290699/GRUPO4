package com.example.electronicazytron.vistas

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.electronicazytron.modelo.Producto
import com.example.electronicazytron.modelo.ProductoViewModel

@Composable
fun UpdateProductScreen(codigo: String,
                        productoViewModel: ProductoViewModel,
                        navController: NavController) {
    val producto = productoViewModel.productos.find { it.codigo == codigo }


    if (producto != null) {
        var costo by remember { mutableStateOf(producto.costo.toString()) }
        var disponibilidad by remember { mutableStateOf(producto.disponibilidad.toString()) }
        var fechaFab by remember { mutableStateOf(producto.fecha_fab) }
        var descripcion by remember { mutableStateOf(producto.descripcion) }

        Column(modifier = Modifier.fillMaxSize().padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = producto.codigo)
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Descripción")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Fecha de Fabricación")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = fechaFab,
                onValueChange = { fechaFab = it },
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Costo")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = costo,
                onValueChange = {newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        costo = newValue
                    }
                },
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Existencias")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = disponibilidad,
                onValueChange = {newValue ->
                    if (newValue.matches(Regex("^\\d*\$"))) {
                        disponibilidad = newValue
                    }
                },
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                // Actualizar el producto en el ViewModel
                productoViewModel.update(
                    codigo,
                    Producto(
                        codigo,
                        descripcion,
                        fechaFab,
                        costo.toDouble(),
                        disponibilidad.toInt()
                    )
                )
                // Volver a ProductScreen
                navController.navigate("productos") {
                    popUpTo("updateProduct/$codigo") { inclusive = true }
                }
            }) {
                Text(text = "Modificar")
            }
            Button(onClick = {
                navController.navigate("productos"){
                    popUpTo("updateProduct/$codigo") { inclusive = true }
                }
            }) {
                Text(text = "Regresar")
            }

        }
    } else {
        Text("Producto no encontrado")
    }
}



//////******************************************************************************************

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview2() {
    val producto =
        Producto("P001", "Laptop Lenovo IdeaPad 3", "2024-01-15", 750.0, 10)
    BodyContentPreview2(producto)
}
@Composable
fun BodyContentPreview2(producto: Producto) {
    var costoText by remember { mutableStateOf(producto.costo.toString()) }
    var existenciaText by remember { mutableStateOf(producto.disponibilidad.toString()) }

    if (producto != null) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text(text = producto.codigo)
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Descripción")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = producto.descripcion,
                onValueChange = { producto.descripcion = it },
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Fecha de Fabricación")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = producto.fecha_fab,
                onValueChange = { producto.fecha_fab = it },
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Costo")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = costoText,
                onValueChange = {newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        costoText = newValue
                    }
                },
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Existencias")
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = existenciaText,
                onValueChange = {newValue ->
                    if (newValue.matches(Regex("^\\d*\$"))) {
                        existenciaText = newValue
                    }
                },
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {}) {
                Text(text = "Modificar")
            }
            // Cuando guardes el producto
            //val costoDouble = costoText.toDoubleOrNull() ?: 0.0
        }
    } else {
        Text("Producto no encontrado")
    }
}