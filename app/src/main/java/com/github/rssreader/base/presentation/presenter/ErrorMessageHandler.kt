package com.github.rssreader.base.presentation.presenter


interface ErrorMessageHandler {

    /**
     * Handles an exception error message returning an appropriate message to the view
     *
     * @param  exceptionErrorMessage exception message
     * @return appropriate message to the view
     */
    fun handle(exceptionErrorMessage: String?): String

}