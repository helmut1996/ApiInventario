package api.model
import kotlinx.serialization.Serializable

@Serializable
data class Proveedor(
	val idProveedor: Int? = null,
	val numContacto: Double? = null,
	val nomProveedor: String? = null,
	val direccion: String? = null,
	val email: String? = null
)

