package com.example.cupcake.data

import com.example.cupcake.R

object DataSource {
    val flavors = listOf(
        R.string.vanilla,
        R.string.chocolate,
        R.string.red_velvet,
        R.string.salted_caramel,
        R.string.coffee,
        R.string.special_flavor
    )

    // NUEVO: Mapa de precios basado en IDs, no en texto
    val flavorPrices = mapOf(
        R.string.vanilla to 1.50,
        R.string.chocolate to 1.80,
        R.string.red_velvet to 2.00,
        R.string.salted_caramel to 1.75,
        R.string.coffee to 1.90,
        R.string.special_flavor to 2.50
    )

    val quantityOptions = listOf(
        Pair(R.string.one_cupcake, 1),
        Pair(R.string.six_cupcakes, 6),
        Pair(R.string.twelve_cupcakes, 12)
    )
}