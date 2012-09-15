/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mnode.newsagent

import groovy.util.logging.Slf4j;

import java.net.URL

import org.mnode.newsagent.FeedCallback
import org.mnode.newsagent.FeedReader
import org.rometools.fetcher.FeedFetcher
import org.rometools.fetcher.impl.FeedFetcherCache
import org.rometools.fetcher.impl.HashMapFeedInfoCache
import org.rometools.fetcher.impl.HttpURLFeedFetcher

import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.feed.synd.SyndLink

@Slf4j
class FeedReaderImpl implements FeedReader {

	public void read(URL feedUrl, FeedCallback callback) {
		// rome uses Thread.contextClassLoader..
		Thread.currentThread().contextClassLoader = FeedReaderImpl.classLoader

		FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.instance
		FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache)
		SyndFeed feed
		try {
			feed = feedFetcher.retrieveFeed(feedUrl)
		}
		catch (Exception e) {
			log.error "Invalid feed: $feedUrl"
			return
		}

		callback.feed(feed.title, feed.description, feedUrl)
		/*
		if (feed.link) {
			URL url
			try {
				url = [feed.link]
			} catch (MalformedURLException mue) {
				url = feedUrl
			}
			callback.feed(feed.title, feed.description, url)
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
		*/
		
		feed.entries.each { entry ->
			def text = entry.contents.collect { it.value }
			URI uri
			try {
				uri = [entry.uri]
			}
			catch (Exception e) {
				uri = new URL(entry.link).toURI()
			}
			callback.feedEntry(uri, entry.title, entry.description?.value,
				text as String[], entry.link, entry.publishedDate)
			
			entry.enclosures.each { enclosure ->
				try {
					callback.enclosure(new URL(enclosure.url), enclosure.length, enclosure.type)
				} catch (Exception e) {
					log.error "Error processing enclosure: $enclosure.url"
				}
			}
		}
	}

}
