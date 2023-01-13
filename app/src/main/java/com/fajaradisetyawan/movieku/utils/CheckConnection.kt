/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.utils

import android.content.Context
import android.net.ConnectivityManager

object CheckConnection {
    fun isInternetAvailable(context: Context): Boolean {
        val isAvailable: Boolean
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        isAvailable = cm.activeNetworkInfo != null
        return isAvailable
    }
}