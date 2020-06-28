package hu.bitraptors.fazakas.homework.fragments

import android.graphics.Bitmap
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
        const val ICON_HEIGHT = 40

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
        /*nameTextView.text = "CBA Budapest"
        verificationImageView.visibility = ImageView.VISIBLE
        ratingBar.rating = 3.2f

        hoursTextView.text = "H-Sz : 8-18"
        locationTextView.text = "MyStreet 20, Budapest, Hungary"
        descriptionTextView.text = "Good food, good life"
        urlTextView.text = "www.cba.hu"
        phoneTextView.text = "+367012345678"*/

    }

    private fun getIcons(){
        var i= 0
        selectedVenueDetailItem?.getMaxFourCategoryIcons()?.forEach { icon ->
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
        selectedVenueDetailItem?.getMaxThreePhotos()?.forEach { photo ->
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

    private fun initLayout(){
        val density = resources.displayMetrics.density;
        val size = (ICON_HEIGHT * density).roundToInt()

        nameTextView = view!!.findViewById(R.id.VenueDetailNameTextView)
        verificationImageView = view!!.findViewById(R.id.VenueDetailVerificationImageView)
        ratingBar = view!!.findViewById(R.id.VenueDetailRatingBar)
        hoursTextView = view!!.findViewById(R.id.VenueDetailHoursTextView)
            val clockDrawable =  resources.getDrawable(R.drawable.time)
            clockDrawable.setBounds(0, 0, size, size)
            hoursTextView.setCompoundDrawables(clockDrawable, null, null, null)
        locationTextView = view!!.findViewById(R.id.VenueDetailLocationTextView)
            val locationDrawable =  resources.getDrawable(R.drawable.location)
            locationDrawable.setBounds(0, 0, size, size)
            locationTextView.setCompoundDrawables(locationDrawable, null, null, null)
        descriptionTextView = view!!.findViewById(R.id.VenueDetailDescriptionTextView)
            val moreDrawable =  resources.getDrawable(R.drawable.more)
            moreDrawable.setBounds(0, 0, size, size)
            descriptionTextView.setCompoundDrawables(moreDrawable, null, null, null)
        urlTextView = view!!.findViewById(R.id.VenueDetailURLTextView)
            val webDrawable =  resources.getDrawable(R.drawable.earthgrid)
            webDrawable.setBounds(0, 0, size, size)
            urlTextView.setCompoundDrawables(webDrawable, null, null, null)
        phoneTextView = view!!.findViewById(R.id.VenueDetailPhoneTextView)
            val phoneDrawable =  resources.getDrawable(R.drawable.phone)
            phoneDrawable.setBounds(0, 0, size, size)
            phoneTextView.setCompoundDrawables(phoneDrawable, null, null, null)

        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon1))
        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon2))
        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon3))
        categoryIcons.add(view!!.findViewById(R.id.VenueDetailIcon4))

        photos.add(view!!.findViewById(R.id.VenueDetailPhoto1))
        photos.add(view!!.findViewById(R.id.VenueDetailPhoto2))
        photos.add(view!!.findViewById(R.id.VenueDetailPhoto3))

        detailsLinearLayout = view!!.findViewById(R.id.VenueDetailDetailsLinearLayout)
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
}