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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.databinding.ItemAllCastCrewBinding


class AllCrewAdapter: RecyclerView.Adapter<AllCrewAdapter.ViewHolder>() {
    private var crew: List<Crew>? = null

    private var onItemClickListener: ((Crew) -> Unit)? = null

    fun setOnItemClickListener(listener: (Crew) -> Unit) {
        onItemClickListener = listener
    }

    fun setCrew(crewList: List<Crew>?) {
        this.crew = crewList
    }

    inner class ViewHolder(private val binding: ItemAllCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(crew: Crew) {
            with(binding) {

                if (crew.profilePath == "" || crew.profilePath == null){
                    when (crew.gender) {
                        1 -> {
                            ivProfile.setImageResource(R.drawable.placeholder_avatar_w)
                        }
                        2 -> {
                            ivProfile.setImageResource(R.drawable.placeholder_avatar_m)
                        }
                        else -> {
                            ivProfile.setImageResource(R.drawable.placeholder_avatar_o)
                        }
                    }
                }else{
                    Glide.with(itemView)
                        .load("${crew.baseUrl}${crew.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .transform(RoundedCorners(25))
                        .into(ivProfile)
                }

                tvName.text = crew.name
                tvCharacterOrJob.text = crew.job
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(crew)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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