package com.helcode.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuarios(
    val idUsuario : Int? = null,
    val nomUsuario : String? = null,
    val usuario : String? = null,
    val contrasena : String? = null,
    val idPermiso : Int? = null,
    val idEstado : Int? = null
)
