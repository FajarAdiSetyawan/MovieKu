/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:47:40
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.Collection
import com.fajaradisetyawan.movieku.databinding.ItemListCompanyBinding


class CollectionSearchPagingAdapter: PagingDataAdapter<Collection, CollectionSearchPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Collection) -> Unit)? = null

    fun setOnItemClickListener(listener: (Collection) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemListCompanyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(collection: Collection) {
            with(binding) {
                if (collection.posterPath != null) {
                    Glide.with(itemView)
                        .load("${collection.baseUrl}${collection.posterPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_portrait_img)
                        .into(ivPoster)
                } else {
                    ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                }

                tvName.text = collection.name
                if (collection.overview != ""){
                    tvCountry.text = collection.overview
                    tvCountry.visibility = View.VISIBLE
                }else{
                    tvCountry.visibility = View.GONE
                }
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(collection)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<Collection>(){
            override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean =
                oldItem == newItem
        }
    }
}