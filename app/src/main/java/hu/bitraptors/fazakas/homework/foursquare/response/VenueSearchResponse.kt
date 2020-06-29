package hu.bitraptors.fazakas.homework.foursquare.response

import hu.bitraptors.fazakas.homework.foursquare.model.Meta
import hu.bitraptors.fazakas.homework.foursquare.model.Response

data class VenueSearchResponse(
    val meta: Meta,
    val response: Response
)