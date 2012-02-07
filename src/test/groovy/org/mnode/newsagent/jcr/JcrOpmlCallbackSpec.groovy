package org.mnode.newsagent.jcr

import java.net.URL;

import groovy.util.logging.Slf4j;

import javax.jcr.NamespaceException

import org.mnode.newsagent.OpmlCallback;
import org.mnode.newsagent.OpmlImporterImpl;

@Slf4j
class JcrOpmlCallbackSpec extends AbstractJcrSpec {

	JcrOpmlCallback callback
	
	def setup() {
		try {
			session.workspace.namespaceRegistry.registerNamespace('mn', 'http://mnode.org/namespace')
		}
		catch (NamespaceException e) {
			log.warn e.message
		}
		callback = [node:session.rootNode]
	}
	
	def 'verify callback invocation'() {
		setup:
		FileInputStream source = ['src/test/resources/google-reader-subscriptions.xml']
		
		and:
		OpmlImporterImpl importer = []
		importer.importOpml(source, callback)
		
		expect:
		assert session.rootNode.nodes.size == 3
	}

}
