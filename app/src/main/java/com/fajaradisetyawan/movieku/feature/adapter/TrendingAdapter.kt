/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.Trending
import com.fajaradisetyawan.movieku.databinding.ItemListBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime

class TrendingAdapter: RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {
    private var trending: List<Trending>? = null

    private var onItemClickListener: ((Trending) -> Unit)? = null

    fun setOnItemClickListener(listener: (Trending) -> Unit) {
        onItemClickListener = listener
    }

    fun setTrending(trendingList: List<Trending>?) {
        this.trending = trendingList
    }

    inner class ViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("ResourceAsColor")
        fun bind(trending: Trending) {
            with(binding) {

                if (trending.posterPath == null) {
                    if (trending.backdropPath == null) {
                        ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                    } else {
                        Glide.with(itemView)
                            .load("${trending.baseUrl}${trending.backdropPath}")
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_baseline_close_24)
                            .into(ivPoster)
                    }
                } else {
                    Glide.with(itemView)
                        .load("${trending.baseUrl}${trending.posterPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_baseline_close_24)
                        .into(ivPoster)
                }

                val name: String = trending.name.toString()
                val title: String = trending.title.toString()

                val release: String = trending.releaseDate.toString()
                val firstAir: String = trending.firstAirDate.toString()

                when {
                    name == "" || name == "null" || name.equals(null) || name.isEmpty() -> {
                        tvTitle.text = title
                    }
                    else -> {
                        tvTitle.text = name
                    }
                }

                when {
                    title == "" || title == "null" || title.equals(null) || title.isEmpty() -> {
                        tvTitle.text = name
                    }
                    else -> {
                        tvTitle.text = title
                    }
                }

                when {
                    release == "" || release == "null" || release.equals(null) || release.isEmpty() -> {
                        tvReleaseDate.text = ParseDateTime.parseDate(firstAir)
                    }
                    else -> {
                        tvReleaseDate.text = ParseDateTime.parseDate(release)
                    }
                }

                when {
                    firstAir == "" || firstAir == "null" || firstAir.equals(null) || firstAir.isEmpty() -> {
                        tvReleaseDate.text = ParseDateTime.parseDate(release)
                    }
                    else -> {
                        tvReleaseDate.text = ParseDateTime.parseDate(firstAir)
                    }
                }

                val progress = trending.voteAverage * 10

                if (progress >= 70) {
                    progressCircular.progressColor =
                        itemView.context.getColor(R.color.md_green_A700)
                    progressCircular.dotColor = itemView.context.getColor(R.color.md_green_900)
                } else {
                    progressCircular.progressColor =
                        itemView.context.getColor(R.color.md_yellow_500)
                    progressCircular.dotColor = itemView.context.getColor(R.color.md_yellow_800)
                }

                progressCircular.setProgress(progress, 100.0)
                progressCircular.maxProgress = 100.0
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(trending)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trending!![position])
    }

    override fun getItemCount(): Int {
        return if (trending != null) {
            trending!!.size
        } else {
            0
        }
    }
}