package hu.bitraptors.fazakas.homework.foursquare

import hu.bitraptors.fazakas.homework.foursquare.response.VenueDetailsResponse
import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FourSquare {
    companion object {
        const val CLIENT_ID = "OBQME4NGFJE1032H42NYXAFHYN14T1W4OYDRG4XBED2GVA0Z"
        const val CLIENT_SECRET = "OPTHI5MSQTOMU1F5MR3H2VBOCVXCPH3G321Y5KJTMC2YBZAO"
        const val VERSION = "20200624"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.foursquare.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @GET("/v2/venues/search")
    fun requestVenueSearch(
        @Query("ll") ll: String,
        @Query("client_id") client_id: String = CLIENT_ID,
        @Query("client_secret") client_secret: String = CLIENT_SECRET,
        @Query("v") version: String = VERSION
    ): Call<VenueSearchResponse>

    @GET("/v2/venues/{VENUE_ID}")
    fun requestVenueDetails(
        @Path("VENUE_ID") venueID: String,
        @Query("client_id") client_id: String = CLIENT_ID,
        @Query("client_secret") client_secret: String = CLIENT_SECRET,
        @Query("v") version: String = VERSION
    ): Call<VenueDetailsResponse>


}