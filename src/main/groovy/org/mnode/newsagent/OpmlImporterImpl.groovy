package org.mnode.newsagent;

import java.io.InputStream;

import org.mnode.newsagent.OpmlCallback;
import org.mnode.newsagent.OpmlImporter;

import com.sun.syndication.feed.opml.Opml;
import com.sun.syndication.io.WireFeedInput;

class OpmlImporterImpl implements OpmlImporter {

	public void importOpml(InputStream source, OpmlCallback callback) {
		InputStreamReader reader = [source]
		Opml opml = new WireFeedInput().build(reader)
		opml.outlines.each {
			invokeCallback(it, callback)
		}
	}

	private void invokeCallback(def outline, OpmlCallback callback) {
		if (outline.xmlUrl || outline.htmlUrl) {
			callback.outline(outline.title, outline.text, new URL(outline.xmlUrl), new URL(outline.htmlUrl))
		}
		else {
			callback.outline(outline.title, outline.text)
		}
		
		outline.children.each {
			invokeCallback(it, callback)
		}
	}
}
