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
package org.mnode.newsagent.jcr

import groovy.util.logging.Slf4j;

import javax.jcr.NamespaceException;

import org.mnode.newsagent.FeedReader
import org.mnode.newsagent.FeedReaderImpl
import org.mnode.newsagent.FeedResolverImpl

@Slf4j
class JcrFeedCallbackSpec extends AbstractJcrSpec {

	JcrFeedCallback callback
	
	def setup() {
		try {
			session.workspace.namespaceRegistry.registerNamespace('mn', 'http://mnode.org/namespace')
		}
		catch (NamespaceException e) {
			log.warn e.message
		}
		callback = [node:session.rootNode << 'mn:subscriptions']
	}
	
	def 'should load feeds into repository'() {
		setup:
		FeedReader reader = new FeedReaderImpl()
		reader.read(new FeedResolverImpl().resolve("slashdot.org")[0], callback)
		
		expect:
		assert session.rootNode['mn:subscriptions/org/slashdot'].nodes.size == 1
		
		for (node in session.rootNode['mn:subscriptions/org/slashdot'].nodes) {
			assert node['mn:title'].string == 'Slashdot'
			assert node['mn:description'].string == 'News for nerds, stuff that matters'
			
			assert node.nodes.size > 0
			
			for (entry in node.nodes) {
				log.info entry['mn:title'].string
			}
		}
	}
}
