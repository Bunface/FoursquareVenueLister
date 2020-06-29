package hu.bitraptors.fazakas.homework

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bitraptors.fazakas.homework.recyclerview.VenueAdapter
import hu.bitraptors.fazakas.homework.data.VenueItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import android.widget.Toast
import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import hu.bitraptors.fazakas.homework.location.LocationProviderForFoursquare
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.lang.NullPointerException


class MainActivity :
    AppCompatActivity(),
    VenueAdapter.VenueItemClickListener,
    LocationProviderForFoursquare.LocationUpdate{

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VenueAdapter
    private lateinit var locationProviderForFoursquare: LocationProviderForFoursquare
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        locationProviderForFoursquare = LocationProviderForFoursquare (this, this )
        locationProviderForFoursquare.startLocationMonitoring()
    }

    override fun onStop(){
        locationProviderForFoursquare.stopLocationMonitoring()
        super.onStop()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView)
        adapter = VenueAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        var latitude: String = ""
        var longitude: String = ""

        try{
            latitude = location!!.latitude.toString()
            longitude = location!!.longitude.toString()
        }catch(e: NullPointerException){
            return //location null
        }

        val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)

        val venueSearchCall
                = fourSquare.requestVenueSearch("$latitude,$longitude")

        venueSearchCall.enqueue(object : Callback<VenueSearchResponse> {

            override fun onResponse(
                call: Call<VenueSearchResponse>,
                response: Response<VenueSearchResponse>
            ) {
                val venueItems = mutableListOf<VenueItem>()
                response.body()?.response?.venues?.forEach { venue ->
                    venueItems.add(VenueItem(venue))
                }
                adapter.update(venueItems)
            }


            override fun onFailure(call: Call<VenueSearchResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onItemClicked(item: VenueItem?) {
        val intent = Intent(this, VenueDetailActivity::class.java)
        intent.putExtra(VenueDetailActivity.KEY_VENUE_ITEM, item!!)
        startActivity(intent)
    }

    override fun onLocationUpdate(location: Location) {
        this.location = location
        initRecyclerView()
        locationProviderForFoursquare.stopLocationMonitoring()
    }
}
