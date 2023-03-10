/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
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
import com.fajaradisetyawan.movieku.feature.adapter.TvShowBigListPagingAdapter
import com.fajaradisetyawan.movieku.feature.adapter.paging.StateAdapter
import com.fajaradisetyawan.movieku.databinding.FragmentDetailNetworkBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.viewmodel.DetailNetworkViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailNetworkFragment : Fragment() {
    private var fragmentDetailNetworkBinding: FragmentDetailNetworkBinding? = null
    private val binding get() = fragmentDetailNetworkBinding!!

    private val args by navArgs<DetailNetworkFragmentArgs>()
    private val viewModel by viewModels<DetailNetworkViewModel>()
    private var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDetailNetworkBinding = FragmentDetailNetworkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val network = args.network

        name = network.name

        binding.collapsingToolbar.title = name
        binding.tvTitle.text = name
        binding.collapsingToolbar.title = binding.tvTitle.text.toString()

        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        viewModel.getDetailNetwork(network.id)
        viewModel.detail.observe(viewLifecycleOwner) { detail ->
            Log.d("TAG", "onDetailCompanySuccess: $detail")
            if (detail!!.homepage == ""|| detail.homepage == null) {
                binding.layoutHomepage.visibility = View.GONE

            } else {
                binding.layoutHomepage.visibility = View.VISIBLE

                binding.layoutHomepage.setOnClickListener {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("${detail.homepage}"))
                    startActivity(intent)
                }
            }

            if (detail.headquarters == ""|| detail.headquarters == null) {
                binding.layoutLocation.visibility = View.GONE
            } else {
                binding.layoutLocation.visibility = View.VISIBLE
                binding.tvLocation.text = detail.headquarters
            }


            if (detail.originCountry == ""|| detail.originCountry == null) {
                binding.layoutGlobe.visibility = View.GONE
            } else {
                binding.layoutGlobe.visibility = View.VISIBLE
                binding.tvGlobe.text = detail.originCountry
            }

        }

        if (network.logoPath != null) {
            Glide.with(requireActivity())
                .asBitmap()
                .load("${network.baseUrl}${network.logoPath}")
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
                .error(R.drawable.placeholder_portrait_img)
                .into(binding.ivPoster)
        } else {
            toolbarColor(null)
            binding.ivPoster.setBackgroundResource(R.drawable.placeholder_portrait_img)
        }

        getAllTv(network.id.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllTv(id: String){
        val adapter = TvShowBigListPagingAdapter()

        binding.included.apply {
            rvCompanyNetwork.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvCompanyNetwork.setHasFixedSize(true)
            rvCompanyNetwork.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StateAdapter {adapter.retry()},
                footer = StateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.getTvByNetwork(id)
        viewModel.tvShow.observe(viewLifecycleOwner){ tvShow ->
            if (tvShow != null) {
                adapter.submitData(viewLifecycleOwner.lifecycle, tvShow)
            }else{
                binding.included.layoutEmpty.visibility = View.VISIBLE
                binding.included.tvTitleEmpty.text = resources.getString(R.string.data_for_tvshow_empty)
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                when (val state = loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        hideLoading()
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

        adapter.setOnItemClickListener { tvShow ->
            val sendData = DetailNetworkFragmentDirections.actionDetailNetworkFragmentToDetailTvFragment(tvShow.id)
            Navigation.findNavController(requireView()).navigate(sendData)
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

                    if (lightVibrantSwatch == null) {
                        if (darkVibrantSwatch == null) {
                            if (dominantSwatch == null) {
                                scrollToolbar(null, null)
                            } else {
                                scrollToolbar(dominantSwatch.rgb, dominantSwatch.bodyTextColor)
                            }

                        } else {
                            scrollToolbar(darkVibrantSwatch.rgb, darkVibrantSwatch.bodyTextColor)
                        }
                    } else {
                        scrollToolbar(
                            lightVibrantSwatch.rgb,
                            lightVibrantSwatch.bodyTextColor
                        )
                    }
                }
        } else {
            scrollToolbar(null, null)
        }
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun scrollToolbar(colorToolbar: Int?, textColor: Int?) {
        val nav: Drawable = binding.toolbar.navigationIcon!!

        if (colorToolbar != null && textColor != null) {
            requireActivity().window.statusBarColor = colorToolbar
            binding.tvGlobe.setTextColor(colorToolbar)
            binding.tvLocation.setTextColor(colorToolbar)
            binding.tvHomepage.setTextColor(colorToolbar)
            binding.tvTitle.setTextColor(colorToolbar)
            ImageViewCompat.setImageTintList(binding.ivLink, ColorStateList.valueOf(colorToolbar))
            ImageViewCompat.setImageTintList(binding.ivLoc, ColorStateList.valueOf(colorToolbar))
            ImageViewCompat.setImageTintList(binding.ivGlobe, ColorStateList.valueOf(colorToolbar))
        } else {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.color_primary)
            binding.ivLink.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.ivGlobe.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.ivLoc.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.tvGlobe.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.tvTitle.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.tvLocation.setTextColor(
                ContextCompat.getColor(requireActivity(), R.color.white))
            binding.tvHomepage.setTextColor(
                ContextCompat.getColor(requireActivity(), R.color.white))
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
                        binding.collapsingToolbar.setBackgroundColor(colorToolbar)
                        nav.setTint(textColor)
                    } else {
                        binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
                        binding.collapsingToolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
                        nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
                    }
                } else {
                    if (colorToolbar != null && textColor != null) {
                        nav.setTint(colorToolbar)
                        binding.layoutToolbar.setBackgroundColor(textColor)
                    } else {
                        binding.layoutToolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_primary))
                        nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
                    }
                    binding.collapsingToolbar.setExpandedTitleMargin(-100, 0, 0, -100)
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        })
    }

    private fun hideLoading(){
        binding.included.apply {
            shimmerCompanyNetwork.visibility = View.GONE
            shimmerCompanyNetwork.stopShimmer()
            rvCompanyNetwork.visibility = View.VISIBLE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.included.apply {
            shimmerCompanyNetwork.visibility = View.VISIBLE
            shimmerCompanyNetwork.startShimmer()
            rvCompanyNetwork.visibility = View.GONE
            failedLoad.visibility = View.GONE
        }
    }

    private fun showError(){
        binding.included.apply {
            shimmerCompanyNetwork.visibility = View.GONE
            shimmerCompanyNetwork.stopShimmer()
            rvCompanyNetwork.visibility = View.GONE
            failedLoad.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentDetailNetworkBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDetailNetworkBinding = null
    }
}