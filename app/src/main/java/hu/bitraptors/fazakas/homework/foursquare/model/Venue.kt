package hu.bitraptors.fazakas.homework.foursquare.model

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String,
    val venuePage: VenuePage
)