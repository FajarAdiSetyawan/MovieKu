/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.tvshow

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
import com.fajaradisetyawan.movieku.databinding.FragmentWeekTrendingTvShowBinding
import com.fajaradisetyawan.movieku.feature.ui.home.viewmodel.WeekTrendingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekTrendingTvShowFragment : Fragment() {
    private var fragmentWeekTrendingTvShowBinding: FragmentWeekTrendingTvShowBinding? = null
    private val binding get() = fragmentWeekTrendingTvShowBinding!!

    private val viewModel by viewModels<WeekTrendingViewModel>()

    private var adapter: TrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentWeekTrendingTvShowBinding = FragmentWeekTrendingTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvWeekTrendingTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvWeekTrendingTv.setHasFixedSize(true)

            adapter = TrendingAdapter()
            rvWeekTrendingTv.adapter = adapter

            shimmerWeekTrendingTv.visibility = View.VISIBLE

            viewModel.trending.observe(viewLifecycleOwner) { trending ->
                shimmerWeekTrendingTv.visibility = View.GONE
                if (trending!!.results.isNotEmpty()) {
                    adapter!!.setTrending(trending.results)
                    adapter!!.notifyDataSetChanged()
                    rvWeekTrendingTv.visibility = View.VISIBLE
                } else {
                    rvWeekTrendingTv.visibility = View.INVISIBLE
                }
            }



            adapter!!.setOnItemClickListener { trending ->
                val sendData =
                    TvShowFragmentDirections.actionTvShowFragmentToDetailTvFragment(trending.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentWeekTrendingTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWeekTrendingTvShowBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}