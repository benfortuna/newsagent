package org.mnode.newsagent.util

import spock.lang.Specification;

class PathGeneratorSpec extends Specification {

	PathGenerator generator
	
	def setup() {
		generator = []
	}
	
	def 'verify URL path generation'() {
		expect:
		println generator.generatePath(new URL('http://coucou.im/feed'))
	}
}
