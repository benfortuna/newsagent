package org.mnode.newsagent.json

import spock.lang.Specification

/**
 * Created by fortuna on 6/05/15.
 */
class JsonFeedCallbackTest extends Specification {

    JsonFeedCallback callback = []

    def 'verify generated json for feed'() {
        expect: 'the generated json string'
        callback.feed(title, description, feedUrl, link)
        callback.builder.toPrettyString() == expectedJson

        where:
        title           | description           | feedUrl                       | link                  | expectedJson
        'A feed title'  | 'Feed description'    | new URL('http://example.com') | 'http://example.com'  | '''\
{
    "feed": {
        "title": "A feed title",
        "description": "Feed description",
        "url": "http://example.com",
        "links": "http://example.com"
    }
}'''
    }
}
