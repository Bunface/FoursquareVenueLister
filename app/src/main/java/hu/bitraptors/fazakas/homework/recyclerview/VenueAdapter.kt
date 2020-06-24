package hu.bitraptors.fazakas.homework.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bitraptors.fazakas.homework.R


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
    }

    interface VenueItemClickListener{
        fun onItemClicked(item: VenueItem?)
    }

    inner class VenueViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var item : VenueItem? = null

        var nameTextView : TextView = itemView.findViewById(R.id.VenueItemNameTextView)
        var itemLayout : LinearLayout = itemView.findViewById(R.id.VenueItemLayout)

        init{
            itemLayout.setOnClickListener{ listener.onItemClicked(item)}
        }
    }

    fun update(venueItems: List<VenueItem>){
        items.clear()
        items.addAll(venueItems)
        notifyDataSetChanged()
    }

    fun deleteAll() {
        items.clear()
        notifyDataSetChanged()
    }

}