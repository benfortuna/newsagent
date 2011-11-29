package org.mnode.newsagent.jcr

import javax.jcr.NamespaceException;

import org.mnode.newsagent.FeedReader
import org.mnode.newsagent.FeedReaderImpl
import org.mnode.newsagent.FeedResolverImpl

class JcrFeedCallbackSpec extends AbstractJcrSpec {

	JcrFeedCallback callback
	
	def setup() {
		try {
			session.workspace.namespaceRegistry.registerNamespace('mn', 'http://mnode.org/namespace')
		}
		catch (NamespaceException e) {
			println e.message
		}
		callback = [node:session.rootNode.addNode('mn:subscriptions')]
	}
	
	def 'should load feeds into repository'() {
		setup:
		FeedReader reader = new FeedReaderImpl()
		reader.read(new FeedResolverImpl().resolve("slashdot.org")[0], callback)
	}
}
