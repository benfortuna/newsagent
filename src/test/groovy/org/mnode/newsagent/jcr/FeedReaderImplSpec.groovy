package org.mnode.newsagent.jcr

import org.mnode.newsagent.FeedCallback;

import spock.lang.Specification;

class FeedReaderImplSpec extends Specification {

	FeedReaderImpl reader
	
	def setup() {
		reader = []
	}
	
	def 'verify callback invocation'() {
		setup:
		def callback = new FeedCallback() {
			void feed(String title, String description, URL link) {
				println "$title : $description : $link"
			}
			void feed(String title, String description, URL[] links) {
				println "$title : $description : $links"
			}
			void feedEntry(URI uri, String title, String description, String[] text, URL link, Date publishedDate) {
				println "$uri : $title : $description : $text : $link : $publishedDate"
			}
		}
		
		expect:
		reader.read(new URL("http://coucou.im/feed"), callback)
	}
}
