package org.mnode.newsagent.jcr

import java.net.URL

import org.mnode.newsagent.FeedCallback
import org.mnode.newsagent.FeedReader
import org.rometools.fetcher.FeedFetcher
import org.rometools.fetcher.impl.FeedFetcherCache
import org.rometools.fetcher.impl.HashMapFeedInfoCache
import org.rometools.fetcher.impl.HttpURLFeedFetcher

import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.feed.synd.SyndLink

class FeedReaderImpl implements FeedReader {

	public void read(URL feedUrl, FeedCallback callback) {
		// rome uses Thread.contextClassLoader..
		Thread.currentThread().contextClassLoader = FeedReaderImpl.classLoader

		FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.instance
		FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache)
		SyndFeed feed = feedFetcher.retrieveFeed(feedUrl)

		if (feed.link) {
			callback.feed(feed.title, feed.description, new URL(feed.link))
		}
		else {
			def links = feed.links.collect { link ->
				if (link instanceof SyndLink) {
					new URL(link.href)
				}
				else {
					new URL(link)
				}
			}
			callback.feed(feed.title, feed.description, links as URL[])
		}
		
		feed.entries.each { entry ->
			def text = entry.contents.collect { it.value }
			callback.feedEntry(URI.create(entry.uri), entry.title, entry.description.value,
				text as String[], new URL(entry.link), entry.publishedDate)
		}
	}

}
