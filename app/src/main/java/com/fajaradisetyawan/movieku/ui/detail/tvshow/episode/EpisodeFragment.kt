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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.adapter.*
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.databinding.FragmentEpisodeBinding
import com.fajaradisetyawan.movieku.ui.detail.tvshow.episode.viewmodel.EpisodeViewModel
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
    private val viewModel by viewModels<EpisodeViewModel>()

    private var duration = ""
    private var name = ""

    private var directedByAdapter: DirectedByAdapter? = null
    private var writtenByAdapter: WrittenByAdapter? = null

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

        val episode = args.episode

        Log.d("TAG", "onSuccessEpisode: $episode")

        populateEpisode(episode)
        getCrew(episode)
        getRegularSeason(episode)
        getGuestStar(episode)
        getDirectedBy(episode)
        getWrittenBy(episode)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun populateEpisode(episode: Episode) {
        with(binding) {
            name = episode.name.toString()
            binding.tvTitle.text = episode.name

            val progress = episode.voteAverage * 10

            if (progress >= 70) {
                layoutContent.progressCircular.progressColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_green_A700)
                layoutContent.progressCircular.dotColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_green_900)
            } else {
                layoutContent.progressCircular.progressColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_yellow_500)
                layoutContent.progressCircular.dotColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_yellow_800)
            }

            layoutContent.progressCircular.setProgress(progress, 100.0)
            layoutContent.progressCircular.maxProgress = 100.0

            val voteCount = NumberFormat.getNumberInstance(Locale.US).format(episode.voteCount)
            layoutContent.tvVote.text = resources.getString(R.string.vote, voteCount)

            if (episode.airDate == null || episode.airDate == "") {
                layoutContent.tvReleaseDate.text = "-"
            } else {
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
    private fun getRegularSeason(episode: Episode) {
        val adapter = CastPagingAdapter()

        binding.layoutContent.apply {
            rvRegular.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvRegular.setHasFixedSize(true)

            rvRegular.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsEpisode(episode)
            viewModel.cast.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingCast()
                        if (adapter.itemCount < 1) {
                            tvRegularEmpty.visibility = View.VISIBLE
                        } else {
                            tvRegularEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingCast()
                    }
                    is LoadState.Error -> {
                        showErrorCast()
                        Toast.makeText(
                            requireActivity(),
                            state.error.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            adapter.setOnItemClickListener { cast ->
                val sendData = EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(cast.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getGuestStar(episode: Episode) {
        val adapter = GuestStarPagingAdapter()

        binding.layoutContent.apply {
            rvGuestStar.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvGuestStar.setHasFixedSize(true)

            rvGuestStar.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsEpisode(episode)
            viewModel.guestStar.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingGuest()
                        if (adapter.itemCount < 1) {
                            tvGuestStarEmpty.visibility = View.VISIBLE
                        } else {
                            tvGuestStarEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingGuest()
                    }
                    is LoadState.Error -> {
                        showErrorGuest()
                        Toast.makeText(
                            requireActivity(),
                            state.error.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            adapter.setOnItemClickListener { cast ->
                val sendData = EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(cast.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCrew(episode: Episode) {
        val adapter = CrewPagingAdapter()

        binding.layoutContent.apply {
            rvCrew.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvCrew.setHasFixedSize(true)

            rvCrew.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsEpisode(episode)
            viewModel.crew.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingCrew()
                        if (adapter.itemCount < 1) {
                            tvCrewEmpty.visibility = View.VISIBLE
                        } else {
                            tvCrewEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingCrew()
                    }
                    is LoadState.Error -> {
                        showErrorCrew()
                        Toast.makeText(
                            requireActivity(),
                            state.error.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            adapter.setOnItemClickListener { crew ->
                val sendData =
                    EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(crew.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDirectedBy(episode: Episode){
        binding.layoutContent.apply {
            rvDirected.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvDirected.setHasFixedSize(true)

            directedByAdapter = DirectedByAdapter()
            rvDirected.adapter = directedByAdapter

            viewModel.getDetailEpisode(episode.idTvShow, episode.seasonNumber, episode.episodeNumber)
            viewModel.detail.observe(viewLifecycleOwner) { detail ->

                if (detail!!.crew.isNotEmpty()) {
                    tvDirectedEmpty.visibility = View.GONE
                    directedByAdapter!!.setDirector(detail.crew)
                    directedByAdapter!!.notifyDataSetChanged()
                    rvDirected.visibility = View.VISIBLE
                } else {
                    rvDirected.visibility = View.GONE
                    tvDirectedEmpty.visibility = View.VISIBLE
                }
            }

            directedByAdapter!!.setOnItemClickListener { crew ->
                val sendData =
                    EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(crew.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getWrittenBy(episode: Episode){
        binding.layoutContent.apply {
            rvWritten.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvWritten.setHasFixedSize(true)

            writtenByAdapter = WrittenByAdapter()
            rvWritten.adapter = writtenByAdapter

            viewModel.getDetailEpisode(episode.idTvShow, episode.seasonNumber, episode.episodeNumber)
            viewModel.detail.observe(viewLifecycleOwner) { detail ->

                if (detail!!.crew.isNotEmpty()) {
                    tvWrittenEmpty.visibility = View.GONE
                    writtenByAdapter!!.setWriter(detail.crew)
                    writtenByAdapter!!.notifyDataSetChanged()
                    rvWritten.visibility = View.VISIBLE
                } else {
                    rvWritten.visibility = View.GONE
                    tvWrittenEmpty.visibility = View.VISIBLE
                }
            }

            directedByAdapter!!.setOnItemClickListener { crew ->
                val sendData =
                    EpisodeFragmentDirections.actionEpisodeFragmentToDetailPeopleFragment(crew.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
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
        if (colorToolbar != null && textColor != null) {
            requireActivity().window.statusBarColor = colorToolbar
            binding.layoutToolbar.setBackgroundColor(colorToolbar)
            binding.tvTitle.setTextColor(textColor)
            binding.tvOverview.setTextColor(textColor)
            binding.btnBack.setTextColor(colorToolbar)
            binding.btnBack.setBackgroundColor(textColor)
            binding.btnBack.compoundDrawableTintList =
                ColorStateList.valueOf(colorToolbar)
        } else {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.color_primary)
            binding.layoutToolbar.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.color_primary
                )
            )
            binding.tvTitle.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.tvOverview.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.btnBack.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.color_primary
                )
            )
            binding.btnBack.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.btnBack.compoundDrawableTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.color_primary
                    )
                )
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
                            binding.toolbar.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.color_primary
                                )
                            )
                            binding.collapsingToolbar.setCollapsedTitleTextColor(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.white
                                )
                            )
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

    private fun showLoadingCast() {
        binding.layoutContent.apply {
            shimmerRegular.visibility = View.VISIBLE
            shimmerRegular.startShimmer()
            rvRegular.visibility = View.GONE
            failedLoadRegular.visibility = View.GONE
            tvRegularEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCast() {
        binding.layoutContent.apply {
            shimmerRegular.visibility = View.GONE
            shimmerRegular.stopShimmer()
            rvRegular.visibility = View.VISIBLE
            failedLoadRegular.visibility = View.GONE
            tvRegularEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCast() {
        binding.layoutContent.apply {
            shimmerRegular.visibility = View.GONE
            shimmerRegular.stopShimmer()
            rvRegular.visibility = View.GONE
            failedLoadRegular.visibility = View.VISIBLE
            tvRegularEmpty.visibility = View.GONE
        }
    }

    private fun showLoadingCrew() {
        binding.layoutContent.apply {
            shimmerCrew.visibility = View.VISIBLE
            shimmerCrew.startShimmer()
            rvCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCrew() {
        binding.layoutContent.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvCrew.visibility = View.VISIBLE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCrew() {
        binding.layoutContent.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.VISIBLE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun showLoadingGuest() {
        binding.layoutContent.apply {
            shimmerGuestStar.visibility = View.VISIBLE
            shimmerGuestStar.startShimmer()
            rvGuestStar.visibility = View.GONE
            failedLoadGuest.visibility = View.GONE
            tvGuestStarEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingGuest() {
        binding.layoutContent.apply {
            shimmerGuestStar.visibility = View.GONE
            shimmerGuestStar.stopShimmer()
            rvGuestStar.visibility = View.VISIBLE
            failedLoadGuest.visibility = View.GONE
            tvGuestStarEmpty.visibility = View.GONE
        }
    }

    private fun showErrorGuest() {
        binding.layoutContent.apply {
            shimmerGuestStar.visibility = View.GONE
            shimmerGuestStar.stopShimmer()
            rvGuestStar.visibility = View.GONE
            failedLoadGuest.visibility = View.VISIBLE
            tvGuestStarEmpty.visibility = View.GONE
        }
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