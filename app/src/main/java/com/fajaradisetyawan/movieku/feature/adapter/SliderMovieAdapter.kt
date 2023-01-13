/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.Movie
import com.fajaradisetyawan.movieku.databinding.ItemSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderMovieAdapter: SliderViewAdapter<SliderMovieAdapter.ViewHolder>() {
    private var movie: List<Movie>? = null
    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    fun setMovie(movieList: List<Movie>?) {
        this.movie = movieList
    }

    inner class ViewHolder(private val binding: ItemSliderBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {


                if (movie.backdropPath == "" || movie.backdropPath.equals(null)) {
                    if (movie.posterPath == "" || movie.posterPath.equals(null)) {
                        ivBackdrop.setImageResource(R.drawable.placeholder_landscape_img)
                    } else {
                        Glide.with(itemView)
                            .load("${movie.baseUrl}${movie.posterPath}")
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.placeholder_landscape_img)
                            .into(ivBackdrop)
                    }
                } else {
                    Glide.with(itemView)
                        .load("${movie.baseUrl}${movie.backdropPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_landscape_img)
                        .into(ivBackdrop)
                }

                tvTitle.text = movie.title
                tvOverview.text = movie.overview
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(movie)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SliderMovieAdapter.ViewHolder, position: Int) {
        holder.bind(movie!![position])
    }

    override fun getCount(): Int {
        return if (movie != null) {
            movie!!.size
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val binding =
            ItemSliderBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return ViewHolder(binding)
    }
}