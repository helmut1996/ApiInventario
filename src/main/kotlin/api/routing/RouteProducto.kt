package com.helcode.api.routing

import api.model.Productos
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityProductos
import com.helcode.api.services.GenericRespose
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.io.File
import java.nio.file.Paths

fun Application.routeProducto(){
    val db: Database = DBConnection.getDatabaseInstance()

    routing {
        authenticate("auth-jwt") {
            get("/Productos")
            {
                try {
                    val listP = db.from(EntityProductos)
                        .select()
                        .map {
                            Productos(
                                id = it[EntityProductos.id],
                                idProducto = it[EntityProductos.idProducto],
                                numProducto = it[EntityProductos.numProducto],
                                nomSerie = it[EntityProductos.nomSerie],
                                descripcion = it[EntityProductos.descripcion],
                                stock = it[EntityProductos.stock],
                                precio = it[EntityProductos.precio],
                                url_imagen = it[EntityProductos.url_imagen],
                                idProveedor = it[EntityProductos.idProveedor],
                            )
                        }

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
                    val producto =  db.from(EntityProductos)
                        .select()
                        .where{EntityProductos.id eq productoId}
                        .map {
                            Productos(
                                id = it[EntityProductos.id],
                                idProducto = it[EntityProductos.idProducto],
                                numProducto = it[EntityProductos.numProducto],
                                nomSerie = it[EntityProductos.nomSerie],
                                stock = it[EntityProductos.stock],
                                precio = it[EntityProductos.precio],
                                descripcion = it[EntityProductos.descripcion],
                                idProveedor = it[EntityProductos.idProveedor],
                                url_imagen = it[EntityProductos.url_imagen]

                            )
                        }
                        .firstOrNull()

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

                val desktopPath = Paths.get(System.getProperty("user.home"), "Desktop", "imagenes_productos").toString()
                val directory = File(desktopPath)
                if (!directory.exists()) directory.mkdirs()

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
                            val fileName = "${System.currentTimeMillis()}_${part.originalFileName ?: "temp.jpg"}"
                            println("Imagen recibida: $fileName")
                            val file = File(directory, fileName)

                            try {
                                part.streamProvider().use { input ->
                                    file.outputStream().buffered().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                imageUrl = file.absolutePath
                                println("Imagen guardada en: $imageUrl")
                            } catch (e: Exception) {
                                println("Error al guardar imagen: ${e.message}")
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                if (producto != null && imageUrl != null) {
                    val nuevoProducto = producto!!.copy(url_imagen = imageUrl)

                    try {
                        db.insert(EntityProductos) {
                            set(it.descripcion, nuevoProducto.descripcion)
                            set(it.precio, nuevoProducto.precio)
                            set(it.nomSerie, nuevoProducto.nomSerie)
                            set(it.idProveedor, nuevoProducto.idProveedor)
                            set(it.numProducto, nuevoProducto.numProducto)
                            set(it.idProducto, nuevoProducto.idProducto)
                            set(it.stock, nuevoProducto.stock)
                            set(it.url_imagen, nuevoProducto.url_imagen)
                        }
                        call.respond(HttpStatusCode.Created, nuevoProducto)
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

                val desktopPath = Paths.get(System.getProperty("user.home"), "Desktop", "imagenes_productos").toString()
                val directory = File(desktopPath)
                if (!directory.exists()) directory.mkdirs()

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "descripcion" -> producto = (producto ?: Productos()).copy(descripcion = part.value)
                                "precio" -> producto = (producto ?: Productos()).copy(precio = part.value.toDoubleOrNull() ?: 0.0)
                                "nomSerie" -> producto = (producto ?: Productos()).copy(nomSerie = part.value)
                                "idProveedor" -> producto = (producto ?: Productos()).copy(idProveedor = part.value.toIntOrNull() ?: 0)
                                "numProducto" -> producto = (producto ?: Productos()).copy(numProducto = part.value)
                                "stock" -> producto = (producto ?: Productos()).copy(stock = part.value.toIntOrNull() ?: 0)
                            }
                        }

                        is PartData.FileItem -> {
                            val fileName = "${System.currentTimeMillis()}_${part.originalFileName ?: "temp.jpg"}"
                            val file = File(directory, fileName)

                            try {
                                part.streamProvider().use { input ->
                                    file.outputStream().buffered().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                imageUrl = file.absolutePath
                            } catch (e: Exception) {
                                println("Error al guardar imagen: ${e.message}")
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                if (producto != null) {
                    try {
                        val updateQuery = db.update(EntityProductos) {
                            where { it.id eq productoId }

                            producto?.let { p ->
                                set(EntityProductos.idProducto, p.idProducto)
                                set(EntityProductos.descripcion, p.descripcion)
                                set(EntityProductos.precio, p.precio)
                                set(EntityProductos.nomSerie, p.nomSerie)
                                set(EntityProductos.idProveedor, p.idProveedor)
                                set(EntityProductos.numProducto, p.numProducto)
                                set(EntityProductos.stock, p.stock)
                            }


                            if (imageUrl != null) {
                                set(it.url_imagen, imageUrl!!)
                            }
                        }

                        if (updateQuery == 0) {
                            call.respond(HttpStatusCode.NotFound, "Producto no encontrado")
                        } else {
                            call.respond(HttpStatusCode.OK, "Producto actualizado correctamente")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "Error en el servidor: ${e.localizedMessage}")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Datos insuficientes para la actualización")
                }
            }


            delete("/DeleteProducto/{id}") {
                try {
                    val productoIdsr = call.parameters["id"]
                    val productoId = productoIdsr?.toInt() ?: -1

                    val noOfRowsAffected = db.delete(EntityProductos)
                    {
                        it.id eq productoId
                    }

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