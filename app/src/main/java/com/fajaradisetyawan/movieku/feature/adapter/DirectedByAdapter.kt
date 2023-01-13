/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.databinding.ItemAlsoKnownBinding


class DirectedByAdapter: RecyclerView.Adapter<DirectedByAdapter.ViewHolder>() {
    private var crew: List<Crew>? = null

    fun setDirector(crewList: List<Crew>?) {
        this.crew = crewList
    }

    private var onItemClickListener: ((Crew) -> Unit)? = null

    fun setOnItemClickListener(listener: (Crew) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemAlsoKnownBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(crew: Crew) {
            with(binding) {
                if (crew.job == "Director"){
                    tvAlso.text = crew.name
                }else{
                    tvAlso.visibility = View.GONE
                }
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(crew)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlsoKnownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(crew!![position])
    }

    override fun getItemCount(): Int {
        return if (crew != null) {
            crew!!.size
        } else {
            0
        }
    }
}