package com.helcode.api.routing

import api.model.Estado
import com.helcode.api.Database.DBConnection
import com.helcode.api.repository.estadoRepository.EstadoRepository
import com.helcode.api.repository.estadoRepository.EstadoRepositoryImpl
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Application.routeEstado() {
    val db: Database = DBConnection.getDatabaseInstance()
    val estadoRepository: EstadoRepository = EstadoRepositoryImpl(db)
    routing {

            get("/Estados")
            {
                try {
                    val listE = estadoRepository.getEstado()

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