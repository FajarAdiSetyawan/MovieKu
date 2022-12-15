/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.people.allPeople

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.adapter.CastPagingAdapter
import com.fajaradisetyawan.movieku.adapter.CrewPagingAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvShowDetailResponse
import com.fajaradisetyawan.movieku.databinding.FragmentAllPeopleTvShowBinding
import com.fajaradisetyawan.movieku.ui.detail.people.allPeople.viewmodel.AllPeopleTvShowViewModel
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPeopleTvShowFragment : Fragment() {
    private var fragmentAllPeopleTvShowBinding: FragmentAllPeopleTvShowBinding? = null
    private val binding get() = fragmentAllPeopleTvShowBinding!!

    private val args by navArgs<AllPeopleTvShowFragmentArgs>()
    private val viewModel by viewModels<AllPeopleTvShowViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllPeopleTvShowBinding = FragmentAllPeopleTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShow = args.tvShowDetail

        binding.toolbar.title = tvShow.name + " (${ParseDateTime.getYear(tvShow.firstAirDate)})"

        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        populateTvShow(tvShow)

        allCastTvShow(tvShow.id)
        allCrewTvShow(tvShow.id)
    }

    private fun populateTvShow(tvShowDetail: TvShowDetailResponse){
        if (tvShowDetail.backdropPath == null) {
            if (tvShowDetail.posterPath == null) {
                binding.ivBackdrops.setImageResource(R.drawable.placeholder_landscape_img)
                toolbarColor(null)
            } else {
                Glide.with(this)
                    .asBitmap()
                    .load("${tvShowDetail.baseUrl}${tvShowDetail.posterPath}")
                    .centerCrop()
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
                    .into(binding.ivBackdrops)
            }
        } else {
            Glide.with(this)
                .asBitmap()
                .load("${tvShowDetail.baseUrl}${tvShowDetail.backdropPath}")
                .centerCrop()
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
                .into(binding.ivBackdrops)
        }


        if (tvShowDetail.backdropPath == null) {
            if (tvShowDetail.posterPath == null) {
                binding.ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
            } else {
                Glide.with(this)
                    .load("${tvShowDetail.baseUrl}${tvShowDetail.backdropPath}")
                    .centerCrop()
                    .transform(RoundedCorners(25))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.placeholder_portrait_img)
                    .into(binding.ivPoster)
            }
        } else {
            Glide.with(this)
                .load("${tvShowDetail.baseUrl}${tvShowDetail.posterPath}")
                .centerCrop()
                .transform(RoundedCorners(25))
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.placeholder_portrait_img)
                .into(binding.ivPoster)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allCastTvShow(idTvShow: Int){
        val adapter = CastPagingAdapter()

        binding.layoutContent.apply {
            rvAllCast.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvAllCast.setHasFixedSize(true)

            rvAllCast.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsTv(idTvShow)
            viewModel.cast.observe(viewLifecycleOwner) { result ->
                adapter.submitData(viewLifecycleOwner.lifecycle, result)
            }

            adapter.addLoadStateListener { loadState ->
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoadingCast()
                        if (adapter.itemCount < 1) {
                            tvCastEmpty.visibility = View.VISIBLE
                        } else {
                            tvCastEmpty.visibility = View.GONE
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
                val sendData = AllPeopleTvShowFragmentDirections.actionAllPeopleTvShowFragmentToDetailPeopleFragment(cast.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allCrewTvShow(idTvShow: Int){
        val adapter = CrewPagingAdapter()

        binding.layoutContent.apply {
            rvAllCrew.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvAllCrew.setHasFixedSize(true)

            rvAllCrew.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { adapter.retry() },
                footer = StateAdapter { adapter.retry() }
            )

            viewModel.getCreditsTv(idTvShow)
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
                val sendData = AllPeopleTvShowFragmentDirections.actionAllPeopleTvShowFragmentToDetailPeopleFragment(crew.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

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
                            scrollToolbar(
                                lightVibrantSwatch!!.rgb,
                                lightVibrantSwatch.bodyTextColor
                            )
                        } else {
                            scrollToolbar(darkVibrantSwatch.rgb, darkVibrantSwatch.bodyTextColor)
                        }
                    } else {
                        scrollToolbar(dominantSwatch.rgb, dominantSwatch.bodyTextColor)
                    }
                }
        } else {
            scrollToolbar(null, null)
        }
    }

    private fun scrollToolbar(colorToolbar: Int?, textColor: Int?) {
        val nav: Drawable = binding.toolbar.navigationIcon!!

        if (colorToolbar != null && textColor != null) {
            requireActivity().window.statusBarColor = colorToolbar
            nav.setTint(textColor)
        }else{
            nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_primary)
        }

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
                    if (colorToolbar != null && textColor != null) {
                        binding.toolbar.setBackgroundColor(colorToolbar)
                        binding.collapsingToolbar.setCollapsedTitleTextColor(textColor)
                    } else {
                        binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
                        binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                        binding.toolbar.setTitleTextColor(R.color.white)
                    }
                } else {
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.collapsingToolbar.setCollapsedTitleTextColor(R.color.text_color)
                }
            }
        })
    }

    private fun showLoadingCast() {
        binding.layoutContent.apply {
            shimmerCast.visibility = View.VISIBLE
            shimmerCast.startShimmer()
            rvAllCast.visibility = View.GONE
            failedLoadCast.visibility = View.GONE
            tvCastEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCast() {
        binding.layoutContent.apply {
            shimmerCast.visibility = View.GONE
            shimmerCast.stopShimmer()
            rvAllCast.visibility = View.VISIBLE
            failedLoadCast.visibility = View.GONE
            tvCastEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCast() {
        binding.layoutContent.apply {
            shimmerCast.visibility = View.GONE
            shimmerCast.stopShimmer()
            rvAllCast.visibility = View.GONE
            failedLoadCast.visibility = View.VISIBLE
            tvCastEmpty.visibility = View.GONE
        }
    }

    private fun showLoadingCrew() {
        binding.layoutContent.apply {
            shimmerCrew.visibility = View.VISIBLE
            shimmerCrew.startShimmer()
            rvAllCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun hideLoadingCrew() {
        binding.layoutContent.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvAllCrew.visibility = View.VISIBLE
            failedLoadCrew.visibility = View.GONE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    private fun showErrorCrew() {
        binding.layoutContent.apply {
            shimmerCrew.visibility = View.GONE
            shimmerCrew.stopShimmer()
            rvAllCrew.visibility = View.GONE
            failedLoadCrew.visibility = View.VISIBLE
            tvCrewEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAllPeopleTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllPeopleTvShowBinding = null
    }
}