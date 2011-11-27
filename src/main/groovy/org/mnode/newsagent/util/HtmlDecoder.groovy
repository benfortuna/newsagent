/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2011, Ben Fortuna [fortuna@micronode.com]
 *
 * Coucou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Coucou is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Coucou.  If not, see <http://www.gnu.org/licenses/>.
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
