package com.github.rssreader.features

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.github.rssreader.R
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionFragment
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import kotlinx.android.synthetic.main.activity_home.toolbar as myToolbar


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupDrawer()
        RxBus.get().register(this)
    }

    override fun onDestroy() {
        RxBus.get().unregister(this)
        super.onDestroy()
    }

    private fun setupDrawer() {
        drawer {
            toolbar = myToolbar as Toolbar
            primaryItem(R.string.add_subcription) {
                icon = R.drawable.ic_add_grey
                selectedIcon = R.drawable.ic_add_secondary_color
                textColorRes = R.color.md_grey_500
                selectedTextColorRes = R.color.secondaryColor
                onClick { _ ->
                    onClickAddSubscriptionItem()
                    false
                }
            }
        }
    }

    private fun onClickAddSubscriptionItem() {
        (myToolbar as Toolbar).title = getString(R.string.feed_subscription_toolbar_title)
        RxBus.get().post(FeedSubscriptionFragment())
    }

    @Subscribe
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
    }
}