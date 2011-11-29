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
		// TODO Auto-generated method stub

	}

}
