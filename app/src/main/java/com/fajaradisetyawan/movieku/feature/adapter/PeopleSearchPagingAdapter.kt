/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:51:11
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.People
import com.fajaradisetyawan.movieku.databinding.ItemListCompanyBinding


class PeopleSearchPagingAdapter: PagingDataAdapter<People, PeopleSearchPagingAdapter.ViewHolder>(COMPARATOR) {

    private var onItemClickListener: ((People) -> Unit)? = null

    fun setOnItemClickListener(listener: (People) -> Unit) {
        onItemClickListener = listener
    }


    inner class ViewHolder(private val binding: ItemListCompanyBinding) :
        RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(people: People){
            with(binding) {

                tvName.text = people.name

                if (people.profilePath == "" || people.profilePath == null){
                    when (people.gender) {
                        1 -> {
                            ivPoster.setImageResource(R.drawable.placeholder_avatar_w)
                        }
                        2 -> {
                            ivPoster.setImageResource(R.drawable.placeholder_avatar_m)
                        }
                        else -> {
                            ivPoster.setImageResource(R.drawable.placeholder_avatar_o)
                        }
                    }
                }else{
                    Glide.with(itemView)
                        .load("${people.baseUrl}${people.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .into(ivPoster)
                }

                tvCountry.text = people.knownForDepartment
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(people)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleSearchPagingAdapter.ViewHolder {
        val binding = ItemListCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PeopleSearchPagingAdapter.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<People>(){
            override fun areItemsTheSame(oldItem: People, newItem: People): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: People, newItem: People): Boolean =
                oldItem == newItem
        }
    }
}