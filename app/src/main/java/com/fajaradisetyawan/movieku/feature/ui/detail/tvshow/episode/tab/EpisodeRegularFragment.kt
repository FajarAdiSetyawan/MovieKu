/*
 * Created by Fajar Adi Setyawan on 16/1/2023 - 12:24:47
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
import com.fajaradisetyawan.movieku.databinding.FragmentEpisodeRegularBinding
import com.fajaradisetyawan.movieku.feature.adapter.CastPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.EpisodeFragmentDirections
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.tab.viewmodel.EpisodeRegularViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeRegularFragment : Fragment() {
    private var fragmentEpisodeRegularBinding: FragmentEpisodeRegularBinding? = null
    private val binding get() = fragmentEpisodeRegularBinding!!

    private val viewModel by viewModels<EpisodeRegularViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEpisodeRegularBinding = FragmentEpisodeRegularBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = this.arguments
        val episode = bundle!!.getParcelable<Episode>("episode")

        val adapter = CastPagingAdapter()

        binding.apply {
            rvRegular.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvRegular.setHasFixedSize(true)

            rvRegular.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsEpisode(episode)
            viewModel.cast.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingCast()
                        if (adapter.itemCount < 1) {
                            tvRegularEmpty.visibility = View.VISIBLE
                        } else {
                            tvRegularEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingCast()
                    }
                    is LoadState.Error -> {
                        showErrorCast()
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

    private fun showLoadingCast() {
        binding.apply {
            shimmerRegular.visibility = View.VISIBLE
            shimmerRegular.startShimmer()
            rvRegular.visibility = View.GONE
            failedLoadRegular.visibility = View.GONE
            tvRegularEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCast() {
        binding.apply {
            shimmerRegular.visibility = View.GONE
            shimmerRegular.stopShimmer()
            rvRegular.visibility = View.VISIBLE
            failedLoadRegular.visibility = View.GONE
            tvRegularEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCast() {
        binding.apply {
            shimmerRegular.visibility = View.GONE
            shimmerRegular.stopShimmer()
            rvRegular.visibility = View.GONE
            failedLoadRegular.visibility = View.VISIBLE
            tvRegularEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEpisodeRegularBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentEpisodeRegularBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}