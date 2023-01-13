/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentFavoriteBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var fragmentFavoriteBinding: FragmentFavoriteBinding? = null
    private val binding get() = fragmentFavoriteBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment'
        fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val pagerAdapterSearch = ViewPagerAdapter(requireActivity())
        binding.pagerFavorite.adapter = pagerAdapterSearch

        TabLayoutMediator(binding.tabFav, binding.pagerFavorite) { tab, position ->
            val tabNames =
                listOf(
                    resources.getString(R.string.movie),
                    resources.getString(R.string.tvshow),
                    resources.getString(R.string.people),
                )
            tab.text = tabNames[position]
        }.attach()
    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {

            return when (position) {
                0 -> MovieFavoriteFragment()
                1 -> TvShowFavoriteFragment()
                else -> PersonFavoriteFragment()
            }
        }

        override fun getItemCount(): Int {
            return 3
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentFavoriteBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentFavoriteBinding = null
    }
}