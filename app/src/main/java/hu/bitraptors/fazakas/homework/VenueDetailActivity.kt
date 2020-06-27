package hu.bitraptors.fazakas.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bitraptors.fazakas.homework.fragments.VenueDetailFragment
import hu.bitraptors.fazakas.homework.data.VenueItem


class VenueDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_VENUE_ITEM = "VenueItem"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)

        val item = intent.getSerializableExtra(KEY_VENUE_ITEM) as VenueItem
        val pageDetailFragment = VenueDetailFragment.newInstance(item)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.venue_detail_scroll_view, pageDetailFragment)
        fragmentTransaction.commit()


    }
}