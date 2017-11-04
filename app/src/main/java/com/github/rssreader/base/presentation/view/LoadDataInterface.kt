package com.github.rssreader.base.presentation.view


interface LoadDataInterface {

    fun showLoading()

    fun hideLoading()

    fun showErrorMessage(message: String)

}