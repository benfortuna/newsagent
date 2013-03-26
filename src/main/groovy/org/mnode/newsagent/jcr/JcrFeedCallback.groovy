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
package org.mnode.newsagent.jcr

import groovy.util.logging.Slf4j;

import java.awt.image.BufferedImage
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.imageio.ImageIO;

import net.sf.image4j.codec.ico.ICODecoder

import org.apache.jackrabbit.util.Text;
import org.mnode.newsagent.FeedCallback;
import org.mnode.newsagent.util.SiteResolver;
import org.mnode.newsagent.util.PathGenerator;

@Slf4j
class JcrFeedCallback extends AbstractJcrCallback implements FeedCallback {

	boolean downloadEnclosures = true
	
	PathGenerator pathGenerator = []
	
	javax.jcr.Node currentFeedNode
	
	SiteResolver feedResolver = []
	
	public void feed(String title, String description, URL feedUrl, String link,
        String...tags) {
        
		def path = pathGenerator.generatePath(feedUrl)
		node.session.withLock(sessionLock) {
			currentFeedNode = node
			path.each {
				currentFeedNode = currentFeedNode << it
			}
			currentFeedNode['mn:title'] = title
			currentFeedNode['mn:description'] = description ?: ''
			currentFeedNode['mn:source'] = feedUrl as String
		    currentFeedNode['mn:link'] = link ?: ''
			currentFeedNode['mn:status'] = 'OK'
            tags.each {
                def tagNode = node.session.rootNode << 'mn:tags' << it
                if (currentFeedNode['mn:tag']) {
                    currentFeedNode['mn:tag'] << tagNode
                }
                else {
                    currentFeedNode['mn:tag'] = tagNode
                }
            }
            save()
		}
		
		// XXX: Improve this by parsing html to get the link-rel=icon..
		if (!currentFeedNode['mn:icon'] && link) {
			def localFeedNode = currentFeedNode
			Thread.start {
				try {
//					URL favicon = ['http', link ? new URL(link).host : feedUrl.host, '/favicon.ico']
					URL favicon = feedResolver.getFavIconUrl(new URL(link))
					def faviconImage = null
					if (favicon.path.endsWith('.ico')) {
						List<BufferedImage> image = ICODecoder.read(favicon.openStream())
						if (image) {
                            image.each {
                                if (!faviconImage || faviconImage.height > it.height) {
                                    faviconImage = it
                                }
                            }
//                            faviconImage = image[-1]
						}
					}
					else {
						faviconImage = ImageIO.read(favicon)
					}
					if (faviconImage) {
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(faviconImage, 'gif', os);
						InputStream is = new ByteArrayInputStream(os.toByteArray());
						def feedIcon = localFeedNode.session.valueFactory.createBinary(is)
						localFeedNode.session.withLock(sessionLock) {
							localFeedNode['mn:icon'] = feedIcon
							save()
						}
					}
				}
				catch (Exception e) {
					log.debug "No favicon for $link"
				}
			}
		}
	}

	public void feed(String title, String description, URL[] links) {
		// TODO Auto-generated method stub

	}

	public void feedEntry(URI uri, String title, String description,
			String[] text, String link, Date publishedDate) {
			
		currentFeedNode.session.withLock(sessionLock) {
			def entryNode
			if (uri) {
				try {
					entryNode = currentFeedNode << Text.escapeIllegalJcrChars(uri as String)
				} catch (Exception e) {
					log.warn "Error creating node from uri: $uri"
				}
			}
			
			if (!entryNode) {
				entryNode = currentFeedNode << Text.escapeIllegalJcrChars(title)
			}
			
			entryNode['mn:title'] = title
			entryNode['mn:description'] = description ?: ''
			entryNode['mn:link'] = link
			if (publishedDate) {
				entryNode['mn:date'] = publishedDate.toCalendar()
			} else if (!entryNode['mn:date']) {
				entryNode['mn:date'] = Calendar.instance
			}
			entryNode['mn:seen'] = entryNode['mn:seen']?.boolean ?: false
            save()
		}
	}

	public void feedEntry(URI uri, String title, String description,
			URL thumbnail, String link, Date publishedDate) {
			
		currentFeedNode.session.withLock(sessionLock) {
			def entryNode
			if (uri) {
				try {
					entryNode = currentFeedNode << Text.escapeIllegalJcrChars(uri as String)
				} catch (Exception e) {
					log.warn "Error creating node from uri: $uri"
				}
			}
			
			if (!entryNode) {
				entryNode = currentFeedNode << Text.escapeIllegalJcrChars(title)
			}
			
			entryNode['mn:title'] = title
			entryNode['mn:description'] = description ?: ''
			entryNode['mn:link'] = link
			entryNode['mn:thumbnail'] = thumbnail
			if (publishedDate) {
				entryNode['mn:date'] = publishedDate.toCalendar()
			} else if (!entryNode['mn:date']) {
				entryNode['mn:date'] = Calendar.instance
			}
			entryNode['mn:seen'] = entryNode['mn:seen']?.boolean ?: false
            save()
		}
	}
	
	public void enclosure(URL url, long length, String type) {
		if (downloadEnclosures) {
			try {
				def bytes = url.bytes
				def path = pathGenerator.generatePath(bytes)
				node.session.withLock(sessionLock) {
					def feedNode = node
					path.each {
						feedNode = feedNode << it
					}
					// TODO: add file content
                    
                    save()
				}
			} catch (Exception e) {
				log.warn "Error loading enclosure: $url"
			}
		}
		else {
			log.info "Skipping enclosure: $url"
		}
	}
}
