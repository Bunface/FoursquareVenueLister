package hu.bitraptors.fazakas.homework.foursquare.model

data class LocationX(
    val address: String,
    val cc: String,
    val city: String,
    val country: String,
    val crossStreet: String,
    val formattedAddress: List<String>,
    val lat: Double,
    val lng: Double,
    val postalCode: String,
    val state: String
)