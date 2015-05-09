package org.mnode.newsagent.http;

import java.net.URL;

/**
 * REST-ful controller for managing feeds.
 */
public interface FeedController {

    /**
     * Returns a JSON representation of the feed at the specified URL.
     * @param url a feed URL
     * @param limit the maximum feed entries to return
     * @return a JSON string
     */
    String doGetJson(URL url, int limit);

    /**
     * Returns a YAML representation of the feed at the specified URL.
     * @param url a feed URL
     * @param limit the maximum feed entries to return
     * @return a YAML string
     */
    String doGetYaml(URL url, int limit);
}
