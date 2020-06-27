package hu.bitraptors.fazakas.homework.data

import hu.bitraptors.fazakas.homework.R
import hu.bitraptors.fazakas.homework.foursquare.model.VenueX
import java.lang.Exception
import java.lang.NullPointerException


class VenueDetailItem ( private val venue : VenueX ){
    val id: String
        get(){
            return venue.id
        }

    val name: String
        get(){
            return venue.name
        }

    val rating: Double
        get(){
            return try {
                venue.rating
            }catch(e: Exception){
                0.0
            }
        }

    val verified: Boolean
        get(){
            return try {
                venue.verified
            }catch(e: Exception){
                false
            }
        }

    val hours: String
        get(){
            return try {
                val status = venue.hours.status
                var openingHours = "$status\n"
                /*venue.hours.timeframes.forEach { timeFrame ->
                    openingHours += "${timeFrame.days}: "
                    timeFrame.open.forEach{ open ->
                        openingHours += "$open, "
                    }
                    openingHours += "\n"
                }*/
                return openingHours
            }catch(e: Exception){
                " unavailable"
            }
        }

    val address: String
        get(){

            return try {
                var address= ""
                venue.location.formattedAddress?.forEach {
                    address += "$it\n"
                }
                address += "lat: ${venue.location.lat}\nlong: ${venue.location.lng}"
                return address
            }catch(e: Exception){
                " unavailable"
            }
        }

    val photos: MutableList<Photo> = mutableListOf<Photo>()
        init{
            try {
                venue.photos.groups.forEach { group ->
                    group.items.forEach { photo ->
                        this.photos.add(
                            Photo(
                                photo.id,
                                photo.height,
                                photo.width,
                                photo.prefix,
                                photo.suffix
                            )
                        )
                    }
                }
            }catch (e: Exception){
                photos.clear()
            }
        }

    var categories: MutableList<Category> = mutableListOf<Category>()
        init{
            try {
                venue.categories.forEach { category ->
                    VenueDetailItem.Category(
                        CategoryIcon(category.icon.prefix, category.icon.suffix),
                        category.id,
                        category.name
                    )
                }
            }catch(e: Exception){
                categories.clear()
            }
        }

    val description: String
        get(){
            return try {
                if (venue.description.contentEquals("null")) throw NullPointerException()
                venue.description
            } catch (e: Exception) {
                " unavailable"
            }
        }

    val url: String
        get(){
            return try {
                if (venue.url.contentEquals("null")) throw NullPointerException()
                " " + venue.url
            } catch (e: Exception) {
                " unavailable"
            }
        }

    val formattedPhone: String
        get(){
            return try {
                if (venue.contact.phone.contentEquals("null")) throw NullPointerException()
                " " + venue.contact.formattedPhone
            } catch (e: Exception) {
                " unavailable"
            }
        }


    data class Category(
        val icon: CategoryIcon,
        val id: String,
        val name: String
    )
    data class Photo(
        val id: String,
        val height: Int,
        val width: Int,
        val prefix: String,
        val suffix: String
    )

}

