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

	static codes = [
		quot: '"',
		amp: '&',
		lt: '<',
		gt: '>',
		nbsp: '\u00a0',
		iexcl: '\u00a1',
		cent: '\u00a2',
		pound: '\u00a3',
		curren: '\u00a4',
		yen: '\u00a5',
		brvbar: '\u00a6',
		sect: '\u00a7',
		uml: '\u00a8',
		copy: '\u00a9',
		ordf: '\u00aa',
		laquo: '\u00ab',
		not: '\u00ac',
		shy: '\u00ad',
		reg: '\u00ae',
		macr: '\u00af',
		deg: '\u00ab0',
		plusmn: '\u00b1',
		sup2: '\u00b2',
		sup3: '\u00b3',
		acute: '\u00b4',
		micro: '\u00b5',
		para: '\u00b6',
		middot: '\u00b7',
		cedil: '\u00b8',
		sup1: '\u00b9',
		ordm: '\u00ba',
		raquo: '\u00bb',
		frac14: '\u00bc',
		frac12: '\u00bd',
		frac34: '\u00be',
		iquest: '\u00bf',
		Agrave: '\u00c0',
		Aacute: '\u00c1',
		Acirc: '\u00c2',
		Atilde: '\u00c3',
		Auml: '\u00c4',
		Aring: '\u00c5',
		AElig: '\u00c6',
		Ccedil: '\u00c7',
		Egrave: '\u00c8',
		Eacute: '\u00c9',
		Ecirc: '\u00ca',
		Euml: '\u00cb',
		Igrave: '\u00cc',
		Iacute: '\u00cd',
		Icirc: '\u00ce',
		Iuml: '\u00cf',
		ETH: '\u00d0',
		Ntilde: '\u00d1',
		Ograve: '\u00d2',
		Oacute: '\u00d3',
		Ocirc: '\u00d4',
		Otilde: '\u00d5',
		Ouml: '\u00d6',
		times: '\u00d7',
		Oslash: '\u00d8',
		Ugrave: '\u00d9',
		Uacute: '\u00da',
		Ucirc: '\u00db',
		Uuml: '\u00dc',
		Yacute: '\u00dd',
		THORN: '\u00de',
		szlig: '\u00df',
		agrave: '\u00e0',
		aacute: '\u00e1',
		acirc: '\u00e2',
		atilde: '\u00e3',
		auml: '\u00e4',
		aring: '\u00e5',
		aelig: '\u00e6',
		ccedil: '\u00e7',
		egrave: '\u00e8',
		eacute: '\u00e9',
		ecirc: '\u00ea',
		euml: '\u00eb',
		igrave: '\u00ec',
		iacute: '\u00ed',
		icirc: '\u00ee',
		iuml: '\u00ef',
		eth: '\u00f0',
		ntilde: '\u00f1',
		ograve: '\u00f2',
		oacute: '\u00f3',
		ocirc: '\u00f4',
		otilde: '\u00f5',
		ouml: '\u00f6',
		divide: '\u00f7',
		oslash: '\u00f8',
		ugrave: '\u00f9',
		uacute: '\u00fa',
		ucirc: '\u00fb',
		uuml: '\u00fc',
		yacute: '\u00fd',
		thorn: '\u00fe',
		yuml: '\u00ff',
		euro: '\20ac',
		ndash: '\u2013',
		mdash: '\u2014'
	]
	
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
