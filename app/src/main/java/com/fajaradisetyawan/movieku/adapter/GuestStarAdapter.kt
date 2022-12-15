/*
 * Created by Fajar Adi Setyawan on 14/12/2022 - 12:25:43
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.tvshow.GuestStar
import com.fajaradisetyawan.movieku.databinding.ItemAllCastCrewBinding


class GuestStarAdapter: RecyclerView.Adapter<GuestStarAdapter.ViewHolder>() {
    private var guestStar: List<GuestStar>? = null

    private var onItemClickListener: ((GuestStar) -> Unit)? = null

    fun setOnItemClickListener(listener: (GuestStar) -> Unit) {
        onItemClickListener = listener
    }

    fun setGuestStar(guestStarList: List<GuestStar>?) {
        this.guestStar = guestStarList
    }

    inner class ViewHolder(private val binding: ItemAllCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(guestStar: GuestStar) {
            with(binding) {

                if (guestStar.profilePath == "" || guestStar.profilePath == null){
                    when (guestStar.gender) {
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
                        .load("${guestStar.baseUrl}${guestStar.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .transform(RoundedCorners(25))
                        .into(ivProfile)
                }

                tvName.text = guestStar.name
                tvCharacterOrJob.text = guestStar.job
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(guestStar)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(guestStar!![position])
    }

    override fun getItemCount(): Int {
        return if (guestStar != null) {
            guestStar!!.size
        } else {
            0
        }
    }
}