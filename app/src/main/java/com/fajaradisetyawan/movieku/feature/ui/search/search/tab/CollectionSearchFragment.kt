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
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentCollectionSearchBinding
import com.fajaradisetyawan.movieku.feature.adapter.CollectionSearchPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.search.search.SearchFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.search.search.tab.viewmodel.CollectionSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionSearchFragment : Fragment() {
    private var fragmentCollectionSearchBinding: FragmentCollectionSearchBinding? = null
    private val binding get() = fragmentCollectionSearchBinding!!

    private val viewModel by viewModels<CollectionSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentCollectionSearchBinding = FragmentCollectionSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        val adapter = CollectionSearchPagingAdapter()

        binding.apply {
            rvSearchCollection.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearchCollection.setHasFixedSize(true)
            rvSearchCollection.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getSearchCollection(query!!)
        viewModel.collection.observe(viewLifecycleOwner){ collection ->
            adapter.submitData(viewLifecycleOwner.lifecycle, collection)
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

        adapter.setOnItemClickListener { collection ->
            val sendData = SearchFragmentDirections.actionSearchFragmentToCollectionFragment(collection)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerSearchCollection.visibility = View.GONE
            shimmerSearchCollection.stopShimmer()
            rvSearchCollection.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerSearchCollection.visibility = View.VISIBLE
            shimmerSearchCollection.startShimmer()
            rvSearchCollection.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerSearchCollection.visibility = View.GONE
            shimmerSearchCollection.stopShimmer()
            rvSearchCollection.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentCollectionSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCollectionSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}