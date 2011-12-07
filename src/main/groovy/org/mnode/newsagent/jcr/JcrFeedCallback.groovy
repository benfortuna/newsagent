/**
 * Copyright (c) 2011, Ben Fortuna
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

import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.mnode.newsagent.FeedCallback;
import org.mnode.newsagent.util.PathGenerator;

class JcrFeedCallback implements FeedCallback {

	PathGenerator pathGenerator = []
	
	javax.jcr.Node node
	
	public void feed(String title, String description, URL link) {
		def path = pathGenerator.generatePath(link)
		node.session.save {
			def feedNode = node
			path.each {
				feedNode = feedNode.addNode it
			}
			feedNode['mn:title'] == title
			feedNode['mn:description'] == description
		}
	}

	public void feed(String title, String description, URL[] links) {
		// TODO Auto-generated method stub

	}

	public void feedEntry(URI uri, String title, String description,
			String[] text, URL link, Date publishedDate) {
		// TODO Auto-generated method stub

	}

	public void enclosure(URL url, long length, String type) {
		def bytes = url.bytes
		def path = pathGenerator.generatePath(bytes)
		node.session.save {
			def feedNode = node
			path.each {
				feedNode = feedNode.addNode it
			}
			// TODO: add file content
		}
	}

}
