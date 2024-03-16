package com.example.bondoman.ui.scan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.models.Item

class ScanItemAdapter(private var itemList: List<Item>) : RecyclerView.Adapter<ScanItemAdapter.ScanItemViewHolder>() {

    inner class ScanItemViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName : TextView = itemView.findViewById(R.id.item_name)
        val itemQty : TextView = itemView.findViewById(R.id.item_qty)
        val itemPrice : TextView = itemView.findViewById(R.id.item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_scan, parent, false)
        return ScanItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScanItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
        holder.itemQty.text = currentItem.qty.toString() + " pcs"
        holder.itemPrice.text = "$" + currentItem.price.toString()
    }

    fun setItemList(newItemList: List<Item>) {
        itemList = newItemList
    }

    override fun getItemCount() = itemList.size
}