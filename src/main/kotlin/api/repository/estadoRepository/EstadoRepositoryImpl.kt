package com.helcode.api.repository.estadoRepository

import api.model.Estado
import com.helcode.api.Database.Entity.EntityEstado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

class EstadoRepositoryImpl(private val db:Database) : EstadoRepository {
    override suspend fun getEstado(): List<Estado>  = withContext(Dispatchers.IO){
         db.from(EntityEstado)
            .select()
            .map {
                Estado(
                    idEstado = it[EntityEstado.idEstado],
                    estado =  it[EntityEstado.estado],
                    descripcion = it[EntityEstado.descripcion],
                )
            }
    }
}