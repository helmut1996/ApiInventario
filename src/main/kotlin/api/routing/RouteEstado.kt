package com.helcode.api.routing

import api.model.Estado
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityEstado
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

fun Application.routeEstado() {
    val db: Database = DBConnection.getDatabaseInstance()
    routing {

            get("/Estados")
            {
                try {
                    val listE = db.from(EntityEstado)
                        .select()
                        .map {
                            Estado(
                                idEstado = it[EntityEstado.idEstado],
                                estado =  it[EntityEstado.estado],
                                descripcion = it[EntityEstado.descripcion],
                            )
                        }

                    if (listE.isNotEmpty()){
                        call.respond(
                            HttpStatusCode.OK, GenericRespose(isSuccess = true,
                                data = listE)
                        )
                    }else {
                        call.respond(
                            HttpStatusCode.OK, GenericRespose(isSuccess = false,
                                data = "No hay  Estados")
                        )
                    }

                }catch (e:Exception){
                    println("Error al conectar: ${e.message}")
                }
            }

    }
}