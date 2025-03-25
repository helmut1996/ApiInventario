package com.helcode.api.security

import com.helcode.api.Database.Entity.EntityUsuarios
import com.helcode.api.model.Usuarios
import org.ktorm.database.Database
import org.ktorm.dsl.*

// Función para obtener un usuario por su nombre de usuario
fun getUserByUsername(db: Database, username: String): Usuarios? {
    return db.from(EntityUsuarios)
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