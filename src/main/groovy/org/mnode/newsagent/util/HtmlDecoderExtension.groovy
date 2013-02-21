package org.mnode.newsagent.util


class HtmlDecoderExtension {

	static String decodeHtml(String self) {
		HtmlDecoder.decode(self)
	}
}
