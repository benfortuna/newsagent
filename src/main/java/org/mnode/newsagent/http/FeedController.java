package org.mnode.newsagent.http;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
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
    Response doGetJson(URL url, int limit, Request request);

    /**
     * Returns a YAML representation of the feed at the specified URL.
     * @param url a feed URL
     * @param limit the maximum feed entries to return
     * @return a YAML string
     */
    Response doGetYaml(URL url, int limit, Request request);
}
