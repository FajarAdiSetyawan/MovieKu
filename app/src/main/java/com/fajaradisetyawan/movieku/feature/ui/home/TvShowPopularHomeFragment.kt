/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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
import com.fajaradisetyawan.movieku.feature.adapter.TvShowListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowPopularHomeBinding
import com.fajaradisetyawan.movieku.feature.ui.home.viewmodel.TvShowPopularHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowPopularHomeFragment : Fragment() {
    private var fragmentTvShowPopularHomeBinding: FragmentTvShowPopularHomeBinding? = null
    private val binding get() = fragmentTvShowPopularHomeBinding!!

    private val viewModel by viewModels<TvShowPopularHomeViewModel>()

    private var adapter: TvShowListSmallAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowPopularHomeBinding = FragmentTvShowPopularHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvTvPopularHome.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTvPopularHome.setHasFixedSize(true)

            adapter = TvShowListSmallAdapter()
            rvTvPopularHome.adapter = adapter

            shimmerTvPopularHome.visibility = View.VISIBLE

            viewModel.popularTvShow.observe(viewLifecycleOwner) { tvShow ->
                shimmerTvPopularHome.visibility = View.GONE
                if (tvShow!!.results.isNotEmpty()) {
                    adapter!!.setTvShow(tvShow.results)
                    adapter!!.notifyDataSetChanged()
                    rvTvPopularHome.visibility = View.VISIBLE
                } else {
                    rvTvPopularHome.visibility = View.INVISIBLE
                }
            }



            adapter!!.setOnItemClickListener { tvShow ->
                val sendData =
                    HomeFragmentDirections.actionHomeFragmentToDetailTvFragment(tvShow.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowPopularHomeBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowPopularHomeBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}