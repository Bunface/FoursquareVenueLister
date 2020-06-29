package hu.bitraptors.fazakas.homework.foursquare.model

data class Photo(
    val createdAt: Int,
    val height: Int,
    val id: String,
    val prefix: String,
    val suffix: String,
    val visibility: String,
    val width: Int
)