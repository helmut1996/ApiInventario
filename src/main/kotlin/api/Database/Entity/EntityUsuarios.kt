package com.helcode.api.Database.Entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityUsuarios : Table<Nothing>(tableName = "usuarios")
{
    val idUsuario = int(name = "idUsuario").primaryKey()
    val nomUsuario = varchar(name = "nomUsuario")
    val usuario = varchar(name = "usuario")
    val contrasena = varchar(name = "contrasena")
    val idPermiso = int(name = "idPermiso")
    val idEstado = int(name = "idEstado")
}