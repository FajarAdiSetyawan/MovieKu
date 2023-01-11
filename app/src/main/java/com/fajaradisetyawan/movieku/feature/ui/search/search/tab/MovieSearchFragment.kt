/*
 * Created by Fajar Adi Setyawan on 11/1/2023 - 12:28:28
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentMovieSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchFragment : Fragment() {
    private var fragmentMovieSearchBinding: FragmentMovieSearchBinding? = null
    private val binding get() = fragmentMovieSearchBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieSearchBinding = FragmentMovieSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}