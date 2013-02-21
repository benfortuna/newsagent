package org.mnode.newsagent.util


class PathGeneratorExtension {

	static String[] toMD5Path(URL self) {
		PathGenerator.generatePath(self)
	}

	static String[] toMD5Path(byte[] self) {
		PathGenerator.generatePath(self)
	}
}
