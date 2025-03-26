package com.helcode.api.repository.authRepository

import com.helcode.api.Database.Entity.EntityUsuarios
import com.helcode.api.model.Usuarios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.*

class AuthRepositoryImpl(private val db: Database) : AuthRepostory  {
    override suspend fun getUserByUsername(username: String): Usuarios?  = withContext(Dispatchers.IO){
        db.from(EntityUsuarios)
            .select()
            .where { EntityUsuarios.usuario eq username }
            .map {
                Usuarios(
                    idUsuario = it[EntityUsuarios.idUsuario],
                    nomUsuario = it[EntityUsuarios.nomUsuario],
                    usuario = it[EntityUsuarios.usuario],
                    contrasena = it[EntityUsuarios.contrasena],
                    idPermiso = it[EntityUsuarios.idPermiso],
                    idEstado = it[EntityUsuarios.idEstado]
                )
            }
            .firstOrNull()
    }
}