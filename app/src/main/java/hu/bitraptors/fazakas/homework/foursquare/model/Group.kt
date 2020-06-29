package hu.bitraptors.fazakas.homework.foursquare.model

data class Group(
    val count: Int,
    val items: List<Item>,
    val name: String,
    val summary: String,
    val type: String
)