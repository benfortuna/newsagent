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
