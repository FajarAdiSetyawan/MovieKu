/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.Movie
import com.fajaradisetyawan.movieku.databinding.ItemListBigBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import java.math.RoundingMode

class MovieListBigAdapter: RecyclerView.Adapter<MovieListBigAdapter.ViewHolder>() {
    private var movie: List<Movie>? = null

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    fun setMovie(movieList: List<Movie>?) {
        this.movie = movieList
    }

    inner class ViewHolder(private val binding: ItemListBigBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {

                if (movie.posterPath == "" || movie.posterPath.equals(null)) {
                    if (movie.backdropPath == "" || movie.backdropPath.equals(null)) {
                        ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
                    } else {
                        Glide.with(itemView)
                            .load("${movie.baseUrl}${movie.backdropPath}")
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.placeholder_portrait_img)
                            .into(ivPoster)
                    }
                } else {
                    Glide.with(itemView)
                        .load("${movie.baseUrl}${movie.posterPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_portrait_img)
                        .into(ivPoster)
                }

                tvTitle.text = movie.title

                if (movie.releaseDate.isNullOrBlank() || movie.releaseDate == "" || movie.releaseDate.isNullOrEmpty()){
                    tvRelease.text = "-"
                }else{
                    tvRelease.text = ParseDateTime.parseDate(movie.releaseDate)
                }

                if (movie.overview == "") {
                    tvOverview.text = itemView.resources.getString(
                        R.string.overview_episode_empty, movie.title,
                    )
                } else {
                    tvOverview.text = movie.overview
                }

                tvRating.text =
                    movie.voteAverage.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                val rating = movie.voteAverage * 0.1
                ratingBar.rating = rating.toFloat()

            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(movie)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movie!![position])
    }

    override fun getItemCount(): Int {
        return if (movie != null) {
            movie!!.size
        } else {
            0
        }
    }
}