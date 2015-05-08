package org.mnode.newsagent
/**
 * Created by fortuna on 6/05/15.
 */
class FeedCallbackImpl implements FeedCallback {

    def feed = [entries: [], enclosures: []]

    @Override
    void feed(String title, String description, URL feedUrl, String link, String... tags) {
        feed.title = title
        feed.description = description
        feed.url = feedUrl as String
        feed.links = [link]
        feed.tags = tags as List
    }

    @Override
    void feed(String title, String description, URL[] links) {
        feed.title = title
        feed.description = description
        feed.links = links as List
    }

    @Override
    void feedEntry(URI uri, String title, String description, String[] text, String link, Date publishedDate) {
        feed.entries << [
                uri: uri as String,
                title: title,
                description: description,
                text: text as List,
                link: link,
                published: publishedDate
        ]
    }

    @Override
    void feedEntry(URI uri, String title, String description, URL thumbnail, String link, Date publishedDate) {
        feed.entries << [
                uri: uri as String,
                title: title,
                description: description,
                thumbnail: thumbnail,
                link: link,
                published: publishedDate
        ]
    }

    @Override
    void enclosure(URL url, long length, String type) {
        feed.enclosures << [
                url: url as String,
                length: length,
                type: type
        ]
    }
}
