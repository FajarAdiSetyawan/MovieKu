/*
 * Created by Fajar Adi Setyawan on 7/2/2023 - 11:58:8
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.watchlist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.databinding.FragmentWatchListMovieBinding
import com.fajaradisetyawan.movieku.feature.adapter.MovieFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.watchlist.viewmodel.WatchListMovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WatchListMovieFragment : Fragment() {
    private var fragmentWatchListMovieBinding: FragmentWatchListMovieBinding? = null
    private val binding get() = fragmentWatchListMovieBinding!!

    private val viewModel by viewModels<WatchListMovieViewModel>()

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
        fragmentWatchListMovieBinding = FragmentWatchListMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        binding.fabFilter.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))

        binding.apply {
            rvWlMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvWlMovie.setHasFixedSize(true)

            rvWlMovie.adapter = adapter

            shimmerWlMovie.visibility = View.VISIBLE
            viewModel.searchWatchList("%$query%")

            viewModel.movie.observe(viewLifecycleOwner) { movie ->
                shimmerWlMovie.visibility = View.GONE
                if (movie!!.isNotEmpty()) {
                    adapter.setMovie(movie)
                    adapter.notifyDataSetChanged()
                    rvWlMovie.visibility = View.VISIBLE
                } else {
                    rvWlMovie.visibility = View.GONE
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
                    WatchListFragmentDirections.actionWatchListFragmentToDetailMovieFragment(movie.id)
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

    override fun onDestroy() {
        super.onDestroy()
        fragmentWatchListMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWatchListMovieBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}