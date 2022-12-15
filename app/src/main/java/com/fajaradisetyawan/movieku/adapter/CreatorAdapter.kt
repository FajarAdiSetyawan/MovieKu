/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.tvshow.CreatedBy
import com.fajaradisetyawan.movieku.databinding.ItemCreatorBinding

class CreatorAdapter: RecyclerView.Adapter<CreatorAdapter.ViewHolder>() {
    private var createdBy: List<CreatedBy>? = null

    private var onItemClickListener: ((CreatedBy) -> Unit)? = null


    fun setOnItemClickListener(listener: (CreatedBy) -> Unit) {
        onItemClickListener = listener
    }

    fun setCreator(CreatedByList: List<CreatedBy>?) {
        this.createdBy = CreatedByList
    }

    inner class ViewHolder(private val binding: ItemCreatorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(createdBy: CreatedBy) {
            with(binding) {
                tvNameCreator.text = createdBy.name
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(createdBy)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCreatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(createdBy!![position])

    }

    override fun getItemCount(): Int {
        return if (createdBy != null) {
            createdBy!!.size
        } else {
            0
        }
    }
}