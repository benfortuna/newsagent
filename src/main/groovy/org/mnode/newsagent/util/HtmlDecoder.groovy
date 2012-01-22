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

import groovy.util.logging.Slf4j

import java.util.regex.Matcher

@Slf4j
class HtmlDecoder {

	static codes = [:]
	
	static {
		codes['nbsp'] = ' '
		codes['amp'] = '&'
		codes['quot'] = '"'
		codes['ndash'] = '\u2013'
		codes['mdash'] = '\u2014'
	}
	
	static String decode(String input) {
		codes.each {
			input = input.replaceAll("&$it.key;", it.value)
			def unicodePattern = ~/&#(\d+);/
			def matcher = unicodePattern.matcher(input)
			while (matcher.find()) {
				def decValue = matcher.group(1)
				def unicodeValue = (char) Integer.parseInt(decValue)
				
				try {
					input = input.replaceAll("&#$decValue;", Matcher.quoteReplacement("$unicodeValue"))
					matcher = unicodePattern.matcher(input)
				} catch (e) {
					log.error("$input, $decValue - $e.message" as String)
				}
			}
		}
		input
	}
}
