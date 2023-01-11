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
import com.fajaradisetyawan.movieku.databinding.FragmentUpComingMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel.UpComingMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpComingMovieFragment : Fragment() {
    private var fragmentUpComingMovieBinding: FragmentUpComingMovieBinding? = null
    private val binding get() = fragmentUpComingMovieBinding!!

    private val viewModel by viewModels<UpComingMovieViewModel>()

    private var adapter: MovieListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentUpComingMovieBinding =
            FragmentUpComingMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvUpComingMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvUpComingMovie.setHasFixedSize(true)

            adapter = MovieListSmallAdapter()
            rvUpComingMovie.adapter = adapter

            shimmerUpComingMovie.visibility = View.VISIBLE

            viewModel.upComingMovie.observe(viewLifecycleOwner) { movie ->
                shimmerUpComingMovie.visibility = View.GONE
                if (movie!!.results.isNotEmpty()) {
                    adapter!!.setMovie(movie.results)
                    adapter!!.notifyDataSetChanged()
                    rvUpComingMovie.visibility = View.VISIBLE
                } else {
                    rvUpComingMovie.visibility = View.INVISIBLE
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
        fragmentUpComingMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentUpComingMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}