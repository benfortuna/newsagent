package org.mnode.newsagent.http

import groovy.json.JsonBuilder
import org.amdatu.web.rest.doc.Description
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.mnode.newsagent.FeedCallbackImpl
import org.mnode.newsagent.FeedReader
import org.mnode.newsagent.FeedReaderImpl
import org.yaml.snakeyaml.Yaml

import javax.ws.rs.*

@Component(immediate = true)
@Service(value = FeedController)
@Path("feed")
@Description("Feed management")
class FeedControllerImpl implements FeedController {

    FeedReader reader = new FeedReaderImpl()

    @GET
    @Produces("application/json")
    @Override
    String doGetJson(@QueryParam("url") URL url, @DefaultValue("-1") @QueryParam("limit") int limit) {
        FeedCallbackImpl callback = []
        reader.read(url, callback)
        return new JsonBuilder(limitEntries(callback.feed, limit)).toString()
    }

    @GET
    @Produces("application/yaml")
    @Override
    String doGetYaml(@QueryParam("url") URL url, @DefaultValue("-1") @QueryParam("limit") int limit) {
        FeedCallbackImpl callback = []
        reader.read(url, callback)
        return new Yaml().dump(limitEntries(callback.feed, limit))
    }

    private def limitEntries(def feed, int limit) {
        if (limit > 0) {
            def limitedFeed = feed.clone()
            limitedFeed.entries = limitedFeed.entries[0..limit-1]
            limitedFeed
        } else if (limit == 0) {
            def limitedFeed = feed.clone()
            limitedFeed.remove('entries')
            limitedFeed
        } else {
            feed
        }
    }
}
