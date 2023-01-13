/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import androidx.navigation.Navigation
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.feature.ui.people.PopularPersonFragmentDirections
import com.fajaradisetyawan.movieku.utils.LanguageSetting.defaultLanguage
import com.fajaradisetyawan.movieku.utils.LanguageSetting.getLocale
import com.fajaradisetyawan.movieku.utils.LanguageSetting.setLanguage
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class LocalizationAgent (private val activity: Activity) {
    // Boolean flag to check that activity was recreated from locale changed.
    private var isLocalizationChanged = false

    // Prepare default language.
    private var currentLanguage = defaultLanguage
    private val localeChangedListeners: MutableList<OnLocaleChangedListener> = ArrayList()
    fun addOnLocaleChangedListener(onLocaleChangedListener: OnLocaleChangedListener) {
        localeChangedListeners.add(onLocaleChangedListener)
    }

    fun onCreate() {
        setupLanguage()
        checkBeforeLocaleChanging()
    }

    // Provide method to set application language by locale.
    fun setLanguages(locale: Locale) {
        language = locale.language
    }

    fun setDefaultLanguage(language: String?) {
        defaultLanguage = language!!
    }

    fun setDefaultLanguage(locale: Locale) {
        defaultLanguage = locale.language
    }

    // Provide method to set application language by country name.
    // Get current language
    var language: String
        get() = LanguageSetting.language
        set(language) {
            if (isCurrentLanguageSetting(language)) {
                setLanguage(activity, language)
                notifyLanguageChanged()
            }
        }

    // Get current locale
    val locale: Locale
        get() = getLocale(activity)

    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private fun checkBeforeLocaleChanging() {
        val isLocalizationChanged =
            activity.intent.getBooleanExtra(KEY_ACTIVITY_LOCALE_CHANGED, false)
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true
            activity.intent.removeExtra(KEY_ACTIVITY_LOCALE_CHANGED)
        }
    }

    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private fun setupLanguage() {
        val locale = getLocale(activity)
        setupLocale(locale)
        currentLanguage = locale.language
        setLanguage(activity, locale.language)
    }

    // Set locale configuration.
    private fun setupLocale(locale: Locale) {
        updateLocaleConfiguration(activity, locale)
    }

    private fun updateLocaleConfiguration(context: Context, locale: Locale) {
        val config = context.resources.configuration
        config.locale = locale
        val dm = context.resources.displayMetrics
        context.resources.updateConfiguration(config, dm)
    }

    // Avoid duplicated setup
    private fun isCurrentLanguageSetting(language: String): Boolean {
        return language != LanguageSetting.language
    }

    // Let's take it change! (Using recreate method that available on API 11 or more.
    private fun notifyLanguageChanged() {
        sendOnBeforeLocaleChangedEvent()
        activity.intent.putExtra(KEY_ACTIVITY_LOCALE_CHANGED, true)
        callDummyActivity()
        activity.recreate()
    }

    // If activity is run to backStack. So we have to check if this activity is resume working.
    fun onResume() {
        Handler().post {
            checkLocaleChange()
            checkAfterLocaleChanging()
        }
    }

    // Check if locale has change while this activity was run to backStack.
    private fun checkLocaleChange() {
        if (isCurrentLanguageSetting(currentLanguage)) {
            sendOnBeforeLocaleChangedEvent()
            isLocalizationChanged = true
            callDummyActivity()
            activity.recreate()
        }
    }

    // Call override method if local is really changed
    private fun checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            sendOnAfterLocaleChangedEvent()
            isLocalizationChanged = false
        }
    }

    private fun sendOnBeforeLocaleChangedEvent() {
        for (changedListener in localeChangedListeners) {
            changedListener.beforeLocaleChanged()
        }
    }

    private fun sendOnAfterLocaleChangedEvent() {
        for (listener in localeChangedListeners) {
            listener.afterLocaleChanged()
        }
    }

    private fun callDummyActivity() {
//        activity.startActivity(Intent(activity, SplashScreenActivity::class.java))
        val bottomNavigationView: BottomNavigationView =
            activity.findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView.menu.findItem(R.id.homeFragment).isChecked = true
        activity.recreate()
    }

    companion object {
        private const val KEY_ACTIVITY_LOCALE_CHANGED = "activity_locale_changed"
    }
}