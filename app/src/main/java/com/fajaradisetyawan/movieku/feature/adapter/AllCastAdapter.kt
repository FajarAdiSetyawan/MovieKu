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
import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.databinding.ItemAllCastCrewBinding


class AllCastAdapter: RecyclerView.Adapter<AllCastAdapter.ViewHolder>() {
    private var cast: List<Cast>? = null

    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) {
        onItemClickListener = listener
    }

    fun setCast(castList: List<Cast>?) {
        this.cast = castList
    }

    inner class ViewHolder(private val binding: ItemAllCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            with(binding) {
                if (cast.profilePath == "" || cast.profilePath == null){
                    when (cast.gender) {
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
                        .load("${cast.baseUrl}${cast.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .transform(RoundedCorners(25))
                        .into(ivProfile)
                }

                tvName.text = cast.name
                tvCharacterOrJob.text = cast.character
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(cast)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cast!![position])
    }

    override fun getItemCount(): Int {
        return if (cast != null) {
            cast!!.size
        } else {
            0
        }
    }
}