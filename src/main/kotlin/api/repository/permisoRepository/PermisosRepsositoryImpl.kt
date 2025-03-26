package com.helcode.api.repository.permisoRepository

import api.model.Permisos
import com.helcode.api.Database.Entity.EntityPermisos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

class PermisosRepsositoryImpl(private val db: Database): PermisosRepository {
    override suspend fun getPermisos(): List<Permisos> = withContext(Dispatchers.IO){
        db.from(EntityPermisos)
            .select()
            .map {
                Permisos(
                    idPermiso = it[EntityPermisos.idPermiso],
                    permiso =  it[EntityPermisos.permiso],
                )
            }
    }
}