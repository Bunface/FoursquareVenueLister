package hu.bitraptors.fazakas.homework.foursquare

import hu.bitraptors.fazakas.homework.foursquare.response.VenueDetailsResponse
import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import okhttp3.ResponseBody

interface FourSquare {
    companion object {
        const val CLIENT_ID = "ZXCSBPOO4XVGV2EQFNLGTBJX3GXY0OQGTVYM11NBTYC4ELV0"
        const val CLIENT_SECRET = "C5LVYM3OFJY2ITT3LL5V5QPNY1DGS1T4JZQB0ERYHQXWAJRW"
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

    @GET
    fun downloadPhoto(
        @Url url: String,
        @Query("client_id") client_id: String = CLIENT_ID,
        @Query("client_secret") client_secret: String = CLIENT_SECRET,
        @Query("v") version: String = VERSION
    ): Call<ResponseBody>

}