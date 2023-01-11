/*
 * Created by Fajar Adi Setyawan on 10/1/2023 - 10:1:0
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.databinding.ItemListPeopleBigBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import java.math.RoundingMode


class PeopleFavAdapter: RecyclerView.Adapter<PeopleFavAdapter.ViewHolder>() {
    private var people : List<PeopleDetail>? = null

    private var onItemClickListener: ((PeopleDetail) -> Unit)? = null

    fun setOnItemClickListener(listener: (PeopleDetail) -> Unit) {
        onItemClickListener = listener
    }

    fun setPeople(peopleList: List<PeopleDetail>?) {
        this.people = peopleList
    }

    inner class ViewHolder(private val binding: ItemListPeopleBigBinding) :
        RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(peopleDetail: PeopleDetail){
            with(binding) {

                tvName.text = peopleDetail.name

                if (peopleDetail.profilePath == "" || peopleDetail.profilePath == null){
                    when (peopleDetail.gender) {
                        1 -> {
                            ivPoster.setImageResource(R.drawable.placeholder_avatar_w)
                        }
                        2 -> {
                            ivPoster.setImageResource(R.drawable.placeholder_avatar_m)
                        }
                        else -> {
                            ivPoster.setImageResource(R.drawable.placeholder_avatar_o)
                        }
                    }
                }else{
                    Glide.with(itemView)
                        .load("${peopleDetail.baseUrl}${peopleDetail.profilePath}")
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.placeholder_avatar_o)
                        .into(ivPoster)
                }

                if (peopleDetail.birthday.equals(null) || peopleDetail.birthday == "" || peopleDetail.birthday == null) {
                    tvBirthdate.text = "-"
                } else {
                    tvBirthdate.visibility = View.VISIBLE
                    tvBirthdate.text = itemView.resources.getString(
                        R.string.birthdate_with_age, peopleDetail.birthday, ParseDateTime.getAge(
                            peopleDetail.birthday!!
                        )
                    )
                }

                if (peopleDetail.place != null || peopleDetail.place != ""){
                    tvPlace.text = peopleDetail.place
                }else{
                    tvPlace.text = "-"
                }

                tvKnowFor.text = peopleDetail.department

                tvRating.text =
                    peopleDetail.popularity.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                val rating = peopleDetail.popularity * 0.1
                ratingBar.rating = rating.toFloat()

            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(peopleDetail)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListPeopleBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(people!![position])
    }

    override fun getItemCount(): Int {
        return if (people != null) {
            people!!.size
        } else {
            0
        }
    }
}