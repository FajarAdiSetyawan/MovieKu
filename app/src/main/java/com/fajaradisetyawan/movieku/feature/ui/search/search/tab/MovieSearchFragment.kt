/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search.tab

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
import com.fajaradisetyawan.movieku.databinding.FragmentMovieSearchBinding
import com.fajaradisetyawan.movieku.feature.adapter.MovieBigListPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.search.search.SearchFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.search.search.tab.viewmodel.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchFragment : Fragment() {
    private var fragmentMovieSearchBinding: FragmentMovieSearchBinding? = null
    private val binding get() = fragmentMovieSearchBinding!!

    private val viewModel by viewModels<MovieSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieSearchBinding = FragmentMovieSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        val adapter = MovieBigListPagingAdapter()

        binding.apply {
            rvSearchMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearchMovie.setHasFixedSize(true)
            rvSearchMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getSearchMovie(query!!)
        viewModel.movie.observe(viewLifecycleOwner){ movies ->
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
            val sendData = SearchFragmentDirections.actionSearchFragmentToDetailMovieFragment(movie.id)
            Navigation.findNavController(view).navigate(sendData)
        }

    }

    private fun hideLoading(){
        binding.apply {
            shimmerSearchMovie.visibility = View.GONE
            shimmerSearchMovie.stopShimmer()
            rvSearchMovie.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerSearchMovie.visibility = View.VISIBLE
            shimmerSearchMovie.startShimmer()
            rvSearchMovie.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerSearchMovie.visibility = View.GONE
            shimmerSearchMovie.stopShimmer()
            rvSearchMovie.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}