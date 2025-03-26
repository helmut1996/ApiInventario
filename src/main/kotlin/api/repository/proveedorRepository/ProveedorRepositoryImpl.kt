package com.helcode.api.repository.proveedorRepository

import api.model.Proveedor
import com.helcode.api.Database.Entity.EntityProveedor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.*

class ProveedorRepositoryImpl(private val db:Database):ProveedorRepository {
    override suspend fun insertProveedor(proveedor: Proveedor):Int = withContext(Dispatchers.IO) {
        db.insert( EntityProveedor)
        {
            set(it.nomProveedor, proveedor.nomProveedor)
            set(it.numContacto, proveedor.numContacto)
            set(it.direccion, proveedor.direccion)
            set(it.email, proveedor.email)


        }
    }

    override suspend fun getProveedor(): List<Proveedor> = withContext(Dispatchers.IO){
        db.from(EntityProveedor)
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
    }

    override suspend fun getProveedorById(idProveedor: Int): Proveedor?  = withContext(Dispatchers.IO){
        db.from(EntityProveedor)
            .select()
            .where{
                EntityProveedor.idProveedor eq idProveedor
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
    }

    override suspend fun updateProveedor(proveedorId: Int, proveedor: Proveedor): Int = withContext(Dispatchers.IO) {
        db.update( EntityProveedor)
        {
            set(it.nomProveedor, proveedor.nomProveedor)
            set(it.numContacto, proveedor.numContacto)
            set(it.direccion, proveedor.direccion)
            set(it.email, proveedor.email)

            where{
                it.idProveedor eq proveedorId
            }

        }
    }

    override suspend fun deleteProveedor(idProveedor: Int): Int = withContext(Dispatchers.IO) {
        db.delete( EntityProveedor)
        {
            it.idProveedor eq  idProveedor

        }
    }
}