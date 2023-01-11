/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowFavoriteBinding
import com.fajaradisetyawan.movieku.feature.adapter.TvShowFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel.TvShowFavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowFavoriteFragment : Fragment() {
    private var fragmentTvShowFavoriteBinding: FragmentTvShowFavoriteBinding? = null
    private val binding get() = fragmentTvShowFavoriteBinding!!

    private val viewModel by viewModels<TvShowFavViewModel>()

    private var adapter : TvShowFavAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowFavoriteBinding = FragmentTvShowFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvFavTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFavTv.setHasFixedSize(true)

            adapter = TvShowFavAdapter()
            rvFavTv.adapter = adapter

            shimmerFavTv.visibility = View.VISIBLE

            viewModel.tvShow.observe(viewLifecycleOwner) { tvShow ->
                shimmerFavTv.visibility = View.GONE
                if (tvShow!!.isNotEmpty()) {
                    adapter!!.setTvShow(tvShow)
                    adapter!!.notifyDataSetChanged()
                    rvFavTv.visibility = View.VISIBLE
                } else {
                    rvFavTv.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }
            }

            adapter!!.setOnItemClickListener { tvShow ->
                val sendData =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailTvFragment(tvShow.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowFavoriteBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowFavoriteBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}