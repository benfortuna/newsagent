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

import java.net.URL

import org.apache.jackrabbit.util.Text
import org.mnode.newsagent.OpmlCallback
import org.mnode.newsagent.util.PathGenerator

class JcrOpmlCallback implements OpmlCallback {

	PathGenerator pathGenerator = []
	
	javax.jcr.Node node
	
	javax.jcr.Node currentOutlineNode
	
	public void outline(String title, String text, URL xmlUrl, URL htmlUrl) {
		def path = pathGenerator.generatePath(xmlUrl)
		def currentFeedNode = node << 'mn:subscriptions'
		currentFeedNode.session.save {
			path.each {
				currentFeedNode = currentFeedNode << Text.escapeIllegalJcrChars(it)
			}
			currentFeedNode['mn:title'] = title
			currentFeedNode['mn:link'] = xmlUrl as String
			currentFeedNode['mn:source'] = htmlUrl as String
			if (currentOutlineNode) {
				currentFeedNode['mn:tag'] = currentOutlineNode
			}
		}
		currentOutlineNode = null
	}

	public void outline(String title, String text) {
		node.session.save {
			if (!currentOutlineNode) {
				currentOutlineNode = node << 'mn:tags'
			}
			currentOutlineNode = currentOutlineNode << Text.escapeIllegalJcrChars(title)
			currentOutlineNode.addMixin('mix:referenceable')
		}
	}

}
