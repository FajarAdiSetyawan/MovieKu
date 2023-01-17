/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.movie

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.remote.response.movie.CollectionResponse
import com.fajaradisetyawan.movieku.databinding.FragmentCollectionBinding
import com.fajaradisetyawan.movieku.feature.adapter.MovieListBigAdapter
import com.fajaradisetyawan.movieku.feature.ui.detail.movie.viewmodel.CollectionViewModel
import com.fajaradisetyawan.movieku.utils.CustomToastDialog
import com.fajaradisetyawan.movieku.utils.Translator
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class CollectionFragment : Fragment() {
    private var fragmentCollectionBinding : FragmentCollectionBinding? = null
    private val binding get() = fragmentCollectionBinding!!

    private val args by navArgs<CollectionFragmentArgs>()
    private val viewModel by viewModels<CollectionViewModel>()

    private var adapter: MovieListBigAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentCollectionBinding = FragmentCollectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collection = args.collection
        binding.toolbar.title = resources.getString(R.string.part_of_the, collection.name)
        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        viewModel.getCollection(collection.id)
        viewModel.collection.observe(viewLifecycleOwner) { collections ->
            prepareTranslate(collections!!)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllMovie(collection: CollectionResponse){
        binding.layoutContent.apply {
            rvCollectionMovie.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvCollectionMovie.setHasFixedSize(true)

            adapter = MovieListBigAdapter()
            rvCollectionMovie.adapter = adapter


            tvNoMovie.text = resources.getString(R.string.number_of_movies, collection.parts.size)

            shimmerCollectionMovie.visibility = View.GONE
            shimmerCollectionMovie.stopShimmer()

            if (collection.parts.isNotEmpty()) {
                adapter!!.setMovie(collection.parts)
                adapter!!.notifyDataSetChanged()
                rvCollectionMovie.visibility = View.VISIBLE
            } else {
                rvCollectionMovie.visibility = View.INVISIBLE
                layoutEmpty.visibility = View.VISIBLE
            }

            adapter!!.setOnItemClickListener { movie ->
                val sendData = CollectionFragmentDirections.actionCollectionFragmentToDetailMovieFragment(movie.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun getImageCollection(collection: CollectionResponse){
        if (collection.backdropPath != null) {
            Glide.with(this)
                .asBitmap()
                .load("${collection.baseUrl}${collection.backdropPath}")
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
        } else {
            if (collection.posterPath != null) {
                Glide.with(this)
                    .asBitmap()
                    .load("${collection.baseUrl}${collection.posterPath}")
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
            } else {
                binding.ivBackdrops.setImageResource(R.drawable.placeholder_landscape_img)
                toolbarColor(null)
            }
        }


        if (collection.backdropPath == null) {
            if (collection.posterPath == null) {
                binding.ivPoster.setImageResource(R.drawable.placeholder_portrait_img)
            } else {
                Glide.with(this)
                    .load("${collection.baseUrl}${collection.backdropPath}")
                    .centerCrop()
                    .transform(RoundedCorners(25))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.placeholder_portrait_img)
                    .into(binding.ivPoster)
            }
        } else {
            Glide.with(this)
                .load("${collection.baseUrl}${collection.posterPath}")
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
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_primary)
            nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
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

    private fun prepareTranslate(collection: CollectionResponse){
        val currentLanguage = resources.configuration.locale.language
        showLoading()
        if (collection.overview != ""){
            if (currentLanguage != "en"){
                Translator.translator.translate(collection.overview)
                    .addOnSuccessListener { translatedText ->
                        goneLoading(collection)
                        binding.layoutContent.tvOverview.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        // Error.
                        CustomToastDialog.errorToast(requireActivity(), "Error", exception.message.toString())
                    }
            }else{
                goneLoading(collection)
                binding.layoutContent.tvOverview.text = collection.overview
            }
        }else{
            goneLoading(collection)
            binding.layoutContent.tvOverview.text = "-"
        }
    }

    private fun showLoading(){
        binding.apply {
            ivBackdropsShimmer.visibility = View.VISIBLE
            ivBackdropsShimmer.startShimmer()
            ivPosterShimmer.visibility = View.VISIBLE
            ivPosterShimmer.startShimmer()
            ivPoster.visibility = View.GONE
            ivBackdrops.visibility = View.GONE
            layoutContent.shimmerOverview.visibility = View.VISIBLE
            layoutContent.shimmerOverview.startShimmer()
            layoutContent.shimmerCollectionMovie.visibility = View.VISIBLE
            layoutContent.shimmerCollectionMovie.startShimmer()
            layoutContent.shimmerNoMovie.visibility = View.VISIBLE
            layoutContent.shimmerNoMovie.startShimmer()
        }
    }

    private fun goneLoading(collection: CollectionResponse){
        binding.apply {
            ivBackdropsShimmer.visibility = View.GONE
            ivBackdropsShimmer.stopShimmer()
            ivPosterShimmer.visibility = View.GONE
            ivPosterShimmer.stopShimmer()
            ivPoster.visibility = View.VISIBLE
            ivBackdrops.visibility = View.VISIBLE
            layoutContent.shimmerOverview.visibility = View.GONE
            layoutContent.shimmerOverview.stopShimmer()
            layoutContent.shimmerCollectionMovie.visibility = View.GONE
            layoutContent.shimmerCollectionMovie.stopShimmer()
            layoutContent.shimmerNoMovie.visibility = View.GONE
            layoutContent.shimmerNoMovie.stopShimmer()
            layoutContent.tvOverview.visibility = View.VISIBLE
        }
        getAllMovie(collection)
        getImageCollection(collection)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCollectionBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentCollectionBinding = null
    }
}