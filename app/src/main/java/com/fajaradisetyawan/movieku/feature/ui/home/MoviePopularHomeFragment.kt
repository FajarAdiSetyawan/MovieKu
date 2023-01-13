/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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
import com.fajaradisetyawan.movieku.feature.adapter.MovieListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentMoviePopularHomeBinding
import com.fajaradisetyawan.movieku.feature.ui.home.viewmodel.MoviePopularHomeViewModel
import com.fajaradisetyawan.movieku.utils.CheckConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviePopularHomeFragment : Fragment() {
    private var fragmentMoviePopularHomeBinding: FragmentMoviePopularHomeBinding? = null
    private val binding get() = fragmentMoviePopularHomeBinding!!

    private val viewModel by viewModels<MoviePopularHomeViewModel>()

    private var adapter: MovieListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMoviePopularHomeBinding =
            FragmentMoviePopularHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvMoviePopularHome.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvMoviePopularHome.setHasFixedSize(true)

            adapter = MovieListSmallAdapter()
            rvMoviePopularHome.adapter = adapter

            shimmerMoviePopularHome.visibility = View.VISIBLE

            if (CheckConnection.isInternetAvailable(requireActivity())) {
                //network request here
                getMovie()
            }

            adapter!!.setOnItemClickListener { movie ->
                val sendData =
                    HomeFragmentDirections.actionHomeFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getMovie(){
        viewModel.popularMovie.observe(viewLifecycleOwner) { movie ->
            binding.shimmerMoviePopularHome.visibility = View.GONE
            if (movie!!.results.isNotEmpty()) {
                adapter!!.setMovie(movie.results)
                adapter!!.notifyDataSetChanged()
                binding.rvMoviePopularHome.visibility = View.VISIBLE
            } else {
                binding.rvMoviePopularHome.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMoviePopularHomeBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMoviePopularHomeBinding = null
    }

    override fun onResume() {
        super.onResume()
        if (CheckConnection.isInternetAvailable(requireActivity())) {
            //network request here
            getMovie()
        }
        binding.root.requestLayout()

    }
}