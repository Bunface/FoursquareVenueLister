package hu.bitraptors.fazakas.homework

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bitraptors.fazakas.homework.recyclerview.VenueAdapter
import hu.bitraptors.fazakas.homework.recyclerview.VenueItem

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
        object : AsyncTask<Void, Void, List<VenueItem>>() {

            override fun doInBackground(vararg voids: Void): List<VenueItem> {
                val item1 = VenueItem()
                item1.name = "Venue1"
                val item2 = VenueItem()
                item2.name = "Venue2"
                val item3 = VenueItem()
                item3.name = "Venue3"

                val items = mutableListOf<VenueItem>()
                items.add(item1)
                items.add(item2)
                items.add(item3)

                return items
            }

            override fun onPostExecute(pageItems: List<VenueItem>) {
                adapter!!.update(pageItems)
            }
        }.execute()
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val toolbarMenu = toolbar.menu
        menuInflater.inflate(R.menu.menu_main, toolbarMenu)

        for (i in 0 until toolbarMenu.size()) {
            val menuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item ->
                onOptionsItemSelected(
                    item
                )
            })
            if (menuItem.hasSubMenu()) {
                val subMenu = menuItem.getSubMenu()
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item ->
                            onOptionsItemSelected(item)
                        })
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    } */

    override fun onItemClicked(item: VenueItem?) {
        //val intent = Intent(this, RecipeDetailActivity::class.java)
        //intent.putExtra(RecipeDetailActivity.KEY_RECIPE_ITEM, item)
        //startActivity(intent)
        Toast.makeText(this, item?.name, Toast.LENGTH_LONG).show()

    }

}
