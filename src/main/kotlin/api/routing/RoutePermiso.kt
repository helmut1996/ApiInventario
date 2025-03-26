package com.helcode.api.routing

import com.helcode.api.Database.DBConnection
import com.helcode.api.repository.permisoRepository.PermisosRepository
import com.helcode.api.repository.permisoRepository.PermisosRepsositoryImpl
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Application.routePermiso(){
    val db: Database = DBConnection.getDatabaseInstance()
    val permisoRepository: PermisosRepository = PermisosRepsositoryImpl(db)
    routing {
        get("/Permisos")
        {
            try {
                val listPermiso = permisoRepository.getPermisos()

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
}