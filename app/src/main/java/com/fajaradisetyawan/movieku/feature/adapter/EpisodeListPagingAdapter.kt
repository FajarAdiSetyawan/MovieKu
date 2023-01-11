/*
 * Created by Fajar Adi Setyawan on 12/12/2022 - 10:40:12
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
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.databinding.ItemEpisodeBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import java.math.RoundingMode


class EpisodeListPagingAdapter : PagingDataAdapter<Episode, EpisodeListPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Episode) -> Unit)? = null

    fun setOnItemClickListener(listener: (Episode) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(episode: Episode) {
            with(binding) {
                if (episode.stillPath != null) {
                    Glide.with(itemView)
                        .load("${episode.baseUrl}${episode.stillPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_portrait_img)
                        .into(ivPoster)
                } else {
                    ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                }

                tvEpisodeName.text = itemView.resources.getString(
                    R.string.episode_name,
                    episode.episodeNumber,
                    episode.name
                )

                if (episode.airDate != null) {
                    tvReleaseDate.text = ParseDateTime.parseDateFull(episode.airDate)
                } else {
                    tvReleaseDate.text = "-"
                }

                val hours: Int = episode.runtime / 60
                val minutes: Int = episode.runtime % 60

                val duration = if (episode.runtime <= 60) {
                    itemView.resources.getString(R.string.timemin, episode.runtime)
                } else {
                    itemView.resources.getString(R.string.time, hours, minutes)
                }

                tvRuntime.text = duration

                if (episode.overview == "") {
                    tvOverview.text = itemView.resources.getString(
                        R.string.overview_episode_empty, episode.name,
                    )
                } else {
                    tvOverview.text = episode.overview
                }

                tvRating.text =
                    episode.voteAverage.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                val rating = episode.voteAverage * 0.1
                ratingBar.rating = rating.toFloat()
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(episode)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<Episode>(){
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem == newItem
        }
    }
}