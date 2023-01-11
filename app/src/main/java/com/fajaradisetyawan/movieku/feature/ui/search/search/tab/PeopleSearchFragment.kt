/*
 * Created by Fajar Adi Setyawan on 11/1/2023 - 12:28:58
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
import com.fajaradisetyawan.movieku.databinding.FragmentPeopleSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleSearchFragment : Fragment() {
    private var fragmentPeopleSearchBinding: FragmentPeopleSearchBinding? = null
    private val binding get() = fragmentPeopleSearchBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPeopleSearchBinding = FragmentPeopleSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentPeopleSearchBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPeopleSearchBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}