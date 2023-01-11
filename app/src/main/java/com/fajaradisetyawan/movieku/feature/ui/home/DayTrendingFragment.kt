/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:39
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.TrendingAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentDayTrendingBinding
import com.fajaradisetyawan.movieku.feature.ui.home.viewmodel.DayTrendingViewModel
import com.fajaradisetyawan.movieku.utils.CheckConnection
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DayTrendingFragment : Fragment() {
    private var fragmentDayTrendingBinding: FragmentDayTrendingBinding? = null
    private val binding get() = fragmentDayTrendingBinding!!

    private val viewModel by viewModels<DayTrendingViewModel>()

    private var adapter: TrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDayTrendingBinding =
            FragmentDayTrendingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvDayTrending.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDayTrending.setHasFixedSize(true)

            adapter = TrendingAdapter()
            rvDayTrending.adapter = adapter

            shimmerTendingDay.visibility = View.VISIBLE

            if (CheckConnection.isInternetAvailable(requireActivity())) {
                //network request here
                getTrending()
            }

            adapter!!.setOnItemClickListener { trending ->
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

    @SuppressLint("NotifyDataSetChanged")
    private fun getTrending(){
        viewModel.trending.observe(viewLifecycleOwner) { trending ->
            binding.shimmerTendingDay.visibility = View.GONE
            if (trending!!.results.isNotEmpty()) {
                adapter!!.setTrending(trending.results)
                adapter!!.notifyDataSetChanged()
                binding.rvDayTrending.visibility = View.VISIBLE
            } else {
                binding.rvDayTrending.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDayTrendingBinding = null
    }



    override fun onDestroy() {
        super.onDestroy()
        fragmentDayTrendingBinding = null
    }


    override fun onResume() {
        super.onResume()
        if (CheckConnection.isInternetAvailable(requireActivity())) {
            //network request here
            getTrending()
        }
        binding.root.requestLayout()
    }
}