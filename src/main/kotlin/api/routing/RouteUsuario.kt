package com.helcode.api.routing

import at.favre.lib.crypto.bcrypt.BCrypt
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityUsuarios
import com.helcode.api.model.Usuarios
import com.helcode.api.services.AuthService
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Route.routeUsuarios() {
    val db: Database = DBConnection.getDatabaseInstance()


        get("/Usuarios") {

            try {
                val listU = db.from(EntityUsuarios)
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
                if (listU.isNotEmpty()){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = listU)
                    )
                }else {
                    call.respond(
                        HttpStatusCode.BadRequest,GenericRespose(isSuccess = true,
                            data = null)
                    )
                }
            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }


        post("/InsertarUsuario") {
            try {
                val usuario = call.receive<Usuarios>()

                // Verifica que los datos sean válidos
                if (usuario.nomUsuario.isNullOrBlank() || usuario.usuario.isNullOrBlank() || usuario.contrasena.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Todos los campos son obligatorios")
                    return@post
                }

                // Encriptar la contraseña usando BCrypt
                val hashedPassword = BCrypt.withDefaults().hashToString(12, usuario.contrasena.toCharArray())

                // Insertar usuario en la base de datos
                val idUsuario = db.insertAndGenerateKey(EntityUsuarios) {
                    set(it.nomUsuario, usuario.nomUsuario)
                    set(it.usuario, usuario.usuario)
                    set(it.contrasena, hashedPassword) // Guardamos la contraseña encriptada
                    set(it.idPermiso, usuario.idPermiso)
                    set(it.idEstado, usuario.idEstado)
                }

                call.respond(
                    HttpStatusCode.Created, GenericRespose(isSuccess = true,
                        data = "Usuario Insertado con id : $idUsuario ")
                )

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error en el servidor: ${e.localizedMessage}")
            }
        }


        delete("DeleteUsuario/{idUsuario}") {
            try {
                val usuarioIdsr = call.parameters["idUsuario"]
                val usuarioId = usuarioIdsr?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityUsuarios)
                {
                    it.idUsuario eq usuarioId
                }

                if (noOfRowsAffected >0){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = "Delete rows are affected $noOfRowsAffected")
                    )
                }else {
                    call.respond(
                        HttpStatusCode.BadRequest,GenericRespose(isSuccess = false,
                            data = "Error Delete rows are affected $noOfRowsAffected\"")
                    )
                }

            }catch (e:Exception){
                println("Error de conexion ${e.localizedMessage}")
            }


        }

}