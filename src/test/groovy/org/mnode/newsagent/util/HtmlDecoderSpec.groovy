package org.mnode.newsagent.util

import spock.lang.Specification;

class HtmlDecoderSpec extends Specification {

	HtmlDecoder decoder
	
	def setup() {
		decoder = []
	}
	
	def 'should decode html escaped characters'() {
		expect:
		decoder.decode(input) == expected
		
		where:
		input	| expected
		'&amp;'	| '&'
	}
}
