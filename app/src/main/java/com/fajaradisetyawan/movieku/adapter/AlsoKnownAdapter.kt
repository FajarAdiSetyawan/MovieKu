/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.ItemAlsoKnownBinding

class AlsoKnownAdapter(var alsoKnown: List<String>) :
    RecyclerView.Adapter<AlsoKnownAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var also: TextView =itemView.findViewById(R.id.tv_also)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_also_known, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.also.text = alsoKnown[position]
    }

    override fun getItemCount(): Int {
        return alsoKnown.size
    }
}