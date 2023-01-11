/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.databinding.FragmentMovieFavoriteBinding
import com.fajaradisetyawan.movieku.feature.adapter.MovieFavAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel.MovieFavViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFavoriteFragment : Fragment() {
    private var fragmentMovieFavoriteBinding: FragmentMovieFavoriteBinding? = null
    private val binding get() = fragmentMovieFavoriteBinding!!

    private val viewModel by viewModels<MovieFavViewModel>()

    private var adapter : MovieFavAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieFavoriteBinding =
            FragmentMovieFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvFavMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFavMovie.setHasFixedSize(true)

            adapter = MovieFavAdapter()
            rvFavMovie.adapter = adapter

            shimmerFavMovie.visibility = View.VISIBLE

            viewModel.movies.observe(viewLifecycleOwner) { movie ->
                shimmerFavMovie.visibility = View.GONE
                if (movie!!.isNotEmpty()) {
                    adapter!!.setMovie(movie)
                    adapter!!.notifyDataSetChanged()
                    rvFavMovie.visibility = View.VISIBLE
                } else {
                    rvFavMovie.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }
            }

            adapter!!.setOnItemClickListener { movie ->
                val sendData =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieFavoriteBinding = null
        requireActivity().viewModelStore.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieFavoriteBinding = null
        requireActivity().viewModelStore.clear()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}