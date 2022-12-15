/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.search.keyword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.adapter.TvShowBigListPagingAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowByKeywordBinding
import com.fajaradisetyawan.movieku.ui.search.keyword.viewmodel.TvShowByKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowByKeywordFragment : Fragment() {
    private var fragmentTvShowByKeywordBinding : FragmentTvShowByKeywordBinding? = null
    private val binding get() = fragmentTvShowByKeywordBinding!!

    private val viewModel by viewModels<TvShowByKeywordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowByKeywordBinding = FragmentTvShowByKeywordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get genre id from activitySearchGenre
        val bundle: Bundle? = this.arguments
        val keywordId = bundle!!.getString("keywordId").toString()

        val adapter = TvShowBigListPagingAdapter()

        binding.apply {
            rvKeywordTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvKeywordTv.setHasFixedSize(true)
            rvKeywordTv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getTvByKeyword(keywordId)
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
            val sendData = KeywordFragmentDirections.actionKeywordFragmentToDetailTvFragment(tvShow.id)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerKeywordTv.visibility = View.GONE
            shimmerKeywordTv.stopShimmer()
            rvKeywordTv.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerKeywordTv.visibility = View.VISIBLE
            shimmerKeywordTv.startShimmer()
            rvKeywordTv.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerKeywordTv.visibility = View.GONE
            shimmerKeywordTv.stopShimmer()
            rvKeywordTv.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowByKeywordBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowByKeywordBinding = null
    }

}