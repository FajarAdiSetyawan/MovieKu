/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.Trending
import com.fajaradisetyawan.movieku.databinding.ItemSearchBinding


class SearchTrendingAdapter: RecyclerView.Adapter<SearchTrendingAdapter.ViewHolder>() {
    private var trending: List<Trending>? = null

    private val limit = 10

    private var onItemClickListener: ((Trending) -> Unit)? = null

    fun setOnItemClickListener(listener: (Trending) -> Unit) {
        onItemClickListener = listener
    }

    fun setTrending(trendingList: List<Trending>?) {
        this.trending = trendingList
    }

    inner class ViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(trending: Trending) {
            with(binding) {
                val name: String = trending.name.toString()
                val title: String = trending.title.toString()

                when {
                    name == "" || name == "null" || name.equals(null) || name.isEmpty() -> {
                        tvSearch.text = title
                    }
                    else -> {
                        tvSearch.text = name
                    }
                }

                when {
                    title == "" || title == "null" || title.equals(null) || title.isEmpty() -> {
                        tvSearch.text = name
                    }
                    else -> {
                        tvSearch.text = title
                    }
                }
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(trending)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

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