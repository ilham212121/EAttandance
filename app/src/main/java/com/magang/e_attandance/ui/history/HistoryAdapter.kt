package com.magang.e_attandance.ui.history
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magang.e_attandance.R

class HistoryAdapter(private val mList: List<DetailHistory>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_history, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class

        // sets the text to the textview from our itemHolder class
        holder.tanggal.text = ItemsViewModel.tanggal
        // sets the text to the textview from our itemHolder class
        holder.waktu.text = ItemsViewModel.waktu
        if (position%2 == 0){
            holder.background.setBackgroundResource(R.color.grey)
        }
        else{
            holder.background.setBackgroundResource(R.color.white)
        }
        if (ItemsViewModel.status == "tepat waktu"){
            holder.status.text = ItemsViewModel.status
            holder.backgroundstatus.setBackgroundResource(R.color.greenlight)
        }
        else if (ItemsViewModel.status == "lembur?"){
            holder.status.text = ItemsViewModel.status
            holder.backgroundstatus.setBackgroundResource(R.color.yellow)
        }
        else{
            holder.status.text = ItemsViewModel.status
            holder.backgroundstatus.setBackgroundResource(R.color.red)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tanggal: TextView = itemView.findViewById(R.id.tanggal)
        val waktu: TextView = itemView.findViewById(R.id.waktu)
        val status: TextView = itemView.findViewById(R.id.status)
        val background: LinearLayout = itemView.findViewById(R.id.background)
        val backgroundstatus: LinearLayout = itemView.findViewById(R.id.backgroundstatus)
    }
}