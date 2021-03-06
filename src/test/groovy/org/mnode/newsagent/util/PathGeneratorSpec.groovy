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

import spock.lang.Specification;

class PathGeneratorSpec extends Specification {

	PathGenerator generator
	
	def setup() {
		generator = []
	}
	
	def 'verify URL path generation'() {
		expect:
		println generator.generatePath(new URL('http://coucou.im/feed'))
		println generator.generatePath(new URL('http://www.abc.net.au/atthemovies/'))
	}
	
	def 'verify bytes path generation'() {
		expect:
		println generator.generatePath(new File('pom.xml').bytes)
	}
	
	def 'test extension module: toMD5Path'() {
		expect:
		new URL('http://localhost').toMD5Path() == ['localhost', '86a9106ae65537651a8e456835b316ab'] as String[]
		'http://localhost'.bytes.toMD5Path() == ['86','a9','10','6a','e6','55','37','65','1a','8e','45','68','35','b3','16','ab', '86a9106ae65537651a8e456835b316ab'] as String[]
	}
}
