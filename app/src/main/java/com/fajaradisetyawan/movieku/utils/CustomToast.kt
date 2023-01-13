/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.utils

import android.app.Activity
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.fajaradisetyawan.movieku.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

object CustomToast {
    fun noInternet(context: Context) {
        MotionToast.createToast(
            context as Activity,
            context.resources.getString(R.string.offline),
            context.resources.getString(R.string.msgoffline),
            MotionToastStyle.NO_INTERNET,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.quicksand)
        )
    }
}