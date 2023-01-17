/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.View
import android.webkit.ConsoleMessage
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.fajaradisetyawan.movieku.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

object CustomToastDialog {
    private lateinit var dialog: AlertDialog

    fun errorToast(
        context: Context,
        title: String,
        message: String
    ) {
        return MotionToast.darkColorToast(
            context as Activity,
            title,
            message,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.quicksand)
        )
    }

    fun successToast(
        context: Context,
        title: String,
        message: String
    ) {
        return MotionToast.darkColorToast(
            context as Activity,
            title,
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.quicksand)
        )
    }

    fun deleteToast(
        context: Context,
        title: String,
        message: String
    ) {
        return MotionToast.darkColorToast(
            context as Activity,
            title,
            message,
            MotionToastStyle.DELETE,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.quicksand)
        )
    }

    fun showLoadingDialog(
        fragmentActivity: FragmentActivity,
        message: String
    ): AlertDialog {
        val builder = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_loading,null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        dialog.window!!.setBackgroundDrawable(inset)
        val lottie =
            dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.setAnimation(R.raw.loading_orange)
        lottie.playAnimation()
        val titleView = dialogView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = message
        dialog.show()

        return dialog
    }

    fun goneLoadingDialog(){
        dialog.dismiss()
    }
}