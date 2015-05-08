package org.mnode.newsagent

import spock.lang.Specification

/**
 * Created by fortuna on 6/05/15.
 */
class FeedCallbackImplTest extends Specification {

    FeedCallbackImpl callback = []

    def 'verify generated json for feed'() {
        when: 'the callback is invoked'
        callback.feed(title, description, feedUrl, link)

        then: 'the generated collection is as exected'
        callback.feed.title == title
        callback.feed.description == description
        callback.feed.url == feedUrl as String
        callback.feed.links == [link]

        where:
        title           | description           | feedUrl                       | link
        'A feed title'  | 'Feed description'    | new URL('http://example.com') | 'http://example.com'
    }
}
