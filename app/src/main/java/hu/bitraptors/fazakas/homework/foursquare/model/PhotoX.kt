package hu.bitraptors.fazakas.homework.foursquare.model

data class PhotoX(
    val createdAt: Int,
    val height: Int,
    val id: String,
    val prefix: String,
    val suffix: String,
    val user: User,
    val visibility: String,
    val width: Int
)