/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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
import com.fajaradisetyawan.movieku.feature.adapter.MoviePeopleCastAdapter
import com.fajaradisetyawan.movieku.feature.adapter.MoviePeopleCrewAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentMoviePeopleBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.detail.viewmodel.MoviePeopleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviePeopleFragment : Fragment() {
    private var fragmentMoviePeopleBinding: FragmentMoviePeopleBinding? = null
    private val binding get() = fragmentMoviePeopleBinding!!

    private val viewModel by viewModels<MoviePeopleViewModel>()

    private var adapterCast: MoviePeopleCastAdapter? = null
    private var adapterCrew: MoviePeopleCrewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMoviePeopleBinding = FragmentMoviePeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        val peopleId = bundle!!.getInt("peopleId")

        binding.apply {
            rvMoviePeople.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvMoviePeople.setHasFixedSize(true)

            shimmerAllMovieCast.visibility = View.VISIBLE
            shimmerAllMovieCast.startShimmer()

            viewModel.getMoviePeople(peopleId)
            viewModel.moviePeople.observe(viewLifecycleOwner) { credits ->

                shimmerAllMovieCast.visibility = View.GONE
                shimmerAllMovieCast.stopShimmer()

                when {
                    credits!!.castMovie.isNotEmpty() -> {
                        adapterCast = MoviePeopleCastAdapter()
                        rvMoviePeople.adapter = adapterCast

                        tvMovieEmpty.visibility = View.GONE
                        adapterCast!!.setMoviePeopleCast(credits.castMovie)
                        adapterCast!!.notifyDataSetChanged()
                        rvMoviePeople.visibility = View.VISIBLE

                        adapterCast!!.setOnItemClickListener { movie ->
                            val sendData =
                                DetailPeopleFragmentDirections.actionDetailPersonFragmentToDetailMovieFragment(
                                    movie.id
                                )
                            Navigation.findNavController(requireView()).navigate(sendData)
                        }
                    }
                    credits.crewMovie.isNotEmpty() -> {
                        adapterCrew = MoviePeopleCrewAdapter()
                        rvMoviePeople.adapter = adapterCast

                        tvMovieEmpty.visibility = View.GONE
                        adapterCrew!!.setMoviePeopleCrew(credits.crewMovie)
                        adapterCrew!!.notifyDataSetChanged()
                        rvMoviePeople.visibility = View.VISIBLE

                        adapterCrew!!.setOnItemClickListener { movie ->
                            val sendData =
                                DetailPeopleFragmentDirections.actionDetailPersonFragmentToDetailMovieFragment(
                                    movie.id
                                )
                            Navigation.findNavController(requireView()).navigate(sendData)
                        }
                    }
                    else -> {
                        rvMoviePeople.visibility = View.INVISIBLE
                        tvMovieEmpty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMoviePeopleBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMoviePeopleBinding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }


}