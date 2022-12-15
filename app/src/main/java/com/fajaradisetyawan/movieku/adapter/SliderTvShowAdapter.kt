/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShow
import com.fajaradisetyawan.movieku.databinding.ItemSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderTvShowAdapter: SliderViewAdapter<SliderTvShowAdapter.ViewHolder>() {
    private var tvShow: List<TvShow>? = null
    private var onItemClickListener: ((TvShow) -> Unit)? = null

    fun setOnItemClickListener(listener: (TvShow) -> Unit) {
        onItemClickListener = listener
    }

    fun setTvShow(tvShowList: List<TvShow>?) {
        this.tvShow = tvShowList
    }

    inner class ViewHolder (private val binding: ItemSliderBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(tvShow: TvShow) {
            with(binding) {

                if (tvShow.backdropPath == "" || tvShow.backdropPath == null) {
                    if (tvShow.posterPath == "" || tvShow.posterPath == null) {
                        ivBackdrop.setImageResource(R.drawable.placeholder_landscape_img)
                    } else {
                        Glide.with(itemView)
                            .load("${tvShow.baseUrl}${tvShow.posterPath}")
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.placeholder_landscape_img)
                            .into(ivBackdrop)
                    }
                } else {
                    Glide.with(itemView)
                        .load("${tvShow.baseUrl}${tvShow.backdropPath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_landscape_img)
                        .into(ivBackdrop)
                }



                tvTitle.text = tvShow.name
                tvOverview.text = tvShow.overview
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(tvShow)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SliderTvShowAdapter.ViewHolder, position: Int) {
        holder.bind(tvShow!![position])
    }

    override fun getCount(): Int {
        return if (tvShow != null) {
            tvShow!!.size
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return ViewHolder(binding)
    }
}