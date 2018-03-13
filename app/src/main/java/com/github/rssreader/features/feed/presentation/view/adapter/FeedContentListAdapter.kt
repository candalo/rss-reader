package com.github.rssreader.features.feed.presentation.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.rssreader.R
import com.github.rssreader.features.feed.domain.models.FeedItem


class FeedContentListAdapter : RecyclerView.Adapter<FeedContentListAdapter.FeedContentViewHolder> {

    private val feedItems: List<FeedItem>

    constructor(feedItems: List<FeedItem>) {
        this.feedItems = feedItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_feed_content, parent, false)
        return FeedContentViewHolder(view)
    }

    override fun getItemCount(): Int = feedItems.size

    override fun onBindViewHolder(holder: FeedContentViewHolder, position: Int) {
        val feedItem = feedItems[position]
        holder.feedContentTitleTextView.text = feedItem.title
        holder.feedContentDescriptionTextView.text = feedItem.description
        holder.feedContentPublishTimeTextView.text = feedItem.pubDate
    }

    inner class FeedContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val feedContentTitleTextView: TextView = view.findViewById(R.id.tv_feed_content_title)
        val feedContentDescriptionTextView: TextView = view.findViewById(R.id.tv_feed_content_description)
        val feedContentPublishTimeTextView: TextView = view.findViewById(R.id.tv_feed_content_publish_time)
    }
}