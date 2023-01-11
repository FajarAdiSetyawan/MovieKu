/*
 * Created by Fajar Adi Setyawan on 21/12/2022 - 11:39:52
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.tv

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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.databinding.FragmentAllPeopleTvShowBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.tv.viewmodel.AllPeopleTvShowViewModel
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
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

        val pagerAdapter = ViewPagerAdapter(requireActivity(), tvShow.id)
        binding.pagerAllPeople.adapter = pagerAdapter

        viewModel.getCreditsTv(tvShow.id)
        viewModel.tvCredits.observe(viewLifecycleOwner){ result ->
            val badge1Drawable: BadgeDrawable = binding.tabAllPeople.getTabAt(0)!!.orCreateBadge
            badge1Drawable.isVisible = true
            badge1Drawable.maxCharacterCount = 3
            badge1Drawable.badgeGravity = BadgeDrawable.BOTTOM_END
            if (result!!.cast.isNotEmpty()){
                badge1Drawable.number = result.cast.size
            }else{
                badge1Drawable.number = 0
            }

            val badge2Drawable: BadgeDrawable = binding.tabAllPeople.getTabAt(1)!!.orCreateBadge
            badge2Drawable.isVisible = true
            badge2Drawable.maxCharacterCount = 3
            badge2Drawable.badgeGravity = BadgeDrawable.BOTTOM_END
            if (result.crew.isNotEmpty()){
                badge2Drawable.number = result.crew.size
            }else{
                badge2Drawable.number = 0
            }
        }

        TabLayoutMediator(binding.tabAllPeople, binding.pagerAllPeople) { tab, position ->
            val tabNames =
                listOf(resources.getString(R.string.cast), resources.getString(R.string.crew))
            tab.text = tabNames[position]
        }.attach()


    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val tvShowId: Int
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            //bundle to send genre id
            val bundle = Bundle()
            bundle.putInt("tvShowId", tvShowId)

            val castMovie = AllCastTvFragment()
            val crewMovie = AllCrewTvFragment()

            castMovie.arguments = bundle
            crewMovie.arguments = bundle

            return when (position) {
                0 -> castMovie
                else -> crewMovie
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    private fun populateTvShow(tvShowDetail: TvShowDetail){
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


    override fun onDestroy() {
        super.onDestroy()
        fragmentAllPeopleTvShowBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllPeopleTvShowBinding = null
    }
}