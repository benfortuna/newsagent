package org.mnode.newsagent


import org.mnode.newsagent.OpmlCallback
import org.mnode.newsagent.OpmlImporterImpl;

import spock.lang.Specification

class OpmlImporterImplSpec extends Specification {

	OpmlImporterImpl importer
	
	def setup() {
		importer = []
	}
	
	def 'verify callback invocation'() {
		setup:
		FileInputStream source = ['src/test/resources/google-reader-subscriptions.xml']
		
		and:
		def callback = new OpmlCallback() {
			void outline(String title, String text) {
				println "$title : $text"
			}
			
			void outline(String title, String text, URL xmlUrl, URL htmlUrl) {
				println "$title : $text : $xmlUrl : $htmlUrl"
			}
		}
	
		expect:
		importer.importOpml(source, callback)
	}
}
