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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.databinding.ItemListBigBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import java.math.RoundingMode


class TvShowFavAdapter: RecyclerView.Adapter<TvShowFavAdapter.ViewHolder>() {
    private var tvShow: List<TvShowDetail>? = null

    private var onItemClickListener: ((TvShowDetail) -> Unit)? = null

    fun setOnItemClickListener(listener: (TvShowDetail) -> Unit) {
        onItemClickListener = listener
    }

    fun setTvShow(tvShowList: List<TvShowDetail>?) {
        this.tvShow = tvShowList
    }

    inner class ViewHolder(private val binding: ItemListBigBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(tvShow: TvShowDetail){
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvShow!![position])
    }

    override fun getItemCount(): Int {
        return if (tvShow != null) {
            tvShow!!.size
        } else {
            0
        }
    }
}