package com.helcode.api.repository.productosRepository

import api.model.Productos
import io.ktor.http.content.*


interface ProductosRepository {
    suspend fun insertProducto(proveedor: Productos): Productos?

    suspend fun getProducto(): List<Productos>

    suspend fun getProductoById(idProducto: Int): Productos?

    suspend fun updateProducto(productoId: Int,proveedor: Productos):Int

    suspend fun deleteProducto(idProducto: Int):Int

    suspend fun saveImage(part: PartData.FileItem): String? // New function


}
