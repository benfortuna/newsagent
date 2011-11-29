package org.mnode.newsagent.util

import java.security.MessageDigest;

import org.bouncycastle.util.encoders.Hex;

class PathGenerator {

	public String[] generatePath(URL url) {
		def uid = url.host.split(/\s*\.\s*/).reverse() as List
		uid.addAll url.path.split(/\s*\/\s*/).findAll { !it.empty }
		def digest = MessageDigest.getInstance('md5')
		def checksum = digest.digest url.toString().bytes
		uid << new String(Hex.encode(checksum))
	}
}
