package org.mnode.newsagent

import org.mnode.newsagent.FeedResolverImpl;

import spock.lang.Specification;

class FeedResolverImplSpec extends Specification {

	FeedResolverImpl resolver
	
	def setup() {
		resolver = []
	}
	
	def 'resolve single feed'() {
		expect:
		resolver.resolve(source).length == 1
		
		where:
		source << ['slashdot.org', 'http://osnews.com', 'readwriteweb.com']
	}
	
	def 'resolve multiple feeds'() {
		expect:
		resolver.resolve(source).length == 3
		
		where:
		source << ['coucou.im']
	}
	
	def 'resolve no feeds'() {
		when:
		resolver.resolve('google.com')
		
		then:
		thrown(IllegalArgumentException)
	}
}
