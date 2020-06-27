package hu.bitraptors.fazakas.homework.foursquare.response

import hu.bitraptors.fazakas.homework.foursquare.model.MetaX
import hu.bitraptors.fazakas.homework.foursquare.model.ResponseX

data class VenueDetailsResponse(
    val meta: MetaX,
    val response: ResponseX
)