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
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowSearchBinding
import com.fajaradisetyawan.movieku.feature.adapter.TvShowBigListPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.search.search.SearchFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.search.search.tab.viewmodel.TvShowSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowSearchFragment : Fragment() {
    private var fragmentTvShowSearchBinding: FragmentTvShowSearchBinding? = null
    private val binding get() = fragmentTvShowSearchBinding!!

    private val viewModel by viewModels<TvShowSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowSearchBinding = FragmentTvShowSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        val adapter = TvShowBigListPagingAdapter()

        binding.apply {
            rvSearchTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearchTv.setHasFixedSize(true)
            rvSearchTv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getSearchTvShow(query!!)
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
            val sendData = SearchFragmentDirections.actionSearchFragmentToDetailTvFragment(tvShow.id)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerSearchTv.visibility = View.GONE
            shimmerSearchTv.stopShimmer()
            rvSearchTv.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerSearchTv.visibility = View.VISIBLE
            shimmerSearchTv.startShimmer()
            rvSearchTv.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerSearchTv.visibility = View.GONE
            shimmerSearchTv.stopShimmer()
            rvSearchTv.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowSearchBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}