/*
 * Created by Fajar Adi Setyawan on 12/12/2022 - 10:30:16
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.tvshow.season

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.fajaradisetyawan.movieku.adapter.SeasonListPagingAdapter
import com.fajaradisetyawan.movieku.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvShowDetailResponse
import com.fajaradisetyawan.movieku.databinding.FragmentAllSeasonBinding
import com.fajaradisetyawan.movieku.ui.detail.tvshow.season.viewmodel.AllSeasonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllSeasonFragment : Fragment() {
    private var fragmentAllSeasonBinding: FragmentAllSeasonBinding? = null
    private val binding get() = fragmentAllSeasonBinding!!

    private val viewModel by viewModels<AllSeasonViewModel>()

    private val args by navArgs<AllSeasonFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllSeasonBinding = FragmentAllSeasonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvShowDetail = args.tvShowDetail

        binding.apply {
            tvTitle.text = tvShowDetail.name

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        allSeason(tvShowDetail)

        if (tvShowDetail.posterPath != null){
            Glide.with(requireActivity())
                .asBitmap()
                .load("${tvShowDetail.baseUrl}${tvShowDetail.posterPath}")
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
        }else{
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
                            binding.layoutTvInfo.setBackgroundColor(lightVibrantSwatch.rgb)
                            binding.tvTitle.setTextColor(lightVibrantSwatch.titleTextColor)
                            binding.btnBack.setTextColor(lightVibrantSwatch.rgb)
                            binding.btnBack.setBackgroundColor(lightVibrantSwatch.bodyTextColor)
                            binding.btnBack.compoundDrawableTintList = ColorStateList.valueOf(lightVibrantSwatch.rgb)
                        } else {
                            requireActivity().window.statusBarColor = darkVibrantSwatch.rgb
                            binding.layoutTvInfo.setBackgroundColor(darkVibrantSwatch.rgb)
                            binding.tvTitle.setTextColor(darkVibrantSwatch.titleTextColor)
                            binding.btnBack.setTextColor(darkVibrantSwatch.rgb)
                            binding.btnBack.setBackgroundColor(darkVibrantSwatch.bodyTextColor)
                            binding.btnBack.compoundDrawableTintList = ColorStateList.valueOf(darkVibrantSwatch.rgb)

                        }
                    } else {
                        requireActivity().window.statusBarColor = dominantSwatch.rgb
                        binding.layoutTvInfo.setBackgroundColor(dominantSwatch.rgb)
                        binding.tvTitle.setTextColor(dominantSwatch.titleTextColor)
                        binding.btnBack.setTextColor(dominantSwatch.rgb)
                        binding.btnBack.setBackgroundColor(dominantSwatch.bodyTextColor)
                        binding.btnBack.compoundDrawableTintList = ColorStateList.valueOf(dominantSwatch.rgb)
                    }
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allSeason(detail: TvShowDetailResponse){
        val adapter = SeasonListPagingAdapter(detail.name)

        binding.apply {
            rvSeason.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvSeason.setHasFixedSize(true)

            rvSeason.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )

            btnTryAgain.setOnClickListener {
                adapter.retry()
            }

            shimmerSeason.visibility = View.VISIBLE
            shimmerSeason.startShimmer()

            viewModel.getAllSeason(detail.id)
            viewModel.allSeason.observe(viewLifecycleOwner) { season ->
                adapter.submitData(viewLifecycleOwner.lifecycle, season)
            }

            adapter.addLoadStateListener { loadState ->
                binding.apply {
                    when (val state = loadState.source.refresh) {
                        is LoadState.NotLoading -> {
                            hideLoading()
                            if (adapter.itemCount < 1){
                                binding.layoutEmpty.visibility = View.VISIBLE
                            }else{
                                binding.layoutEmpty.visibility = View.GONE
                            }
                        }
                        is LoadState.Loading -> {
                            showLoading()
                        }
                        is LoadState.Error -> {
                            showError()
                            Toast.makeText(requireActivity(), state.error.message.orEmpty(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            adapter.setOnItemClickListener { season ->
                val sendData = AllSeasonFragmentDirections.actionAllSeasonFragmentToSeasonFragment(detail, season)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun hideLoading(){
        binding.apply {
            shimmerSeason.visibility = View.GONE
            shimmerSeason.stopShimmer()
            rvSeason.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            shimmerSeason.visibility = View.VISIBLE
            shimmerSeason.startShimmer()
            rvSeason.visibility = View.GONE
            failedLoad.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.apply {
            shimmerSeason.visibility = View.GONE
            shimmerSeason.stopShimmer()
            rvSeason.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAllSeasonBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllSeasonBinding = null
    }
}