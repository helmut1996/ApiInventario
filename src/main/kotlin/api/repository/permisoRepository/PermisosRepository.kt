package com.helcode.api.repository.permisoRepository

import api.model.Permisos

interface PermisosRepository {
    suspend fun getPermisos() : List<Permisos>
}