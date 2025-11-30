package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import com.example.cupcake.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// enviando el precio numérico directamente al ViewModel.

/**
 * Precios fijos de recargo progresivo según la fecha de retiro.
 * El índice 0 corresponde a la fecha más cercana (hoy), que tiene el recargo más alto.
 */
private val PICKUP_DAY_SURCHARGE = listOf(
    3.00,   // Índice 0: hoy (recargo más alto)
    2.00,   // Índice 1: mañana
    1.00,   // Índice 2: pasado mañana
    0.50    // Índice 3: en 3 días (recargo más bajo)
)

/**
 * ViewModel que gestiona el estado del pedido (cantidad, sabor, fecha, precio).
 */
class OrderViewModel : ViewModel() {

    // El objeto que almacena el estado actual del pedido (MutableStateFlow permite modificarlo).
    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions()))

    // La versión inmutable del estado (StateFlow) que la UI consume para reaccionar a los cambios.
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    // Variable privada que almacena el precio por unidad del sabor seleccionado actualmente.
    // Esto es clave para la independencia del idioma, ya que siempre usamos este Double para calcular el total.
    private var currentFlavorPrice: Double = 1.50 // Precio por defecto (ej. el precio de Vainilla)

    /**
     * Actualiza la cantidad de galletas y recalcula el precio total.
     */
    fun setQuantity(numberCookies: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberCookies,
                // Llama a calculatePrice, usando la nueva cantidad y manteniendo el sabor/fecha actuales.
                price = calculatePrice(quantity = numberCookies)
            )
        }
    }

    /**
     * Define el sabor y su precio asociado.
     * @param desiredFlavor El nombre traducido del sabor (para almacenar en el estado y mostrar en el resumen).
     * @param price El precio numérico por unidad, resuelto previamente en la capa de UI.
     */
    fun setFlavor(desiredFlavor: String, price: Double) {
        // Almacena el precio numérico. Esto es independiente del idioma del 'desiredFlavor' string.
        currentFlavorPrice = price

        _uiState.update { currentState ->
            currentState.copy(
                flavor = desiredFlavor,
                // Recalcula el precio total con el nuevo precio unitario.
                price = calculatePrice(flavor = desiredFlavor)
            )
        }
    }

    /**
     * Define la fecha de retiro y recalcula el precio total aplicando el recargo.
     */
    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = pickupDate,
                // Recalcula el precio con el nuevo recargo de fecha.
                price = calculatePrice(pickupDate = pickupDate)
            )
        }
    }

    /**
     * Reinicia el estado del pedido a sus valores iniciales.
     */
    fun resetOrder() {
        currentFlavorPrice = 1.50 // Restablece el precio base a su valor por defecto.
        // Crea un nuevo OrderUiState con los valores por defecto.
        _uiState.value = OrderUiState(pickupOptions = pickupOptions())
    }

    /**
     * Función principal para calcular el precio final del pedido.
     * Utiliza valores por defecto si no se proporcionan, tomándolos del estado actual (_uiState).
     */
    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date,
        flavor: String = _uiState.value.flavor // Se mantiene aquí pero no se usa para la lógica de precio
    ): String {

        // Cálculo base: Cantidad * Precio unitario (currentFlavorPrice).
        // NOTA: Usar currentFlavorPrice garantiza la independencia del idioma.
        var calculatedPrice = quantity * currentFlavorPrice

        // --- Aplicar recargo por fecha ---
        val options = pickupOptions()
        // Encuentra el índice de la fecha de retiro seleccionada en la lista de opciones.
        val selectedIndex = options.indexOf(pickupDate)

        if (selectedIndex != -1) {
            // Usa el índice para aplicar el recargo correspondiente de PICKUP_DAY_SURCHARGE.
            calculatedPrice += PICKUP_DAY_SURCHARGE[selectedIndex]
        }

        // Formatea el precio final como moneda local usando la configuración regional del dispositivo.
        return NumberFormat.getCurrencyInstance().format(calculatedPrice)
    }

    /**
     * Genera la lista de los próximos 4 días disponibles para el retiro (incluido hoy).
     */
    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        // Formatea la fecha usando el Locale por defecto del dispositivo para la traducción de días/meses.
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Repite 4 veces (hoy + 3 días)
        repeat(4) {
            // Añade la fecha formateada a la lista
            dateOptions.add(formatter.format(calendar.time))
            // Avanza el calendario al día siguiente
            calendar.add(Calendar.DATE, 1)
        }

        return dateOptions
    }
}