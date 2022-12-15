/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.fajaradisetyawan.movieku.adapter.PopularPeopleAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentPopularPersonBinding
import com.fajaradisetyawan.movieku.ui.people.viewmodel.PopularPersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularPersonFragment : Fragment() {
    private var fragmentPopularPersonBinding: FragmentPopularPersonBinding? = null
    private val binding get() = fragmentPopularPersonBinding!!

    private val viewModel by viewModels<PopularPersonViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPopularPersonBinding = FragmentPopularPersonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PopularPeopleAdapter()

        binding.apply {
            rvPopularPeople.layoutManager = GridLayoutManager(requireActivity(), 2)
            rvPopularPeople.setHasFixedSize(true)
            rvPopularPeople.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.popularPeople.observe(viewLifecycleOwner){ people ->
            adapter.submitData(viewLifecycleOwner.lifecycle, people)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoading()
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
            val sendData = PopularPersonFragmentDirections.actionPopularPersonFragmentToDetailPersonFragment(people.id)
            Navigation.findNavController(view).navigate(sendData)
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerPopularPeople.visibility = View.GONE
            shimmerPopularPeople.stopShimmer()
            rvPopularPeople.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerPopularPeople.visibility = View.VISIBLE
            shimmerPopularPeople.startShimmer()
            rvPopularPeople.visibility = View.GONE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerPopularPeople.visibility = View.GONE
            shimmerPopularPeople.stopShimmer()
            rvPopularPeople.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentPopularPersonBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPopularPersonBinding = null
    }


}