package com.helcode.api.Database.Entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityPermisos : Table<Nothing>(tableName = "permisos") {
    val idPermiso = int(name = "idPermiso").primaryKey()
    val permiso = varchar(name = "permiso")
}