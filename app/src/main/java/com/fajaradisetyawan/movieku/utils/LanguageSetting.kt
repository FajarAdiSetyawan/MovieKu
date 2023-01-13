/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.*


object LanguageSetting {
    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_INDONESIA = "in"
    private const val PREFERENCE_LANGUAGE = "pref_language"
    private const val KEY_LANGUAGE = "key_language"
    var defaultLanguage = Locale.ENGLISH.language
    var language = Locale.ENGLISH.language
        private set

    fun setLanguage(context: Context, language: String) {
        LanguageSetting.language = language
        val editor = getLanguagePreference(context).edit()
        editor.putString(KEY_LANGUAGE, language)
        editor.apply()
    }

    fun setLanguage(context: Context, locale: Locale) {
        setLanguage(context, locale.language)
    }

    private fun getLanguage(context: Context): String? {
        return getLanguagePreference(context).getString(KEY_LANGUAGE, defaultLanguage)
    }

    private fun getLanguagePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_LANGUAGE, Activity.MODE_PRIVATE)
    }

    fun setDefaultLanguage(locale: Locale) {
        defaultLanguage = locale.language
    }

    val locale: Locale
        get() = getLocale(language)

    fun getLocale(context: Context): Locale {
        return getLocale(getLanguage(context))
    }

    private fun getLocale(language: String?): Locale {
        return Locale(language!!.lowercase(Locale.getDefault()))
    }
}