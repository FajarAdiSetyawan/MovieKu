/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.people.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.adapter.AlsoKnownAdapter
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.databinding.FragmentDetailPeopleBinding
import com.fajaradisetyawan.movieku.ui.detail.people.detail.viewmodel.DetailPeopleViewModel
import com.fajaradisetyawan.movieku.utils.ParseDateTime
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPeopleFragment : Fragment() {
    private var fragmentDetailPeopleBinding: FragmentDetailPeopleBinding? = null
    private val binding get() = fragmentDetailPeopleBinding!!

    private val viewModel by viewModels<DetailPeopleViewModel>()

    private val args by navArgs<DetailPeopleFragmentArgs>()

    private var adapter: AlsoKnownAdapter? = null

    var peopleId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDetailPeopleBinding =
            FragmentDetailPeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        peopleId = args.personId

        viewModel.getDetailPeople(peopleId)
        viewModel.detailPeople.observe(viewLifecycleOwner) { detail ->
            binding.collapsingToolbar.title = detail!!.name
            toolbar()
            populatePerson(detail)
        }

        getExternalIds(peopleId)

        binding.layoutContent.apply {
            val pagerAdapterPerson = ViewPagerAdapter(requireActivity(), peopleId)
            pagerPerson.adapter = pagerAdapterPerson
            pagerPerson.isUserInputEnabled = false

            TabLayoutMediator(
                tabPerson,
                pagerPerson
            ) { tab, position ->
                val tabNames =
                    listOf(
                        resources.getString(R.string.movie),
                        resources.getString(R.string.tvshow)
                    )
                tab.text = tabNames[position]
            }.attach()
        }
    }

    private fun toolbar() {
        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.color_primary)

        val nav: Drawable = binding.toolbar.navigationIcon!!
        nav.setTint(ContextCompat.getColor(requireActivity(), R.color.white))
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
                    binding.toolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.color_primary
                        )
                    )
                    binding.ivProfile.visibility = View.GONE
                } else {
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.ivProfile.visibility = View.VISIBLE
                }
            }
        })
    }

    internal class ViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val peopleId: Int
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            //bundle to send genre id
            val bundle = Bundle()
            bundle.putInt("peopleId", peopleId)

            val movieCast = MoviePeopleFragment()
            val tvCast = TvShowPeopleFragment()

            movieCast.arguments = bundle
            tvCast.arguments = bundle

            return when (position) {
                0 -> movieCast
                else -> tvCast
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun populatePerson(people: PeopleDetail) {
        binding.layoutContent.apply {

            tvKnowFor.text = people.department

            if (people.place == "" || people.place == null) {
                tvPlace.text = "-"
            } else {
                tvPlace.text = people.place
            }

            if (people.homepage == "" || people.homepage == null) {
                ivLink.visibility = View.GONE
            } else {
                ivLink.visibility = View.VISIBLE
                ivLink.setOnClickListener {
                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(people.homepage)
                        )
                    startActivity(intent)
                }
            }

            if (people.deathday.equals(null) || people.deathday == "" || people.deathday == null) {
                layoutDeathday.visibility = View.GONE
            } else {
                layoutDeathday.visibility = View.VISIBLE
                tvDeathday.text = people.deathday
            }

            if (people.birthday.equals(null) || people.birthday == "" || people.birthday == null) {
                layoutBirthdate.visibility = View.GONE
            } else {
                layoutBirthdate.visibility = View.VISIBLE
                tvBirthdate.text = resources.getString(
                    R.string.birthdate_with_age, people.birthday, ParseDateTime.getAge(
                        people.birthday!!
                    )
                )
            }

            if (people.biography.equals("") || people.biography == "") {
                tvBiography.text = resources.getString(R.string.biography_empty, people.name)
            } else {
                tvBiography.text = people.biography
            }

            when (people.gender) {
                1 -> {
                    tvGender.text = resources.getString(R.string.female)
                }
                2 -> {
                    tvGender.text = resources.getString(R.string.male)
                }
                else -> {
                    tvGender.text = "-"
                }
            }

            if (people.profilePath == "" || people.profilePath == null) {
                when (people.gender) {
                    1 -> {
                        binding.ivProfile.setImageResource(R.drawable.placeholder_avatar_w)
                    }
                    2 -> {
                        binding.ivProfile.setImageResource(R.drawable.placeholder_avatar_m)
                    }
                    else -> {
                        binding.ivProfile.setImageResource(R.drawable.placeholder_avatar_o)
                    }
                }
            } else {
                Glide.with(requireActivity())
                    .load("${people.baseUrl}${people.profilePath}")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.placeholder_avatar_o)
                    .transform(RoundedCorners(25))
                    .into(binding.ivProfile)
            }



            if (people.alsoKnownAs.isNotEmpty()){
                layoutAlsoKnownAs.visibility = View.VISIBLE

                rvAlso.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                rvAlso.setHasFixedSize(true)

                adapter = AlsoKnownAdapter(people.alsoKnownAs)
                rvAlso.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }else{
                layoutAlsoKnownAs.visibility = View.GONE
            }


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getExternalIds(idPeople: Int) {

        viewModel.getExternalPeople(idPeople)
        viewModel.externalPeople.observe(viewLifecycleOwner) { external ->
            Log.d("TAG", "onSuccessExternalPerson: $external")
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

    override fun onDestroy() {
        super.onDestroy()
        fragmentDetailPeopleBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDetailPeopleBinding = null
    }

}