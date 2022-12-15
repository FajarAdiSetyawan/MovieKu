/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.airbnb.lottie.LottieAnimationView
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.databinding.ActivityMainBinding
import com.fajaradisetyawan.movieku.utils.Animatoo
import com.fajaradisetyawan.movieku.utils.ConnectionType
import com.fajaradisetyawan.movieku.utils.NetworkMonitorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var activityMainBinding: ActivityMainBinding? = null
    private val binding get() = activityMainBinding!!

    private val networkMonitor = NetworkMonitorUtil(this)

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkConnection()

        setupNav()

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.homeFragment -> showBottomNav()
//                R.id.movieFragment -> showBottomNav()
//                R.id.tvShowFragment -> showBottomNav()
//                R.id.popularPersonFragment -> showBottomNav()
//                R.id.favoriteFragment -> showBottomNav()
//                R.id.settingFragment -> showBottomNav()
//                else -> hideBottomNav()
//            }
//        }


    }

    private fun setupNav() {
        val navController = findNavController(R.id.fragment_container)
        binding.navigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNav()
                R.id.movieFragment -> showBottomNav()
                R.id.tvShowFragment -> showBottomNav()
                R.id.popularPersonFragment -> showBottomNav()
                R.id.settingFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

//    private fun setupDrawerLayout() {
//        setSupportActionBar(binding.appBarMain.toolbar)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.homeFragment,
//                R.id.movieFragment,
//                R.id.tvShowFragment,
//                R.id.popularPersonFragment,
//                R.id.favoriteFragment,
//                R.id.settingFragment
//            ), binding.drawerLayout
//        )
//
//        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
//
//        binding.navView.setupWithNavController(navController)
//
//        val toggle = ActionBarDrawerToggle(
//            this, binding.drawerLayout, binding.appBarMain.toolbar,
//            R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
//
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(binding.drawerLayout) || super.onSupportNavigateUp()
//    }

    private fun showBottomNav() {
        binding.navigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.navigation.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.animate_swipe_right_enter, R.anim.animate_swipe_right_exit)

//        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        }
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
    }

    // fungsi untuk menghentikan pengecekan internet
    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    // Check Internet Connection
    private fun checkConnection() {
        // buat dialog error internet
        val li = LayoutInflater.from(this)
        // panggil layout dialog error
        val promptsView: View = li.inflate(R.layout.dialog_error, null)
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )
        alertDialogBuilder.setView(promptsView)
        // dialog error tidak dapat di tutup selain menekan tombol close
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20)

        // panggil textview judul error dari layout dialog
        val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_error) as TextView
        titleView.text = (resources.getString(R.string.offline))

        // panggil textview desc error dari layout dialog
        val descView = promptsView.findViewById<View>(R.id.tv_desc_dialog_error) as TextView
        descView.text = (resources.getString(R.string.msgoffline))

        // panggil animasi lottie error dari layout dialog
        val lottieView =
            promptsView.findViewById<View>(R.id.lottie_dialog_error) as LottieAnimationView
        lottieView.playAnimation()
        lottieView.setAnimation(R.raw.offline)

        // panggil button error dari layout dialog
        val btnView = promptsView.findViewById<View>(R.id.btn_error_dialog) as Button
        btnView.text = (resources.getString(R.string.connect))
        btnView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            this.let { it1 -> Animatoo.animateSlideUp(it1) }
        }
        // panggil gambar close error dari layout dialog
        val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_error_dialog) as ImageView
        ivDismiss.setOnClickListener {
            networkMonitor.result = { isAvailable, type ->
                runOnUiThread {
                    // cek ada status wifi/data seluler
                    when (isAvailable) {
                        true -> {
                            when (type) {
                                ConnectionType.Wifi -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                    // dialog error ditutup
                                    alertDialog.dismiss()
                                }
                                ConnectionType.Cellular -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                    alertDialog.dismiss()
                                }
                                else -> {
                                    alertDialog.dismiss()
                                }
                            }
                        }
                        false -> {
                            Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                            // dialog error ditampilkan
                            alertDialog.show()
                        }
                    }
                }
            }
        }
        alertDialog.window!!.setBackgroundDrawable(inset)
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                alertDialog.dismiss()
                            }
                            ConnectionType.Cellular -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                alertDialog.dismiss()
                            }
                            else -> {
                                alertDialog.dismiss()
                            }
                        }
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        alertDialog.show()
                    }
                }
            }
        }
    }
}