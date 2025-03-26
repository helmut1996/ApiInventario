package com.helcode.api.repository.proveedorRepository

import api.model.Proveedor

interface ProveedorRepository {
    suspend fun insertProveedor(proveedor: Proveedor): Int

    suspend fun getProveedor(): List<Proveedor>

    suspend fun getProveedorById(idProveedor: Int): Proveedor?

    suspend fun updateProveedor(proveedorId: Int,proveedor: Proveedor):Int

    suspend fun deleteProveedor(idProveedor: Int):Int

}