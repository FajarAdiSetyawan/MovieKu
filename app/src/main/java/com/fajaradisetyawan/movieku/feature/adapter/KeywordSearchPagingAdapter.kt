/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:43:26
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.Keyword
import com.fajaradisetyawan.movieku.databinding.ItemKeywordBinding


class KeywordSearchPagingAdapter: PagingDataAdapter<Keyword, KeywordSearchPagingAdapter.ViewHolder>(COMPARATOR) {
    private var onItemClickListener: ((Keyword) -> Unit)? = null

    fun setOnItemClickListener(listener: (Keyword) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemKeywordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(keyword: Keyword) {
            with(binding) {
                tvKeyword.text = keyword.name
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(keyword)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<Keyword>(){
            override fun areItemsTheSame(oldItem: Keyword, newItem: Keyword): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Keyword, newItem: Keyword): Boolean =
                oldItem == newItem
        }
    }
}