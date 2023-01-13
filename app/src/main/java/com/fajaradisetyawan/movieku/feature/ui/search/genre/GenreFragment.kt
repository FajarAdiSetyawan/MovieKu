/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.genre

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
import com.fajaradisetyawan.movieku.databinding.FragmentGenreBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreFragment : Fragment() {
    private var fragmentGenreBinding: FragmentGenreBinding? = null
    private val binding get() = fragmentGenreBinding!!

    private val args by navArgs<GenreFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentGenreBinding = FragmentGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genre = args.genre

        binding.toolbar.title = genre.name

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

        val pagerAdapterGenre = ViewPagerAdapter(requireActivity(), genre.id.toString())
        binding.pagerGenre.adapter = pagerAdapterGenre

        TabLayoutMediator(binding.tabGenreSearch, binding.pagerGenre) { tab, position ->
            val tabNames =
                listOf(resources.getString(R.string.movie), resources.getString(R.string.tvshow))
            tab.text = tabNames[position]
        }.attach()
    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val genreId: String
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            //bundle to send genre id
            val bundle = Bundle()
            bundle.putString("genreId", genreId)

            val movieGenre = MovieByGenreFragment()
            val tvShowGenre = TvShowByGenreFragment()

            movieGenre.arguments = bundle
            tvShowGenre.arguments = bundle

            return when (position) {
                0 -> movieGenre
                else -> tvShowGenre
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentGenreBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentGenreBinding = null
    }
}