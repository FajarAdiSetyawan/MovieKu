/*
 * Created by Fajar Adi Setyawan on 21/12/2022 - 11:38:13
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.CrewPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentAllCrewMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie.viewmodel.AllCrewMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCrewMovieFragment : Fragment() {
    private var fragmentAllCrewMovieBinding: FragmentAllCrewMovieBinding? = null
    private val binding get() = fragmentAllCrewMovieBinding!!

    private val viewModel by viewModels<AllCrewMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllCrewMovieBinding = FragmentAllCrewMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val movieId = bundle!!.getInt("movieId")

        allCrewMovie(movieId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allCrewMovie(idMovie: Int) {
        val adapter = CrewPagingAdapter()

        binding.apply {
            rvAllCrew.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvAllCrew.setHasFixedSize(true)

            rvAllCrew.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getMovieCredits(idMovie)
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
                    AllPeopleMovieFragmentDirections.actionAllPeopleMovieFragmentToDetailPeopleFragment(
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
        fragmentAllCrewMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllCrewMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}