/*
 * Created by Fajar Adi Setyawan on 15/12/2022 - 10:4:5
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

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
import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.databinding.ItemAllCastCrewBinding


class CastPagingAdapter: PagingDataAdapter<Cast, CastPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemAllCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            with(binding) {
                if (cast.profilePath == "" || cast.profilePath == null){
                    when (cast.gender) {
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
                        .load("${cast.baseUrl}${cast.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .transform(RoundedCorners(25))
                        .into(ivProfile)
                }

                tvName.text = cast.name
                if (cast.totalEpisodeCount != 0){
                    tvCharacterOrJob.text = itemView.resources.getString(R.string.episode_count, cast.name, cast.totalEpisodeCount)
                }else{
                    tvCharacterOrJob.text = cast.character
                }
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(cast)
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<Cast>(){
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean =
                oldItem == newItem
        }
    }
}