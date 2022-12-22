/*
 * Created by Fajar Adi Setyawan on 21/12/2022 - 11:39:18
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.people.allPeople.tv

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
import com.fajaradisetyawan.movieku.adapter.CrewPagingAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentAllCrewTvBinding
import com.fajaradisetyawan.movieku.ui.detail.people.allPeople.tv.viewmodel.AllCrewTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCrewTvFragment : Fragment() {
    private var fragmentAllCrewTvBinding: FragmentAllCrewTvBinding? = null
    private val binding get() = fragmentAllCrewTvBinding!!

    private val viewModel by viewModels<AllCrewTvViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllCrewTvBinding = FragmentAllCrewTvBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val tvShowId = bundle!!.getInt("tvShowId")

        allCrewTvShow(tvShowId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allCrewTvShow(idTvShow: Int){
        val adapter = CrewPagingAdapter()

        binding.apply {
            rvAllCrew.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvAllCrew.setHasFixedSize(true)

            rvAllCrew.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getTvCredits(idTvShow)
            viewModel.crew.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingCrew()
                        if (adapter.itemCount < 1) {
                            tvCrewEmpty.visibility = View.VISIBLE
                        } else {
                            tvCrewEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingCrew()
                    }
                    is LoadState.Error -> {
                        showErrorCrew()
                        Toast.makeText(
                            requireActivity(),
                            state.error.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            adapter.setOnItemClickListener { crew ->
                val sendData = AllPeopleTvShowFragmentDirections.actionAllPeopleTvShowFragmentToDetailPeopleFragment(
                        crew.id
                    )
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun showLoadingCrew() {
        binding.apply {
            shimmerCrew.visibility = View.VISIBLE
            shimmerCrew.startShimmer()
            rvAllCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCrew() {
        binding.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvAllCrew.visibility = View.VISIBLE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCrew() {
        binding.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvAllCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.VISIBLE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAllCrewTvBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllCrewTvBinding = null
    }
}