package com.example.recipebox.ui.navigation

object ArgsNav {
    const val RECETA_ID = "recetaId"
    const val SIN_ID = -1L
}

sealed class Destino(val ruta: String) {

    object Inicio : Destino("inicio")

    object Configuracion : Destino("configuracion")

    object RecetaForm : Destino(
        "receta_form?${ArgsNav.RECETA_ID}={${ArgsNav.RECETA_ID}}"
    ) {
        // Sin argumento = crear receta. Con id = editar esa receta.
        fun crearRuta(recetaId: Long = ArgsNav.SIN_ID): String =
            "receta_form?${ArgsNav.RECETA_ID}=$recetaId"
    }
}