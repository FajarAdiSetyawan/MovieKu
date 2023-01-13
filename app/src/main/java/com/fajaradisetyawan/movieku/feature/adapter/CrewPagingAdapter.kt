/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.databinding.ItemAllCastCrewBinding


class CrewPagingAdapter: PagingDataAdapter<Crew, CrewPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Crew) -> Unit)? = null

    fun setOnItemClickListener(listener: (Crew) -> Unit) {
        onItemClickListener = listener
    }

    var job = ""

    inner class ViewHolder(private val binding: ItemAllCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(crew: Crew) {
            with(binding) {

                if (crew.profilePath == "" || crew.profilePath == null){
                    when (crew.gender) {
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
                        .load("${crew.baseUrl}${crew.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .transform(RoundedCorners(25))
                        .into(ivProfile)
                }

                tvName.text = crew.name

                if (crew.totalEpisodeCount != 0){
                    for ((index, roles) in crew.jobs.withIndex()) {
                        job = roles.job

                        if (index != (crew.jobs.size - 1)) {
                            job = ""
                        }
                    }
                    tvCharacterOrJob.text = itemView.resources.getString(R.string.episode_count, job , crew.totalEpisodeCount)
                }else{
                    tvCharacterOrJob.text = crew.job
                }
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(crew)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<Crew>(){
            override fun areItemsTheSame(oldItem: Crew, newItem: Crew): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Crew, newItem: Crew): Boolean =
                oldItem == newItem
        }
    }
}