/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.search.genre

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
import com.fajaradisetyawan.movieku.adapter.TvShowBigListPagingAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowByGenreBinding
import com.fajaradisetyawan.movieku.ui.search.genre.viewmodel.TvShowByGenreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowByGenreFragment : Fragment() {
    private var fragmentTvShowByGenreBinding: FragmentTvShowByGenreBinding? = null
    private val binding get() = fragmentTvShowByGenreBinding!!

    private val viewModel by viewModels<TvShowByGenreViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowByGenreBinding = FragmentTvShowByGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get genre id from activitySearchGenre
        val bundle: Bundle? = this.arguments
        val genreId = bundle!!.getString("genreId").toString()

        val adapter = TvShowBigListPagingAdapter()

        binding.apply {
            rvGenreTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvGenreTv.setHasFixedSize(true)
            rvGenreTv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getTvByGenre(genreId)
        viewModel.tvShow.observe(viewLifecycleOwner){ tvShow ->
            adapter.submitData(viewLifecycleOwner.lifecycle, tvShow)
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

        adapter.setOnItemClickListener { tvShow ->
            val sendData = GenreFragmentDirections.actionGenreFragmentToDetailTvFragment(tvShow.id)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerGenreTv.visibility = View.GONE
            shimmerGenreTv.stopShimmer()
            rvGenreTv.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerGenreTv.visibility = View.VISIBLE
            shimmerGenreTv.startShimmer()
            rvGenreTv.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerGenreTv.visibility = View.GONE
            shimmerGenreTv.stopShimmer()
            rvGenreTv.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowByGenreBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowByGenreBinding = null
    }
}