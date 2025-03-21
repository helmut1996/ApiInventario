package com.helcode.api.Database.Entity

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityProductos : Table<Nothing>(tableName = "productos") {
    val id = int(name = "id").primaryKey()
    val idProducto = varchar(name = "idProducto")
    val numProducto = varchar(name = "numProducto")
    val nomSerie = varchar(name = "nomSerie")
    val stock = int(name = "stock")
    val precio = double(name = "precio")
    val descripcion = varchar(name = "descripcion")
    val idProveedor = int(name = "idProveedor")
    val url_imagen = varchar(name = "url_imagen")
}