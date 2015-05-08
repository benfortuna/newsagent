package org.mnode.newsagent.http

import groovy.json.JsonBuilder
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.mnode.newsagent.FeedCallbackImpl
import org.mnode.newsagent.FeedReader
import org.mnode.newsagent.FeedReaderImpl

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam

/**
 * REST-ful controller for managing feeds.
 */
@Component(immediate = true)
@Service(value = FeedController)
@Path("feed")
class FeedController {

    FeedReader reader = new FeedReaderImpl()

    @GET
    @Produces("application/json")
    @Path("list")
    String list(@QueryParam('url') URL url) {
        FeedCallbackImpl callback = []
        reader.read(url, callback)
        return new JsonBuilder(callback.feed).toString()
    }
}
