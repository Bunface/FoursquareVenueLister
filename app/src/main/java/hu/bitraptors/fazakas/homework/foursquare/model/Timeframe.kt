package hu.bitraptors.fazakas.homework.foursquare.model

data class Timeframe(
    val days: String,
    val includesToday: Boolean,
    val `open`: List<Open>,
    val segments: List<Any>
)