## Subscription, retrieval and storage for RSS feeds ##

As there are a number of existing parsers and object models for RSS and Atom feeds, newsagent focuses on augmenting such functionality to provide a simple API for feed-related activities. Features include:

  * **Feed Callback** - a simple callback API for consuming feed elements
  * **OPML Callback** - a callback API for consuming OPML outline elements
  * **Feed Resolver** - support for resolving feeds associated with a URL (eg. site feed)
  * **HTML Decoder** - decode HTML-escaped characters in text (useful when displaying marked-up title/description in plain text)
  * **Feed Store** - persistent storage
  * **Feed Poll** - read feeds at regular intervals
  * **Feed Search** - simple search API
  * **Flags and Tags** - support item flagging (e.g. retention/importance, etc.), and tagging (e.g. categorisation). The primary difference is that flags allow a finite set of values, and tags are arbitrary.
  * **Enclosures** - automatic download and storage of content specified using RSS enclosures.

### Maven ###

Find the latest releases in the Maven Central Repo [here](http://search.maven.org/#browse%7C-715415387)