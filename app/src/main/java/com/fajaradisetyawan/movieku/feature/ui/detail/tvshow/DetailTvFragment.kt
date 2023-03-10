/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
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
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.data.model.tvshow.Seasons
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.databinding.FragmentDetailTvBinding
import com.fajaradisetyawan.movieku.feature.adapter.*
import com.fajaradisetyawan.movieku.feature.adapter.detail.CastDetailAdapter
import com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.viewmodel.DetailTvShowViewModels
import com.fajaradisetyawan.movieku.utils.CustomToastDialog
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.fajaradisetyawan.movieku.utils.Translator
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import render.animations.Fade
import render.animations.Render
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class DetailTvFragment : Fragment() {

    private var fragmentDetailTvBinding: FragmentDetailTvBinding? = null
    private val binding get() = fragmentDetailTvBinding!!

    private var adapterRecommend: TvShowListSmallAdapter? = null
    private var keywordListAdapter: KeywordListAdapter? = null
    private var networkListAdapter: NetworkListAdapter? = null
    private var genreListAdapter: GenreListAdapter? = null
    private var creatorAdapter: CreatorAdapter? = null
    private var castDetailAdapter: CastDetailAdapter? = null
    private var spokenLanguageAdapter: SpokenLanguageAdapter? = null

    private val viewModel by viewModels<DetailTvShowViewModels>()
    private var isFavorite: Boolean = false
    private var isWatchList: Boolean = false
    private val args by navArgs<DetailTvFragmentArgs>()

    var duration = ""
    var title = ""

    private var currentLanguage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDetailTvBinding = FragmentDetailTvBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentLanguage = resources.configuration.locale.language

        val tvId = args.tvShowId

        binding.apply {
            viewModel.getDetailTvShow(tvId)
            viewModel.tvDetail.observe(viewLifecycleOwner) { detail ->
                Log.d("TAG", "onSuccessDetailTvShow: $detail")
                title = detail!!.name
                binding.collapsingToolbar.title = title

                val activity = activity as AppCompatActivity?

                activity!!.setSupportActionBar(binding.toolbar)
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                activity.supportActionBar?.setDisplayShowHomeEnabled(true)
                binding.toolbar.setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }

                activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

                prepareTranslate(detail)

                if (detail.seasons.isEmpty()){
                    binding.layoutContent.cardCollection.visibility = View.GONE
                }else{
                    binding.layoutContent.cardCollection.visibility = View.VISIBLE
                }

                /**
                 * Part of Favorite.
                 */
                favoriteTvShow(detail)

                /**
                 * Part of Watchlist.
                 */
                watchListTv(detail)
            }
        }

        binding.fabTop.setOnClickListener {
            binding.nested.post {
                binding.nested.fling(0)
                binding.nested.smoothScrollTo(0, 0)
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun populateTvShow(tvShowDetail: TvShowDetail) {

        with(binding.layoutContent) {

            val progress = tvShowDetail.voteAverage * 10

            if (progress >= 70) {
                progressCircular.progressColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_green_A700)
                progressCircular.dotColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_green_900)
            } else {
                progressCircular.progressColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_yellow_500)
                progressCircular.dotColor =
                    ContextCompat.getColor(requireActivity(), R.color.md_yellow_800)
            }

            progressCircular.setProgress(progress, 100.0)
            progressCircular.maxProgress = 100.0

            val voteCount = NumberFormat.getNumberInstance(Locale.US).format(tvShowDetail.voteCount)
            tvVote.text = resources.getString(R.string.vote, voteCount)

            tvReleaseDate.text = ParseDateTime.parseDate(tvShowDetail.firstAirDate)

            if (tvShowDetail.episodeRunTime!!.isNotEmpty()) {
                val hours: Int = tvShowDetail.episodeRunTime!![0] / 60
                val minutes: Int = tvShowDetail.episodeRunTime!![0] % 60

                duration = if (tvShowDetail.episodeRunTime!![0] <= 60) {
                    resources.getString(R.string.timemin, tvShowDetail.episodeRunTime!![0])
                } else {
                    resources.getString(R.string.time, hours, minutes)
                }
                tvRuntime.text = duration

            } else {
                tvRuntime.text = "-"
            }

            if (tvShowDetail.tagline == "") {
                tvTagline.visibility = View.GONE

            } else {
                tvTagline.text = tvShowDetail.tagline
                tvTagline.visibility = View.VISIBLE
            }

            when (tvShowDetail.overview) {
                "" -> tvOverview.text = "-"
                else -> tvOverview.text = tvShowDetail.overview
            }

            tvStatus.text = tvShowDetail.status
            tvType.text = tvShowDetail.type


            if (tvShowDetail.spokenLanguages.isNotEmpty()) {

                spokenLanguageAdapter = SpokenLanguageAdapter()
                rvLanguage.adapter = spokenLanguageAdapter

                rvLanguage.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                rvLanguage.setHasFixedSize(true)

                spokenLanguageAdapter!!.setSpokenLanguage(tvShowDetail.spokenLanguages)
                spokenLanguageAdapter!!.notifyDataSetChanged()
                rvLanguage.visibility = View.VISIBLE

                tvLanguageEmpty.visibility = View.GONE
            } else {
                rvLanguage.visibility = View.GONE
                tvLanguageEmpty.visibility = View.VISIBLE
            }

            when {
                tvShowDetail.homepage != "" -> {
                    ivLink.visibility = View.VISIBLE
                    ivLink.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(tvShowDetail.homepage))
                        startActivity(intent)
                    }
                }
                else -> {
                    ivLink.visibility = View.GONE
                }
            }
        }

        if (tvShowDetail.backdropPath != null) {
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
        } else {
            if (tvShowDetail.posterPath != null) {
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
            } else {
                binding.ivBackdrops.setImageResource(R.drawable.placeholder_landscape_img)
                binding.ivPoster.visibility = View.GONE
                toolbarColor(null)
            }
        }

        if (tvShowDetail.posterPath != null) {
            binding.ivPoster.visibility = View.VISIBLE
            Glide.with(this)
                .load("${tvShowDetail.baseUrl}${tvShowDetail.posterPath}")
                .centerCrop()
                .transform(RoundedCorners(25))
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.placeholder_portrait_img)
                .into(binding.ivPoster)
        } else {
            binding.ivPoster.visibility = View.GONE
        }

        binding.layoutContent.tvMoreCast.setOnClickListener {
            val sendData =
                DetailTvFragmentDirections.actionDetailTvFragmentToAllPeopleTvShowFragment(
                    tvShowDetail
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
        val animation = Render(requireActivity())

        if (colorToolbar != null && textColor != null) {
            binding.layoutContent.tvSeason.setTextColor(textColor)
            binding.layoutContent.tvOverviewSeason.setTextColor(textColor)
            binding.layoutContent.tvEpisode.setTextColor(textColor)
            binding.layoutContent.tvYearSeason.setTextColor(textColor)
            binding.layoutContent.cardCollection.background.setTint(colorToolbar)
            binding.layoutContent.divider.setBackgroundColor(textColor)
            requireActivity().window.statusBarColor = colorToolbar
            nav.setTint(textColor)
            binding.cardBottomSheet.setCardBackgroundColor(colorToolbar)
        } else {
            binding.layoutContent.tvSeason.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.layoutContent.tvOverviewSeason.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.layoutContent.tvEpisode.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.cardBottomSheet.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.color_primary
                )
            )
            binding.layoutContent.tvYearSeason.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.layoutContent.cardCollection.background.setTint(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.color_primary
                )
            )
            binding.layoutContent.divider.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
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
                    binding.fabTop.visibility = View.VISIBLE
                    animation.setAnimation(Fade.InUp(binding.fabTop))
                    animation.start()
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
                    binding.fabTop.visibility = View.GONE
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.collapsingToolbar.setCollapsedTitleTextColor(R.color.text_color)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getGenre(tvShowDetail: TvShowDetail) {
        binding.layoutContent.apply {
            val layoutManager = FlexboxLayoutManager(requireActivity())
            layoutManager.flexDirection = FlexDirection.ROW
            rvGenre.layoutManager = layoutManager
            rvGenre.setHasFixedSize(true)

            genreListAdapter = GenreListAdapter()
            rvGenre.adapter = genreListAdapter

            if (tvShowDetail.genre.isNotEmpty()) {
                tvGenreEmpty.visibility = View.GONE
                genreListAdapter!!.setGenre(tvShowDetail.genre)
                genreListAdapter!!.notifyDataSetChanged()
                rvGenre.visibility = View.VISIBLE
            } else {
                rvGenre.visibility = View.INVISIBLE
                tvGenreEmpty.visibility = View.VISIBLE
            }

            genreListAdapter!!.setOnItemClickListener { genre ->
                val sendData =
                    DetailTvFragmentDirections.actionDetailTvFragmentToGenreFragment(genre)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSeason(tvShowDetail: TvShowDetail) {
        binding.layoutContent.apply {
            // reverse list season
            val seasons: Seasons = tvShowDetail.seasons.reversed()[0]

            tvSeason.text = seasons.name
            tvEpisode.text = resources.getString(R.string.episode, seasons.episodeCount)
            if (seasons.airDate == null) {
                tvYearSeason.text = "-"
            } else {
                tvYearSeason.text = ParseDateTime.getYear(seasons.airDate)
            }

            val yearSeasons = tvYearSeason.text.toString()

            if (seasons.overview == "") {
                tvOverviewSeason.text = resources.getString(
                    R.string.overview_season_empty,
                    seasons.name,
                    title,
                    yearSeasons
                )
            } else {
                if (currentLanguage != "en") {
                    Translator.translator.translate(seasons.overview!!)
                        .addOnSuccessListener { translatedText ->
                            tvOverviewSeason.text = translatedText
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
                    tvOverviewSeason.text = seasons.overview
                }

            }

            Glide.with(requireActivity())
                .asBitmap()
                .load("${seasons.baseUrl}${seasons.posterPath}")
                .centerCrop()
                .into(ivPosterSeason)

            cardCollection.setOnClickListener {
                if(seasons.episodes != null) {
                    val sendData =
                        DetailTvFragmentDirections.actionDetailTvFragmentToSeasonFragment(
                            tvShowDetail,
                            seasons
                        )
                    Navigation.findNavController(requireView()).navigate(sendData)
                }
            }

            tvAllSeason.setOnClickListener {
                val sendData = DetailTvFragmentDirections.actionDetailTvFragmentToAllSeasonFragment(
                    tvShowDetail
                )
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getKeyword(idTv: Int) {

        binding.layoutContent.apply {
            val layoutManager = FlexboxLayoutManager(requireActivity())
            layoutManager.flexDirection = FlexDirection.ROW
            rvKeyword.layoutManager = layoutManager
            rvKeyword.setHasFixedSize(true)

            keywordListAdapter = KeywordListAdapter()
            rvKeyword.adapter = keywordListAdapter

            shimmerKeyword.visibility = View.VISIBLE
            shimmerKeyword.startShimmer()

            viewModel.getKeywordTvShow(idTv)
            viewModel.tvKeyword.observe(viewLifecycleOwner) { keyword ->

                shimmerKeyword.visibility = View.GONE
                shimmerKeyword.stopShimmer()

                if (keyword!!.results.isNotEmpty()) {
                    tvKeywordEmpty.visibility = View.GONE
                    keywordListAdapter!!.setKeyword(keyword.results)
                    keywordListAdapter!!.notifyDataSetChanged()
                    rvKeyword.visibility = View.VISIBLE
                } else {
                    rvKeyword.visibility = View.GONE
                    tvKeywordEmpty.visibility = View.VISIBLE
                }
            }

            keywordListAdapter!!.setOnItemClickListener { keyword ->
                val sendData =
                    DetailTvFragmentDirections.actionDetailTvFragmentToKeywordFragment(keyword)
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

            adapterRecommend = TvShowListSmallAdapter()
            rvRecommend.adapter = adapterRecommend

            shimmerRecommend.visibility = View.VISIBLE
            shimmerRecommend.startShimmer()

            viewModel.getRecommendationTv(idMovie)
            viewModel.tvRecommendation.observe(viewLifecycleOwner) { tvShow ->

                shimmerRecommend.visibility = View.GONE
                shimmerRecommend.stopShimmer()

                if (tvShow!!.results.isNotEmpty()) {
                    tvRecommendEmpty.visibility = View.GONE
                    adapterRecommend!!.setTvShow(tvShow.results)
                    adapterRecommend!!.notifyDataSetChanged()
                    rvRecommend.visibility = View.VISIBLE
                } else {
                    rvRecommend.visibility = View.INVISIBLE
                    tvRecommendEmpty.visibility = View.VISIBLE
                    tvRecommendEmpty.text =
                        resources.getString(R.string.data_for_recommendations_tv_empty, title)
                }
            }

            adapterRecommend!!.setOnItemClickListener { movie ->
                val sendData = DetailTvFragmentDirections.actionDetailTvFragmentSelf(movie.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    private fun getTrailerLink(idTv: Int) {
        viewModel.getTrailerTv(idTv)
        binding.layoutContent.layoutTrailer.setOnClickListener {
            viewModel.tvTrailer.observe(viewLifecycleOwner) { video ->
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=${video!!.results[0].key}")
                    )
                startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNetwork(tvShowDetail: TvShowDetail) {
        binding.layoutContent.apply {
            rvNetwork.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvNetwork.setHasFixedSize(true)

            networkListAdapter = NetworkListAdapter()
            rvNetwork.adapter = networkListAdapter

            if (tvShowDetail.networks.isNotEmpty()) {
                networkListAdapter!!.setNetwork(tvShowDetail.networks)
                networkListAdapter!!.notifyDataSetChanged()
                rvNetwork.visibility = View.VISIBLE
                tvNetworkEmpty.visibility = View.GONE
            } else {
                rvNetwork.visibility = View.INVISIBLE
                tvNetworkEmpty.visibility = View.VISIBLE
            }

            networkListAdapter!!.setOnItemClickListener { network ->
                val sendData =
                    DetailTvFragmentDirections.actionDetailTvFragmentToDetailNetworkFragment(network)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCreator(tvShowDetail: TvShowDetail) {
        binding.layoutContent.apply {
            rvCreator.setHasFixedSize(true)

            creatorAdapter = CreatorAdapter()
            rvCreator.adapter = creatorAdapter

            if (tvShowDetail.createdBy.isNotEmpty()) {
                creatorAdapter!!.setCreator(tvShowDetail.createdBy)
                creatorAdapter!!.notifyDataSetChanged()
                rvCreator.visibility = View.VISIBLE
            } else {
                rvCreator.visibility = View.GONE
            }

            creatorAdapter!!.setOnItemClickListener { creator ->
                val sendData =
                    DetailTvFragmentDirections.actionDetailTvFragmentToDetailPersonFragment(creator.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCast(idTv: Int) {
        binding.layoutContent.apply {
            rvCast.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            rvCast.setHasFixedSize(true)

            castDetailAdapter = CastDetailAdapter()
            rvCast.adapter = castDetailAdapter

            shimmerCast.visibility = View.VISIBLE
            shimmerCast.startShimmer()

            viewModel.getCreditsTv(idTv)
            viewModel.tvCredits.observe(viewLifecycleOwner) { results ->

                shimmerCast.visibility = View.GONE
                shimmerCast.stopShimmer()

                if (results!!.cast.isNotEmpty()) {
                    tvCastEmpty.visibility = View.GONE
                    castDetailAdapter!!.setCast(results.cast)
                    castDetailAdapter!!.notifyDataSetChanged()
                    rvCast.visibility = View.VISIBLE
                } else {
                    rvCast.visibility = View.INVISIBLE
                    tvCastEmpty.visibility = View.VISIBLE
                    tvMoreCast.visibility = View.GONE
                }
            }

            castDetailAdapter!!.setOnItemClickListener { cast ->
                val sendData =
                    DetailTvFragmentDirections.actionDetailTvFragmentToDetailPersonFragment(cast.id)
                Navigation.findNavController(requireView()).navigate(sendData)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getExternalLink(idTv: Int) {

        viewModel.getExternalIds(idTv)
        viewModel.tvExternalIds.observe(viewLifecycleOwner) { external ->
            Log.d("TAG", "onSuccessExternalTv: $external")

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

    /**
     * Part of Favorite.
     */
    private fun favoriteTvShow(detail: TvShowDetail) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkTvShow(detail.id)
            withContext(Dispatchers.Main) {
                isFavorite = if (count > 0) {
                    binding.btnFav.setIconResource(R.drawable.ic_baseline_favorite_24)
                    true
                } else {
                    binding.btnFav.setIconResource(R.drawable.ic_baseline_favorite_border_24)
                    false
                }
            }
        }

        binding.btnFav.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                viewModel.addToFavorite(detail)
                CustomToastDialog.successToast(
                    requireActivity(),
                    resources.getString(R.string.success),
                    resources.getString(R.string.success_fav, detail.name)
                )
                binding.btnFav.setIconResource(R.drawable.ic_baseline_favorite_24)
            } else {
                viewModel.removeFromFavorite(detail.id)
                CustomToastDialog.deleteToast(
                    requireActivity(),
                    resources.getString(R.string.delete),
                    resources.getString(R.string.deleted_fav, detail.name)
                )
                binding.btnFav.setIconResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
    }

    /**
     * Part of Watchlist.
     */
    private fun watchListTv(detail: TvShowDetail) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkTvShowWatchList(detail.id)
            withContext(Dispatchers.Main) {
                isWatchList = if (count > 0) {
                    binding.btnWishlist.setIconResource(R.drawable.ic_bookmark_filled)
                    true
                } else {
                    binding.btnWishlist.setIconResource(R.drawable.ic_bookmark_outline)
                    false
                }
            }
        }

        binding.btnWishlist.setOnClickListener {
            isWatchList = !isWatchList
            if (isWatchList) {
                viewModel.addToWatchList(detail)
                CustomToastDialog.successToast(
                    requireActivity(),
                    resources.getString(R.string.success),
                    resources.getString(R.string.success_watchlist, detail.name)
                )
                binding.btnWishlist.setIconResource(R.drawable.ic_bookmark_filled)
            } else {
                viewModel.removeWatchLis(detail.id)
                CustomToastDialog.deleteToast(
                    requireActivity(),
                    resources.getString(R.string.delete),
                    resources.getString(R.string.deleted_watchlist, detail.name)
                )
                binding.btnWishlist.setIconResource(R.drawable.ic_bookmark_outline)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareTranslate(detail: TvShowDetail) {
        showLoading()
        if (detail.overview != ""){
            if (currentLanguage != "en"){
                Translator.translator.translate(detail.overview)
                    .addOnSuccessListener { translatedText ->
                        goneLoading(detail)
                        binding.layoutContent.tvOverview.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        // Error.
                        CustomToastDialog.errorToast(requireActivity(), "Error", exception.message.toString())
                    }
            }else{
                goneLoading(detail)
                binding.layoutContent.tvOverview.text = detail.overview
            }
        }else{
            goneLoading(detail)
            binding.layoutContent.tvOverview.text = "-"
        }
    }

    private fun showLoading() {
        binding.apply {
            ivBackdropsShimmer.visibility = View.VISIBLE
            ivBackdropsShimmer.startShimmer()
            ivPosterShimmer.visibility = View.VISIBLE
            ivPosterShimmer.startShimmer()
            ivPoster.visibility = View.GONE
            ivBackdrops.visibility = View.GONE
            layoutContent.shimmerOverview.visibility = View.VISIBLE
            layoutContent.shimmerOverview.startShimmer()
            layoutContent.shimmerGenre.visibility = View.VISIBLE
            layoutContent.shimmerGenre.startShimmer()
            layoutContent.shimmerType.visibility = View.VISIBLE
            layoutContent.shimmerType.startShimmer()
            layoutContent.shimmerRuntime.visibility = View.VISIBLE
            layoutContent.shimmerRuntime.startShimmer()
            layoutContent.shimmerTagline.visibility = View.VISIBLE
            layoutContent.shimmerTagline.startShimmer()
            layoutContent.shimmerReleaseDate.visibility = View.VISIBLE
            layoutContent.shimmerReleaseDate.startShimmer()
            layoutContent.shimmerLanguage.visibility = View.VISIBLE
            layoutContent.shimmerLanguage.startShimmer()
            layoutContent.shimmerStatus.visibility = View.VISIBLE
            layoutContent.shimmerStatus.startShimmer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun goneLoading(detail: TvShowDetail) {
        binding.apply {
            ivBackdropsShimmer.visibility = View.GONE
            ivBackdropsShimmer.stopShimmer()
            ivPosterShimmer.visibility = View.GONE
            ivPosterShimmer.stopShimmer()
            ivPoster.visibility = View.VISIBLE
            ivBackdrops.visibility = View.VISIBLE
            layoutContent.shimmerOverview.visibility = View.GONE
            layoutContent.shimmerOverview.stopShimmer()
            layoutContent.shimmerGenre.visibility = View.GONE
            layoutContent.shimmerGenre.stopShimmer()
            layoutContent.shimmerType.visibility = View.GONE
            layoutContent.shimmerType.stopShimmer()
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
            layoutContent.tvOverview.visibility = View.VISIBLE
            layoutContent.tvType.visibility = View.VISIBLE
            layoutContent.tvRuntime.visibility = View.VISIBLE
            layoutContent.tvTagline.visibility = View.VISIBLE
            layoutContent.tvReleaseDate.visibility = View.VISIBLE
            layoutContent.rvLanguage.visibility = View.VISIBLE
            layoutContent.tvStatus.visibility = View.VISIBLE
        }

        populateTvShow(detail)
        getGenre(detail)
        getNetwork(detail)
        getCreator(detail)
        if (detail.seasons.isNotEmpty()){
            getSeason(detail)
        }
        getRecommend(detail.id)
        getTrailerLink(detail.id)
        getKeyword(detail.id)
        getExternalLink(detail.id)
        getCast(detail.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentDetailTvBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDetailTvBinding = null
    }
}