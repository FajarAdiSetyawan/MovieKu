/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentHomeBinding
import com.fajaradisetyawan.movieku.utils.CustomToastDialog
import com.fajaradisetyawan.movieku.utils.Translator
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.color_primary)

        val pagerAdapterPopular = ViewPagerAdapterPopular(requireActivity())
        binding.pagerPopular.adapter = pagerAdapterPopular
        binding.pagerPopular.isUserInputEnabled = false

        val pagerAdapterTrending = ViewPagerAdapterTrending(requireActivity())
        binding.pagerTrending.adapter = pagerAdapterTrending
        binding.pagerTrending.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayoutTrending, binding.pagerTrending) { tab, position ->
            val tabNames =
                listOf(resources.getString(R.string.today), resources.getString(R.string.week))
            tab.text = tabNames[position]
        }.attach()

        TabLayoutMediator(binding.tabLayoutPopular, binding.pagerPopular) { tab, position ->
            val tabNames =
                listOf(resources.getString(R.string.movie), resources.getString(R.string.tvshow))
            tab.text = tabNames[position]
        }.attach()

        binding.searchBoxContainer.itemSearch.setOnClickListener {
            val sendData = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            Navigation.findNavController(view).navigate(sendData)
        }

        binding.searchBoxContainer.ivFav.setOnClickListener {
            val sendData = HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
            Navigation.findNavController(view).navigate(sendData)
        }

        binding.searchBoxContainer.ivWatchlist.setOnClickListener {
            val sendData = HomeFragmentDirections.actionHomeFragmentToWatchListFragment()
            Navigation.findNavController(view).navigate(sendData)
        }

        val currentLanguage = resources.configuration.locale.language
        if (currentLanguage != "en") {

            var firstrun: Boolean =
                requireActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true)

            if (firstrun){
                CustomToastDialog.showLoadingDialog(
                    requireActivity(),
                    resources.getString(R.string.downloading_modal)
                )

                Translator.translator.downloadModelIfNeeded()
                    .addOnCompleteListener {
                        CustomToastDialog.goneLoadingDialog()
                    }
                    .addOnSuccessListener {
                        CustomToastDialog.goneLoadingDialog()
                    }
                    .addOnFailureListener { exception ->
                        // Model couldn’t be downloaded or other internal error.
                        CustomToastDialog.goneLoadingDialog()
                        CustomToastDialog.errorToast(
                            requireActivity(),
                            "Error",
                            exception.message.toString()
                        )
                    }

                requireActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit()
            }


        }
    }

    internal class ViewPagerAdapterTrending(
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DayTrendingFragment()
                else -> WeekTrendingFragment()
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }


    internal class ViewPagerAdapterPopular(
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MoviePopularHomeFragment()
                else -> TvShowPopularHomeFragment()
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentHomeBinding = null
    }

}