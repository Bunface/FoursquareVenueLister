package hu.bitraptors.fazakas.homework.data

import hu.bitraptors.fazakas.homework.foursquare.FourSquare
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
                venue.hours.timeframes.forEach { timeFrame ->
                    openingHours += "${timeFrame.days}: "
                    timeFrame.open.forEach{ open ->
                        openingHours += "${open.renderedTime}, "
                    }
                    if (venue.hours.timeframes.indexOf(timeFrame) != venue.hours.timeframes.size-1) {
                        openingHours += "\n"
                    }
                }
                return openingHours
            }catch(e: Exception){
                " unavailable"
            }
        }

    val address: String
        get(){

            return try {
                var address= ""
                venue.location.formattedAddress.forEach {
                    address += "$it\n"
                }
                address += "lat: ${venue.location.lat}\nlong: ${venue.location.lng}"
                return address
            }catch(e: Exception){
                " unavailable"
            }
        }

    val photos: List<Photo>?
        get (){
            val photosTmp = mutableListOf<Photo>()
            return try {
                try{
                    venue.listed.groups.forEach { group ->
                        group.items.forEach {item ->
                            try{
                                photosTmp.add(
                                    Photo(
                                        item.photo.id,
                                        item.photo.height,
                                        item.photo.width,
                                        item.photo.prefix,
                                        item.photo.suffix
                                    )
                                )
                            }catch(e: Exception){
                                //there is no photo
                            }
                        }
                    }

                }catch(e: Exception){
                   //there is no listing
                }
                venue.photos.groups.forEach { group ->
                    group.items.forEach { photo ->
                        photosTmp.add(
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
                photosTmp
            }catch (e: Exception){
                null
            }
        }

    fun getMaxThreePhotos() : List<Photo>{
        val photosTmp = mutableListOf<Photo>()
        var numOfPhotos = photos?.size ?: 0
        println("photos: " + photos?.size)
        if(numOfPhotos > 3) numOfPhotos = 3
        for(i in 0 until numOfPhotos){
            val photo = photos?.get(i)
            if(photo != null) {
                photosTmp.add(photo)
            }
        }
        return photosTmp
    }

    val categories: List<Category>?
        get(){
            return try {
                val categoriesTmp = mutableListOf<Category>()
                venue.categories.forEach { category ->
                    categoriesTmp. add(
                        VenueDetailItem.Category(
                            CategoryIcon(category.icon.prefix, category.icon.suffix),
                            category.id,
                            category.name
                        )
                    )
                }
                categoriesTmp
            }catch(e: Exception){
                null
            }
        }

    fun getMaxFourCategoryIcons() : List<CategoryIcon>{
        val icons = mutableListOf<CategoryIcon>()
        var numOfIcons = categories?.size ?: 0
        if(numOfIcons > 4) numOfIcons = 4
        for(i in 0 until numOfIcons){
            val icon = categories?.get(i)?.icon
            if(icon != null) {
                icons.add(icon)
            }
        }
        return icons
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
    ){
        val url: String
        get(){
            return prefix + width + "x" + height +suffix
        }
    }

}

