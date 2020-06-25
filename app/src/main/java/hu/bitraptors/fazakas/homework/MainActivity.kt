package hu.bitraptors.fazakas.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bitraptors.fazakas.homework.recyclerview.VenueAdapter
import hu.bitraptors.fazakas.homework.recyclerview.VenueItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import android.widget.Toast
import hu.bitraptors.fazakas.homework.foursquare.response.VenueSearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
                val venueRecommendationsCall = fourSquare.requestRecommendations(
                    FourSquare.CLIENT_ID,
                    FourSquare.CLIENT_SECRET,
                    "40.74224,-73.99386"
                )
                venueRecommendationsCall.enqueue(object: Callback<VenueSearchResponse> {
                    override fun onResponse(call: Call<VenueSearchResponse>, response: Response<VenueSearchResponse>) {
                        val items = mutableListOf<String>()
                        println(response.code())
                        response.body()?.response?.venues?.forEach {
                            items.add(it.name)
                        }
                        val venueItems = mutableListOf<VenueItem>()

                        items.forEach{
                            venueItems.add(VenueItem(it))
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
