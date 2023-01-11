/*
 * Created by Fajar Adi Setyawan on 11/1/2023 - 12:29:49
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
import com.fajaradisetyawan.movieku.databinding.FragmentKeywordSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordSearchFragment : Fragment() {
    private var fragmentKeywordSearchBinding: FragmentKeywordSearchBinding? = null
    private val binding get() = fragmentKeywordSearchBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentKeywordSearchBinding = FragmentKeywordSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentKeywordSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentKeywordSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}