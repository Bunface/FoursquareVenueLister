package hu.bitraptors.fazakas.homework.fragments

import androidx.fragment.app.Fragment
import hu.bitraptors.fazakas.homework.data.VenueItem
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import hu.bitraptors.fazakas.homework.data.VenueDetailItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import hu.bitraptors.fazakas.homework.foursquare.model.VenueX
import hu.bitraptors.fazakas.homework.foursquare.response.VenueDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt
import okhttp3.ResponseBody
import android.graphics.BitmapFactory
import android.widget.*
import hu.bitraptors.fazakas.homework.R
import hu.bitraptors.fazakas.homework.data.CategoryIcon


class VenueDetailFragment : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var verificationImageView: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var hoursTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var detailsLinearLayout: LinearLayout

    private val categoryIcons: MutableList<ImageView> = mutableListOf()
    private var photos: MutableList<ImageView> = mutableListOf()

    companion object{
        lateinit var selectedVenueItem : VenueItem
        private var selectedVenueDetailItem: VenueDetailItem? = null

        fun newInstance(item: VenueItem) : VenueDetailFragment{
            selectedVenueItem = item
            return VenueDetailFragment()
        }
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

        initLayout()
        getVenueDetailItem()
    }

    private fun initLayout(){

        nameTextView = view!!.findViewById(R.id.VenueDetailNameTextView)
        verificationImageView = view!!.findViewById(R.id.VenueDetailVerificationImageView)
        ratingBar = view!!.findViewById(R.id.VenueDetailRatingBar)
        hoursTextView = view!!.findViewById(R.id.VenueDetailHoursTextView)
        locationTextView = view!!.findViewById(R.id.VenueDetailLocationTextView)
        descriptionTextView = view!!.findViewById(R.id.VenueDetailDescriptionTextView)
        urlTextView = view!!.findViewById(R.id.VenueDetailURLTextView)
        phoneTextView = view!!.findViewById(R.id.VenueDetailPhoneTextView)

        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon1))
        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon2))
        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon3))
        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon4))

        photos.add(view!!.findViewById(R.id.VenueDetailPhoto1))
        photos.add(view!!.findViewById(R.id.VenueDetailPhoto2))
        photos.add(view!!.findViewById(R.id.VenueDetailPhoto3))
        photos.add(view!!.findViewById(R.id.VenueDetailPhoto4))

        detailsLinearLayout = view!!.findViewById(R.id.VenueDetailDetailsLinearLayout)
    }

    private fun getVenueDetailItem(){
        val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)

        val venueDetailsCall = fourSquare.requestVenueDetails(
            selectedVenueItem.id
        )

        venueDetailsCall.enqueue(object: Callback<VenueDetailsResponse> {

            override fun onResponse(call: Call<VenueDetailsResponse>, response: Response<VenueDetailsResponse>) {
                val venueX = response.body()?.response?.venue
                fillLayout(venueX)
                getIcons()
                getPhotos()
            }

            override fun onFailure(call: Call<VenueDetailsResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun fillLayout(venueX : VenueX?){
        if (venueX != null){
            selectedVenueDetailItem = VenueDetailItem(venueX)

            nameTextView.text = selectedVenueDetailItem?.name
            if (selectedVenueDetailItem?.verified == true) verificationImageView.visibility = ImageView.VISIBLE
            ratingBar.rating = selectedVenueDetailItem?.rating?.toFloat()?.div(2.0f)  ?: 0.0f

            hoursTextView.text = selectedVenueDetailItem?.hours
            locationTextView.text = selectedVenueDetailItem?.address
            descriptionTextView.text = selectedVenueDetailItem?.description
            urlTextView.text = selectedVenueDetailItem?.url
            phoneTextView.text = selectedVenueDetailItem?.formattedPhone
        }else{
            nameTextView.text = getString(R.string.cant_download_details)
            detailsLinearLayout.visibility = LinearLayout.INVISIBLE
        }

    }

    private fun getIcons(){
        var i= 0
        selectedVenueDetailItem?.getCategoryIcons()?.forEach { icon ->
            fillIcon(categoryIcons[i], icon)
            i++
        }
    }
    private fun fillIcon(imageView: ImageView, icon: CategoryIcon){
        val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)
        val photoCall = fourSquare.downloadPhoto(icon.url)

        photoCall.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val byteStream = response.body()?.byteStream()

                if (byteStream != null){
                    val bmp = BitmapFactory.decodeStream(byteStream)
                    imageView.setImageBitmap(bmp)
                    imageView.visibility = ImageView.VISIBLE
                }else{
                    Toast.makeText(context, getString(R.string.could_not_decode_icon), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getPhotos(){
        var i= 0
        selectedVenueDetailItem?.getVenuePhotos()?.forEach { photo ->
            fillPhoto(photos[i], photo)
            i++
        }
    }
    private fun fillPhoto(imageView: ImageView, photo: VenueDetailItem.Photo){
        val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)
        val photoCall = fourSquare.downloadPhoto(photo.url)

        photoCall.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val byteStream = response.body()?.byteStream()
                if (byteStream != null){
                    val bmp = BitmapFactory.decodeStream(byteStream)
                    imageView.setImageBitmap(bmp)
                    imageView.visibility = ImageView.VISIBLE
                }else{
                    Toast.makeText(context, getString(R.string.could_not_decode_ohoto), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


}