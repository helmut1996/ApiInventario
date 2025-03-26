package com.helcode.api.repository.usuariosRepository

import com.helcode.api.model.Usuarios

interface UsuariosRepository {
    suspend fun insertUsuario(usuarios: Usuarios): Int?

    suspend fun getUsuarios(): List<Usuarios>

    suspend fun getUsuarioById(usuafioId:Int): Usuarios ?

    suspend fun updateUsuario(usuarioId:Int, usuarios: Usuarios):Int

    suspend fun deleteUsuario(idUsuario:Int): Int?

}