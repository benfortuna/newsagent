package org.mnode.newsagent

import groovy.util.logging.Slf4j;

import java.net.URL;

import org.mnode.newsagent.FeedResolver;

@Slf4j
class FeedResolverImpl implements FeedResolver {

	public URL[] resolve(String source) {
		def sourceUrl
		try {
			sourceUrl = new URL(source)
		}
		catch (MalformedURLException e) {
			sourceUrl = new URL("http://${source}")
		}
		 
		def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(sourceUrl.content)
		def feeds = html.head.link.findAll { it.@type == 'application/rss+xml' || it.@type == 'application/atom+xml' }
		  
		log.info "Found ${feeds.size()} feeds: ${feeds.collect { it.@href.text() }}"
		  
		if (feeds.isEmpty()) {
			throw new IllegalArgumentException("No feeds found at source: ${source}")
		}
		
		def feedUrls = feeds.collect { new URL(it.@href.text()) }.unique()
	}

}
