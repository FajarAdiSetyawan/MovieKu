/*
 * Created by Fajar Adi Setyawan on 12/12/2022 - 11:29:11
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.tvshow.episode

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.adapter.AllCastAdapter
import com.fajaradisetyawan.movieku.adapter.AllCrewAdapter
import com.fajaradisetyawan.movieku.adapter.GuestStarAdapter
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.databinding.FragmentEpisodeBinding
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class EpisodeFragment : Fragment() {
    private var fragmentEpisodeBinding: FragmentEpisodeBinding? = null
    private val binding get() = fragmentEpisodeBinding!!

    private val args by navArgs<EpisodeFragmentArgs>()

    private var duration = ""
    private var name = ""

    private var guestStarAdapter: GuestStarAdapter? = null
    private var regularSeasonAdapter: AllCastAdapter? = null
    private var crewEpisodeAdapter: AllCrewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEpisodeBinding = FragmentEpisodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShowDetail = args.tvShowDetail
        val seasons = args.season
        val episode = args.episode

        populateEpisode(episode)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun populateEpisode(episode: Episode){
        with(binding) {
            name = episode.name.toString()
            binding.tvTitle.text = episode.name

            val progress = episode.voteAverage * 10

            if (progress >= 70) {
                layoutContent.progressCircular.progressColor = ContextCompat.getColor(requireActivity(), R.color.md_green_A700)
                layoutContent.progressCircular.dotColor = ContextCompat.getColor(requireActivity(), R.color.md_green_900)
            } else {
                layoutContent.progressCircular.progressColor = ContextCompat.getColor(requireActivity(), R.color.md_yellow_500)
                layoutContent.progressCircular.dotColor = ContextCompat.getColor(requireActivity(), R.color.md_yellow_800)
            }

            layoutContent.progressCircular.setProgress(progress, 100.0)
            layoutContent.progressCircular.maxProgress = 100.0

            val voteCount = NumberFormat.getNumberInstance(Locale.US).format(episode.voteCount)
            layoutContent.tvVote.text = resources.getString(R.string.vote, voteCount)

            if (episode.airDate == null || episode.airDate == ""){
                layoutContent.tvReleaseDate.text = "-"
            }else{
                layoutContent.tvReleaseDate.text = ParseDateTime.parseDate(episode.airDate)
            }

            val hours: Int = episode.runtime / 60
            val minutes: Int = episode.runtime % 60

            duration = if (episode.runtime <= 60) {
                resources.getString(R.string.timemin, episode.runtime)
            } else {
                resources.getString(R.string.time, hours, minutes)
            }

            layoutContent.tvRuntime.text = duration


            when (episode.overview) {
                "" -> binding.tvOverview.text =
                    "-"
                else -> binding.tvOverview.text = episode.overview
            }

        }

        if (episode.stillPath == null) {
            toolbarColor(null)
        } else {
            Glide.with(this)
                .asBitmap()
                .load("${episode.baseUrl}${episode.stillPath}")
                .centerCrop()
                .transform(RoundedCorners(15))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    @SuppressLint("ResourceType")
                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        toolbarColor(resource!!)
                        return false
                    }

                })
                .into(binding.ivPoster)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRegularSeason(episode: Episode){
//        binding.layoutContent.apply {
//            rvRegular.layoutManager =
//                LinearLayoutManager(
//                    requireActivity(),
//                    LinearLayoutManager.HORIZONTAL,
//                    false
//                )
//            rvRegular.setHasFixedSize(true)
//
//            regularSeasonAdapter = AllCastAdapter()
//            rvRegular.adapter = regularSeasonAdapter
//
//            shimmerRegular.visibility = View.VISIBLE
//            shimmerRegular.startShimmer()
//
//            if (episode.) {
//                rvRegular.visibility = View.INVISIBLE
//                tvRegularEmpty.visibility = View.VISIBLE
//            } else {
//                regularSeasonAdapter!!.submitList(cast)
//                regularSeasonAdapter!!.notifyDataSetChanged()
//                rvRegular.visibility = View.VISIBLE
//
//            }
//
//            viewModel.episodeCastTvShowFailure.observe(viewLifecycleOwner){error ->
//                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            regularSeasonAdapter!!.setOnItemClickListener { cast ->
//                val sendData = EpisodeFragmentDirections.actionEpisodeFragmentToDetailPersonFragment(cast.id)
//                Navigation.findNavController(requireView()).navigate(sendData)
//            }
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getGuestStar(idTv: Int, idSeason: Int, idEpisode: Int){
//        binding.layoutContent.apply {
//            rvGuestStar.layoutManager =
//                LinearLayoutManager(
//                    requireActivity(),
//                    LinearLayoutManager.HORIZONTAL,
//                    false
//                )
//            rvGuestStar.setHasFixedSize(true)
//
//            guestStarAdapter = GuestStarAdapter()
//            rvGuestStar.adapter = guestStarAdapter
//
//            shimmerGuestStar.visibility = View.VISIBLE
//            shimmerGuestStar.startShimmer()
//
//            viewModel.episodeGuestStar(idTv, idSeason, idEpisode)
//            viewModel.episodeGuestStarSuccess.observe(viewLifecycleOwner){guest ->
//                shimmerGuestStar.visibility = View.GONE
//                shimmerGuestStar.stopShimmer()
//
//                if (guest!!.isEmpty()) {
//                    rvGuestStar.visibility = View.INVISIBLE
//                    tvGuestStarEmpty.visibility = View.VISIBLE
//                } else {
//                    guestStarAdapter!!.submitList(guest)
//                    guestStarAdapter!!.notifyDataSetChanged()
//                    rvGuestStar.visibility = View.VISIBLE
//
//                }
//                Log.d("TAG", "onSuccessGuestStar: $guest")
//            }
//
//            viewModel.episodeGuestStarFailure.observe(viewLifecycleOwner){error ->
//                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            guestStarAdapter!!.setOnItemClickListener { guest ->
//                val sendData = EpisodeFragmentDirections.actionEpisodeFragmentToDetailPersonFragment(guest.id)
//                Navigation.findNavController(requireView()).navigate(sendData)
//            }
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCrew(idTv: Int, idSeason: Int, idEpisode: Int){
//        binding.layoutContent.apply {
//            rvCrew.layoutManager =
//                LinearLayoutManager(
//                    requireActivity(),
//                    LinearLayoutManager.HORIZONTAL,
//                    false
//                )
//            rvCrew.setHasFixedSize(true)
//
//            crewEpisodeAdapter = CrewEpisodeAdapter()
//            rvCrew.adapter = crewEpisodeAdapter
//
//            shimmerCrew.visibility = View.VISIBLE
//            shimmerCrew.startShimmer()
//
//            viewModel.episodeCrewTvShow(idTv, idSeason, idEpisode)
//            viewModel.episodeCrewTvShowSuccess.observe(viewLifecycleOwner){crew ->
//                shimmerCrew.visibility = View.GONE
//                shimmerCrew.stopShimmer()
//
//                if (crew!!.isEmpty()) {
//                    rvCrew.visibility = View.INVISIBLE
//                    tvCrewEmpty.visibility = View.VISIBLE
//                } else {
//                    crewEpisodeAdapter!!.setCrew(crew)
//                    crewEpisodeAdapter!!.notifyDataSetChanged()
//                    rvCrew.visibility = View.VISIBLE
//
//                }
//                Log.d("TAG", "onSuccessCrewEpisode: $crew")
//            }
//
//            viewModel.episodeCrewTvShowFailure.observe(viewLifecycleOwner){error ->
//                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            crewEpisodeAdapter!!.setOnItemClickListener { crew ->
//                val sendData = EpisodeFragmentDirections.actionEpisodeFragmentToDetailPersonFragment(crew.id)
//                Navigation.findNavController(requireView()).navigate(sendData)
//            }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    private fun toolbarColor(resource: Bitmap?) {
        if (resource != null) {
            Palette
                .from(resource)
                .generate { palette ->
                    val darkVibrantSwatch = palette!!.darkVibrantSwatch
                    val dominantSwatch = palette.dominantSwatch
                    val lightVibrantSwatch = palette.lightVibrantSwatch

                    if (dominantSwatch == null) {
                        if (darkVibrantSwatch == null) {
                            requireActivity().window.statusBarColor = lightVibrantSwatch!!.rgb

                            scrollToolbar(
                                lightVibrantSwatch.rgb,
                                lightVibrantSwatch.bodyTextColor
                            )
                        } else {
                            requireActivity().window.statusBarColor = darkVibrantSwatch.rgb

                            scrollToolbar(darkVibrantSwatch.rgb, darkVibrantSwatch.bodyTextColor)
                        }
                    } else {
                        requireActivity().window.statusBarColor = dominantSwatch.rgb
                        scrollToolbar(dominantSwatch.rgb, dominantSwatch.bodyTextColor)
                    }
                }
        } else {
            scrollToolbar(null, null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun scrollToolbar(colorToolbar: Int?, textColor: Int?) {
        if (colorToolbar != null && textColor != null){
            requireActivity().window.statusBarColor = colorToolbar
            binding.layoutToolbar.setBackgroundColor(colorToolbar)
            binding.tvTitle.setTextColor(textColor)
            binding.tvOverview.setTextColor(textColor)
            binding.btnBack.setTextColor(colorToolbar)
            binding.btnBack.setBackgroundColor(textColor)
            binding.btnBack.compoundDrawableTintList =
                ColorStateList.valueOf(colorToolbar)
        }else {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_primary)
            binding.layoutToolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
            binding.tvTitle.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.tvOverview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.btnBack.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
            binding.btnBack.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.btnBack.compoundDrawableTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.color_primary))
        }

        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)


        binding.appbar.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            var scrollRange = -1

            @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
            override fun onOffsetChanged(
                appBarLayout: AppBarLayout,
                verticalOffset: Int
            ) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {


                    if (activity.supportActionBar != null) {
                        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        activity.supportActionBar!!.setHomeButtonEnabled(true)
                        activity.supportActionBar!!.title = binding.tvTitle.text.toString()
                        binding.collapsingToolbar.title = name
                        val nav: Drawable = binding.toolbar.navigationIcon!!

                        if (colorToolbar != null && textColor != null) {
                            binding.toolbar.setBackgroundColor(colorToolbar)
                            binding.collapsingToolbar.setCollapsedTitleTextColor(textColor)
                            nav.setTint(textColor)
                        } else {
                            binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
                            binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                            binding.toolbar.setTitleTextColor(R.color.white)
                            nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
                        }
                    }
                } else {
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.collapsingToolbar.title = ""

                    if (activity.supportActionBar != null) {
                        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                        activity.supportActionBar!!.setHomeButtonEnabled(false)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEpisodeBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentEpisodeBinding = null
    }
}