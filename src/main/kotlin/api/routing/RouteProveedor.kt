package com.helcode.api.routing

import api.model.Proveedor
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityProveedor
import com.helcode.api.services.AuthService
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Route.routeProvedor() {
    val db: Database = DBConnection.getDatabaseInstance()

    authenticate("auth-jwt") { // Protegemos todas las rutas dentro de este bloque
        route("/provedor") {

            get("/") {
                call.respondText("WELCOME TO KTOR MYSQL")
            }

            post("/registerProveedor") {
                try {
                    val proveedor: Proveedor = call.receive()
                    val noOfRowsAffected = db.insert(EntityProveedor) {
                        set(it.nomProveedor, proveedor.nomProveedor)
                        set(it.numContacto, proveedor.numContacto)
                        set(it.direccion, proveedor.direccion)
                        set(it.email, proveedor.email)
                    }

                    if (noOfRowsAffected > 0) {
                        call.respond(HttpStatusCode.OK, GenericRespose(true, "$noOfRowsAffected row(s) inserted"))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, GenericRespose(false, "Failed to insert"))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, GenericRespose(false, "Error: ${e.message}"))
                }
            }

            get("/Proveedores") {
                try {
                    val listP = db.from(EntityProveedor)
                        .select()
                        .map {
                            Proveedor(
                                idProveedor = it[EntityProveedor.idProveedor],
                                numContacto = it[EntityProveedor.numContacto],
                                nomProveedor = it[EntityProveedor.nomProveedor],
                                direccion = it[EntityProveedor.direccion],
                                email = it[EntityProveedor.email]
                            )
                        }

                    if (listP.isNotEmpty()) {
                        call.respond(HttpStatusCode.OK, GenericRespose(true, listP))
                    } else {
                        call.respond(HttpStatusCode.OK, GenericRespose(false, "No hay Proveedores"))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, GenericRespose(false, "Error: ${e.message}"))
                }
            }

            get("/Proveedores/{idProveedor}") {
                try {
                    val proveedorId = call.parameters["idProveedor"]?.toIntOrNull()
                    if (proveedorId == null) {
                        call.respond(HttpStatusCode.BadRequest, GenericRespose(false, "ID inválido"))
                        return@get
                    }

                    val proveedor = db.from(EntityProveedor)
                        .select()
                        .where { EntityProveedor.idProveedor eq proveedorId }
                        .map {
                            Proveedor(
                                idProveedor = it[EntityProveedor.idProveedor],
                                numContacto = it[EntityProveedor.numContacto],
                                nomProveedor = it[EntityProveedor.nomProveedor],
                                direccion = it[EntityProveedor.direccion],
                                email = it[EntityProveedor.email]
                            )
                        }
                        .firstOrNull()

                    if (proveedor != null) {
                        call.respond(HttpStatusCode.OK, GenericRespose(true, proveedor))
                    } else {
                        call.respond(HttpStatusCode.NotFound, GenericRespose(false, "Proveedor no encontrado"))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, GenericRespose(false, "Error: ${e.message}"))
                }
            }

            put("/Proveedores/{idProveedor}") {
                try {
                    val proveedorId = call.parameters["idProveedor"]?.toIntOrNull()
                    if (proveedorId == null) {
                        call.respond(HttpStatusCode.BadRequest, GenericRespose(false, "ID inválido"))
                        return@put
                    }

                    val proveedorReq: Proveedor = call.receive()

                    val noOfRowsAffected = db.update(EntityProveedor) {
                        set(it.nomProveedor, proveedorReq.nomProveedor)
                        set(it.numContacto, proveedorReq.numContacto)
                        set(it.direccion, proveedorReq.direccion)
                        set(it.email, proveedorReq.email)
                        where { it.idProveedor eq proveedorId }
                    }

                    if (noOfRowsAffected > 0) {
                        call.respond(HttpStatusCode.OK, GenericRespose(true, "Updated $noOfRowsAffected row(s)"))
                    } else {
                        call.respond(HttpStatusCode.NotFound, GenericRespose(false, "No rows updated"))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, GenericRespose(false, "Error: ${e.message}"))
                }
            }

            delete("/DeleteProveedores/{idProveedor}") {
                try {
                    val proveedorId = call.parameters["idProveedor"]?.toIntOrNull()
                    if (proveedorId == null) {
                        call.respond(HttpStatusCode.BadRequest, GenericRespose(false, "ID inválido"))
                        return@delete
                    }

                    val noOfRowsAffected = db.delete(EntityProveedor) {
                        it.idProveedor eq proveedorId
                    }

                    if (noOfRowsAffected > 0) {
                        call.respond(HttpStatusCode.OK, GenericRespose(true, "Deleted $noOfRowsAffected row(s)"))
                    } else {
                        call.respond(HttpStatusCode.NotFound, GenericRespose(false, "Proveedor no encontrado"))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, GenericRespose(false, "Error: ${e.message}"))
                }
            }
        }
    }
}
