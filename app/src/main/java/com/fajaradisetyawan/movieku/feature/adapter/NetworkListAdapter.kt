/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.Network
import com.fajaradisetyawan.movieku.databinding.ItemNetworkBinding

class NetworkListAdapter: RecyclerView.Adapter<NetworkListAdapter.ViewHolder>() {
    private var network: List<Network>? = null

    private var onItemClickListener: ((Network) -> Unit)? = null

    fun setOnItemClickListener(listener: (Network) -> Unit) {
        onItemClickListener = listener
    }

    fun setNetwork(networkList: List<Network>?) {
        this.network = networkList
    }

    inner class ViewHolder(private val binding: ItemNetworkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(network: Network) {
            with(binding) {
                Glide.with(itemView)
                    .load("${network.baseUrl}${network.logoPath}")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.placeholder_portrait_img)
                    .fitCenter()
                    .into(ivNetwork)

            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(network)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNetworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(network!![position])
    }

    override fun getItemCount(): Int {
        return if (network != null) {
            network!!.size
        } else {
            0
        }
    }
}