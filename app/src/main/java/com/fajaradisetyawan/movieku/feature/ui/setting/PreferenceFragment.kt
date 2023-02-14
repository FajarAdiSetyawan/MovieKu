/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatSpinner
import androidx.navigation.Navigation
import androidx.preference.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.receiver.NewReleaseMovieWorker
import com.fajaradisetyawan.movieku.receiver.ReminderReceiver
import com.fajaradisetyawan.movieku.utils.CustomToastDialog
import com.fajaradisetyawan.movieku.utils.LanguageSetting
import com.fajaradisetyawan.movieku.utils.LocalizationAgent
import com.fajaradisetyawan.movieku.utils.OnLocaleChangedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.util.concurrent.TimeUnit


class PreferenceFragment : PreferenceFragmentCompat(), OnLocaleChangedListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var prefTheme: ListPreference
    private lateinit var prefLang: Preference
    private lateinit var prefShare: Preference
    private lateinit var prefAbout: Preference

    private var localizationAgent: LocalizationAgent? = null

    private lateinit var dailyReminder: String
    private lateinit var releaseReminder: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_setting)
        initComponents()

        localizationAgent = LocalizationAgent(requireActivity())
        localizationAgent?.addOnLocaleChangedListener(this)
        localizationAgent?.onCreate()
    }

    override fun beforeLocaleChanged() = Unit
    override fun afterLocaleChanged() = Unit

    private fun setLanguage(language: String) {
        localizationAgent?.language = language
    }

    @SuppressLint("StringFormatInvalid")
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

        prefAbout = findPreference<Preference>(getString(R.string.pref_key_about))
                as Preference

        prefAbout.summary = resources.getString(R.string.pref_about_summary, BuildConfig.VERSION_NAME)


        dailyReminder = resources.getString(R.string.pref_key_daily_reminder)
        releaseReminder = resources.getString(R.string.pref_key_release_reminder)
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
                            Toast.makeText(
                                requireActivity(),
                                "Bahasa Indonesia",
                                Toast.LENGTH_SHORT
                            ).show()
                            val recreate =
                                SettingFragmentDirections.actionSettingFragmentToHomeFragment()
                            Navigation.findNavController(requireView()).navigate(recreate)
                            return@OnClickListener
                        }
                        else //By default set to english

                        -> {
                            setLanguage(LanguageSetting.LANGUAGE_ENGLISH)
                            Toast.makeText(requireActivity(), "English", Toast.LENGTH_SHORT).show()
                            val recreate =
                                SettingFragmentDirections.actionSettingFragmentToHomeFragment()
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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val reminderReceiver = ReminderReceiver()

        if (key == dailyReminder) {
            val selected = sharedPreferences?.getBoolean(dailyReminder, false)
            if (selected == true) {
                reminderReceiver.isAlarmSet(requireActivity())
                reminderReceiver.setRepeatingAlarm(requireActivity())
                CustomToastDialog.successToast(requireActivity(), "Notification Active", resources.getString(R.string.active_reminder_notif))
            } else {
                reminderReceiver.cancelAlarm(requireActivity())
                CustomToastDialog.successToast(requireActivity(), "Notification Deactivate", resources.getString(R.string.deactive_reminder_notif))
            }
        } else if (key == releaseReminder) {
            val selected = sharedPreferences?.getBoolean(releaseReminder, false)
            if (selected == true) {
                stopReleaseMovie()
                scheduleReleaseMovie(12, 5)
                CustomToastDialog.successToast(requireActivity(), "Notification Active", resources.getString(R.string.active_release_notif))
            } else {
                stopReleaseMovie()
                CustomToastDialog.successToast(requireActivity(), "Notification Deactivate", resources.getString(R.string.deactive_release_notif))
            }
        }
    }

    private fun scheduleReleaseMovie(hour: Int, minute: Int){
        val timeDelay = calculateTimeDelay(hour, minute)

        val mWorkManager = WorkManager.getInstance(requireContext())

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val mRequest =
            PeriodicWorkRequest.Builder(NewReleaseMovieWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS)
                .addTag(NewReleaseMovieWorker.TAG)
                .build()

        mWorkManager.enqueue(mRequest)
    }

    private fun stopReleaseMovie(){
        WorkManager.getInstance(requireActivity()).cancelAllWorkByTag(NewReleaseMovieWorker.TAG)
    }

    private fun calculateTimeDelay(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        val nowMillis = calendar.timeInMillis
        // jika jam saat ini lebih dari jam yang ingin di set misalkan jam 7 pagi
        if (calendar[Calendar.HOUR_OF_DAY] > hour ||
            calendar[Calendar.HOUR_OF_DAY] == hour && calendar[Calendar.MINUTE] >= minute
        ) {
            // tambahkan 1 hari ke vairbale calendar
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // set jam dan menit nya menjadi 7.00
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        // hitung berapa selisih waktu saat ini yang dibutuhkan untuk menuju hari esok pukul 7 pagi
        return calendar.timeInMillis - nowMillis
    }



    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        localizationAgent?.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

    }

}