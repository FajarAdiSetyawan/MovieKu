/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.tvshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.TvShowListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentDayTrendingTvShowBinding
import com.fajaradisetyawan.movieku.feature.ui.tvshow.viewmodel.DayTrendingTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayTrendingTvShowFragment : Fragment() {
    private var fragmentDayTrendingTvShowBinding: FragmentDayTrendingTvShowBinding? = null
    private val binding get() = fragmentDayTrendingTvShowBinding!!

    private val viewModel by viewModels<DayTrendingTvViewModel>()

    private var adapter: TvShowListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDayTrendingTvShowBinding =
            FragmentDayTrendingTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvDayTrendingTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDayTrendingTv.setHasFixedSize(true)

            adapter = TvShowListSmallAdapter()
            rvDayTrendingTv.adapter = adapter

            shimmerDayTrendingTv.visibility = View.VISIBLE

            viewModel.trending.observe(viewLifecycleOwner) { trending ->
                shimmerDayTrendingTv.visibility = View.GONE
                if (trending!!.results.isNotEmpty()) {
                    adapter!!.setTvShow(trending.results)
                    adapter!!.notifyDataSetChanged()
                    rvDayTrendingTv.visibility = View.VISIBLE
                } else {
                    rvDayTrendingTv.visibility = View.INVISIBLE
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
        fragmentDayTrendingTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDayTrendingTvShowBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

}