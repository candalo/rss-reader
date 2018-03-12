package com.github.rssreader.features.feedsubscription.presentation.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.github.rssreader.R
import com.github.rssreader.base.data.di.DaggerBaseComponent
import com.github.rssreader.base.presentation.view.ActivityUtils
import com.github.rssreader.features.feedsubscription.data.di.DaggerFeedSubscriptionComponent
import com.github.rssreader.features.feedsubscription.data.di.FeedSubscriptionModule
import com.github.rssreader.features.feedsubscription.presentation.presenter.FeedSubscriptionPresenter
import com.github.rssreader.network.DaggerNetworkComponent
import kotlinx.android.synthetic.main.fragment_feed_subscription.text_input_layout_feed_url as feedUrlTextInputLayout
import kotlinx.android.synthetic.main.fragment_feed_subscription.pb_feed_url as feedUrlProgressBar
import javax.inject.Inject

class FeedSubscriptionFragment : Fragment(), FeedSubscriptionView {

    @Inject lateinit var presenter: FeedSubscriptionPresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_subscription, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        setupPresenter()
        setupEditTextListener()
    }

    private fun injectDependencies() {
        val baseComponent = DaggerBaseComponent.create()
        val networkComponent = DaggerNetworkComponent.create()

        DaggerFeedSubscriptionComponent
                .builder()
                .baseComponent(baseComponent)
                .networkComponent(networkComponent)
                .feedSubscriptionModule(context?.let { FeedSubscriptionModule(it) })
                .build()
                .inject(this)
    }

    private fun setupPresenter() {
        presenter.attachTo(this)
    }

    private fun setupEditTextListener() {
        feedUrlTextInputLayout.editText?.setOnEditorActionListener(urlConfirmationActionListener)
    }

    private val urlConfirmationActionListener = TextView.OnEditorActionListener { _, keyCode, _ ->
        if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
            activity?.let { ActivityUtils.hideKeyboard(it) }
            presenter.onFeedSubscriptionUrlConfirmed(feedUrlTextInputLayout.editText?.text.toString())
        }
        false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showLoading() {
        feedUrlProgressBar.visibility = View.VISIBLE
        feedUrlTextInputLayout.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        feedUrlProgressBar.visibility = View.INVISIBLE
        feedUrlTextInputLayout.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity?.findViewById(android.R.id.content)!!, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showFeedSubscribedMessage() {
        Snackbar.make(activity?.findViewById(android.R.id.content)!!, getString(R.string.feed_subscription_success), Snackbar.LENGTH_LONG).show()
    }
}
