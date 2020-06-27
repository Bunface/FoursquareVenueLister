package hu.bitraptors.fazakas.homework.foursquare.model

data class Hours(
    val isLocalHoliday: Boolean,
    val isOpen: Boolean,
    val status: String,
    val timeframes: List<Timeframe>
)