/*
 * Created by Fajar Adi Setyawan on 21/12/2022 - 11:39:38
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.tv

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
import com.fajaradisetyawan.movieku.databinding.FragmentAllCastTvBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.tv.viewmodel.AllCastTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCastTvFragment : Fragment() {
    private var fragmentAllCastTvBinding: FragmentAllCastTvBinding? = null
    private val binding get() = fragmentAllCastTvBinding!!

    private val viewModel by viewModels<AllCastTvViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllCastTvBinding =
            FragmentAllCastTvBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val tvShowId = bundle!!.getInt("tvShowId")

        allCastTvShow(tvShowId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allCastTvShow(idTvShow: Int) {
        val adapter = CastPagingAdapter()

        binding.apply {
            rvAllCast.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvAllCast.setHasFixedSize(true)

            rvAllCast.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getTvCredits(idTvShow)
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
                val sendData = AllPeopleTvShowFragmentDirections.actionAllPeopleTvShowFragmentToDetailPeopleFragment(
                        cast.id
                    )
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
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
        fragmentAllCastTvBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllCastTvBinding = null

    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}