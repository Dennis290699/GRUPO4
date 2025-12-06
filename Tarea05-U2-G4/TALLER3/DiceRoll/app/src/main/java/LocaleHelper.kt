package com.example.diceroller

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Cambia el idioma (locale) de la aplicación y devuelve un nuevo Context configurado.
 *
 * Este método permite forzar el idioma de la app independientemente del idioma del sistema.
 * Se utiliza normalmente antes de cargar recursos como strings, layouts o activities,
 * para que estos se muestren en el idioma seleccionado.
 *
 * @param language Código del idioma a establecer (por ejemplo: "es", "en", "fr").
 * @return Un nuevo Context con la configuración regional actualizada.
 */
fun Context.setAppLocale(language: String): Context {
    // Crear el Locale usando el código de idioma solicitado
    val locale = Locale(language)

    // Establecer este locale como predeterminado para toda la app
    Locale.setDefault(locale)

    // Crear una copia de la configuración actual del dispositivo
    val config = Configuration(resources.configuration)

    // Aplicar el nuevo idioma a la configuración
    config.setLocale(locale)

    // Ajustar la dirección del layout según el idioma (LTR o RTL)
    config.setLayoutDirection(locale)

    // Devolver un nuevo Context basado en esta configuración
    return createConfigurationContext(config)
}
