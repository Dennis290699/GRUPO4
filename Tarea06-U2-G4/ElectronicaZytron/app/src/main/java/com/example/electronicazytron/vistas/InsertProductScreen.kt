package com.example.electronicazytron.vista

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.electronicazytron.modelo.Producto
import com.example.electronicazytron.modelo.ProductoViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
//vista y pagina donde se ingresa nuevos productos a la lista estatica que se tiene
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable//Metodo InsertProduct el cual tiene un productViewModel la cual es una clase que se comunica con el repositorio evitando exponer metodos inportantes
            //ademas se define un navController que se usa para poder acceder a las rutas definidas en el appnavigator, en pocas palabras poder moverse entre pantallas
fun InsertProducScreen(productoViewModel: ProductoViewModel,
                       navController: NavController
) {
    Scaffold {
        BodyContent(productoViewModel,navController) //constructor de bodycontent el cual pasa como parametros un model y un controller
    }
}

@Composable
fun BodyContent(productoViewModel: ProductoViewModel,
                navController: NavController) {
    var codigo by remember { mutableStateOf("") } //para poder modificar y enviar variables se usa variables mutables las cuales constan de su tipo de variable
                                                //var acompañados del nombre de la variable codigo, by , remember que repesenta la variable a recordar si se modific
                                                //segudos del metodo mutableStateOf y la inicializacion de la variable, ademas son necesarios para los TextField a usar
                                                //estos TextFiel representan los input para ingreso de datos
    var descripcion by remember { mutableStateOf("") }
    var fecha_fab by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var disponibilidad by remember { mutableStateOf("") }

//aqui dentro se ingresan los nuevos elementos a dibujarse en la pantalla, en este caso se estan listando los atributos para agregar un nuevo producto
    Column(modifier = Modifier.fillMaxSize().padding(top = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Código de Producto:")
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = codigo,
            onValueChange = { codigo = it },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Fecha de Fabricación:")
        Spacer(modifier = Modifier.height(15.dp))
        //TextFiel = usado para ingreso de datos consta de dos partes, la variable o el valor inicial del texto con value="VariableAModificarYEnviar"
        //onValueChange metodo que permite la modificacion de la variable, es necesario agregar = it para que se habilite la posibilidad de cambiar el texto
        TextField(
            value = fecha_fab,
            onValueChange = { fecha_fab = it },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Costo:")
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = costo,
            onValueChange = {newValue ->
                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    costo = newValue
                }
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Disponibididad de Producto:")
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = disponibilidad,
            onValueChange = {newValue ->
                if (newValue.matches(Regex("^\\d*\$"))) {
                    disponibilidad = newValue
                }
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Descripción de Producto:")
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
        )
        Spacer(modifier = Modifier.height(20.dp))
        //boton de envio de datos al modelo de productos el cual espera un objeto Producto a ingresar en la lista que se definio
        Button(onClick = {
            productoViewModel.insert(Producto(codigo,descripcion,fecha_fab,costo.toDouble(),disponibilidad.toInt()))

            navController.navigate("productos"){ //navControler usado para regresar a la pantalla de productos
                popUpTo("insertProduct") {//quita todo el back stack de las rutas asta la ruta insertProduct Evitando bugs de regreso de pantalla
                    inclusive = true
                }}

        }) {
            Text("Ingresar")
        }
    }
}


// Preview para InsertProductScreen
@Preview(showSystemUi = true)
@Composable
fun InsertProductScreenEmptyPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Código de Producto:")
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(8.dp))
        Text("Fecha de Fabricación:")
        TextField(value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(8.dp))
        Text("Costo:")
        TextField(value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(8.dp))
        Text("Disponibilidad:")
        TextField(value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(8.dp))
        Text("Descripción:")
        TextField(value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text("Ingresar")
        }
    }
}
