package com.example.lemonade

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Busca y devuelve la [Activity] asociada a este [Context].
 *
 * Esta función de extensión es útil cuando se trabaja con capas de Context en Android,
 * especialmente en Jetpack Compose, donde el `LocalContext` puede estar envuelto dentro
 * de varios `ContextWrapper`.
 *
 * El comportamiento es:
 * - Si el Context actual **es una Activity**, se retorna directamente.
 * - Si es un **ContextWrapper**, se navega recursivamente a su `baseContext` para intentar
 *   encontrar la Activity real detrás de los wrappers.
 * - Si no es ninguno de los anteriores, retorna `null`.
 *
 * @receiver Context desde el cual se desea obtener la Activity.
 * @return La [Activity] encontrada o `null` si no existe una Activity en la cadena de Context.
 *
 * ## Ejemplos de uso:
 * ```
 * val activity = LocalContext.current.findActivity()
 * activity?.finish()
 *
 * val window = LocalContext.current.findActivity()?.window
 * ```
 */
fun Context.findActivity(): Activity? = when (this) {

    // Si el contexto actual es una Activity, se devuelve directamente.
    is Activity -> this

    // Si es un ContextWrapper, se intenta obtener la Activity desde su baseContext.
    is ContextWrapper -> baseContext.findActivity()

    // Si no es Activity ni ContextWrapper, no se puede obtener una Activity.
    else -> null
}
