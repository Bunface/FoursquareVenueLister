package hu.bitraptors.fazakas.homework.foursquare

import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface FourSquare {
    companion object {
        const val CLIENT_ID = "OBQME4NGFJE1032H42NYXAFHYN14T1W4OYDRG4XBED2GVA0Z"
        const val CLIENT_SECRET = "OPTHI5MSQTOMU1F5MR3H2VBOCVXCPH3G321Y5KJTMC2YBZAO"

        var latitude = "40.7463956"
        var longitude = "-73.9852992"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.foursquare.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @GET("/v2/venues/search")
    fun requestRecommendations(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("ll") ll: String,
        @Query("v") version: String = "20200624"
    ): Call<VenueSearchResponse>


}

//var testModel = gson.fromJson(jsonString, TestModel::class.java)