package org.mnode.newsagent.jcr

import java.net.URI;
import java.net.URL;

import org.apache.jackrabbit.util.Text;
import org.mnode.newsagent.Subscription;

class JcrSubscription implements Subscription {

	javax.jcr.Node node
	
	public URL getUrl() {
		return new URL(node.url.string)
	}

	public String getTitle() {
		return node.title.string
	}
	
	public URL getSource() {
		return new URL(node.source.string)
	}
}
