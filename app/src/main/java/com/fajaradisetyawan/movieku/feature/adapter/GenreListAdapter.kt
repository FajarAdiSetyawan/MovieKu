/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.Genre
import com.fajaradisetyawan.movieku.databinding.ItemKeywordGenreBinding

class GenreListAdapter: RecyclerView.Adapter<GenreListAdapter.ViewHolder>() {
    private var genre: List<Genre>? = null

    private var onItemClickListener: ((Genre) -> Unit)? = null

    fun setOnItemClickListener(listener: (Genre) -> Unit) {
        onItemClickListener = listener
    }

    fun setGenre(genreList: List<Genre>?) {
        this.genre = genreList
    }

    inner class ViewHolder(private val binding: ItemKeywordGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            with(binding) {
                tvKeywordGenre.text = genre.name
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(genre)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeywordGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(genre!![position])
    }

    override fun getItemCount(): Int {
        return if (genre != null) {
            genre!!.size
        } else {
            0
        }
    }
}