package com.github.rssreader.features.feedsubscription.presentation.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.github.rssreader.R
import com.github.rssreader.base.data.di.DaggerBaseComponent
import com.github.rssreader.features.feedsubscription.data.di.DaggerFeedSubscriptionComponent
import com.github.rssreader.features.feedsubscription.data.di.FeedSubscriptionModule
import com.github.rssreader.features.feedsubscription.presentation.presenter.FeedSubscriptionPresenter
import com.github.rssreader.network.DaggerNetworkComponent
import kotlinx.android.synthetic.main.activity_feed_subscription.toolbar
import kotlinx.android.synthetic.main.activity_feed_subscription.text_input_layout_feed_url as feedUrlTextInputLayout
import kotlinx.android.synthetic.main.activity_feed_subscription.pb_feed_url as feedUrlProgressBar
import kotlinx.android.synthetic.main.activity_feed_subscription.constraint_layout_feed_url as constraintLayout
import javax.inject.Inject

class FeedSubscriptionActivity : AppCompatActivity(), FeedSubscriptionView {

    @Inject lateinit var presenter: FeedSubscriptionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_subscription)
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
                .feedSubscriptionModule(FeedSubscriptionModule(this))
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
            presenter.onFeedSubscriptionUrlConfirmed(feedUrlTextInputLayout.editText?.text.toString())
        }
        false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun setupToolbar() {
        (toolbar as Toolbar).title = getString(R.string.feed_subscription_toolbar_title)
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
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showFeedSubscribedMessage() {
        Snackbar.make(constraintLayout, getString(R.string.feed_subscription_success), Snackbar.LENGTH_LONG).show()
    }
}
