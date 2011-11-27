package org.mnode.newsagent;

import java.net.URL;

public interface FeedResolver {

	URL[] resolve(String source);
}
