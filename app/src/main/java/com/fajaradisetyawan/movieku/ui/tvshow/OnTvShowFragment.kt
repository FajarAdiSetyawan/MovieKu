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
import com.fajaradisetyawan.movieku.adapter.TvShowListSmallAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentOnTvShowBinding
import com.fajaradisetyawan.movieku.ui.tvshow.viewmodel.OnTvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnTvShowFragment : Fragment() {
    private var fragmentOnTvShowBinding: FragmentOnTvShowBinding? = null
    private val binding get() = fragmentOnTvShowBinding!!

    private val viewModel by viewModels<OnTvViewModel>()

    private var adapter: TvShowListSmallAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentOnTvShowBinding = FragmentOnTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvOnTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvOnTv.setHasFixedSize(true)

            adapter = TvShowListSmallAdapter()
            rvOnTv.adapter = adapter

            shimmerOnTv.visibility = View.VISIBLE

            viewModel.onTv.observe(viewLifecycleOwner) { tv ->
                shimmerOnTv.visibility = View.GONE
                if (tv!!.results.isNotEmpty()) {
                    adapter!!.setTvShow(tv.results)
                    adapter!!.notifyDataSetChanged()
                    rvOnTv.visibility = View.VISIBLE
                } else {
                    rvOnTv.visibility = View.INVISIBLE
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
        fragmentOnTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentOnTvShowBinding = null
    }
}