/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.MovieCrewPeople
import com.fajaradisetyawan.movieku.databinding.ItemListBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime

class MoviePeopleCrewAdapter: RecyclerView.Adapter<MoviePeopleCrewAdapter.ViewHolder>() {
    private var movie: List<MovieCrewPeople>? = null

    private var onItemClickListener: ((MovieCrewPeople) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieCrewPeople) -> Unit) {
        onItemClickListener = listener
    }

    fun setMoviePeopleCrew(movieList: List<MovieCrewPeople>?) {
        this.movie = movieList
    }

    inner class ViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(movie: MovieCrewPeople) {
            with(binding) {

                if (movie.posterPath == "" || movie.posterPath == null) {
                    if (movie.backdropPath == "" || movie.backdropPath == null) {
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

                tvReleaseDate.text = movie.job

                val progress = movie.voteAverage * 10

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
                    it(movie)
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