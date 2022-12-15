/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.tvshow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.adapter.TvShowListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentPopularTvShowBinding
import com.fajaradisetyawan.movieku.ui.tvshow.viewmodel.PopularTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularTvShowFragment : Fragment() {
    private var fragmentPopularTvShowBinding: FragmentPopularTvShowBinding? = null
    private val binding get() = fragmentPopularTvShowBinding!!

    private val viewModel by viewModels<PopularTvViewModel>()

    private var adapter: TvShowListSmallAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPopularTvShowBinding = FragmentPopularTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvPopularTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPopularTv.setHasFixedSize(true)

            adapter = TvShowListSmallAdapter()
            rvPopularTv.adapter = adapter

            shimmerPopularTv.visibility = View.VISIBLE

            viewModel.popular.observe(viewLifecycleOwner) { tv ->
                shimmerPopularTv.visibility = View.GONE
                if (tv!!.results.isNotEmpty()) {
                    adapter!!.setTvShow(tv.results)
                    adapter!!.notifyDataSetChanged()
                    rvPopularTv.visibility = View.VISIBLE
                } else {
                    rvPopularTv.visibility = View.INVISIBLE
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
        fragmentPopularTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPopularTvShowBinding = null
    }
}