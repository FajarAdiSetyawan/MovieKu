/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.movieku.feature.adapter.TvPeopleCastAdapter
import com.fajaradisetyawan.movieku.feature.adapter.TvPeopleCrewAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentTvShowPeopleBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.detail.viewmodel.TvShowPeopleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowPeopleFragment : Fragment() {

    private var fragmentTvShowPeopleBinding: FragmentTvShowPeopleBinding? = null
    private val binding get() = fragmentTvShowPeopleBinding!!

    private val viewModel by viewModels<TvShowPeopleViewModel>()

    private var adapterCast: TvPeopleCastAdapter? = null
    private var adapterCrew: TvPeopleCrewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentTvShowPeopleBinding = FragmentTvShowPeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val peopleId = bundle!!.getInt("peopleId")

        binding.apply {
            rvTvPeople.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTvPeople.setHasFixedSize(true)

            shimmerTvPeople.visibility = View.VISIBLE
            shimmerTvPeople.startShimmer()

            viewModel.getTvShowPeople(peopleId)
            viewModel.tvShowPeople.observe(viewLifecycleOwner) { credits ->

                shimmerTvPeople.visibility = View.GONE
                shimmerTvPeople.stopShimmer()

                when {
                    credits!!.castTvShow.isNotEmpty() -> {
                        adapterCast = TvPeopleCastAdapter()
                        rvTvPeople.adapter = adapterCast

                        tvTvEmpty.visibility = View.GONE
                        adapterCast!!.setTvShowPeopleCast(credits.castTvShow)
                        adapterCast!!.notifyDataSetChanged()
                        rvTvPeople.visibility = View.VISIBLE

                        adapterCast!!.setOnItemClickListener { tv ->
                            val sendData =
                                DetailPeopleFragmentDirections.actionDetailPersonFragmentToDetailTvFragment(
                                    tv.id
                                )
                            Navigation.findNavController(requireView()).navigate(sendData)
                        }
                    }
                    credits.crewTvShow.isNotEmpty() -> {
                        adapterCrew = TvPeopleCrewAdapter()
                        rvTvPeople.adapter = adapterCast

                        tvTvEmpty.visibility = View.GONE
                        adapterCrew!!.setTvShowPeopleCrew(credits.crewTvShow)
                        adapterCrew!!.notifyDataSetChanged()
                        rvTvPeople.visibility = View.VISIBLE

                        adapterCrew!!.setOnItemClickListener { tv ->
                            val sendData =
                                DetailPeopleFragmentDirections.actionDetailPersonFragmentToDetailTvFragment(
                                    tv.id
                                )
                            Navigation.findNavController(requireView()).navigate(sendData)
                        }
                    }
                    else -> {
                        rvTvPeople.visibility = View.INVISIBLE
                        tvTvEmpty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentTvShowPeopleBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTvShowPeopleBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

}