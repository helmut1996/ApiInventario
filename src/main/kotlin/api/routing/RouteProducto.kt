package com.helcode.api.routing

import api.model.Productos
import com.helcode.api.Database.DBConnection
import com.helcode.api.repository.productosRepository.ProductosRepository
import com.helcode.api.repository.productosRepository.ProductosRepositoryImpl
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database


fun Application.routeProducto(){
    val db: Database = DBConnection.getDatabaseInstance()
    val repoProductos: ProductosRepository = ProductosRepositoryImpl(db)

    routing {
        authenticate("auth-jwt") {
            get("/Productos")
            {
                try {
                    val listP = repoProductos.getProducto()

                    if (listP.isNotEmpty()){
                        call.respond(
                            HttpStatusCode.OK, GenericRespose(isSuccess = true,
                                data = listP)
                        )
                    }else {
                        call.respond(
                            HttpStatusCode.BadRequest, GenericRespose(isSuccess = false,
                                data = "No hay  Productos")
                        )
                    }

                }catch (e:Exception){
                    println("Error al conectar: ${e.message}")
                }
            }

            get ( "/Producto/{id}" )
            {
                try{
                    val productoIdsr = call.parameters["id"]
                    val productoId = productoIdsr?.toInt() ?: -1
                    val producto = repoProductos.getProductoById(productoId)

                    if (producto !=null){
                        call.respond(
                            HttpStatusCode.OK,GenericRespose(isSuccess = true,
                                data = producto)
                        )
                    }else {
                        call.respond(
                            HttpStatusCode.BadRequest,GenericRespose(isSuccess = false,
                                data = null)
                        )
                    }

                }catch (e:Exception){
                    println("Errror al conectar ${e.message}")
                }
            }





            post("/NuevoProducto") {
                val multipart = call.receiveMultipart()
                var producto: Productos? = null
                var imageUrl: String? = null

                multipart.forEachPart { part ->
                    println("Recibido: ${part.name}") // LOG PARA DEBUG
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "descripcion" -> producto = (producto ?: Productos()).copy(descripcion = part.value)
                                "precio" -> producto = (producto ?: Productos()).copy(precio = part.value.toDoubleOrNull() ?: 0.0)
                                "nomSerie" -> producto = (producto ?: Productos()).copy(nomSerie = part.value)
                                "idProveedor" -> producto = (producto ?: Productos()).copy(idProveedor = part.value.toIntOrNull() ?: 0)
                                "numProducto" -> producto = (producto ?: Productos()).copy(numProducto = part.value)
                                "idProducto" -> producto = (producto ?: Productos()).copy(idProducto = part.value)
                                "stock" -> producto = (producto ?: Productos()).copy(stock = part.value.toIntOrNull() ?: 0)
                            }
                        }

                        is PartData.FileItem -> {
                            imageUrl = repoProductos.saveImage(part)
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                if (producto != null && imageUrl != null) {
                    val nuevoProducto = producto!!.copy(url_imagen = imageUrl)

                    try {
                        val productoInsert = repoProductos.insertProducto(nuevoProducto)
                        if (productoInsert != null) {
                            call.respond(HttpStatusCode.Created, productoInsert)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError, "Error al insertar el producto")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace() // Imprime el error en la consola
                        call.respond(HttpStatusCode.InternalServerError, "Error en el servidor: ${e.localizedMessage}")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Datos insuficientes")
                }
            }

            put("/ActualizarProducto/{id}") {
                val multipart = call.receiveMultipart()
                var producto: Productos? = null
                var imageUrl: String? = null
                val productoId = call.parameters["id"]?.toIntOrNull()

                if (productoId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de producto requerido")
                    return@put
                }

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "descripcion" -> producto = (producto ?: Productos()).copy(descripcion = part.value)
                                "precio" -> producto = (producto ?: Productos()).copy(precio = part.value.toDoubleOrNull() ?: 0.0)
                                "nomSerie" -> producto = (producto ?: Productos()).copy(nomSerie = part.value)
                                "idProveedor" -> producto = (producto ?: Productos()).copy(idProveedor = part.value.toIntOrNull() ?: 0)
                                "numProducto" -> producto = (producto ?: Productos()).copy(numProducto = part.value)
                                "idProducto" -> producto = (producto ?: Productos()).copy(idProducto = part.value)
                                "stock" -> producto = (producto ?: Productos()).copy(stock = part.value.toIntOrNull() ?: 0)
                            }
                        }

                        is PartData.FileItem -> {
                            imageUrl = repoProductos.saveImage(part)
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                if (producto != null) {
                    try {
                        // If there is a new image, update the url_imagen
                        if (imageUrl != null) {
                            producto = producto!!.copy(url_imagen = imageUrl)
                        }

                        val noOfRowsAffected = repoProductos.updateProducto(productoId, producto!!)

                        if (noOfRowsAffected == 0) {
                            call.respond(HttpStatusCode.NotFound, "Producto no encontrado")
                        } else {
                            call.respond(HttpStatusCode.OK, "Producto actualizado correctamente")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "Error en el servidor: ${e.localizedMessage}")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Datos insuficientes para la actualizacion")
                }
            }

            delete("/DeleteProducto/{id}") {
                try {
                    val productoIdsr = call.parameters["id"]
                    val productoId = productoIdsr?.toInt() ?: -1

                    val noOfRowsAffected = repoProductos.deleteProducto(productoId)

                    if (noOfRowsAffected > 0){
                        call.respond(
                            HttpStatusCode.OK,GenericRespose(isSuccess = true,
                                data = "Delete rows are affected $noOfRowsAffected")
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.OK,GenericRespose(isSuccess = false,
                                data = "Error Delete rows are affected $noOfRowsAffected")
                        )
                    }


                }catch (e:Exception){
                    println("Error al Conectar ${e.message}")
                }
            }
        }

    }

}