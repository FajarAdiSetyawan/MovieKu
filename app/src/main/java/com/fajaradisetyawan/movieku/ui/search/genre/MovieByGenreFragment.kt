/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.search.genre

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.adapter.MovieBigListPagingAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentMovieByGenreBinding
import com.fajaradisetyawan.movieku.ui.search.genre.viewmodel.MovieByGenreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieByGenreFragment : Fragment() {
    private var fragmentMovieByGenreBinding: FragmentMovieByGenreBinding? = null
    private val binding get() = fragmentMovieByGenreBinding!!

    private val viewModel by viewModels<MovieByGenreViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieByGenreBinding = FragmentMovieByGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get genre id from activitySearchGenre
        val bundle: Bundle? = this.arguments
        val genreId = bundle!!.getString("genreId").toString()

        val adapter = MovieBigListPagingAdapter()

        binding.apply {
            rvGenreMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvGenreMovie.setHasFixedSize(true)
            rvGenreMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getMovieByGenre(genreId)
        viewModel.movies.observe(viewLifecycleOwner){ movies ->
            adapter.submitData(viewLifecycleOwner.lifecycle, movies)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoading()
                        if (adapter.itemCount < 1){
                            binding.layoutEmpty.visibility = View.VISIBLE
                        }else{
                            binding.layoutEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoading()
                    }
                    is LoadState.Error -> {
                        showError()
                        Toast.makeText(requireActivity(), state.error.message.orEmpty(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        adapter.setOnItemClickListener { movie ->
            val sendData = GenreFragmentDirections.actionGenreFragmentToDetailMovieFragment(movie.id)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerGenreMovie.visibility = View.GONE
            shimmerGenreMovie.stopShimmer()
            rvGenreMovie.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerGenreMovie.visibility = View.VISIBLE
            shimmerGenreMovie.startShimmer()
            rvGenreMovie.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerGenreMovie.visibility = View.GONE
            shimmerGenreMovie.stopShimmer()
            rvGenreMovie.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieByGenreBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieByGenreBinding = null
    }

}