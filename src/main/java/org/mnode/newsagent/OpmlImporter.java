package org.mnode.newsagent;

import java.io.InputStream;

public interface OpmlImporter {

	void importOpml(InputStream source, OpmlCallback callback);
}
