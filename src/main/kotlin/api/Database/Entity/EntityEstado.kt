package com.helcode.api.Database.Entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityEstado : Table<Nothing>(tableName = "estados"){
val idEstado = int(name = "idEstado").primaryKey()
val estado = int(name = "estado")
val descripcion = varchar(name = "descripcion")


}