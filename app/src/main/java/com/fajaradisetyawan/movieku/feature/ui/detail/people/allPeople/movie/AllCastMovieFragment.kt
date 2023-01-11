/*
 * Created by Fajar Adi Setyawan on 21/12/2022 - 11:38:28
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
import com.fajaradisetyawan.movieku.feature.adapter.CastPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentAllCastMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie.viewmodel.AllCastMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCastMovieFragment : Fragment() {
    private var fragmentAllCastMovieBinding: FragmentAllCastMovieBinding? = null
    private val binding get() = fragmentAllCastMovieBinding!!

    private val viewModel by viewModels<AllCastMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllCastMovieBinding = FragmentAllCastMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val movieId = bundle!!.getInt("movieId")

        val adapter = CastPagingAdapter()

        binding.apply {
            rvAllCast.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvAllCast.setHasFixedSize(true)

            rvAllCast.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getMovieCredits(movieId)
            viewModel.cast.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingCast()
                        if (adapter.itemCount < 1) {
                            tvCastEmpty.visibility = View.VISIBLE
                        } else {
                            tvCastEmpty.visibility = View.GONE
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
                val sendData =
                    AllPeopleMovieFragmentDirections.actionAllPeopleMovieFragmentToDetailPeopleFragment(
                        cast.id
                    )
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allCastMovie(idMovie: Int) {

    }

    private fun showLoadingCast() {
        binding.apply {
            shimmerCast.visibility = View.VISIBLE
            shimmerCast.startShimmer()
            rvAllCast.visibility = View.GONE
            failedLoadCast.visibility = View.GONE
            tvCastEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCast() {
        binding.apply {
            shimmerCast.visibility = View.GONE
            shimmerCast.stopShimmer()
            rvAllCast.visibility = View.VISIBLE
            failedLoadCast.visibility = View.GONE
            tvCastEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCast() {
        binding.apply {
            shimmerCast.visibility = View.GONE
            shimmerCast.stopShimmer()
            rvAllCast.visibility = View.GONE
            failedLoadCast.visibility = View.VISIBLE
            tvCastEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAllCastMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllCastMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}