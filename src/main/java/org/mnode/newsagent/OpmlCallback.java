package org.mnode.newsagent;

import java.net.URL;

public interface OpmlCallback {

	void outline(String title, String text, URL xmlUrl, URL htmlUrl);
	
	void outline(String title, String text);
}
