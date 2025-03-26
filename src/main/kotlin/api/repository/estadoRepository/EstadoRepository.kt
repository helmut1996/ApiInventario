package com.helcode.api.repository.estadoRepository

import api.model.Estado

interface EstadoRepository {
    suspend fun  getEstado() : List<Estado>
}