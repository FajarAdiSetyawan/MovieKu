/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:39
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.movie

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
import com.fajaradisetyawan.movieku.databinding.FragmentWeekTrendingMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel.WeekTrendingMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekTrendingMovieFragment : Fragment() {
    private var fragmentWeekTrendingMovieBinding: FragmentWeekTrendingMovieBinding? = null
    private val binding get() = fragmentWeekTrendingMovieBinding!!

    private val viewModel by viewModels<WeekTrendingMovieViewModel>()

    private var adapter: MovieListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentWeekTrendingMovieBinding =
            FragmentWeekTrendingMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvWeekTrendingMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvWeekTrendingMovie.setHasFixedSize(true)

            adapter = MovieListSmallAdapter()
            rvWeekTrendingMovie.adapter = adapter

            shimmerWeekTrendingMovie.visibility = View.VISIBLE

            viewModel.trending.observe(viewLifecycleOwner) { movie ->
                shimmerWeekTrendingMovie.visibility = View.GONE
                if (movie!!.results.isNotEmpty()) {
                    adapter!!.setMovie(movie.results)
                    adapter!!.notifyDataSetChanged()
                    rvWeekTrendingMovie.visibility = View.VISIBLE
                } else {
                    rvWeekTrendingMovie.visibility = View.INVISIBLE
                }
            }

            adapter!!.setOnItemClickListener { movie ->
                val sendData =
                    MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentWeekTrendingMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWeekTrendingMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}