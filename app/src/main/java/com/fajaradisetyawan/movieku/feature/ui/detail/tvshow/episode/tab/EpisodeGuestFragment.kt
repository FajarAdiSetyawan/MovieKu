/*
 * Created by Fajar Adi Setyawan on 16/1/2023 - 12:25:33
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
import com.fajaradisetyawan.movieku.databinding.FragmentEpisodeGuestBinding
import com.fajaradisetyawan.movieku.feature.adapter.GuestStarPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.EpisodeFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.tab.viewmodel.EpisodeGuestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeGuestFragment : Fragment() {
    private var fragmentEpisodeGuestBinding: FragmentEpisodeGuestBinding? = null
    private val binding get() = fragmentEpisodeGuestBinding!!

    private val viewModel by viewModels<EpisodeGuestViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEpisodeGuestBinding = FragmentEpisodeGuestBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val episode = bundle!!.getParcelable<Episode>("episode")

        val adapter = GuestStarPagingAdapter()

        binding.apply {
            rvGuestStar.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvGuestStar.setHasFixedSize(true)

            rvGuestStar.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsEpisode(episode)
            viewModel.guestStar.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingGuest()
                        if (adapter.itemCount < 1) {
                            tvGuestStarEmpty.visibility = View.VISIBLE
                        } else {
                            tvGuestStarEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingGuest()
                    }
                    is LoadState.Error -> {
                        showErrorGuest()
                        Toast.makeText(
                            requireActivity(),
                            state.error.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            adapter.setOnItemClickListener { cast ->
                val sendData = EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(cast.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun showLoadingGuest() {
        binding.apply {
            shimmerGuestStar.visibility = View.VISIBLE
            shimmerGuestStar.startShimmer()
            rvGuestStar.visibility = View.GONE
            failedLoadGuest.visibility = View.GONE
            tvGuestStarEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingGuest() {
        binding.apply {
            shimmerGuestStar.visibility = View.GONE
            shimmerGuestStar.stopShimmer()
            rvGuestStar.visibility = View.VISIBLE
            failedLoadGuest.visibility = View.GONE
            tvGuestStarEmpty.visibility = View.GONE
        }
    }

    private fun showErrorGuest() {
        binding.apply {
            shimmerGuestStar.visibility = View.GONE
            shimmerGuestStar.stopShimmer()
            rvGuestStar.visibility = View.GONE
            failedLoadGuest.visibility = View.VISIBLE
            tvGuestStarEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentEpisodeGuestBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEpisodeGuestBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}