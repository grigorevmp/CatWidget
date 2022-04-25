package com.grigorevmp.catwidget.common

import android.app.Application
import com.grigorevmp.catwidget.utils.Utils


class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
        Utils.setSharedPreferences()

    }

    init {
        instance = this
    }


    companion object {
        lateinit var instance: Application
    }
}