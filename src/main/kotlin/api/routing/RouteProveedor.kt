package com.helcode.api.routing

import api.model.Proveedor
import com.helcode.api.Database.DBConnection
import com.helcode.api.repository.proveedorRepository.ProveedorRepository
import com.helcode.api.repository.proveedorRepository.ProveedorRepositoryImpl
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database


fun Application.routeProvedor(){
    val db:Database = DBConnection.getDatabaseInstance()
    val proveedorRepository: ProveedorRepository = ProveedorRepositoryImpl(db)
    routing {
        authenticate("auth-jwt") {
            post("/registerProveedor") {

                try {
                    val proveedor:Proveedor = call.receive()
                    val noOfRowsAffected = proveedorRepository.insertProveedor(proveedor)

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
                    val listP = proveedorRepository.getProveedor()

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
                    val proveedor = proveedorRepository.getProveedorById(proveedorId)

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

                    val noOfRowsAffected = proveedorRepository.updateProveedor(proveedorId,proveedorReq)


                    if (noOfRowsAffected >0){
                        call.respond(
                            HttpStatusCode.OK,GenericRespose(isSuccess = true,
                                data = "Update rows are affected $noOfRowsAffected")
                        )
                    }else {
                        call.respond(
                            HttpStatusCode.BadRequest,GenericRespose(isSuccess = false,
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


                    val noOfRowsAffected = proveedorRepository .deleteProveedor(proveedorId)


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
}