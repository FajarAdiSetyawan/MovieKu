/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.keyword

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
import com.fajaradisetyawan.movieku.feature.adapter.MovieBigListPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentMovieByKeywordBinding
import com.fajaradisetyawan.movieku.feature.ui.search.keyword.viewmodel.MovieByKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieByKeywordFragment : Fragment() {
    private var fragmentMovieByKeywordBinding: FragmentMovieByKeywordBinding? = null
    private val binding get() = fragmentMovieByKeywordBinding!!

    private val viewModel by viewModels<MovieByKeywordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieByKeywordBinding = FragmentMovieByKeywordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get genre id from activitySearchGenre
        val bundle: Bundle? = this.arguments
        val keywordId = bundle!!.getString("keywordId").toString()

        val adapter = MovieBigListPagingAdapter()

        binding.apply {
            rvKeywordMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvKeywordMovie.setHasFixedSize(true)
            rvKeywordMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getMovieByKeyword(keywordId)
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
            val sendData = KeywordFragmentDirections.actionKeywordFragmentToDetailMovieFragment(movie.id)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerKeywordMovie.visibility = View.GONE
            shimmerKeywordMovie.stopShimmer()
            rvKeywordMovie.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerKeywordMovie.visibility = View.VISIBLE
            shimmerKeywordMovie.startShimmer()
            rvKeywordMovie.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerKeywordMovie.visibility = View.GONE
            shimmerKeywordMovie.stopShimmer()
            rvKeywordMovie.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieByKeywordBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieByKeywordBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}