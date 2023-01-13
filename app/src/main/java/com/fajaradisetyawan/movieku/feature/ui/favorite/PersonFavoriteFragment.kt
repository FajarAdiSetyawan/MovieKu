/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.databinding.FragmentPersonFavoriteBinding
import com.fajaradisetyawan.movieku.feature.adapter.PeopleFavAdapter
import com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel.PeopleFavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFavoriteFragment : Fragment() {
    private var fragmentPersonFavoriteBinding: FragmentPersonFavoriteBinding? = null
    private val binding get() = fragmentPersonFavoriteBinding!!

    private val viewModel by viewModels<PeopleFavViewModel>()

    private var adapter : PeopleFavAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPersonFavoriteBinding = FragmentPersonFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvFavPeople.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvFavPeople.setHasFixedSize(true)

            adapter = PeopleFavAdapter()
            rvFavPeople.adapter = adapter

            shimmerFavPeople.visibility = View.VISIBLE

            viewModel.people.observe(viewLifecycleOwner) { people ->
                shimmerFavPeople.visibility = View.GONE
                if (people!!.isNotEmpty()) {
                    adapter!!.setPeople(people)
                    adapter!!.notifyDataSetChanged()
                    rvFavPeople.visibility = View.VISIBLE
                } else {
                    rvFavPeople.visibility = View.GONE
                    layoutEmpty.visibility = View.VISIBLE
                }
            }

            adapter!!.setOnItemClickListener { people ->
                val sendData =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailPeopleFragment(people.id)
                Navigation.findNavController(view).navigate(sendData)
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