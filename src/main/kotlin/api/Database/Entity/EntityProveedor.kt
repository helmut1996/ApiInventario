package com.helcode.api.Database.Entity

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityProveedor : Table<Nothing>(tableName = "proveedores") {
    val idProveedor = int(name = "idProveedor").primaryKey()
    val nomProveedor = varchar(name = "nomProveedor")  // Coincide con el JSON
    val numContacto = double(name = "numContacto")
    val direccion = varchar(name = "direccion") // Corregido para coincidir con el JSON
    val email = varchar(name = "email")
}