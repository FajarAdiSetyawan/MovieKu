/*
 * Created by Fajar Adi Setyawan on 7/2/2023 - 11:57:37
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.watchlist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentWatchListBinding
import com.fajaradisetyawan.movieku.feature.ui.favorite.FavoriteFragment
import com.fajaradisetyawan.movieku.feature.ui.favorite.MovieFavoriteFragment
import com.fajaradisetyawan.movieku.feature.ui.favorite.PersonFavoriteFragment
import com.fajaradisetyawan.movieku.feature.ui.favorite.TvShowFavoriteFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListFragment : Fragment() {
    private var fragmentWatchListBinding: FragmentWatchListBinding? = null
    private val binding get() = fragmentWatchListBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentWatchListBinding = FragmentWatchListBinding.inflate(layoutInflater, container, false)
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

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.color_primary)
        val nav: Drawable = binding.toolbar.navigationIcon!!
        nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))

        val adapterWatchList = ViewPagerAdapter(requireActivity(), "")
        binding.pagerWatchList.adapter = adapterWatchList

        TabLayoutMediator(binding.tabWatchList, binding.pagerWatchList) { tab, position ->
            val tabNames =
                listOf(
                    resources.getString(R.string.movie),
                    resources.getString(R.string.tvshow),
                )
            tab.text = tabNames[position]
        }.attach()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val adapterWatchList = ViewPagerAdapter(requireActivity(), query)
                    binding.pagerWatchList.adapter = adapterWatchList
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    val adapterWatchList =
                       ViewPagerAdapter(requireActivity(), query)
                    binding.pagerWatchList.adapter = adapterWatchList

                }
                return true
            }
        })
    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val query: String
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString("query", query)

            val movieFav = WatchListMovieFragment()
            val tvFav = WatchListTvFragment()

            movieFav.arguments = bundle
            tvFav.arguments = bundle

            return when (position) {
                0 -> movieFav
                else -> tvFav
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentWatchListBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWatchListBinding = null
    }


}