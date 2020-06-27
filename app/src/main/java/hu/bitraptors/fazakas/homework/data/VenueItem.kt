package hu.bitraptors.fazakas.homework.data

import hu.bitraptors.fazakas.homework.foursquare.model.Venue
import java.io.Serializable
import java.lang.Exception

class VenueItem (venue: Venue) :Serializable{

    val id: String = venue.id

    val name: String = venue.name

    val categoryIcon: CategoryIcon? = try {
         CategoryIcon(
             venue.categories[0].icon.prefix,
             venue.categories[0].icon.suffix
         )
     }catch(e: Exception){
         null
     }
}

data class CategoryIcon(
    val prefix: String,
    val suffix: String
) : Serializable
