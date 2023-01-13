/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import com.fajaradisetyawan.movieku.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object ParseDateTime {
    @SuppressLint("SimpleDateFormat")
    fun parseDate(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd MMM yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        val date: Date?
        var str: String? = null
        try {
            date = inputFormat.parse(time!!)
            str = outputFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDateFull(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd MMMM yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        val date: Date?
        var str: String? = null
        try {
            date = inputFormat.parse(time!!)
            str = outputFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun parseTime(time: Int): String {
        val hour = time / 60
        val min = time % 60

        return Resources.getSystem().getString(R.string.time, hour, min)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(dobString: String): Int {

        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dobString , firstApiFormat)

        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob[year, month] = day

        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]

        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }

        return age
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getYear(time: String?): String {
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(time, firstApiFormat)

        return date.year.toString()
    }
}