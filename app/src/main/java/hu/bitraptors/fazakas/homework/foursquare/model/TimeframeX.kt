package hu.bitraptors.fazakas.homework.foursquare.model

data class TimeframeX(
    val days: String,
    val `open`: List<OpenX>,
    val segments: List<Any>
)