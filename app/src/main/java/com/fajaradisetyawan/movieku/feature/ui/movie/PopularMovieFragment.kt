/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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
import com.fajaradisetyawan.movieku.databinding.FragmentPopularMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel.PopularMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMovieFragment : Fragment() {
    private var fragmentPopularMovieBinding: FragmentPopularMovieBinding? = null
    private val binding get() = fragmentPopularMovieBinding!!

    private val viewModel by viewModels<PopularMovieViewModel>()

    private var adapter: MovieListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPopularMovieBinding =
            FragmentPopularMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvPopularMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPopularMovie.setHasFixedSize(true)

            adapter = MovieListSmallAdapter()
            rvPopularMovie.adapter = adapter

            shimmerPopularMovie.visibility = View.VISIBLE

            viewModel.moviePopular.observe(viewLifecycleOwner) { movie ->
                shimmerPopularMovie.visibility = View.GONE
                if (movie!!.results.isNotEmpty()) {
                    adapter!!.setMovie(movie.results)
                    adapter!!.notifyDataSetChanged()
                    rvPopularMovie.visibility = View.VISIBLE
                } else {
                    rvPopularMovie.visibility = View.INVISIBLE
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
        fragmentPopularMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPopularMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}