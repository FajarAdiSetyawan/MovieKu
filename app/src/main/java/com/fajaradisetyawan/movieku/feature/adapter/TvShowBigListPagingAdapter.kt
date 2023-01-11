/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShow
import com.fajaradisetyawan.movieku.databinding.ItemListBigBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import java.math.RoundingMode

class TvShowBigListPagingAdapter : PagingDataAdapter<TvShow, TvShowBigListPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((TvShow) -> Unit)? = null

    fun setOnItemClickListener(listener: (TvShow) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemListBigBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(tvShow: TvShow){
            with(binding) {

                if (tvShow.posterPath == null) {
                    if (tvShow.backdropPath == null) {
                        ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                    } else {
                        Glide.with(itemView)
                            .load("${tvShow.baseUrl}${tvShow.backdropPath}")
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.placeholder_portrait_img)
                            .into(ivPoster)
                    }
                } else {
                    Glide.with(itemView)
                        .load("${tvShow.baseUrl}${tvShow.posterPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_portrait_img)
                        .into(ivPoster)
                }

                tvTitle.text = tvShow.name

                tvRelease.text = ParseDateTime.parseDate(tvShow.firstAirDate)
                if (tvShow.overview == "") {
                    tvOverview.text = itemView.resources.getString(
                        R.string.overview_episode_empty, tvShow.name,
                    )
                } else {
                    tvOverview.text = tvShow.overview
                }

                tvRating.text =
                    tvShow.voteAverage.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                val rating = tvShow.voteAverage * 0.1
                ratingBar.rating = rating.toFloat()

            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(tvShow)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<TvShow>(){
            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean =
                oldItem == newItem
        }
    }
}