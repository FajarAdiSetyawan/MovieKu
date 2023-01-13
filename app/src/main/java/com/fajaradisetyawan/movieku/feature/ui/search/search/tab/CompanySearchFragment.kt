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
import com.fajaradisetyawan.movieku.databinding.FragmentCompanySearchBinding
import com.fajaradisetyawan.movieku.feature.adapter.CompanySearchPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.search.search.SearchFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.search.search.tab.viewmodel.CompanySearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanySearchFragment : Fragment() {
    private var fragmentCompanySearchBinding: FragmentCompanySearchBinding? = null
    private val binding get() = fragmentCompanySearchBinding!!

    private val viewModel by viewModels<CompanySearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentCompanySearchBinding = FragmentCompanySearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        val adapter = CompanySearchPagingAdapter()

        binding.apply {
            rvSearchCompany.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearchCompany.setHasFixedSize(true)
            rvSearchCompany.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getSearchCompany(query!!)
        viewModel.company.observe(viewLifecycleOwner){ company ->
            adapter.submitData(viewLifecycleOwner.lifecycle, company)
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

        adapter.setOnItemClickListener { company ->
            val sendData = SearchFragmentDirections.actionSearchFragmentToDetailCompanyFragment(company)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerSearchCompany.visibility = View.GONE
            shimmerSearchCompany.stopShimmer()
            rvSearchCompany.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerSearchCompany.visibility = View.VISIBLE
            shimmerSearchCompany.startShimmer()
            rvSearchCompany.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerSearchCompany.visibility = View.GONE
            shimmerSearchCompany.stopShimmer()
            rvSearchCompany.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCompanySearchBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentCompanySearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}