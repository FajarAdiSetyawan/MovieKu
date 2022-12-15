/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentFavoriteBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var fragmentFavoriteBinding: FragmentFavoriteBinding? = null
    private val binding get() = fragmentFavoriteBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.md_blue_500)

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