# DSpace REST client#

This is my own DSpace REST client to official DSpace REST api. It can do every operation in REST api. You can use simple connection or polled connection. It is using RestEasy library, but you can implement own client with another REST implementation with my interfaces.

## Use ##
Use is simple. Create factory and then create client, which you want, (CommunityClient, CollectionClient, ItemClient, BitstreamClient) and you can use their methods, which are: common methods create, read, update, delete, readAll and then client specific. For usage of specific client look at interface in *src/main/java/cz/novros/dspace/rest/client/interfaces*, there is JavaDoc.

Example:

```
#!java
IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
ICommunityDSpaceClient communityClient = factory.createCommunityClient();

communityClient.create(0,community)

```

Tests are not fully complete.


Copyright (c) 2015, Rostislav Novak. All rights reserved.