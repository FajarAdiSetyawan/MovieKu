/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:39
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.TrendingAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentWeekTrendingBinding
import com.fajaradisetyawan.movieku.feature.ui.home.viewmodel.WeekTrendingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekTrendingFragment : Fragment() {
    private var fragmentWeekTrendingBinding: FragmentWeekTrendingBinding? = null
    private val binding get() = fragmentWeekTrendingBinding!!

    private val viewModel by viewModels<WeekTrendingViewModel>()

    private var trendingAdapter: TrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentWeekTrendingBinding =
            FragmentWeekTrendingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvWeekTrending.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvWeekTrending.setHasFixedSize(true)

            trendingAdapter = TrendingAdapter()
            rvWeekTrending.adapter = trendingAdapter

            shimmerTendingWeek.visibility = View.VISIBLE

            viewModel.trending.observe(viewLifecycleOwner) { trending ->
                shimmerTendingWeek.visibility = View.GONE
                if (trending!!.results.isNotEmpty()) {
                    trendingAdapter!!.setTrending(trending.results)
                    trendingAdapter!!.notifyDataSetChanged()
                    rvWeekTrending.visibility = View.VISIBLE
                } else {
                    rvWeekTrending.visibility = View.INVISIBLE
                }
            }



            trendingAdapter!!.setOnItemClickListener { trending ->
                when (trending.mediaType) {
                    "movie" -> {
                        val sendData = HomeFragmentDirections.actionHomeFragmentToDetailMovieFragment(trending.id)
                        Navigation.findNavController(view).navigate(sendData)
                    }
                    else -> {
                        val sendData =
                            HomeFragmentDirections.actionHomeFragmentToDetailTvFragment(trending.id)
                        Navigation.findNavController(view).navigate(sendData)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWeekTrendingBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentWeekTrendingBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}