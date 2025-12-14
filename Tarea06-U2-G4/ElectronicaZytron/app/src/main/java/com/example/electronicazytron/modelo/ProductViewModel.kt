package com.example.electronicazytron.modelo

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class ProductoViewModel : ViewModel() {
    private val repository = ProductoRepository()

    // Lista observable para la UI
    var productos = mutableStateListOf<Producto>()
        private set

    // Cargar productos desde el repositorio
    fun cargarProductos() {
        productos.clear()
        productos.addAll(repository.listar())
    }

    fun find(codigo: String): Producto? {
        return repository.find(codigo)
    }

    // Actualizar un producto en el repositorio
    fun update(codigo: String, producto: Producto) {
        repository.update(codigo, producto)
        cargarProductos() // refrescar la lista observable
    }

    fun delete(codigo: String){
        repository.delete(codigo)
        cargarProductos()
    }

    fun insert(producto: Producto){
        repository.agregar(producto)
        cargarProductos()
    }
}
