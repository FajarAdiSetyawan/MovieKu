/*
 * Created by Fajar Adi Setyawan on 7/2/2023 - 11:58:25
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.watchlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.databinding.FragmentWatchListTvBinding
import com.fajaradisetyawan.movieku.feature.adapter.TvShowFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.watchlist.viewmodel.WatchListTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListTvFragment : Fragment() {
    private var fragmentWatchListTvBinding: FragmentWatchListTvBinding? = null
    private val binding get() = fragmentWatchListTvBinding!!

    private val viewModel by viewModels<WatchListTvViewModel>()

    private var adapter : TvShowFavAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        fragmentWatchListTvBinding = FragmentWatchListTvBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        binding.apply {
            rvWlTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvWlTv.setHasFixedSize(true)

            adapter = TvShowFavAdapter()
            rvWlTv.adapter = adapter

            shimmerWlTv.visibility = View.VISIBLE

            viewModel.searchTvWatchList("%$query%")
            viewModel.tvShow.observe(viewLifecycleOwner) { tvShow ->
                shimmerWlTv.visibility = View.GONE
                if (tvShow!!.isNotEmpty()) {
                    adapter!!.setTvShow(tvShow)
                    adapter!!.notifyDataSetChanged()
                    rvWlTv.visibility = View.VISIBLE
                } else {
                    rvWlTv.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }
            }

            adapter!!.setOnItemClickListener { tvShow ->
                val sendData =
                    WatchListFragmentDirections.actionWatchListFragmentToDetailTvFragment(tvShow.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentWatchListTvBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWatchListTvBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}