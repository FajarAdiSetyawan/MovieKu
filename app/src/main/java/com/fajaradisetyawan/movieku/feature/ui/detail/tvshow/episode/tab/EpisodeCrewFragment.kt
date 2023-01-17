/*
 * Created by Fajar Adi Setyawan on 16/1/2023 - 12:24:17
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.tab

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
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.databinding.FragmentEpisodeCrewBinding
import com.fajaradisetyawan.movieku.feature.adapter.CrewPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.EpisodeFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.tab.viewmodel.EpisodeCrewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeCrewFragment : Fragment() {
    private var fragmentEpisodeCrewBinding: FragmentEpisodeCrewBinding? = null
    private val binding get() = fragmentEpisodeCrewBinding!!

    private val viewModel by viewModels<EpisodeCrewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEpisodeCrewBinding = FragmentEpisodeCrewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val episode = bundle!!.getParcelable<Episode>("episode")

        val adapter = CrewPagingAdapter()

        binding.apply {
            rvCrew.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvCrew.setHasFixedSize(true)

            rvCrew.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsEpisode(episode)
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
                val sendData =
                    EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(crew.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun showLoadingCrew() {
        binding.apply {
            shimmerCrew.visibility = View.VISIBLE
            shimmerCrew.startShimmer()
            rvCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCrew() {
        binding.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvCrew.visibility = View.VISIBLE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCrew() {
        binding.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.VISIBLE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEpisodeCrewBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentEpisodeCrewBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

}