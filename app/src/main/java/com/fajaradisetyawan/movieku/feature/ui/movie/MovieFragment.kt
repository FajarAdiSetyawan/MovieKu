/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.movie

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.feature.adapter.SliderMovieAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment() {
    private var fragmentMovieBinding: FragmentMovieBinding? = null
    private val binding get() = fragmentMovieBinding!!

    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieBinding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_primary)

        binding.apply {
            val adapter = SliderMovieAdapter()

            imageSlider.setSliderAdapter(adapter)
            imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
            imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            imageSlider.scrollTimeInSec = 3 //set scroll delay in seconds :
            imageSlider.indicatorSelectedColor = Color.WHITE
            imageSlider.indicatorUnselectedColor = Color.GRAY
            imageSlider.isAutoCycle = true
            imageSlider.startAutoCycle()

            viewModel.movieNowPlay.observe(viewLifecycleOwner) { movie ->
                if (movie!!.results.isNotEmpty()) {
                    adapter.setMovie(movie.results)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                }
            }


            adapter.setOnItemClickListener { movie ->
                val sendData =
                    MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }

            val pagerAdapterTrending = ViewPagerAdapterTrending(requireActivity())
            pagerTrendingMovie.adapter = pagerAdapterTrending
            binding.pagerTrendingMovie.isUserInputEnabled = false

            val pagerAdapterMovie = ViewPagerAdapterMovie(requireActivity())
            pagerMovie.adapter = pagerAdapterMovie
            pagerMovie.isUserInputEnabled = false

            TabLayoutMediator(tabLayoutTrendingMovie, pagerTrendingMovie) { tab, position ->
                val tabNames = listOf(resources.getString(R.string.today), resources.getString(R.string.week))
                tab.text = tabNames[position]
            }.attach()

            TabLayoutMediator(tabLayoutMovie, pagerMovie) { tab, position ->
                val tabNames = listOf(resources.getString(R.string.popular), resources.getString(R.string.top_rated), resources.getString(R.string.up_coming))
                tab.text = tabNames[position]
            }.attach()
        }
    }

    internal class ViewPagerAdapterTrending(
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DayTrendingMovieFragment()
                else -> WeekTrendingMovieFragment()
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    internal class ViewPagerAdapterMovie(
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> PopularMovieFragment()
                1 -> TopRatedMovieFragment()
                else -> UpComingMovieFragment()
            }
        }

        override fun getItemCount(): Int {
            return 3
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieBinding = null
    }
}