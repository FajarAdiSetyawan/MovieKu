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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowFavoriteBinding
import com.fajaradisetyawan.movieku.feature.adapter.TvShowFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel.TvShowFavViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TvShowFavoriteFragment : Fragment() {
    private var fragmentTvShowFavoriteBinding: FragmentTvShowFavoriteBinding? = null
    private val binding get() = fragmentTvShowFavoriteBinding!!

    private val viewModel by viewModels<TvShowFavViewModel>()

    private val adapter: TvShowFavAdapter by lazy { TvShowFavAdapter() }

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
        fragmentTvShowFavoriteBinding = FragmentTvShowFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        binding.fabFilter.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))

        binding.apply {
            rvFavTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFavTv.setHasFixedSize(true)

            rvFavTv.adapter = adapter

            shimmerFavTv.visibility = View.VISIBLE

            viewModel.searchTvFav("%$query%")
            viewModel.tvShow.observe(viewLifecycleOwner) { tvShow ->
                shimmerFavTv.visibility = View.GONE
                if (tvShow!!.isNotEmpty()) {
                    adapter.setTvShow(tvShow)
                    adapter.notifyDataSetChanged()
                    rvFavTv.visibility = View.VISIBLE
                } else {
                    rvFavTv.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }

                binding.fabFilter.setOnClickListener {
                    materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
                    customAlertDialogView = LayoutInflater.from(requireActivity())
                        .inflate(R.layout.filter_layout, null, false)
                    launchCustomAlertDialog(tvShow)
                }

            }

            adapter.setOnItemClickListener { tvShow ->
                val sendData =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailTvFragment(tvShow.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun launchCustomAlertDialog(tvShow: List<TvShowDetail>) {
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
                    Collections.sort(tvShow, TvShowDetail.sortByTitleAZ)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_title_desc -> {
                    Collections.sort(tvShow, TvShowDetail.sortByTitleZA)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_pop_asc -> {
                    Collections.sort(tvShow, TvShowDetail.sortByAscPopular)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_pop_desc -> {
                    Collections.sort(tvShow, TvShowDetail.sortByDescPopular)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_vote_asc -> {
                    Collections.sort(tvShow, TvShowDetail.sortByAscVote)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_vote_desc -> {
                    Collections.sort(tvShow, TvShowDetail.sortByDescVote)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowFavoriteBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowFavoriteBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}