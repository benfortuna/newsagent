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

import org.mnode.newsagent.FeedCallback;
import org.mnode.newsagent.util.SiteResolver;

import spock.lang.Ignore;
import spock.lang.IgnoreRest;
import spock.lang.Specification;

class FeedReaderImplSpec extends Specification {

	FeedReaderImpl reader
    
    def callback = new FeedCallback() {
        void feed(String title, String description, URL feedUrl, String link,
             String...tags) {
             
            println "$title : $description : $link"
        }
        void feed(String title, String description, URL[] links) {
            println "$title : $description : $links"
        }
        void feedEntry(URI uri, String title, String description, String[] text, String link, Date publishedDate) {
            println "$uri : $title : $description : $text : $link : $publishedDate"
        }
        void feedEntry(URI uri, String title, String description, URL thumbnail, String link, Date publishedDate) {
            println "$uri : $title : $description : $thumbnail : $link : $publishedDate"
        }
        void enclosure(URL url, long length, String type) {
            println "$url : $length : $type"
        }
    }

	def setup() {
		reader = []
	}
	
	def 'verify callback invocation'() {
		expect:
//		reader.read(new URL("http://coucou.im/feed"), callback)
		reader.read(new SiteResolver().getFeedUrls("slashdot.org")[0], callback)
	}
    
	@Ignore
    def 'verify callback invocation with basic authentication'() {
        expect:
        reader.read(new URL('http://appau182dev225.appdev.corptst.anz.com:8080/nexus/service/local/feeds/authcAuthz'), 'admin', 'admin123'.toCharArray(), callback)
    }
	
	def 'verify callback invocation with thumbnails'() {
		expect:
		reader.read(new SiteResolver().getFeedUrls("reddit.com")[0], callback)
	}
}
