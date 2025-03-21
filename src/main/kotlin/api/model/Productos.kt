package api.model

import kotlinx.serialization.Serializable

@Serializable
data class Productos(

	val id: Int? = null,

	val descripcion: String? = null,

	val precio: Double? = null,

	val nomSerie: String? = null,

	val idProveedor: Int? = null,

	val numProducto: String? = null,

	val idProducto: String? = null,

	val stock: Int? = null,

	val url_imagen: String? = null
)
