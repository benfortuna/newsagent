package org.mnode.newsagent;

import java.net.URL;

public interface FeedReader {

	void read(URL feedUrl, FeedCallback callback);
}
