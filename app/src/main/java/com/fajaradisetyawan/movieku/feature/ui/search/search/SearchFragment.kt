/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.FragmentSearchBinding
import com.fajaradisetyawan.movieku.feature.adapter.SearchTrendingAdapter
import com.fajaradisetyawan.movieku.feature.ui.search.search.tab.*
import com.fajaradisetyawan.movieku.feature.ui.search.search.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var fragmentSearchBinding: FragmentSearchBinding? = null
    private val binding get() = fragmentSearchBinding!!

    private val viewModel by viewModels<SearchViewModel>()

    private var searchTrendingAdapter: SearchTrendingAdapter? = null

    companion object {
        const val SPEECH_REQUEST_CODE = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSearchBinding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.color_primary)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        getTrending()

        binding.searchBoxContainer.apply {
            voiceSearchQuery.setOnClickListener {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                }
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            }

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
                override fun afterTextChanged(edit: Editable) {
                    // Business logic for search here
                    val query = edit.toString().toLowerCase(Locale.getDefault())
                    toggleImageView(query)

                    val pagerAdapterSearch = ViewPagerAdapter(requireActivity(), query)
                    binding.pagerSearch.adapter = pagerAdapterSearch

                    TabLayoutMediator(binding.tabSearch, binding.pagerSearch) { tab, position ->
                        val tabNames =
                            listOf(
                                resources.getString(R.string.movie),
                                resources.getString(R.string.tvshow),
                                resources.getString(R.string.keyword),
                                resources.getString(R.string.people),
                                resources.getString(R.string.collection),
                                resources.getString(R.string.companies)
                            )
                        tab.text = tabNames[position]
                    }.attach()
                }
            })

            clearSearchQuery.setOnClickListener {
                searchEditText.setText("")
            }
        }
    }

    private fun toggleImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBoxContainer.clearSearchQuery.visibility = View.VISIBLE
            binding.searchBoxContainer.voiceSearchQuery.visibility = View.INVISIBLE
            binding.layoutSuggestion.visibility = View.GONE
            binding.tabs.visibility = View.VISIBLE
            binding.pagerSearch.visibility = View.VISIBLE
        } else if (query.isEmpty()) {
            binding.searchBoxContainer.clearSearchQuery.visibility = View.INVISIBLE
            binding.searchBoxContainer.voiceSearchQuery.visibility = View.VISIBLE
            binding.layoutSuggestion.visibility = View.VISIBLE
            binding.tabs.visibility = View.GONE
            binding.pagerSearch.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            // Do something with spokenText
            binding.searchBoxContainer.searchEditText.setText(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getTrending() {
        binding.apply {

            binding.layoutTrending.visibility = View.VISIBLE

            rvTrendingSearch.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvTrendingSearch.setHasFixedSize(true)

            searchTrendingAdapter = SearchTrendingAdapter()
            rvTrendingSearch.adapter = searchTrendingAdapter

            viewModel.trending.observe(viewLifecycleOwner) { trending ->
                if (trending!!.results.isNotEmpty()) {
                    searchTrendingAdapter!!.setTrending(trending.results.subList(0, 9))
                    searchTrendingAdapter!!.notifyDataSetChanged()
                    binding.rvTrendingSearch.visibility = View.VISIBLE
                } else {
                    binding.rvTrendingSearch.visibility = View.INVISIBLE
                }
            }

            searchTrendingAdapter!!.setOnItemClickListener { trending ->
                when (trending.mediaType) {
                    "movie" -> {
                        val sendData =
                            SearchFragmentDirections.actionSearchFragmentToDetailMovieFragment(
                                trending.id
                            )
                        Navigation.findNavController(requireView()).navigate(sendData)
                    }
                    else -> {
                        val sendData =
                            SearchFragmentDirections.actionSearchFragmentToDetailTvFragment(
                                trending.id
                            )
                        Navigation.findNavController(requireView()).navigate(sendData)
                    }
                }
            }
        }
    }


    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val query: String
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            //bundle to send keyword Id
            val bundle = Bundle()
            bundle.putString("query", query)

            val movieSearch = MovieSearchFragment()
            val tvSearch = TvShowSearchFragment()
            val keywordSearch = KeywordSearchFragment()
            val peopleSearch = PeopleSearchFragment()
            val collectionSearch = CollectionSearchFragment()
            val companySearch = CompanySearchFragment()

            movieSearch.arguments = bundle
            tvSearch.arguments = bundle
            keywordSearch.arguments = bundle
            peopleSearch.arguments = bundle
            collectionSearch.arguments = bundle
            companySearch.arguments = bundle

            return when (position) {
                0 -> movieSearch
                1 -> tvSearch
                2 -> keywordSearch
                3 -> peopleSearch
                4 -> collectionSearch
                else -> companySearch
            }
        }

        override fun getItemCount(): Int {
            return 6
        }
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