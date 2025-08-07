package com.vhkfoundation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.vhkfoundation.R
import com.vhkfoundation.model.SliderData

class SliderAdapter2(
    private val context: Context,
    private val sliderItems: List<SliderData>
) : SliderViewAdapter<SliderAdapter2.SliderAdapter2ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapter2ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_layout_item, parent, false)
        return SliderAdapter2ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter2ViewHolder, position: Int) {
        val sliderItem = sliderItems[position]
        Glide.with(viewHolder.itemView)
            .load(sliderItem.imgUrl)
            .fitCenter()
            .into(viewHolder.imageViewBackground)


        Glide.with(viewHolder.itemView)
            .load(sliderItem.imgUrl2)
            .fitCenter()
            .into(viewHolder.imageViewBackground2)
    }

    override fun getCount(): Int = sliderItems.size

    class SliderAdapter2ViewHolder(itemView: View) : ViewHolder(itemView) {
        val imageViewBackground: ImageView = itemView.findViewById(R.id.myimage)
        val imageViewBackground2: ImageView = itemView.findViewById(R.id.myimage2)
    }
}
