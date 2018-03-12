package com.github.rssreader.base.presentation.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


object ActivityUtils {

    fun hideKeyboard(activity: AppCompatActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.rootView.windowToken, 0)
    }

}