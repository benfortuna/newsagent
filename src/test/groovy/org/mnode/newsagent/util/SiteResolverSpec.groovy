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
package org.mnode.newsagent.util

import org.mnode.newsagent.util.SiteResolver
import spock.lang.Ignore;
import spock.lang.Specification;

class SiteResolverSpec extends Specification {

	SiteResolver resolver
	
	def setup() {
		resolver = []
	}
	
	def 'resolve single feed'() {
		expect:
		resolver.getFeedUrls(source).length == 1
		
		where:
		source << ['slashdot.org', 'http://osnews.com', 'readwriteweb.com']
	}

	@Ignore
	def 'resolve multiple feeds'() {
		expect:
		resolver.getFeedUrls(source).length == 2
		
		where:
		source << ['coucou.im']
	}
	
	def 'resolve no feeds'() {
		when:
		resolver.getFeedUrls('google.com')
		
		then:
		thrown(IllegalArgumentException)
	}
	
	def 'resolve favicon'() {
		expect:
		resolver.getFavIconUrl(new URL('http://groovyconsole.appspot.com/')) as String == 'http://groovyconsole.appspot.com/favicon.ico'
	}
	
	def 'test extension module: getFavIconUrl'() {
		expect:
		new URL('http://google.com').favIconUrl == new URL('http://google.com/favicon.ico')
//		new URL('http://wired.com').favIconUrl == new URL('http://wired.com/favicon.ico')
	}
}
