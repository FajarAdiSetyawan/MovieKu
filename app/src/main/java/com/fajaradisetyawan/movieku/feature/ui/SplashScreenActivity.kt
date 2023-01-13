/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.ActivitySplashScreenBinding
import com.fajaradisetyawan.movieku.utils.Animatoo
import com.fajaradisetyawan.movieku.utils.LocalizationAgent
import com.fajaradisetyawan.movieku.utils.OnLocaleChangedListener
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity(), OnLocaleChangedListener {

    private var activitySplashScreenBinding: ActivitySplashScreenBinding? = null
    private val binding get() = activitySplashScreenBinding!!

    private var localizationAgent: LocalizationAgent? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadTheme()

        window.decorView.windowInsetsController!!.hide(
            WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
        )

        window.decorView.windowInsetsController!!.hide(
            WindowInsets.Type.statusBars()
        )

        localizationAgent = LocalizationAgent(this)
        localizationAgent?.addOnLocaleChangedListener(this@SplashScreenActivity)
        localizationAgent?.onCreate()
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            Animatoo.animateSlideUp(this)
            finish()
        }, 3000)
    }

    private fun loadTheme() {
        val preference =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val theme = preference.getString(
            getString(R.string.pref_key_theme),
            getString(R.string.pref_theme_entry_auto)
        )
        when {
            theme.equals(getString(R.string.pref_theme_entry_auto)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            theme.equals(getString(R.string.pref_theme_entry_dark)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            theme.equals(getString(R.string.pref_theme_entry_light)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun updateTheme(mode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(mode)
        return true
    }

    override fun beforeLocaleChanged() = Unit
    override fun afterLocaleChanged() = Unit
}