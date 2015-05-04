/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mnode.newsagent.util;

import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.SyndFeedInfo;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.net.URL;

public class FeedFetcherCacheImpl implements FeedFetcherCache {

    private static final String DEFAULT_CACHE_NAME = "org.mnode.newsagent.feedCache";
    
    private final CacheManager cacheManager;
    
    private final String cacheName;

    public FeedFetcherCacheImpl() {
        this(DEFAULT_CACHE_NAME);
    }
    
    public FeedFetcherCacheImpl(String cacheName) {
        this.cacheName = cacheName;
        cacheManager = CacheManager.newInstance();
        // XXX: move to configuration..
        if (cacheManager.getCache(cacheName) == null) {
            cacheManager.addCache(cacheName);
        }
    }
    
    public void clear() {
        getCache().dispose();
    }

    public SyndFeedInfo getFeedInfo(URL url) {
        Element entry = getCache().get(url);
        if (entry != null) {
            return (SyndFeedInfo) entry.getObjectValue();
        }
        return null;
    }

    public SyndFeedInfo remove(URL url) {
        Element entry = getCache().get(url);
        if (getCache().remove(url)) {
            return (SyndFeedInfo) entry.getObjectValue();
        }
        return null;
    }

    public void setFeedInfo(URL url, SyndFeedInfo syndfeedinfo) {
        Element entry = new Element(url, syndfeedinfo);
        getCache().put(entry);
    }

    private Ehcache getCache() {
        return cacheManager.getEhcache(cacheName);
    }
}
