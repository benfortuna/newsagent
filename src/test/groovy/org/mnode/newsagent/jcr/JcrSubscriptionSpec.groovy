package org.mnode.newsagent.jcr

import org.apache.jackrabbit.util.Text;

import spock.lang.Specification;

class JcrSubscriptionSpec extends AbstractJcrSpec {

	def 'verify property getters'() {
		setup:
		def node = session.rootNode.addNode(Text.escapeIllegalJcrChars(title))
		node.title = title
		node.url = url as String
		node.source = source as String
		
		JcrSubscription subscription = new JcrSubscription(node: node)
		
		expect:
		assert subscription.title == title
		assert subscription.url == url
		assert subscription.source == source
		
		where:
		title					| url									| source
		'Slashdot.org'			| new URL('http://rss.slashdot.org')	| new URL('http://slashdot.org')	
		'A title with spaces'	| new URL('http://spaces.org/rss')		| new URL('http://spaces.org')	
	}
}
