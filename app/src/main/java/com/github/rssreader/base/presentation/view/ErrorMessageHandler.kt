package com.github.rssreader.base.presentation.view


interface ErrorMessageHandler {

    /**
     * Handles an exception error message returning an appropriate message to the view
     *
     * @param  exception
     * @return appropriate message to the view
     */
    fun handle(exception: Throwable): String

}
