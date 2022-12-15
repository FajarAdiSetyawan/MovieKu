/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.People
import com.fajaradisetyawan.movieku.databinding.ItemPeoplePopularBinding

class PopularPeopleAdapter : PagingDataAdapter<People, PopularPeopleAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((People) -> Unit)? = null

    fun setOnItemClickListener(listener: (People) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemPeoplePopularBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(people: People){
                with(binding){
                    tvCastName.text = people.name

                    if (people.profilePath == "" || people.profilePath == null){
                        when (people.gender) {
                            1 -> {
                                ivProfile.setImageResource(R.drawable.placeholder_avatar_w)
                            }
                            2 -> {
                                ivProfile.setImageResource(R.drawable.placeholder_avatar_m)
                            }
                            else -> {
                                ivProfile.setImageResource(R.drawable.placeholder_avatar_o)
                            }
                        }
                    }else{
                        Glide.with(itemView)
                            .load("${people.baseUrl}${people.profilePath}")
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.placeholder_avatar_o)
                            .into(ivProfile)
                    }
                }
                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(people)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPeoplePopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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