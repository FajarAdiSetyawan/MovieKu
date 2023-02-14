/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.databinding.FragmentAllPeopleMovieBinding
import com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie.viewmodel.AllPeopleMovieViewModel
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllPeopleMovieFragment : Fragment() {
    private var fragmentAllPeopleMovieBinding: FragmentAllPeopleMovieBinding? = null
    private val binding get() = fragmentAllPeopleMovieBinding!!

    private val args by navArgs<AllPeopleMovieFragmentArgs>()

    private val viewModel by viewModels<AllPeopleMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllPeopleMovieBinding =
            FragmentAllPeopleMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = args.movieDetail

        if (movie.releaseDate != null || movie.releaseDate != "" || movie.releaseDate.isNotEmpty()){
            binding.toolbar.title = movie.title + " (${ParseDateTime.getYear(movie.releaseDate)})"

        }else{
            binding.toolbar.title = movie.title
        }


        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        populateMovie(movie)
        val pagerAdapter = ViewPagerAdapter(requireActivity(), movie.id)
        binding.pagerAllPeople.adapter = pagerAdapter

        viewModel.getCreditMovie(movie.id)
        viewModel.movieCredit.observe(viewLifecycleOwner){ result ->
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
        private val movieId: Int
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            //bundle to send genre id
            val bundle = Bundle()
            bundle.putInt("movieId", movieId)

            val castMovie = AllCastMovieFragment()
            val crewMovie = AllCrewMovieFragment()

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

    private fun populateMovie(movieDetail: MovieDetail) {
        if (movieDetail.backdropPath == null) {
            if (movieDetail.posterPath == null) {
                binding.ivPoster.visibility = View.GONE
                binding.ivBackdrops.setImageResource(R.drawable.placeholder_landscape_img)
                toolbarColor(null)
            } else {
                binding.ivPoster.visibility = View.VISIBLE
                Glide.with(this)
                    .asBitmap()
                    .load("${movieDetail.baseUrl}${movieDetail.posterPath}")
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
                .load("${movieDetail.baseUrl}${movieDetail.backdropPath}")
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

        if (movieDetail.posterPath == null) {
            binding.ivPoster.visibility = View.GONE
        }else{
            binding.ivPoster.visibility = View.VISIBLE
            Glide.with(this)
                .asBitmap()
                .load("${movieDetail.baseUrl}${movieDetail.posterPath}")
                .centerCrop()
                .transform(RoundedCorners(20))
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
        } else {
            nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.color_primary)
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
        fragmentAllPeopleMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAllPeopleMovieBinding = null
    }
}