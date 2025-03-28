package com.helcode.api.repository.productosRepository

import api.model.Productos
import com.helcode.api.Database.Entity.EntityProductos
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.io.File
import java.nio.file.Paths

class ProductosRepositoryImpl(private  val db: Database):ProductosRepository {
    override suspend fun insertProducto(producto: Productos):Productos = withContext(Dispatchers.IO) {
        val id = db.insertAndGenerateKey(EntityProductos) {
            set(it.descripcion, producto.descripcion)
            set(it.precio, producto.precio)
            set(it.nomSerie, producto.nomSerie)
            set(it.idProveedor, producto.idProveedor)
            set(it.numProducto, producto.numProducto)
            set(it.idProducto, producto.idProducto)
            set(it.stock, producto.stock)
            set(it.url_imagen, producto.url_imagen)
        } as? Int

        (if (id != null) {
            producto.copy(id = id)
        } else {
            null
        })!!
    }

    override suspend fun getProducto(): List<Productos> = withContext(Dispatchers.IO) {
        db.from(EntityProductos)
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
    }

    override suspend fun getProductoById(idProducto: Int): Productos? = withContext(Dispatchers.IO){
        db.from(EntityProductos)
            .select()
            .where{EntityProductos.id eq idProducto}
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
    }

    override suspend fun updateProducto(productoId: Int, producto: Productos): Int = withContext(Dispatchers.IO) {
        db.update(EntityProductos) {
            set(it.idProducto, producto.idProducto)
            set(it.descripcion, producto.descripcion)
            set(it.precio, producto.precio)
            set(it.nomSerie, producto.nomSerie)
            set(it.idProveedor, producto.idProveedor)
            set(it.numProducto, producto.numProducto)
            set(it.stock, producto.stock)
            // We don't update the image here, it's handled separately
            where { it.id eq productoId }
        }
    }

    override suspend fun deleteProducto(idProducto: Int): Int = withContext(Dispatchers.IO) {
        db.delete(EntityProductos)
        {
            it.id eq idProducto
        }

    }

    override suspend fun saveImage(part: PartData.FileItem): String? = withContext(Dispatchers.IO) {
        val desktopPath = Paths.get(System.getProperty("user.home"), "Desktop", "imagenes_productos").toString()
        val directory = File(desktopPath)
        if (!directory.exists()) directory.mkdirs()

        val fileName = "${System.currentTimeMillis()}_${part.originalFileName ?: "temp.jpg"}"
        val file = File(directory, fileName)

        try {
            part.streamProvider().use { input ->
                file.outputStream().buffered().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            println("Error al guardar imagen: ${e.message}")
            null
        }
    }
}