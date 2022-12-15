/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.search.keyword

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentKeywordBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordFragment : Fragment() {
    private var fragmentKeywordBinding: FragmentKeywordBinding? = null
    private val binding get() = fragmentKeywordBinding!!

    private val args by navArgs<KeywordFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentKeywordBinding = FragmentKeywordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keyword = args.keyword

        binding.toolbar.title = keyword.name

        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_primary)

        val nav: Drawable = binding.toolbar.navigationIcon!!
        nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))

        val pagerAdapterKeyword = ViewPagerAdapter(requireActivity(), keyword.id.toString())
        binding.pagerKeyword.adapter = pagerAdapterKeyword

        TabLayoutMediator(binding.tabKeywordSearch, binding.pagerKeyword) { tab, position ->
            val tabNames =
                listOf(resources.getString(R.string.movie), resources.getString(R.string.tvshow))
            tab.text = tabNames[position]
        }.attach()
    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val keywordId: String
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            //bundle to send keyword Id
            val bundle = Bundle()
            bundle.putString("keywordId", keywordId)

            val movieKeyword = MovieByKeywordFragment()
            val tvShowKeyword = TvShowByKeywordFragment()

            movieKeyword.arguments = bundle
            tvShowKeyword.arguments = bundle

            return when (position) {
                0 -> movieKeyword
                else -> tvShowKeyword
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentKeywordBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentKeywordBinding = null
    }

}