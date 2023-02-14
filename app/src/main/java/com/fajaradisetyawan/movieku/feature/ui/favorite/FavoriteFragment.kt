/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentFavoriteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var fragmentFavoriteBinding: FragmentFavoriteBinding? = null
    private val binding get() = fragmentFavoriteBinding!!

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View

    lateinit var rbTitleAsc: MaterialRadioButton
    lateinit var rbTitleDesc: MaterialRadioButton
    lateinit var rbPopAsc: MaterialRadioButton
    lateinit var rbPopDesc: MaterialRadioButton
    lateinit var rbVoteAsc: MaterialRadioButton
    lateinit var rbVoteDesc: MaterialRadioButton
    lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment'
        fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.color_primary)
        val nav: Drawable = binding.toolbar.navigationIcon!!
        nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))

        val pagerAdapterSearch = ViewPagerAdapter(requireActivity(), "")
        binding.pagerFavorite.adapter = pagerAdapterSearch

        TabLayoutMediator(binding.tabFav, binding.pagerFavorite) { tab, position ->
            val tabNames =
                listOf(
                    resources.getString(R.string.movie),
                    resources.getString(R.string.tvshow),
                    resources.getString(R.string.people),
                )
            tab.text = tabNames[position]
        }.attach()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val pagerAdapterSearch = ViewPagerAdapter(requireActivity(), query)
                    binding.pagerFavorite.adapter = pagerAdapterSearch
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    val pagerAdapterSearch = ViewPagerAdapter(requireActivity(), query)
                    binding.pagerFavorite.adapter = pagerAdapterSearch
                }
                return true
            }
        })

    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val query: String,
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString("query", query)

            val movieFav = MovieFavoriteFragment()
            val tvFav = TvShowFavoriteFragment()
            val peopleFav = PersonFavoriteFragment()

            movieFav.arguments = bundle
            tvFav.arguments = bundle
            peopleFav.arguments = bundle

            return when (position) {
                0 -> movieFav
                1 -> tvFav
                else -> peopleFav
            }
        }

        override fun getItemCount(): Int {
            return 3
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        fragmentFavoriteBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentFavoriteBinding = null
    }
}