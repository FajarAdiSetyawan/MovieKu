/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var fragmentSettingBinding: FragmentSettingBinding? = null
    private val binding get() = fragmentSettingBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSettingBinding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .add(R.id.layout_setting, PreferenceFragment())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentSettingBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSettingBinding = null
    }
}