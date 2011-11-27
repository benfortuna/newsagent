package org.mnode.newsagent;

import java.net.URI;
import java.net.URL;
import java.util.Date;

public interface FeedCallback {

	void feed(String title, String description, URL link);
	
	void feed(String title, String description, URL[] links);
	
	void feedEntry(URI uri, String title, String description, String[] text, URL link, Date publishedDate);
}
