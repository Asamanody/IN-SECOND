package com.example.insecondapp.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.insecondapp.R
import kotlinx.android.synthetic.main.home_image_item.view.*

class ViewPagerAdapter (
    val images : List<Int>
        ) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_image_item,parent,false)
        return ViewPagerViewHolder(view)
    }
    override fun getItemCount(): Int {
       return images.size
    }
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
    val currentImage = images[position]
        holder.itemView.item_image.setImageResource(currentImage)
    }


}