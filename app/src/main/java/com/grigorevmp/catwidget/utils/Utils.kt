package com.grigorevmp.catwidget.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object Utils {

    fun init(application: Application) {
        Utils.application = application
    }

    private var sharedPreferences: SharedPreferences? = null

    private var application: Application? = null

    private const val preferences_file = "quickPassPreference"

    fun setSharedPreferences() {
        sharedPreferences = application?.getSharedPreferences(
            preferences_file,
            Context.MODE_PRIVATE
        )
    }

    fun setReload(isReload: Boolean) {
        with(sharedPreferences!!.edit()) {
            putBoolean("prefReload", isReload)
            apply()
        }
    }

    fun setUrl(url: String) {
        with(sharedPreferences!!.edit()) {
            putString("prefUrl", url)
            apply()
        }
    }

    fun setAnimal(type: String) {
        with(sharedPreferences!!.edit()) {
            putString("prefAnimal", type)
            apply()
        }
    }

    fun setMonth(isLong: Boolean) {
        with(sharedPreferences!!.edit()) {
            putBoolean("prefMonth", isLong)
            apply()
        }
    }

    fun setCalendar(openCal: Boolean) {
        with(sharedPreferences!!.edit()) {
            putBoolean("prefCalendar", openCal)
            apply()
        }
    }

    fun getUrl() = sharedPreferences!!.getString("prefUrl", "")
    fun getAnimal() = sharedPreferences!!.getString("prefAnimal", "cat")
    fun getMonthLong() = sharedPreferences!!.getBoolean("prefMonth", false)
    fun getReload() = sharedPreferences!!.getBoolean("prefReload", false)
    fun getCalendar() = sharedPreferences!!.getBoolean("prefCalendar", false)
}