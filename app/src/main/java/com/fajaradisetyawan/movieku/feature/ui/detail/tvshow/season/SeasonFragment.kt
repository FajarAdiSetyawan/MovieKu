/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */


package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.season

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
import com.fajaradisetyawan.movieku.feature.adapter.EpisodeListPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.data.model.tvshow.Seasons
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.databinding.FragmentSeasonBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.season.viewmodel.SeasonViewModel
import com.fajaradisetyawan.movieku.utils.Translator
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeasonFragment : Fragment() {
    private var fragmentSeasonBinding: FragmentSeasonBinding? = null
    private val binding get() = fragmentSeasonBinding!!

    private val args by navArgs<SeasonFragmentArgs>()
    private val viewModel by viewModels<SeasonViewModel>()

    private var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSeasonBinding = FragmentSeasonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvShowDetail = args.tvShowDetail
        val seasons = args.season

        getAllEpisode(tvShowDetail, seasons)

        Log.d("TAG", "onSuccessSeason: $seasons")

        name = seasons.name!!

        binding.tvTitle.text = name
        binding.toolbar.title = name

        if (seasons.overview != "") {
            val currentLanguage = resources.configuration.locale.language
            if (currentLanguage != "en") {
                Translator.translator.translate(seasons.overview!!)
                    .addOnSuccessListener { translatedText ->
                        binding.tvOverview.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        // Error.
                        Toast.makeText(
                            requireActivity(),
                            "Error ${exception.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                binding.tvOverview.text = seasons.overview
            }
        } else {
            binding.tvOverview.visibility = View.GONE
        }

        binding.included.tvTotalEpisode.text = resources.getString(R.string.episodef, seasons.episodeCount)


        if (seasons.posterPath != null) {
            Glide.with(requireActivity())
                .asBitmap()
                .load("${seasons.baseUrl}${seasons.posterPath}")
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
        } else {
            toolbarColor(null)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatTextViewDrawableApis")
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
        }else{
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

            @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor"
            )
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
                    binding.collapsingToolbar.title = name

                    if (activity.supportActionBar != null) {
                        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        activity.supportActionBar!!.setHomeButtonEnabled(true)

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

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllEpisode(tvDetail: TvShowDetail, seasons: Seasons?) {
        val adapter = EpisodeListPagingAdapter()

        binding.included.apply {
            rvEpisode.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvEpisode.setHasFixedSize(true)
            rvEpisode.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )

            btnTryAgain.setOnClickListener {
                adapter.retry()
            }

            viewModel.getAllEpisode(tvDetail.id, seasons!!.seasonNumber)
            viewModel.results.observe(viewLifecycleOwner) { episode ->
                adapter.submitData(viewLifecycleOwner.lifecycle, episode)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingEpisode()
                        if (adapter.itemCount < 1){
                            layoutEmpty.visibility = View.VISIBLE
                        }else{
                            layoutEmpty.visibility = View.GONE
                        }
                    }
                    is LoadState.Loading -> {
                        showLoadingEpisode()
                    }
                    is LoadState.Error -> {
                        showError()
                        Toast.makeText(requireActivity(), state.error.message.orEmpty(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            adapter.setOnItemClickListener { episode ->
                val sendData = SeasonFragmentDirections.actionSeasonFragmentToEpisodeFragment(episode)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun hideLoadingEpisode(){
        binding.included.apply {
            shimmerEpisode.visibility = View.GONE
            shimmerEpisode.stopShimmer()
            rvEpisode.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoadingEpisode(){
        binding.included.apply {
            shimmerEpisode.visibility = View.VISIBLE
            shimmerEpisode.startShimmer()
            rvEpisode.visibility = View.GONE
            failedLoad.visibility = View.GONE
            layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.included.apply {
            shimmerEpisode.visibility = View.GONE
            shimmerEpisode.stopShimmer()
            rvEpisode.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentSeasonBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSeasonBinding = null
    }
}