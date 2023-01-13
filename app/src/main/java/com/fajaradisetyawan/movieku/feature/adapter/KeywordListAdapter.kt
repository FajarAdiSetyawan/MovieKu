/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.Keyword
import com.fajaradisetyawan.movieku.databinding.ItemKeywordGenreBinding

class KeywordListAdapter: RecyclerView.Adapter<KeywordListAdapter.ViewHolder>() {
    private var keyword: List<Keyword>? = null

    private var onItemClickListener: ((Keyword) -> Unit)? = null

    fun setOnItemClickListener(listener: (Keyword) -> Unit) {
        onItemClickListener = listener
    }

    fun setKeyword(keywordList: List<Keyword>?) {
        this.keyword = keywordList
    }

    inner class ViewHolder(private val binding: ItemKeywordGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) {
            with(binding) {
                tvKeywordGenre.text = keyword.name
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(keyword)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeywordGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(keyword!![position])
    }

    override fun getItemCount(): Int {
        return if (keyword != null) {
            keyword!!.size
        } else {
            0
        }
    }
}