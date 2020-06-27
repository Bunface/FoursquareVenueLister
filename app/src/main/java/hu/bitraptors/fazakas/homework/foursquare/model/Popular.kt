package hu.bitraptors.fazakas.homework.foursquare.model

data class Popular(
    val isLocalHoliday: Boolean,
    val isOpen: Boolean,
    val status: String,
    val timeframes: List<TimeframeX>
)