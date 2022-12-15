/*
 * Created by Fajar Adi Setyawan on 9/12/2022 - 12:15:19
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.data.model.SpokenLanguages
import com.fajaradisetyawan.movieku.databinding.ItemAlsoKnownBinding


class SpokenLanguageAdapter: RecyclerView.Adapter<SpokenLanguageAdapter.ViewHolder>() {
    private var spokenLanguages: List<SpokenLanguages>? = null

    fun setSpokenLanguage(spokenLanguagesList: List<SpokenLanguages>?) {
        this.spokenLanguages = spokenLanguagesList
    }

    inner class ViewHolder(private val binding: ItemAlsoKnownBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(spokenLanguages: SpokenLanguages) {
            with(binding) {

                tvAlso.text = spokenLanguages.englishName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlsoKnownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(spokenLanguages!![position])
    }

    override fun getItemCount(): Int {
        return if (spokenLanguages != null) {
            spokenLanguages!!.size
        } else {
            0
        }
    }
}