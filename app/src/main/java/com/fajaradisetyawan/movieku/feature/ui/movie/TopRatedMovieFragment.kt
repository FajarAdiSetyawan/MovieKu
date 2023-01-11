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
import com.fajaradisetyawan.movieku.databinding.FragmentTopRatedMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel.TopRatedMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopRatedMovieFragment : Fragment() {
    private var fragmentTopRatedMovieBinding: FragmentTopRatedMovieBinding? = null
    private val binding get() = fragmentTopRatedMovieBinding!!

    private val viewModel by viewModels<TopRatedMovieViewModel>()

    private var adapter: MovieListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTopRatedMovieBinding = FragmentTopRatedMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvTopRatedMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTopRatedMovie.setHasFixedSize(true)

            adapter = MovieListSmallAdapter()
            rvTopRatedMovie.adapter = adapter

            shimmerTopRatedMovie.visibility = View.VISIBLE

            viewModel.topRatedMovie.observe(viewLifecycleOwner) { movie ->
                shimmerTopRatedMovie.visibility = View.GONE
                if (movie!!.results.isNotEmpty()) {
                    adapter!!.setMovie(movie.results)
                    adapter!!.notifyDataSetChanged()
                    rvTopRatedMovie.visibility = View.VISIBLE
                } else {
                    rvTopRatedMovie.visibility = View.INVISIBLE
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
        fragmentTopRatedMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTopRatedMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}