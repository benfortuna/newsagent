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
package org.mnode.newsagent.util

import groovy.util.logging.Slf4j

@Slf4j
class SiteResolver {

	URL[] getFeedUrls(String source) {
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
		
		def feedUrls = feeds.collect {
			try {
				new URL(it.@href.text())
			} catch (MalformedURLException mue) {
				new URL(sourceUrl, it.@href.text())
			}
		}.unique()
	}
	
	URL getFavIconUrl(URL source) {
		def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(source.content)
		def shortcutIcon = html.head.link.find { it.@rel == 'shortcut icon' ||  it.@rel == 'SHORTCUT ICON' }
		if (shortcutIcon == null) {
			html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(new URL(source, '/').content)
			shortcutIcon = html.head.link.find { it.@rel == 'shortcut icon' ||  it.@rel == 'SHORTCUT ICON' }
		}
		if (shortcutIcon) {
			return new URL(source, shortcutIcon.@href.text())
		}
		else {
			return new URL(source, '/favicon.ico')
		}
	}

}
