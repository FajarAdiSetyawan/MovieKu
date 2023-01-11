/*
 * Created by Fajar Adi Setyawan on 12/12/2022 - 10:39:27
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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
import com.fajaradisetyawan.movieku.data.model.tvshow.Seasons
import com.fajaradisetyawan.movieku.databinding.ItemSeasonBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime


class SeasonListPagingAdapter(val titleTv: String): PagingDataAdapter<Seasons, SeasonListPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Seasons) -> Unit)? = null

    fun setOnItemClickListener(listener: (Seasons) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemSeasonBinding) :
        RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(seasons: Seasons) {
            with(binding) {

                if (seasons.posterPath != null) {
                    Glide.with(itemView)
                        .load("${seasons.baseUrl}${seasons.posterPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_portrait_img)
                        .into(ivPoster)
                } else {
                    ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                }

                tvSeasonName.text = seasons.name
                tvEpisode.text = itemView.resources.getString(R.string.episode, seasons.episodeCount)

                if (seasons.airDate == null){
                    tvYearSeason.text = "-"
                }else{
                    tvYearSeason.text = ParseDateTime.getYear(seasons.airDate)
                }

                val yearSeasons = tvYearSeason.text.toString()

                if (seasons.overview == "") {
                    tvOverviewSeason.text = itemView.resources.getString(
                        R.string.overview_season_empty,
                        seasons.name,
                        titleTv,
                        yearSeasons
                    )
                }else{
                    tvOverviewSeason.text = seasons.overview
                }
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(seasons)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<Seasons>(){
            override fun areItemsTheSame(oldItem: Seasons, newItem: Seasons): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Seasons, newItem: Seasons): Boolean =
                oldItem == newItem
        }
    }
}