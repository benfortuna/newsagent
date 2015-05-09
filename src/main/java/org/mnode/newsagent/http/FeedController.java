package org.mnode.newsagent.http;

import javax.ws.rs.*;
import java.net.URL;

/**
 * REST-ful controller for managing feeds.
 */
@Path("feed")
public interface FeedController {

    @GET
    @Produces("application/json")
    String doGetJson(@QueryParam("url") URL url, @DefaultValue("-1") @QueryParam("limit") int limit);

    @GET
    @Produces("application/yaml")
    String doGetYaml(URL url, int limit);
}
