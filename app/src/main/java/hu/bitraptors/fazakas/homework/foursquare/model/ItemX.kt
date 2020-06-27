package hu.bitraptors.fazakas.homework.foursquare.model

data class ItemX(
    val canonicalUrl: String,
    val collaborative: Boolean,
    val createdAt: Int,
    val description: String,
    val editable: Boolean,
    val followers: Followers,
    val id: String,
    val listItems: ListItems,
    val name: String,
    val photo: PhotoX,
    val `public`: Boolean,
    val type: String,
    val updatedAt: Int,
    val url: String,
    val user: UserX
)