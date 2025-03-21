package api.model

import kotlinx.serialization.Serializable

@Serializable
data class Estado(


	val descripcion: String? = null,
	val idEstado: Int? = null,
	val estado: Int? = null
)
