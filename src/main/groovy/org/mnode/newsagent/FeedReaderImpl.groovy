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

import com.rometools.fetcher.FeedFetcher
import com.rometools.fetcher.impl.FeedFetcherCache
import com.rometools.fetcher.impl.HttpURLFeedFetcher
import com.rometools.modules.mediarss.MediaEntryModule
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import groovy.util.logging.Slf4j
import org.mnode.newsagent.util.FeedFetcherCacheImpl
import org.w3c.tidy.Tidy

@Slf4j
class FeedReaderImpl implements FeedReader {

    private final FeedFetcherCache feedInfoCache;
	
	private final Tidy htmlTidy = [quiet: true, showWarnings: false]
    
    FeedReaderImpl() {
        this(new FeedFetcherCacheImpl())
    }
    
    FeedReaderImpl(FeedFetcherCache feedInfoCache) {
        this.feedInfoCache = feedInfoCache
    }
    
	void read(URL feedUrl, FeedCallback callback, String...tags) {
		// rome uses Thread.contextClassLoader..
		Thread.currentThread().contextClassLoader = FeedReaderImpl.classLoader

		  try {
        SyndFeed feed
        if (System.properties['http.proxyUser']) {
          HttpURLConnection httpcon = feedUrl.openConnection()
          setProxyRequestHeaders(httpcon)
          SyndFeedInput input = []
          feed = input.build(new XmlReader(httpcon))
        } else {
//		FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.instance
          FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache)
          feed = feedFetcher.retrieveFeed(feedUrl)
        }
	      processFeed(feed, feedUrl, callback, tags)
		  }
		  catch (Exception e) {
			  log.warn "Invalid feed: $feedUrl, $e"
        log.debug 'Trace:', e
		  }
    }
    
    void read(URL feedUrl, String username, char[] password, FeedCallback callback) {
        HttpURLConnection httpcon = feedUrl.openConnection()
        String encoding = new sun.misc.BASE64Encoder().encode("$username:${new String(password)}".toString().bytes)
        httpcon.setRequestProperty("Authorization", "Basic $encoding")
        setProxyRequestHeaders(httpcon)
        SyndFeedInput input = []
        SyndFeed feed = input.build(new XmlReader(httpcon))
        processFeed(feed, feedUrl, callback)
    }

    private void setProxyRequestHeaders(HttpURLConnection httpcon) {
      if (System.properties['http.proxyUser']) {
        String username = System.properties['http.proxyUser']
        String password = System.properties['http.proxyPassword']
        String encoding = new sun.misc.BASE64Encoder().encode("$username:${new String(password)}".toString().bytes)
        httpcon.setRequestProperty("Proxy-Connection", "Keep-Alive")
        httpcon.setRequestProperty("Proxy-Authorization", "Basic $encoding")
      }
    }
    
    private void processFeed(SyndFeed feed, URL feedUrl, FeedCallback callback, String...tags) {
        callback.feed(feed.title, feed.description, feedUrl, feed.link, tags)
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
			MediaEntryModule media = entry.getModule(MediaEntryModule.URI)
            URI uri
            try {
                uri = [entry.uri]
            }
            catch (Exception e) {
                uri = new URL(entry.link).toURI()
            }
			
			String description = entry.description?.value
			if (description) {
				StringWriter tidyDescription = []
				htmlTidy.parse(new StringReader(description), tidyDescription)
				description = tidyDescription as String
			}
			
			if (media && media.metadata.thumbnail) {
				callback.feedEntry(uri, entry.title, description,
					media.metadata.thumbnail[0].url.toURL(), entry.link, entry.publishedDate)
			}
			else {
	            callback.feedEntry(uri, entry.title, description,
	                text as String[], entry.link, entry.publishedDate)
			}
            
            entry.enclosures.each { enclosure ->
                try {
                    callback.enclosure(new URL(enclosure.url), enclosure.length, enclosure.type)
                } catch (Exception e) {
                    log.warn "Error processing enclosure: $enclosure.url"
                }
            }
        }
    }
}
