/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.setting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatSpinner
import androidx.navigation.Navigation
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.feature.ui.people.PopularPersonFragmentDirections
import com.fajaradisetyawan.movieku.utils.LanguageSetting
import com.fajaradisetyawan.movieku.utils.LocalizationAgent
import com.fajaradisetyawan.movieku.utils.OnLocaleChangedListener
import com.google.android.material.bottomnavigation.BottomNavigationView


class PreferenceFragment : PreferenceFragmentCompat(), OnLocaleChangedListener {

    private lateinit var prefTheme: ListPreference
    private lateinit var prefLang: Preference
    private lateinit var prefShare: Preference

    private var localizationAgent: LocalizationAgent? = null


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_setting)
        initComponents()

        localizationAgent = LocalizationAgent(requireActivity())
        localizationAgent?.addOnLocaleChangedListener(this)
        localizationAgent?.onCreate()
    }

    override fun beforeLocaleChanged() = Unit
    override fun afterLocaleChanged() = Unit

    override fun onResume() {
        super.onResume()
        localizationAgent?.onResume()
    }

    private fun setLanguage(language: String) {
        localizationAgent?.language = language
    }

    private fun initComponents() {
        prefTheme = findPreference<ListPreference>(getString(R.string.pref_key_theme))
                as ListPreference
        prefTheme.onPreferenceChangeListener = onThemePreferenceChange()

        prefShare = findPreference<Preference>(getString(R.string.pref_key_share))
                as Preference
        prefShare.onPreferenceClickListener = onPreferenceShare()

        prefLang = findPreference<Preference>(getString(R.string.pref_key_lang))
                as Preference
        prefLang.onPreferenceClickListener = onLangPreferenceChange()
    }

    private fun onThemePreferenceChange(): Preference.OnPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            // pengecekan tema
            when {
                newValue.equals(getString(R.string.pref_theme_entry_auto)) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                newValue.equals(getString(R.string.pref_theme_entry_dark)) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                newValue.equals(getString(R.string.pref_theme_entry_light)) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            true
        }


    private fun updateTheme(mode: Int): Boolean {
        // ubah icon ke icon home
        val bottomNavigationView: BottomNavigationView =
            activity?.findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView.menu.findItem(R.id.settingFragment).isChecked = true

        // ubah tema diambil dari user
        AppCompatDelegate.setDefaultNightMode(mode)
        // buat ulang activity
        requireActivity().recreate()
        return true
    }

    private fun onLangPreferenceChange(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {

            val dialogBuilder = AlertDialog.Builder(requireActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.language_dialog, null)
            dialogBuilder.setView(dialogView)

            val spinner1: AppCompatSpinner = dialogView!!.findViewById(R.id.spinner1)

            dialogBuilder.setTitle(resources.getString(R.string.language))
            dialogBuilder.setPositiveButton(
                getString(R.string.change),
                DialogInterface.OnClickListener { _, _ ->
                    when (spinner1.selectedItemPosition) {
                        1 //Indonesia
                        -> {
                            setLanguage(LanguageSetting.LANGUAGE_INDONESIA)
                            Toast.makeText(requireActivity(), "Bahasa Indonesia", Toast.LENGTH_SHORT).show()
                            val recreate = SettingFragmentDirections.actionSettingFragmentToHomeFragment()
                            Navigation.findNavController(requireView()).navigate(recreate)
                            return@OnClickListener
                        }
                        else //By default set to english

                        -> {
                            setLanguage(LanguageSetting.LANGUAGE_ENGLISH)
                            Toast.makeText(requireActivity(), "English", Toast.LENGTH_SHORT).show()
                            val recreate = SettingFragmentDirections.actionSettingFragmentToHomeFragment()
                            Navigation.findNavController(requireView()).navigate(recreate)
                            return@OnClickListener
                        }
                    }
                })
            dialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }
            val b = dialogBuilder.create()
            b.show()
            true
        }

    private fun onPreferenceShare(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            val appPackageName = requireActivity().packageName
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "\n\n Lorem Ipsum, check this cool app at: https://play.google.com/store/apps/details?id=$appPackageName"
            )
            sendIntent.type = "text/plain"
            this.startActivity(sendIntent)
            true
        }
}