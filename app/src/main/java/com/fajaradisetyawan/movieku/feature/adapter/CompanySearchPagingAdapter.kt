/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:36:16
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
import com.fajaradisetyawan.movieku.data.model.Companies
import com.fajaradisetyawan.movieku.databinding.ItemListCompanyBinding


class CompanySearchPagingAdapter: PagingDataAdapter<Companies, CompanySearchPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Companies) -> Unit)? = null

    fun setOnItemClickListener(listener: (Companies) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemListCompanyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(companies: Companies) {
            with(binding) {
                if (companies.logoPath != null) {
                    Glide.with(itemView)
                        .load("${companies.baseUrl}${companies.logoPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_portrait_img)
                        .into(ivPoster)
                } else {
                    ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                }

                tvName.text = companies.name
                if (companies.originCountry != ""){
                    tvCountry.text = companies.originCountry
                    tvCountry.visibility = View.VISIBLE
                }else{
                    tvCountry.visibility = View.GONE
                }
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(companies)
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<Companies>(){
            override fun areItemsTheSame(oldItem: Companies, newItem: Companies): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Companies, newItem: Companies): Boolean =
                oldItem == newItem
        }
    }
}