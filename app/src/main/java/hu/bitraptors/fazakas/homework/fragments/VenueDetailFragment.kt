package hu.bitraptors.fazakas.homework.fragments

import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import hu.bitraptors.fazakas.homework.recyclerview.VenueItem
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.R.attr.description
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import hu.bitraptors.fazakas.homework.R


class VenueDetailFragment : Fragment() {

    companion object{
        lateinit var selectedItem : VenueItem
        fun newInstance(item: VenueItem) : VenueDetailFragment{
            selectedItem = item
            return VenueDetailFragment()
        }
    }

    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var photoImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstances: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_venue_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        nameTextView = getView()!!.findViewById(R.id.VenueNameTextView)
        nameTextView.text = selectedItem.name

        descriptionTextView = getView()!!.findViewById(R.id.VenueDetailTextView)
        descriptionTextView.text = selectedItem.description

        photoImageView = getView()!!.findViewById(R.id.VenueDetailImageView)
        val bitmap = BitmapFactory.decodeFile(selectedItem.photo)
        photoImageView.setImageBitmap(bitmap)
    }


}