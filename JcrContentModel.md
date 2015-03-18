# Introduction #

The following model outline the hierarchical structure of nodes and properties in the JCR-based persistence implementation.

  * mn:subscriptions - data from an external source, preferably that can be identified by a URI
  * mn:files - files (either internally or externally sourced), stored using a hash to prevent duplicates

# Details #

+ - reference nodes



&lt;Root&gt;


  * _mn:subscriptions_
    * 

&lt;com&gt;


      * 

&lt;acme&gt;


        * 

&lt;path&gt;


          * 

&lt;feed-uri-md5-hash&gt;

 (mn:title, mn:description, mn:context+, mn:lastUpdated)
          * 

&lt;entry-uri-md5-hash&gt;

 (mn:title, mn:description, mn:content, mn:publishedDate, mn:flags+, mn:tags+, mn:feed+)
            * 

&lt;mn:attachment&gt;

 (mn:filename, mn:sourceUrl, mn:data+)

  * _mn:annotations_
    * mn:seen (mn:label, mn:icon)
    * mn:important (mn:label, mn:icon)
    * 

&lt;tag1&gt;

 (mn:label, mn:colour)

  * _mn:contexts_
    * 

&lt;context1&gt;

 (mn:label)
      * 

&lt;sub-context1&gt;

 (mn:label)

  * _mn:files_
    * 

&lt;e8&gt;


      * 

&lt;f3&gt;


        * <..>
          * 

&lt;md5-hash&gt;

 (jcr:content)