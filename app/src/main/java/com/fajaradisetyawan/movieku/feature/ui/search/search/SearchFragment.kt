/*
 * Created by Fajar Adi Setyawan on 11/1/2023 - 12:49:15
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fajaradisetyawan.movieku.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var fragmentSearchBinding: FragmentSearchBinding? = null
    private val binding get() = fragmentSearchBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSearchBinding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSearchBinding = null
    }
}