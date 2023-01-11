/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.movie

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.fajaradisetyawan.movieku.data.model.movie.Collection
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.databinding.FragmentDetailMovieBinding
import com.fajaradisetyawan.movieku.feature.adapter.*
import com.fajaradisetyawan.movieku.feature.adapter.detail.CastDetailAdapter
import com.fajaradisetyawan.movieku.feature.ui.detail.movie.viewmodel.DetailMovieViewModel
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.fajaradisetyawan.movieku.utils.Translator.translator
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class DetailMovieFragment : Fragment() {
    private var fragmentDetailMovieBinding: FragmentDetailMovieBinding? = null
    private val binding get() = fragmentDetailMovieBinding!!

    private val viewModel by viewModels<DetailMovieViewModel>()

    private val args by navArgs<DetailMovieFragmentArgs>()
    private var duration = ""
    private var isFavorite: Boolean = false
    private var genreListAdapter: GenreListAdapter? = null
    private var castDetailAdapter: CastDetailAdapter? = null
    private var movieListSmallAdapter: MovieListSmallAdapter? = null
    private var companyAdapter: CompanyAdapter? = null
    private var keywordListAdapter: KeywordListAdapter? = null
    private var spokenLanguageAdapter: SpokenLanguageAdapter? = null
    var title = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDetailMovieBinding =
            FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val movieId = args.movieId

            viewModel.getDetailMovie(movieId)
            viewModel.movieDetail.observe(viewLifecycleOwner) { detail ->

                binding.collapsingToolbar.title = detail!!.title

                title = detail.title

                val activity = activity as AppCompatActivity?

                activity!!.setSupportActionBar(binding.toolbar)
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                activity.supportActionBar?.setDisplayShowHomeEnabled(true)

                binding.toolbar.setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }

                activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

                prepareTranslate(detail)

                if (detail.collection != null) {
                    getCollection(detail.collection!!)
                } else {
                    binding.layoutContent.cardCollection.visibility = View.GONE
                }

                /**
                 * Part of Favorite.
                 */
                favoriteMovie(detail)

            }
        }
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "StringFormatMatches", "NotifyDataSetChanged")
    private fun populateMovie(movieDetail: MovieDetail) {

        with(binding) {

            val progress = movieDetail.voteAverage * 10

            if (movieDetail.originalTitle == movieDetail.title) {
                layoutContent.tvTitleOri.visibility = View.GONE
            } else {
                layoutContent.tvTitleOri.visibility = View.VISIBLE
                layoutContent.tvTitleOri.text = movieDetail.originalTitle
            }


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

            val voteCount = NumberFormat.getNumberInstance(Locale.US).format(movieDetail.voteCount)
            layoutContent.tvVote.text = resources.getString(R.string.vote, voteCount)

            layoutContent.tvReleaseDate.text = ParseDateTime.parseDate(movieDetail.releaseDate)

            val hours: Int = movieDetail.runtime / 60
            val minutes: Int = movieDetail.runtime % 60

            duration = if (movieDetail.runtime <= 60) {
                resources.getString(R.string.timemin, movieDetail.runtime)
            } else {
                resources.getString(R.string.time, hours, minutes)
            }

            layoutContent.tvRuntime.text = duration

            if (movieDetail.tagline == "" || movieDetail.tagline.equals(null)) {
                layoutContent.tvTagline.visibility = View.GONE
            } else {
                layoutContent.tvTagline.text = movieDetail.tagline
                layoutContent.tvTagline.visibility = View.VISIBLE
            }

            if (movieDetail.status != ""){
                layoutContent.tvStatus.text = movieDetail.status
            }else{
                layoutContent.tvStatus.visibility = View.GONE
            }

            when {
                movieDetail.budget != 0L -> {
                    layoutContent.tvBudget.text =
                        "$ " + NumberFormat.getNumberInstance(Locale.US).format(movieDetail.budget)
                }
                else -> {
                    layoutContent.tvBudget.text = "-"
                }
            }

            when (movieDetail.overview) {
                "" -> binding.layoutContent.tvOverview.text = "-"
                else -> binding.layoutContent.tvOverview.text = movieDetail.overview
            }

            when {
                movieDetail.revenue != 0L -> {
                    layoutContent.tvRevenue.text =
                        "$ " + NumberFormat.getNumberInstance(Locale.US).format(movieDetail.revenue)
                }
                else -> {
                    layoutContent.tvRevenue.text = "-"
                }
            }

            when {
                movieDetail.homepage != "" -> {
                    layoutContent.ivLink.visibility = View.VISIBLE
                    layoutContent.ivLink.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(movieDetail.homepage))
                        startActivity(intent)
                    }
                }
                else -> {
                    layoutContent.ivLink.visibility = View.GONE
                }
            }
        }

        if (movieDetail.backdropPath != null || movieDetail.backdropPath != "") {
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
        } else {
            if (movieDetail.posterPath != null || movieDetail.posterPath != "") {
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
            } else {
                binding.ivBackdrops.setImageResource(R.drawable.placeholder_landscape_img)
                toolbarColor(null)
            }
        }

        if (movieDetail.posterPath != null || movieDetail.posterPath != "") {
            Glide.with(this)
                .load("${movieDetail.baseUrl}${movieDetail.posterPath}")
                .centerCrop()
                .transform(RoundedCorners(25))
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.placeholder_portrait_img)
                .into(binding.ivPoster)
        } else {
            if (movieDetail.backdropPath != null || movieDetail.backdropPath != "") {
                Glide.with(this)
                    .load("${movieDetail.baseUrl}${movieDetail.backdropPath}")
                    .centerCrop()
                    .transform(RoundedCorners(25))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.placeholder_portrait_img)
                    .into(binding.ivPoster)

            } else {
                binding.ivPoster.setImageResource(R.drawable.placeholder_portrait_img)

            }
        }

        if (movieDetail.spokenLanguages.isNotEmpty()) {

            spokenLanguageAdapter = SpokenLanguageAdapter()
            binding.layoutContent.rvLanguage.adapter = spokenLanguageAdapter

            binding.layoutContent.rvLanguage.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.layoutContent.rvLanguage.setHasFixedSize(true)

            spokenLanguageAdapter!!.setSpokenLanguage(movieDetail.spokenLanguages)
            spokenLanguageAdapter!!.notifyDataSetChanged()
            binding.layoutContent.rvLanguage.visibility = View.VISIBLE

            binding.layoutContent.tvLanguageEmpty.visibility = View.GONE
        } else {
            binding.layoutContent.rvLanguage.visibility = View.GONE
            binding.layoutContent.tvLanguageEmpty.visibility = View.VISIBLE
        }

        binding.layoutContent.tvMoreCast.setOnClickListener {
            val sendData =
                DetailMovieFragmentDirections.actionDetailMovieFragmentToAllPeopleMovieFragment(
                    movieDetail
                )
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
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.color_primary)
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

    @SuppressLint("ResourceType")
    private fun collectionLayoutColor(resource: Bitmap) {
        binding.layoutContent.apply {
            Palette
                .from(resource)
                .generate { palette ->
                    val darkVibrantSwatch = palette!!.darkVibrantSwatch
                    val dominantSwatch = palette.dominantSwatch
                    val lightVibrantSwatch = palette.lightVibrantSwatch

                    if (dominantSwatch == null) {
                        if (lightVibrantSwatch == null) {
                            btnCollection.setBackgroundColor(darkVibrantSwatch!!.rgb)
                            btnCollection.setTextColor(darkVibrantSwatch.bodyTextColor)
                        } else {
                            btnCollection.setBackgroundColor(lightVibrantSwatch.rgb)
                            btnCollection.setTextColor(lightVibrantSwatch.bodyTextColor)
                        }
                    } else {
                        btnCollection.setBackgroundColor(dominantSwatch.rgb)
                        btnCollection.setTextColor(dominantSwatch.bodyTextColor)
                    }
                }
        }

    }

    private fun getCollection(collection: Collection) {
        binding.layoutContent.cardCollection.visibility = View.VISIBLE
        binding.layoutContent.tvTitleCollection.text =
            resources.getString(R.string.part_of_the, collection.name)

        Glide.with(this)
            .asBitmap()
            .load("${collection.baseUrl}${collection.backdropPath}")
            .centerCrop()
            .error(R.drawable.placeholder_landscape_img)
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
                    collectionLayoutColor(resource!!)
                    return false
                }

            })
            .into(binding.layoutContent.ivBackdropCollection)

        binding.layoutContent.btnCollection.setOnClickListener {
            val sendData =
                DetailMovieFragmentDirections.actionDetailMovieFragmentToCollectionFragment(
                    collection
                )
            Navigation.findNavController(requireView()).navigate(sendData)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getGenreMovie(movieDetail: MovieDetail) {
        binding.layoutContent.apply {
            val layoutManager = FlexboxLayoutManager(requireActivity())
            layoutManager.flexDirection = FlexDirection.ROW
            rvGenre.layoutManager = layoutManager
            rvGenre.setHasFixedSize(true)

            genreListAdapter = GenreListAdapter()
            rvGenre.adapter = genreListAdapter

            if (movieDetail.genre.isNotEmpty()) {
                tvGenreEmpty.visibility = View.GONE
                genreListAdapter!!.setGenre(movieDetail.genre)
                genreListAdapter!!.notifyDataSetChanged()
                rvGenre.visibility = View.VISIBLE
            } else {
                rvGenre.visibility = View.INVISIBLE
                tvGenreEmpty.visibility = View.VISIBLE
            }

            genreListAdapter!!.setOnItemClickListener { genre ->
                val sendData =
                    DetailMovieFragmentDirections.actionDetailMovieFragmentToGenreFragment(genre)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getActorMovie(idMovie: Int) {
        binding.layoutContent.apply {
            rvCast.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvCast.setHasFixedSize(true)

            castDetailAdapter = CastDetailAdapter()
            rvCast.adapter = castDetailAdapter

            shimmerCast.visibility = View.VISIBLE
            shimmerCast.startShimmer()

            viewModel.getCreditMovie(idMovie)
            viewModel.movieCredit.observe(viewLifecycleOwner) { credits ->
                shimmerCast.visibility = View.GONE
                shimmerCast.stopShimmer()

                if (credits!!.cast.isNotEmpty()) {
                    tvCastEmpty.visibility = View.GONE
                    castDetailAdapter!!.setCast(credits.cast)
                    castDetailAdapter!!.notifyDataSetChanged()
                    rvCast.visibility = View.VISIBLE
                    tvMoreCast.visibility = View.VISIBLE
                } else {
                    rvCast.visibility = View.INVISIBLE
                    tvCastEmpty.visibility = View.VISIBLE
                    tvMoreCast.visibility = View.GONE
                }
            }

            castDetailAdapter!!.setOnItemClickListener { cast ->
                val sendData =
                    DetailMovieFragmentDirections.actionDetailMovieFragmentToDetailPersonFragment(
                        cast.id
                    )
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "StringFormatInvalid")
    private fun getRecommend(idMovie: Int) {
        binding.layoutContent.apply {
            rvRecommend.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvRecommend.setHasFixedSize(true)

            movieListSmallAdapter = MovieListSmallAdapter()
            rvRecommend.adapter = movieListSmallAdapter

            shimmerRecommend.visibility = View.VISIBLE
            shimmerRecommend.startShimmer()

            viewModel.getRecommendationMovie(idMovie)
            viewModel.movieRecommendation.observe(viewLifecycleOwner) { movie ->

                shimmerRecommend.visibility = View.GONE
                shimmerRecommend.stopShimmer()

                if (movie!!.results.isNotEmpty()) {
                    tvRecommendEmpty.visibility = View.GONE
                    movieListSmallAdapter!!.setMovie(movie.results)
                    movieListSmallAdapter!!.notifyDataSetChanged()
                    rvRecommend.visibility = View.VISIBLE
                } else {
                    rvRecommend.visibility = View.INVISIBLE
                    tvRecommendEmpty.visibility = View.VISIBLE
                    tvRecommendEmpty.text =
                        resources.getString(R.string.data_for_recommendations_movie_empty, title)
                }
            }


            movieListSmallAdapter!!.setOnItemClickListener { movie ->
                val sendData = DetailMovieFragmentDirections.actionDetailMovieFragmentSelf(movie.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCompany(movieDetail: MovieDetail) {

        binding.layoutContent.apply {
            rvCompany.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvCompany.setHasFixedSize(true)

            companyAdapter = CompanyAdapter()
            rvCompany.adapter = companyAdapter

            if (movieDetail.production.isNotEmpty()) {
                tvCompanyEmpty.visibility = View.GONE
                companyAdapter!!.setCompany(movieDetail.production)
                companyAdapter!!.notifyDataSetChanged()
                rvCompany.visibility = View.VISIBLE
            } else {
                rvCompany.visibility = View.INVISIBLE
                tvCompanyEmpty.visibility = View.VISIBLE
            }

            companyAdapter!!.setOnItemClickListener { company ->
                val sendData =
                    DetailMovieFragmentDirections.actionDetailMovieFragmentToDetailCompanyFragment(
                        company
                    )
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getKeyword(idMovie: Int) {

        binding.layoutContent.apply {
            val layoutManager = FlexboxLayoutManager(requireActivity())
            layoutManager.flexDirection = FlexDirection.ROW
            rvKeyword.layoutManager = layoutManager
            rvKeyword.setHasFixedSize(true)

            keywordListAdapter = KeywordListAdapter()
            rvKeyword.adapter = keywordListAdapter

            shimmerKeyword.visibility = View.VISIBLE
            shimmerKeyword.startShimmer()

            viewModel.getKeywordMovie(idMovie)
            viewModel.movieKeyword.observe(viewLifecycleOwner) { result ->

                shimmerKeyword.visibility = View.GONE
                shimmerKeyword.stopShimmer()

                if (result!!.keywords.isNotEmpty()) {
                    tvKeywordEmpty.visibility = View.GONE
                    keywordListAdapter!!.setKeyword(result.keywords)
                    keywordListAdapter!!.notifyDataSetChanged()
                    rvKeyword.visibility = View.VISIBLE
                } else {
                    rvKeyword.visibility = View.INVISIBLE
                    tvKeywordEmpty.visibility = View.VISIBLE
                }
            }

            keywordListAdapter!!.setOnItemClickListener { keyword ->
                val sendData =
                    DetailMovieFragmentDirections.actionDetailMovieFragmentToKeywordFragment(keyword)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun getTrailerMovie(idMovie: Int) {
        viewModel.getTrailerMovie(idMovie)
        binding.layoutContent.layoutTrailer.setOnClickListener {
            viewModel.movieTrailer.observe(viewLifecycleOwner) { trailer ->
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=${trailer!!.results[0].key}")
                    )
                startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getExternalIds(idMovie: Int) {

        viewModel.getExternalIds(idMovie)
        viewModel.movieExternalIds.observe(viewLifecycleOwner) { external ->
            Log.d("TAG", "onSuccessExternalMovie: $external")

            val facebook = external!!.facebookId
            val twitter = external.twitterId
            val instagram = external.instagramId

            binding.layoutContent.apply {
                if (facebook == "" || facebook == null) {
                    ivFb.visibility = View.GONE
                } else {
                    ivFb.visibility = View.VISIBLE

                    ivFb.setOnClickListener {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://www.facebook.com/${facebook}")
                            )
                        startActivity(intent)
                    }

                }

                if (twitter == "" || twitter == null) {
                    ivTwit.visibility = View.GONE
                } else {
                    ivTwit.visibility = View.VISIBLE

                    ivTwit.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/${twitter}"))
                        startActivity(intent)
                    }
                }

                if (instagram == "" || instagram == null) {
                    ivIg.visibility = View.GONE
                } else {
                    ivIg.visibility = View.VISIBLE

                    ivIg.setOnClickListener {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://www.instagram.com/${instagram}")
                            )
                        startActivity(intent)
                    }
                }
            }

        }
    }

    private fun favoriteMovie(movieDetail: MovieDetail) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkMovie(movieDetail.id)
            withContext(Dispatchers.Main) {
                isFavorite = if (count > 0) {
                    binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
                    true
                } else {
                    binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    false
                }
            }
        }

        binding.fab.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                viewModel.addToFavorite(movieDetail)
                binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
                MotionToast.darkColorToast(
                    requireActivity(),
                    resources.getString(R.string.success),
                    resources.getString(R.string.success_fav, movieDetail.title),
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireActivity(), R.font.quicksand)
                )
            } else {
                viewModel.removeFromFavorite(movieDetail.id)
                MotionToast.darkColorToast(
                    requireActivity(),
                    resources.getString(R.string.delete),
                    resources.getString(R.string.deleted_fav, movieDetail.title),
                    MotionToastStyle.DELETE,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireActivity(), R.font.quicksand)
                )
                binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
    }

    private fun prepareTranslate(detail: MovieDetail){
        val currentLanguage = resources.configuration.locale.language
        if (currentLanguage == "en") {
            goneLoading(detail)
        }else{
            showLoading()
            translator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    // (Set a flag, unhide the translation UI, etc.)
                    if (detail.overview != ""){
                        translator.translate(detail.overview)
                            .addOnSuccessListener { translatedText ->
                                goneLoading(detail)
                                binding.layoutContent.tvOverview.text = translatedText
                            }
                            .addOnFailureListener { exception ->
                                // Error.
                                MotionToast.darkColorToast(
                                    requireActivity(),
                                    "Error",
                                    exception.message.toString(),
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(requireActivity(), R.font.quicksand)
                                )
                            }
                    }else{
                        goneLoading(detail)
                    }

                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    MotionToast.darkColorToast(
                        requireActivity(),
                        "Error",
                        exception.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireActivity(), R.font.quicksand)
                    )
                }
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
            layoutContent.shimmerBudget.visibility = View.VISIBLE
            layoutContent.shimmerBudget.startShimmer()
            layoutContent.shimmerRevenue.visibility = View.VISIBLE
            layoutContent.shimmerRevenue.startShimmer()
            layoutContent.shimmerRuntime.visibility = View.VISIBLE
            layoutContent.shimmerRuntime.startShimmer()
            layoutContent.shimmerTagline.visibility = View.VISIBLE
            layoutContent.shimmerTagline.startShimmer()
            layoutContent.shimmerReleaseDate.visibility = View.VISIBLE
            layoutContent.shimmerReleaseDate.startShimmer()
            layoutContent.shimmerGenre.visibility = View.VISIBLE
            layoutContent.shimmerGenre.startShimmer()
            layoutContent.shimmerLanguage.visibility = View.VISIBLE
            layoutContent.shimmerLanguage.startShimmer()
            layoutContent.shimmerStatus.visibility = View.VISIBLE
            layoutContent.shimmerStatus.startShimmer()
        }
    }

    private fun goneLoading(detail: MovieDetail){
        binding.apply {
            ivBackdropsShimmer.visibility = View.GONE
            ivBackdropsShimmer.stopShimmer()
            ivPosterShimmer.visibility = View.GONE
            ivPosterShimmer.stopShimmer()
            ivPoster.visibility = View.VISIBLE
            ivBackdrops.visibility = View.VISIBLE
            layoutContent.shimmerOverview.visibility = View.GONE
            layoutContent.shimmerOverview.stopShimmer()
            layoutContent.shimmerBudget.visibility = View.GONE
            layoutContent.shimmerBudget.stopShimmer()
            layoutContent.shimmerRevenue.visibility = View.GONE
            layoutContent.shimmerRevenue.stopShimmer()
            layoutContent.shimmerRuntime.visibility = View.GONE
            layoutContent.shimmerRuntime.stopShimmer()
            layoutContent.shimmerTagline.visibility = View.GONE
            layoutContent.shimmerTagline.stopShimmer()
            layoutContent.shimmerReleaseDate.visibility = View.GONE
            layoutContent.shimmerReleaseDate.stopShimmer()
            layoutContent.shimmerLanguage.visibility = View.GONE
            layoutContent.shimmerLanguage.stopShimmer()
            layoutContent.shimmerStatus.visibility = View.GONE
            layoutContent.shimmerStatus.stopShimmer()
            layoutContent.shimmerGenre.visibility = View.GONE
            layoutContent.shimmerGenre.stopShimmer()
            layoutContent.tvOverview.visibility = View.VISIBLE
            layoutContent.tvBudget.visibility = View.VISIBLE
            layoutContent.tvRuntime.visibility = View.VISIBLE
            layoutContent.tvRevenue.visibility = View.VISIBLE
            layoutContent.tvTagline.visibility = View.VISIBLE
            layoutContent.tvReleaseDate.visibility = View.VISIBLE
            layoutContent.rvLanguage.visibility = View.VISIBLE
            layoutContent.tvStatus.visibility = View.VISIBLE
        }

        populateMovie(detail)
        getGenreMovie(detail)
        getCompany(detail)
        getActorMovie(detail.id)
        getRecommend(detail.id)
        getTrailerMovie(detail.id)
        getKeyword(detail.id)
        getExternalIds(detail.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentDetailMovieBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDetailMovieBinding = null
    }


}