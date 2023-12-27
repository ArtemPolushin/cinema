package domain

import kotlinx.serialization.Serializable
@Serializable
data class FilmEntity(
    val name : String,
    val information : String
)
