package com.employeeconnect.ui

import android.app.Application

class App : Application() {

    companion object {
        var instance : Application? = null
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}