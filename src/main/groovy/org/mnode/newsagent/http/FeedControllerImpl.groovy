package org.mnode.newsagent.http

import groovy.json.JsonBuilder
import org.apache.felix.scr.annotations.Component

//import org.amdatu.web.rest.doc.Description
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.mnode.newsagent.FeedCallbackImpl
import org.mnode.newsagent.FeedReader
import org.mnode.newsagent.FeedReaderImpl
import org.yaml.snakeyaml.Yaml

import javax.ws.rs.*
import javax.ws.rs.core.*

@Component(immediate = true, metatype = true, description = 'Feed management REST API', name = 'Newsagent Feed Endpoint')
@Service(value = FeedController)
@Path("feed")
//@Description("Feed management")
class FeedControllerImpl implements FeedController {

    @Property(longValue = 86400l, description = 'The maximum time before the client cache should be refreshed')
    private static final PROPERTY_CACHE_MAX_AGE = 'cache.maxAge'

    FeedReader reader = new FeedReaderImpl()

    long cacheMaxAge = 0l

//    @Activate
    /*
    protected void activate(ComponentContext context) {
        def maxAgeString = context.properties[PROPERTY_CACHE_MAX_AGE]
        if (maxAgeString) {
            cacheMaxAge = Long.valueOf(context.getProperties()['cache.maxAge'])
        }
    }
    */

    @GET
    @Produces("application/json")
    @Override
    Response doGetJson(@QueryParam("url") URL url, @DefaultValue("-1") @QueryParam("limit") int limit,
                       @Context Request request) {

        Response.ResponseBuilder builder
        def etag = reader.getETag(url)
        if (etag) {
            builder = request.evaluatePreconditions(new EntityTag(etag, true))
        }

        if (!builder) {
            FeedCallbackImpl callback = []
            reader.read(url, callback)
            String json = new JsonBuilder(limitEntries(callback.feed, limit)).toString()
            builder = Response.ok(json)
        }

        if (etag) {
            // attach etag to the response
            builder = builder.tag(new EntityTag(etag))
        }

        CacheControl cacheControl = [maxAge: cacheMaxAge]
        return builder.cacheControl(cacheControl).build()
    }

    @GET
    @Produces("application/yaml")
    @Override
    Response doGetYaml(@QueryParam("url") URL url, @DefaultValue("-1") @QueryParam("limit") int limit,
                       @Context Request request) {

        Response.ResponseBuilder builder
        def etag = reader.getETag(url)
        if (etag) {
            builder = request.evaluatePreconditions(new EntityTag(etag))
        }

        if (!builder) {
            FeedCallbackImpl callback = []
            reader.read(url, callback)
            String yaml = new Yaml().dump(limitEntries(callback.feed, limit))
            builder = Response.ok(yaml)
        }

        if (etag) {
            // attach etag to the response
            builder = builder.tag(EntityTag.valueOf(etag))
        }

        CacheControl cacheControl = [maxAge: cacheMaxAge]
        return builder.cacheControl(cacheControl).build()
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
