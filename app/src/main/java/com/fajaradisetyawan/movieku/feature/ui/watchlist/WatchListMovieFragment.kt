/*
 * Created by Fajar Adi Setyawan on 7/2/2023 - 11:58:8
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.watchlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.databinding.FragmentWatchListMovieBinding
import com.fajaradisetyawan.movieku.feature.adapter.MovieFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.watchlist.viewmodel.WatchListMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListMovieFragment : Fragment() {
    private var fragmentWatchListMovieBinding: FragmentWatchListMovieBinding? = null
    private val binding get() = fragmentWatchListMovieBinding!!

    private val viewModel by viewModels<WatchListMovieViewModel>()

    private val adapter: MovieFavAdapter by lazy { MovieFavAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentWatchListMovieBinding = FragmentWatchListMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")


        binding.apply {
            rvWlMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvWlMovie.setHasFixedSize(true)

            rvWlMovie.adapter = adapter

            shimmerWlMovie.visibility = View.VISIBLE
            viewModel.searchWatchList("%$query%")

            viewModel.movie.observe(viewLifecycleOwner) { movie ->
                shimmerWlMovie.visibility = View.GONE
                if (movie!!.isNotEmpty()) {
                    adapter.setMovie(movie)
                    adapter.notifyDataSetChanged()
                    rvWlMovie.visibility = View.VISIBLE
                } else {
                    rvWlMovie.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }
            }

            adapter.setOnItemClickListener { movie ->
                val sendData =
                    WatchListFragmentDirections.actionWatchListFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentWatchListMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWatchListMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}