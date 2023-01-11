/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:39
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.MovieListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentDayTrendingMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel.DayTrendingMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayTrendingMovieFragment : Fragment() {
    private var fragmentDayTrendingMovieBinding: FragmentDayTrendingMovieBinding? = null
    private val binding get() = fragmentDayTrendingMovieBinding!!

    private val viewModel by viewModels<DayTrendingMovieViewModel>()

    private var adapter: MovieListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDayTrendingMovieBinding = FragmentDayTrendingMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvDayTrendingMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDayTrendingMovie.setHasFixedSize(true)

            adapter = MovieListSmallAdapter()
            rvDayTrendingMovie.adapter = adapter

            shimmerDayTrendingMovie.visibility = View.VISIBLE

            viewModel.trending.observe(viewLifecycleOwner) { movie ->
                shimmerDayTrendingMovie.visibility = View.GONE
                if (movie!!.results.isNotEmpty()) {
                    adapter!!.setMovie(movie.results)
                    adapter!!.notifyDataSetChanged()
                    rvDayTrendingMovie.visibility = View.VISIBLE
                } else {
                    rvDayTrendingMovie.visibility = View.INVISIBLE
                }
            }



            adapter!!.setOnItemClickListener { movie ->
                val sendData =
                    MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDayTrendingMovieBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentDayTrendingMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}