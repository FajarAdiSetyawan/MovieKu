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
import com.fajaradisetyawan.movieku.databinding.FragmentPeopleSearchBinding
import com.fajaradisetyawan.movieku.feature.adapter.PeopleSearchPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.search.search.SearchFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.search.search.tab.viewmodel.PeopleSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleSearchFragment : Fragment() {
    private var fragmentPeopleSearchBinding: FragmentPeopleSearchBinding? = null
    private val binding get() = fragmentPeopleSearchBinding!!

    private val viewModel by viewModels<PeopleSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPeopleSearchBinding = FragmentPeopleSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        val adapter = PeopleSearchPagingAdapter()

        binding.apply {
            rvSearchPeople.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearchPeople.setHasFixedSize(true)
            rvSearchPeople.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getSearchPeople(query!!)
        viewModel.people.observe(viewLifecycleOwner){ people ->
            adapter.submitData(viewLifecycleOwner.lifecycle, people)
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

        adapter.setOnItemClickListener { people ->
            val sendData = SearchFragmentDirections.actionSearchFragmentToDetailPeopleFragment(people.id)
            Navigation.findNavController(view).navigate(sendData)
        }

    }

    private fun hideLoading(){
        binding.apply {
            shimmerSearchPeople.visibility = View.GONE
            shimmerSearchPeople.stopShimmer()
            rvSearchPeople.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerSearchPeople.visibility = View.VISIBLE
            shimmerSearchPeople.startShimmer()
            rvSearchPeople.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerSearchPeople.visibility = View.GONE
            shimmerSearchPeople.stopShimmer()
            rvSearchPeople.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentPeopleSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPeopleSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}