package com.example.paperwalls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paperwalls.R
import com.example.paperwalls.models.ImageModel

class ImageAdapter(private var imageList: List<ImageModel>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageModel = imageList[position]
        Glide.with(holder.itemView.context)
            .load(imageModel.imagePath)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateData(newImageList: List<ImageModel>) {
        imageList = newImageList
        notifyDataSetChanged()
    }
}