package hu.bitraptors.fazakas.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bitraptors.fazakas.homework.recyclerview.VenueAdapter
import hu.bitraptors.fazakas.homework.data.VenueItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import android.widget.Toast
import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class MainActivity : AppCompatActivity(), VenueAdapter.VenueItemClickListener {

    private var recyclerView: RecyclerView? = null
    private var adapter: VenueAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView)
        adapter = VenueAdapter(this)
        loadItemsInBackground()
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = adapter
    }

    private fun loadItemsInBackground() {

        val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)

        val venueRecommendationsCall = fourSquare.requestVenueSearch(
            "40.779895,-73.959572"
        )
        venueRecommendationsCall.enqueue(object : Callback<VenueSearchResponse> {

            override fun onResponse(
                call: Call<VenueSearchResponse>,
                response: Response<VenueSearchResponse>
            ) {

                val venueItems = mutableListOf<VenueItem>()
                response.body()?.response?.venues?.forEach { venue ->
                    venueItems.add(VenueItem(venue))
                }
                adapter!!.update(venueItems)
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


}
