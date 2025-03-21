package com.helcode.api.routing

import api.model.Permisos
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityPermisos
import com.helcode.api.services.AuthService
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

fun Route.routePermiso() {
    val db: Database = DBConnection.getDatabaseInstance()

        get("/Permisos")
        {
            try {
                val listPermiso = db.from(EntityPermisos)
                    .select()
                    .map {
                        Permisos(
                            idPermiso = it[EntityPermisos.idPermiso],
                            permiso =  it[EntityPermisos.permiso],
                        )
                    }

                if (listPermiso.isNotEmpty()){
                    call.respond(
                        HttpStatusCode.OK, GenericRespose(isSuccess = true,
                            data = listPermiso)
                    )
                }else {
                    call.respond(
                        HttpStatusCode.OK, GenericRespose(isSuccess = false,
                            data = "No hay  Permisos")
                    )
                }

            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }
}