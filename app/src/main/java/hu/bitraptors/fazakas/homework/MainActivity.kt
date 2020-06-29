package hu.bitraptors.fazakas.homework

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import hu.bitraptors.fazakas.homework.data.VenueItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import hu.bitraptors.fazakas.homework.location.LocationProviderForFoursquare
import hu.bitraptors.fazakas.homework.recyclerview.VenueAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity :
    AppCompatActivity(),
    VenueAdapter.VenueItemClickListener,
    LocationProviderForFoursquare.LocationUpdate{

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VenueAdapter
    private lateinit var locationProviderForFoursquare: LocationProviderForFoursquare
    private var location: Location? = null

    companion object{
        private const val MY_LOCATION_REQUEST_CODE = 1234
    }

    //ON CREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupRefreshLayout()
        initRecyclerView()

        setupLocation()
    }

    //LAYOUT
    private fun setupRefreshLayout(){
        val venueListSwipeRefreshLayout : SwipeRefreshLayout= findViewById(R.id.VenueListSwipeRefreshLayout)
        venueListSwipeRefreshLayout.setOnRefreshListener {
            refreshLocation()
            venueListSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView)
        adapter = VenueAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(item: VenueItem?) {
        val intent = Intent(this, VenueDetailActivity::class.java)
        intent.putExtra(VenueDetailActivity.KEY_VENUE_ITEM, item!!)
        startActivity(intent)
    }



    //LOCATION
    private fun setupLocation(){
        locationProviderForFoursquare = LocationProviderForFoursquare (this, this)
        refreshLocation()
    }

    private fun refreshLocation(){
        if(!locationPermissionOn()) requestLocationPermission()
        else locationProviderForFoursquare.refreshLocation()
    }

    private fun stopRefreshingLocation(){
        locationProviderForFoursquare.stopRefreshingLocation()
    }

    override fun onLocationUpdate(location: Location) {
        this.location = location
        loadItemsInBackground()
        stopRefreshingLocation()
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



    //LOCATION PERMISSION
    private fun locationPermissionOn() : Boolean{
        return (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission(){
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                MY_LOCATION_REQUEST_CODE
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_LOCATION_REQUEST_CODE
            && grantResults.all { result -> result == PackageManager.PERMISSION_GRANTED}){
                locationProviderForFoursquare.refreshLocation()
        }
    }


    //ON STOP
    override fun onStop(){
        stopRefreshingLocation()
        super.onStop()
    }

}
