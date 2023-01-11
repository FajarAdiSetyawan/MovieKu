/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.tvshow

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.feature.adapter.SliderTvShowAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowBinding
import com.fajaradisetyawan.movieku.feature.ui.tvshow.viewmodel.TvShowViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowFragment : Fragment() {
    private var fragmentTvShowBinding: FragmentTvShowBinding? = null
    private val binding get() = fragmentTvShowBinding!!

    private val viewModel by viewModels<TvShowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowBinding = FragmentTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val adapter = SliderTvShowAdapter()

            imageSlider.setSliderAdapter(adapter)
            imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
            imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            imageSlider.scrollTimeInSec = 3 //set scroll delay in seconds :
            imageSlider.indicatorSelectedColor = Color.WHITE
            imageSlider.indicatorUnselectedColor = Color.GRAY
            imageSlider.isAutoCycle = true
            imageSlider.startAutoCycle()

            viewModel.tvShow.observe(viewLifecycleOwner) { tvShow ->
                if (tvShow!!.results.isNotEmpty()) {
                    adapter.setTvShow(tvShow.results)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                }
            }


            adapter.setOnItemClickListener { tv ->
                val sendData =
                    TvShowFragmentDirections.actionTvShowFragmentToDetailTvFragment(tv.id)
                Navigation.findNavController(view).navigate(sendData)
            }

            val pagerAdapterTrending = ViewPagerAdapterTrending(requireActivity())
            pagerTrendingTvShow.adapter = pagerAdapterTrending
            pagerTrendingTvShow.isUserInputEnabled = false

            val pagerAdapterTv = ViewPagerAdapterTvShow(requireActivity())
            pagerTvShow.adapter = pagerAdapterTv
            pagerTvShow.isUserInputEnabled = false

            TabLayoutMediator(tabLayoutTrendingTvShow, pagerTrendingTvShow) { tab, position ->
                val tabNames = listOf(resources.getString(R.string.today), resources.getString(R.string.week))
                tab.text = tabNames[position]
            }.attach()

            TabLayoutMediator(tabLayoutTvShow, pagerTvShow) { tab, position ->
                val tabNames = listOf(resources.getString(R.string.on_air), resources.getString(R.string.popular), resources.getString(R.string.top_rated))
                tab.text = tabNames[position]
            }.attach()
        }
    }

    internal class ViewPagerAdapterTrending(
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DayTrendingTvShowFragment()
                else -> WeekTrendingTvShowFragment()
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    internal class ViewPagerAdapterTvShow(
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OnTvShowFragment()
                1 -> PopularTvShowFragment()
                else -> TopRatedTvShowFragment()
            }
        }

        override fun getItemCount(): Int {
            return 3
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowBinding = null
    }
}