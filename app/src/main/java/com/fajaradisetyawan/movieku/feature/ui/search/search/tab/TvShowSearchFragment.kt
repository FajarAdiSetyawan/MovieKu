/*
 * Created by Fajar Adi Setyawan on 11/1/2023 - 12:28:43
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowSearchFragment : Fragment() {
    private var fragmentTvShowSearchBinding: FragmentTvShowSearchBinding? = null
    private val binding get() = fragmentTvShowSearchBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowSearchBinding = FragmentTvShowSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowSearchBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}