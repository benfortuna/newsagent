package org.mnode.newsagent.json

import groovy.json.JsonBuilder
import org.mnode.newsagent.FeedCallback

/**
 * Created by fortuna on 6/05/15.
 */
class JsonFeedCallback implements FeedCallback {

    JsonBuilder builder = []

    def feed

    @Override
    void feed(String atitle, String adescription, URL feedUrl, String alink, String... tags) {
        feed = builder.feed {
            title atitle
            description adescription
            url feedUrl
            links alink
        }
    }

    @Override
    void feed(String title, String description, URL[] links) {
        feed = builder.feed {
            title title
            description description
            links links
        }
    }

    @Override
    void feedEntry(URI uri, String title, String description, String[] text, String link, Date publishedDate) {

    }

    @Override
    void feedEntry(URI uri, String title, String description, URL thumbnail, String link, Date publishedDate) {

    }

    @Override
    void enclosure(URL url, long length, String type) {

    }
}
