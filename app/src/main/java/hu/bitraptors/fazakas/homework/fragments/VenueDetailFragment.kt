package hu.bitraptors.fazakas.homework.fragments

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import hu.bitraptors.fazakas.homework.data.VenueItem
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import hu.bitraptors.fazakas.homework.R
import hu.bitraptors.fazakas.homework.data.VenueDetailItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import hu.bitraptors.fazakas.homework.foursquare.response.VenueDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VenueDetailFragment : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var verificationImageView: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var hoursTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var categoriesTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var phoneTextView: TextView

    companion object{
        lateinit var selectedVenueItem : VenueItem
        private var selectedVenueDetailItem: VenueDetailItem? = null

        fun newInstance(item: VenueItem) : VenueDetailFragment{
            selectedVenueItem = item
            return VenueDetailFragment()
        }

        const val ICON_HEIGHT = 20
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstances: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_venue_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)


        val density = resources.displayMetrics.density;
        val size = Math.round(ICON_HEIGHT * density)

        nameTextView = getView()!!.findViewById(R.id.VenueDetailNameTextView)
        verificationImageView = getView()!!.findViewById(R.id.VenueDetailVerificationImageView)
        ratingBar = getView()!!.findViewById(R.id.VenueDetailRatingBar)
        hoursTextView = getView()!!.findViewById(R.id.VenueDetailHoursTextView)
            val clockDrawable =  resources.getDrawable(R.drawable.time)
            clockDrawable.setBounds(0, 0, size, size)
            hoursTextView.setCompoundDrawables(clockDrawable, null, null, null)
        locationTextView = getView()!!.findViewById(R.id.VenueDetailLocationTextView)
            val locationDrawable =  resources.getDrawable(R.drawable.location)
            locationDrawable.setBounds(0, 0, size, size)
            locationTextView.setCompoundDrawables(locationDrawable, null, null, null)
        categoriesTextView = getView()!!.findViewById(R.id.VenueDetailCategoriesTextView)
        descriptionTextView = getView()!!.findViewById(R.id.VenueDetailDescriptionTextView)
            val moreDrawable =  resources.getDrawable(R.drawable.more)
            moreDrawable.setBounds(0, 0, size, size)
            descriptionTextView.setCompoundDrawables(moreDrawable, null, null, null)
        urlTextView = getView()!!.findViewById(R.id.VenueDetailURLTextView)
            val webDrawable =  resources.getDrawable(R.drawable.earthgrid)
            webDrawable.setBounds(0, 0, size, size)
            urlTextView.setCompoundDrawables(webDrawable, null, null, null)
        phoneTextView = getView()!!.findViewById(R.id.VenueDetailPhoneTextView)
            val phoneDrawable =  resources.getDrawable(R.drawable.phone)
            phoneDrawable.setBounds(0, 0, size, size)
            phoneTextView.setCompoundDrawables(phoneDrawable, null, null, null)


        getVenueDetailItem()
        /* nameTextView.text = "CBA Budapest"
        verificationImageView.visibility = ImageView.VISIBLE
        ratingBar.rating = 3.2f

        hoursTextView.text = "H-Sz : 8-18"
        locationTextView.text = "MyStreet 20, Budapest, Hungary"
        categoriesTextView.text = "ToBeImplemented"
        descriptionTextView.text = "Good food, good life"
        urlTextView.text = "www.cba.hu"
        phoneTextView.text = "+367012345678" */

    }

    fun getVenueDetailItem(){
        val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)

        val venueDetailsCall = fourSquare.requestVenueDetails(
            selectedVenueItem.id
        )
        venueDetailsCall.enqueue(object: Callback<VenueDetailsResponse> {

            override fun onResponse(call: Call<VenueDetailsResponse>, response: Response<VenueDetailsResponse>) {
                val venueX = response.body()?.response?.venue
                if (venueX != null){
                    selectedVenueDetailItem = VenueDetailItem(venueX)

                    nameTextView.text = selectedVenueDetailItem?.name
                    if (selectedVenueDetailItem?.verified == true) verificationImageView.visibility = ImageView.VISIBLE
                    ratingBar.rating = selectedVenueDetailItem?.rating?.toFloat()?.div(2.0f)  ?: 0.0f

                    hoursTextView.text = selectedVenueDetailItem?.hours
                    locationTextView.text = selectedVenueDetailItem?.address
                    categoriesTextView.text = "ToBeImplemented"
                    descriptionTextView.text = selectedVenueDetailItem?.description
                    urlTextView.text = selectedVenueDetailItem?.url
                    phoneTextView.text = selectedVenueDetailItem?.formattedPhone
                }else{
                    nameTextView.text = getString(R.string.cant_download_details)
                }

            }

            override fun onFailure(call: Call<VenueDetailsResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


}