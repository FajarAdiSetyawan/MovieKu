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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.databinding.FragmentPersonFavoriteBinding
import com.fajaradisetyawan.movieku.feature.adapter.PeopleFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel.PeopleFavViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PersonFavoriteFragment : Fragment() {
    private var fragmentPersonFavoriteBinding: FragmentPersonFavoriteBinding? = null
    private val binding get() = fragmentPersonFavoriteBinding!!

    private val viewModel by viewModels<PeopleFavViewModel>()

    private val adapter: PeopleFavAdapter by lazy { PeopleFavAdapter() }

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View

    lateinit var rbTitleAsc: MaterialRadioButton
    lateinit var rbTitleDesc: MaterialRadioButton
    lateinit var rbPopAsc: MaterialRadioButton
    lateinit var rbPopDesc: MaterialRadioButton
    lateinit var rbVoteAsc: MaterialRadioButton
    lateinit var rbVoteDesc: MaterialRadioButton
    lateinit var tvVote: TextView
    lateinit var radioGroup: RadioGroup

    lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPersonFavoriteBinding =
            FragmentPersonFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = this.arguments
        val query = bundle!!.getString("query")

        binding.fabFilter.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))

        binding.apply {
            rvFavPeople.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFavPeople.setHasFixedSize(true)

            rvFavPeople.adapter = adapter

            shimmerFavPeople.visibility = View.VISIBLE

            viewModel.searchPeople("%$query%")
            viewModel.people.observe(viewLifecycleOwner) { people ->
                shimmerFavPeople.visibility = View.GONE
                if (people!!.isNotEmpty()) {
                    adapter.setPeople(people)
                    adapter.notifyDataSetChanged()
                    rvFavPeople.visibility = View.VISIBLE
                } else {
                    rvFavPeople.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }

                binding.fabFilter.setOnClickListener {
                    materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
                    customAlertDialogView = LayoutInflater.from(requireActivity())
                        .inflate(R.layout.filter_layout, null, false)
                    launchCustomAlertDialog(people)
                }
            }

            adapter.setOnItemClickListener { people ->
                val sendData =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailPeopleFragment(people.id)
                Navigation.findNavController(view).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun launchCustomAlertDialog(people: List<PeopleDetail>) {
        // Building the Alert dialog using materialAlertDialogBuilder instance
        rbTitleAsc = customAlertDialogView.findViewById(R.id.rb_title_asc)
        rbTitleDesc = customAlertDialogView.findViewById(R.id.rb_title_desc)
        rbPopAsc = customAlertDialogView.findViewById(R.id.rb_pop_asc)
        rbPopDesc = customAlertDialogView.findViewById(R.id.rb_pop_desc)
        rbVoteAsc = customAlertDialogView.findViewById(R.id.rb_vote_asc)
        rbVoteDesc = customAlertDialogView.findViewById(R.id.rb_vote_desc)
        tvVote = customAlertDialogView.findViewById(R.id.tv_vote_fillter)
        rbVoteAsc.visibility = View.GONE
        rbVoteDesc.visibility = View.GONE
        tvVote.visibility = View.GONE
        radioGroup = customAlertDialogView.findViewById(R.id.radio_group)

        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Sort & Filter")

        dialog = materialAlertDialogBuilder.show()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) { //you can also use if else
                R.id.rb_title_asc -> {
                    Collections.sort(people, PeopleDetail.sortByTitleAZ)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_title_desc -> {
                    Collections.sort(people, PeopleDetail.sortByTitleZA)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_pop_asc -> {
                    Collections.sort(people, PeopleDetail.sortByAscPopular)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                R.id.rb_pop_desc -> {
                    Collections.sort(people, PeopleDetail.sortByDescPopular)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPersonFavoriteBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentPersonFavoriteBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}