package com.example.electronicazytron.vistas

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.electronicazytron.modelo.ProductoViewModel
import com.example.electronicazytron.modelo.Producto

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductScreen(productoViewModel: ProductoViewModel = viewModel(),
                  navController: NavController) {
    // Cargar productos al entrar a la pantalla, usando el modelo de productos, asi tenemos cargados los productos para poder visualizarlos y usarlos
    LaunchedEffect(Unit) { //launchedEfect para manejo de corrutinas dentro de jetpackcompiose
                                    //unit hace que el metodo se ejecute una sola vez cuando se compone la vista
        productoViewModel.cargarProductos()
    }

    Scaffold {
        BodyContent(productoViewModel,navController)
    }
}

@Composable
fun BodyContent(productoViewModel: ProductoViewModel,navController: NavController) {
    val productos = productoViewModel.productos //retorna la lista de productos
    ProductList(productos,navController,productoViewModel)
}

@Composable
fun ProductList(productos: List<Producto>,navController: NavController,productoViewModel: ProductoViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        //botonera de productos el cual contiene insertar producto y salir que redirige a la pantalla de login
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {navController.navigate("insertProduct")}) { Text("Ingresar") }
            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true } // borra todo el back stack y evita errores de regresion de pantalla
                    launchSingleTop = true          // evita duplicar login
                }
            }) { Text("Salir") }
        }

        Spacer(modifier = Modifier.height(24.dp))
        //lazyColumn usada para representar listas de objetos, la caracteristica de este componente es que carga aquellos elementos que son visibles para el
        //usuario y el resto de elementos los deja en espera, optimizando el consumo de memoria
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //items es un metodo similar a for each usado para iterar la lista de productos y poder acceder a los atributos individuales de cada objeto
            items(productos) { producto ->
                var expanded by remember { mutableStateOf(false) } //metodo lamba de cual indica que por cada producto en la lista va a generar una variable
                                                                            //mutable la cual nos indicara si un elemento esta expandido o unicamente se observan las
                                                                            // primeras dos lineas

                Column(
                    modifier = Modifier
                        .clickable { expanded = !expanded } //a la columna se le agrega el modificador clickable el cual es usado para poder detectar clics sobre elementos
                                                            //de la columna en caso de que se de un toque la variable del producto cambia a verdadero y da una descripcion
                                                            //mas detallada
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //muestra los dos primeros elementos, el codigo de producto y la descripcion del producto
                    Text(text = "${producto.codigo}")
                    Text(text = "${producto.descripcion}")

                    //en caso de que la variable del objeto listado cambie se despliega y se puede observar las demas caracteristicas del producto
                    if (expanded) {
                        Text(text = "$${producto.costo}")
                        Text(text = "Stock: ${producto.disponibilidad}")
                        Text(text = "Fecha de Fabricación: ${producto.fecha_fab}")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            //navController con una ruta con parametros usado para enviar el codigo del producto a la siguiente vista y esta pueda
                            //tener al objeto del cual se hace referencia, toda esta definicion se encuentra en el archivo navController
                            navController.navigate("updateProduct/${producto.codigo}") }) { Text("Modificar") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            productoViewModel.delete(producto.codigo)
                        }) { Text("Eliminar")}
                    }
                }
            }
        }
    }

}



@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreview() {
    val productosFake = listOf(
        Producto("P001", "Laptop Lenovo IdeaPad 3", "2024-01-15", 750.0, 10),
        Producto("P002", "Mouse Logitech M185", "2023-11-20", 18.5, 45)
    )
    BodyContentPreview(productosFake)
}

@Composable
fun BodyContentPreview(productos: List<Producto>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Primera columna: botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {}) {
                Text("Ingresar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {}) {
                Text("Salir")
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) // espacio entre columnas

        // Segunda columna: productos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (producto in productos) {
                var expanded by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Siempre mostrar código y nombre
                    Text(text = "${producto.codigo}")
                    Text(text = "${producto.descripcion}")

                    // Mostrar detalles solo si está expandido
                    if (expanded) {
                        Text(text = "$${producto.costo}")
                        Text(text = "Stock: ${producto.disponibilidad}")
                        Text(text = "Fecha: ${producto.fecha_fab}")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center

                    ) {
                        Button(onClick = {}) {
                            Text("Modificar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {}) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }

}
