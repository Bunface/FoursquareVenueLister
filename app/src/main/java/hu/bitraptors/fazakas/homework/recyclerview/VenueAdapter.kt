package hu.bitraptors.fazakas.homework.recyclerview

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bitraptors.fazakas.homework.R
import hu.bitraptors.fazakas.homework.data.CategoryIcon
import hu.bitraptors.fazakas.homework.data.VenueItem
import hu.bitraptors.fazakas.homework.foursquare.FourSquare
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


public class VenueAdapter(private val listener: VenueItemClickListener) :

    RecyclerView.Adapter<VenueAdapter.VenueViewHolder> (){

    private val items = ArrayList<VenueItem>()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VenueViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.venue_item, parent, false)
        return VenueViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val item = items[position]

        holder.item = item
        holder.nameTextView.text = item.name
        setIcon(holder.itemIcon, item.categoryIcon)
    }

    interface VenueItemClickListener{
        fun onItemClicked(item: VenueItem?)
    }

    inner class VenueViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var item : VenueItem? = null

        var nameTextView : TextView = itemView.findViewById(R.id.VenueItemNameTextView)
        var itemLayout : LinearLayout = itemView.findViewById(R.id.VenueItemLayout)
        var itemIcon: ImageView = itemView.findViewById(R.id.VenueItemIcon)

        init{
            itemLayout.setOnClickListener{ listener.onItemClicked(item)}
        }
    }

    fun update(venueItems: List<VenueItem>){
        items.clear()
        items.addAll(venueItems)
        notifyDataSetChanged()
    }

    private fun setIcon(imageView: ImageView, categoryIcon: CategoryIcon?){
        if(categoryIcon != null){
            val fourSquare = FourSquare.retrofit.create(FourSquare::class.java)
            val photoCall = fourSquare.downloadPhoto(categoryIcon.url)

            photoCall.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val byteStream = response.body()?.byteStream()

                    if (byteStream != null){
                        val bmp = BitmapFactory.decodeStream(byteStream)
                        imageView.setImageBitmap(bmp)
                        imageView.visibility = ImageView.VISIBLE
                    }else{
                        //do nothing no icon
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //do nothing no icon
                }
            })
        }
    }
}