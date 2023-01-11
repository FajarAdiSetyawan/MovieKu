/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.tvshow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.TvShowListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentTopRatedTvShowBinding
import com.fajaradisetyawan.movieku.feature.ui.tvshow.viewmodel.TopRatedTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopRatedTvShowFragment : Fragment() {
    private var fragmentTopRatedTvShowBinding: FragmentTopRatedTvShowBinding? = null
    private val binding get() = fragmentTopRatedTvShowBinding!!

    private val viewModel by viewModels<TopRatedTvViewModel>()

    private var adapter: TvShowListSmallAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTopRatedTvShowBinding = FragmentTopRatedTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvTopRatedTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTopRatedTv.setHasFixedSize(true)

            adapter = TvShowListSmallAdapter()
            rvTopRatedTv.adapter = adapter

            shimmerTopRatedTv.visibility = View.VISIBLE

            viewModel.topRated.observe(viewLifecycleOwner) { tv ->
                shimmerTopRatedTv.visibility = View.GONE
                if (tv!!.results.isNotEmpty()) {
                    adapter!!.setTvShow(tv.results)
                    adapter!!.notifyDataSetChanged()
                    rvTopRatedTv.visibility = View.VISIBLE
                } else {
                    rvTopRatedTv.visibility = View.INVISIBLE
                }
            }

            adapter!!.setOnItemClickListener { tv ->
                val sendData =
                    TvShowFragmentDirections.actionTvShowFragmentToDetailTvFragment(tv.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTopRatedTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTopRatedTvShowBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}