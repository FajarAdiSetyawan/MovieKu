/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.Companies
import com.fajaradisetyawan.movieku.databinding.ItemCompanyBinding

class CompanyAdapter: RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {
    private var companies: List<Companies>? = null

    private var onItemClickListener: ((Companies) -> Unit)? = null

    fun setOnItemClickListener(listener: (Companies) -> Unit) {
        onItemClickListener = listener
    }

    fun setCompany(companiesList: List<Companies>?) {
        this.companies = companiesList
    }

    inner class ViewHolder(private val binding: ItemCompanyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(companies: Companies) {
            with(binding) {
                Glide.with(itemView)
                    .load("${companies.baseUrl}${companies.logoPath}")
                    .centerCrop()
                    .transform(RoundedCorners(25))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.placeholder_portrait_img)
                    .into(ivCompany)

                tvCompany.text = companies.name
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(companies)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(companies!![position])
    }

    override fun getItemCount(): Int {
        return if (companies != null) {
            companies!!.size
        } else {
            0
        }
    }
}