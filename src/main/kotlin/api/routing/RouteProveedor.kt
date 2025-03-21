package com.helcode.api.routing

import api.model.Proveedor
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityProveedor
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeProvedor(){
    val db:Database = DBConnection.getDatabaseInstance()
    routing {
        get("/")
        {
            call.respondText { "WELCOME TO KTOR MYSQL" }
        }


        post("/registerProveedor") {

            try {
                val proveedor:Proveedor = call.receive()
                val noOfRowsAffected = db.insert( EntityProveedor)
                {
                    set(it.nomProveedor, proveedor.nomProveedor)
                    set(it.numContacto, proveedor.numContacto)
                    set(it.direccion, proveedor.direccion)
                    set(it.email, proveedor.email)


                }

                if (noOfRowsAffected >0 ){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = "$noOfRowsAffected insert to row affected")
                    )
                }else {
                    call.respond(
                        HttpStatusCode.BadRequest,GenericRespose(isSuccess = false,
                            data = "Error insert to row affected")
                    )
                }

            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }


        get("/Proveedores")
        {
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

                if (listP.isNotEmpty()){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = listP)
                    )
                }else {
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = false,
                            data = "No hay  Proveedores")
                    )
                }

            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }

        get("/Proveedores/{idProveedor}")
        {
            try {
                val proveedorIdsr = call.parameters["idProveedor"]
                val proveedorId = proveedorIdsr?.toInt() ?: -1
                val proveedor = db.from(EntityProveedor)
                    .select()
                    .where{
                        EntityProveedor.idProveedor eq proveedorId
                    }
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

                if (proveedor != null){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = proveedor)
                    )
                }else {
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = false,
                            data = null)
                    )
                }

            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }





        put("/Proveedores/{idProveedor}")
        {
            try {
                val proveedorIdsr = call.parameters["idProveedor"]
                val proveedorId = proveedorIdsr?.toInt() ?: -1
                val proveedorReq:Proveedor = call.receive()

                val noOfRowsAffected = db.update( EntityProveedor)
                {
                    set(it.nomProveedor, proveedorReq.nomProveedor)
                    set(it.numContacto, proveedorReq.numContacto)
                    set(it.direccion, proveedorReq.direccion)
                    set(it.email, proveedorReq.email)

                    where{
                            it.idProveedor eq proveedorId
                        }

                }


                if (noOfRowsAffected >0){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = "Update rows are affected $noOfRowsAffected")
                    )
                }else {
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = false,
                            data = "Error Update rows are affected $noOfRowsAffected")
                    )
                }

            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }




        delete ("/DeleteProveedores/{idProveedor}")
        {
            try {
                val proveedorIdsr = call.parameters["idProveedor"]
                val proveedorId = proveedorIdsr?.toInt() ?: -1


                val noOfRowsAffected = db.delete( EntityProveedor)
                {
                    it.idProveedor eq  proveedorId

                }


                if (noOfRowsAffected >0){
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = true,
                            data = "Delete rows are affected $noOfRowsAffected")
                    )
                }else {
                    call.respond(
                        HttpStatusCode.OK,GenericRespose(isSuccess = false,
                            data = "Error Delete rows are affected $noOfRowsAffected\"")
                    )
                }

            }catch (e:Exception){
                println("Error al conectar: ${e.message}")
            }
        }

    }
}