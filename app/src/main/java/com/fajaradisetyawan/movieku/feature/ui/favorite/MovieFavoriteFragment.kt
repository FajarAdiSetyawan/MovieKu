/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.databinding.FragmentMovieFavoriteBinding
import com.fajaradisetyawan.movieku.feature.adapter.MovieFavAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel.MovieFavViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MovieFavoriteFragment : Fragment() {
    private var fragmentMovieFavoriteBinding: FragmentMovieFavoriteBinding? = null
    private val binding get() = fragmentMovieFavoriteBinding!!

    private val viewModel by viewModels<MovieFavViewModel>()

    private val adapter: MovieFavAdapter by lazy { MovieFavAdapter() }

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View

    lateinit var rbTitleAsc: MaterialRadioButton
    lateinit var rbTitleDesc: MaterialRadioButton
    lateinit var rbPopAsc: MaterialRadioButton
    lateinit var rbPopDesc: MaterialRadioButton
    lateinit var rbVoteAsc: MaterialRadioButton
    lateinit var rbVoteDesc: MaterialRadioButton
    lateinit var radioGroup: RadioGroup

    lateinit var dialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieFavoriteBinding =
            FragmentMovieFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        binding.fabFilter.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))

        binding.apply {
            rvFavMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFavMovie.setHasFixedSize(true)

            rvFavMovie.adapter = adapter

            shimmerFavMovie.visibility = View.VISIBLE
            viewModel.searchMovies("%$query%")

            viewModel.movie.observe(viewLifecycleOwner) { movie ->
                shimmerFavMovie.visibility = View.GONE
                if (movie!!.isNotEmpty()) {
                    adapter.setMovie(movie)
                    adapter.notifyDataSetChanged()
                    rvFavMovie.visibility = View.VISIBLE
                } else {
                    rvFavMovie.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }

                binding.fabFilter.setOnClickListener {
                    materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
                    customAlertDialogView = LayoutInflater.from(requireActivity())
                        .inflate(R.layout.filter_layout, null, false)
                    launchCustomAlertDialog(movie)
                }
            }

            adapter.setOnItemClickListener { movie ->
                val sendData =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun launchCustomAlertDialog(movie: List<MovieDetail>) {
        // Building the Alert dialog using materialAlertDialogBuilder instance
        rbTitleAsc = customAlertDialogView.findViewById(R.id.rb_title_asc)
        rbTitleDesc = customAlertDialogView.findViewById(R.id.rb_title_desc)
        rbPopAsc = customAlertDialogView.findViewById(R.id.rb_pop_asc)
        rbPopDesc = customAlertDialogView.findViewById(R.id.rb_pop_desc)
        rbVoteAsc = customAlertDialogView.findViewById(R.id.rb_vote_asc)
        rbVoteDesc = customAlertDialogView.findViewById(R.id.rb_vote_desc)
        radioGroup = customAlertDialogView.findViewById(R.id.radio_group)

        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Sort & Filter")

        dialog = materialAlertDialogBuilder.show()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){ //you can also use if else
                R.id.rb_title_asc -> {
                    Collections.sort(movie, MovieDetail.sortByTitleAZ)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_title_desc -> {
                    Collections.sort(movie, MovieDetail.sortByTitleZA)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_pop_asc -> {
                    Collections.sort(movie, MovieDetail.sortByAscPopular)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_pop_desc -> {
                    Collections.sort(movie, MovieDetail.sortByDescPopular)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_vote_asc -> {
                    Collections.sort(movie, MovieDetail.sortByAscVote)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_vote_desc -> {
                    Collections.sort(movie, MovieDetail.sortByDescVote)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMovieFavoriteBinding = null
        requireActivity().viewModelStore.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMovieFavoriteBinding = null
        requireActivity().viewModelStore.clear()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}