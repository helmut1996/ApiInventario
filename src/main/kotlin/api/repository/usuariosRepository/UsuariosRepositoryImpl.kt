package com.helcode.api.repository.usuariosRepository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.helcode.api.Database.Entity.EntityUsuarios
import com.helcode.api.model.Usuarios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.*

class UsuariosRepositoryImpl(private val db: Database): UsuariosRepository {
    override suspend fun insertUsuario(usuario: Usuarios): Int? = withContext(Dispatchers.IO) {
        // Encriptar la contraseña usando BCrypt
        val hashedPassword = BCrypt.withDefaults().hashToString(12, usuario.contrasena!!.toCharArray())

        // Insertar usuario en la base de datos
        db.insertAndGenerateKey(EntityUsuarios) {
            set(it.nomUsuario, usuario.nomUsuario)
            set(it.usuario, usuario.usuario)
            set(it.contrasena, hashedPassword) // Guardamos la contraseña encriptada
            set(it.idPermiso, usuario.idPermiso)
            set(it.idEstado, usuario.idEstado)
        } as? Int
    }

    override suspend fun getUsuarios(): List<Usuarios> = withContext(Dispatchers.IO) {
        db.from(EntityUsuarios)
            .select()
            .map {
                Usuarios(
                    idUsuario = it[EntityUsuarios.idUsuario],
                    nomUsuario = it[EntityUsuarios.nomUsuario],
                    usuario = it[EntityUsuarios.usuario],
                    contrasena = it[EntityUsuarios.contrasena],
                    idEstado = it[EntityUsuarios.idEstado],
                    idPermiso = it[EntityUsuarios.idPermiso],

                    )
            }
    }

    override suspend fun getUsuarioById(usuafioId: Int): Usuarios? {
        TODO("Not yet implemented")
    }


    override suspend fun updateUsuario(usuarioId: Int, usuarios: Usuarios): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUsuario(idUsuario: Int): Int = withContext(Dispatchers.IO) {
        db.delete(EntityUsuarios)
        {
            it.idUsuario eq idUsuario
        }
    }
}