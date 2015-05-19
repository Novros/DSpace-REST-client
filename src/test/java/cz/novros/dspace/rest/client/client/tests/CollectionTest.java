package cz.novros.dspace.rest.client.client.tests;

import cz.novros.dspace.rest.client.utils.RemoteUtil;
import cz.novros.dspace.rest.client.factory.IDSpaceRESTFactory;
import cz.novros.dspace.rest.client.factory.resteasy.RESTeasyBasicFactory;
import cz.novros.dspace.rest.client.interfaces.client.ICollectionDspaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICommunityDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;
import cz.novros.dspace.rest.client.serverside.CollectionResource;
import cz.novros.dspace.rest.client.serverside.CommunityResource;
import org.dspace.rest.common.*;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.*;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Class for local test of collection client.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class CollectionTest {

    private static String baseUri;
    private static TJWSEmbeddedJaxrsServer server;
    @InjectMocks
    private final static CommunityResource communityResource = new CommunityResource();
    @InjectMocks
    private final static CollectionResource collectionResource = new CollectionResource();

    private Collection testCollection;
    private static ICollectionDspaceClient collectionClient;
    private static Community parentCommunity;

    @BeforeClass
    public static void beforeClass() throws Exception {
        int port = RemoteUtil.findFreePort();
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(port);
        server.getDeployment().setResources(Arrays.asList(communityResource, collectionResource));
        server.start();

        baseUri = "http://localhost:" + port + "/";

        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        collectionClient = factory.createCollectionClient();

        createStructure();
    }

    public static void createStructure() throws Exception {
        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        ICommunityDSpaceClient communityDSpaceClient = factory.createCommunityClient();

        Community community = new Community();
        community.setName("Community");
        parentCommunity = communityDSpaceClient.create(0,community);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    private static boolean compareCollections(Collection a, Collection b, boolean testId) {
        if(a.getCopyrightText() != b.getCopyrightText()
                || a.getIntroductoryText() != b.getIntroductoryText()
                || a.getShortDescription() != b.getShortDescription()
                || a.getSidebarText() != b.getShortDescription()
                || a.getName() != b.getName()) {
            return false;
        }

        if(testId) {
            if(a.getID() != b.getID()) {
                return false;
            }
        }

        return true;
    }

    @Before
    public void beforeTest() {
        // Setup testCollection
        testCollection = new Collection();
        testCollection.setName("Top community");
        testCollection.setCopyrightText("Copyright text");
        testCollection.setIntroductoryText("Introductory text");
        testCollection.setShortDescription("Short description");
        testCollection.setSidebarText("Sidebar text");
        testCollection.setID(0);
    }

    @Test
    public void testCRUD() throws Exception {
        // Test create
        Collection addedCollection = collectionClient.create(parentCommunity.getID(), testCollection);
        assertTrue("Added collection should be same as sent collection.", compareCollections(testCollection, addedCollection, false));

        // Test read
        Collection readCollection = collectionClient.read(addedCollection.getID(), "");
        assertTrue("Read collection should be same as added.", compareCollections(addedCollection, readCollection, true));

        // Test update
        testCollection.setIntroductoryText("text");
        testCollection.setSidebarText("sidebar");
        collectionClient.update(addedCollection.getID(), testCollection);
        readCollection = collectionClient.read(addedCollection.getID(),"");
        assertTrue("Read collection should be same as updated collection.", compareCollections(testCollection, readCollection, false));

        // Test delete
        collectionClient.delete(addedCollection.getID());
        readCollection = collectionClient.read(addedCollection.getID(), "");
        assertNull("Read null collection, because is deleted.", readCollection);
    }

    @Test
    public void testReadAll() {
        // Add some top community and some subcommunities
        collectionClient.create(parentCommunity.getID(), testCollection);
        collectionClient.create(parentCommunity.getID(), testCollection);
        collectionClient.create(parentCommunity.getID(), testCollection);

        // Test read top communities.
        List<Collection> collections = collectionClient.readAll("", 100, 0);
        assertEquals("Length of collections should be 3.", 3, collections.size());
    }

    @Test
    public void testFindByName() {

    }

    @Test
    public void testReadDeleteItem() {
        testCollection = collectionClient.create(parentCommunity.getID(), testCollection);

        // Setup Item
        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        IItemDSpaceClient itemDSpaceClient = factory.createItemClient();
        List<MetadataEntry> testMetadata = new ArrayList<MetadataEntry>();
        testMetadata.add(new MetadataEntry("dc.title","REST práce NG","cze"));
        Item item = new Item();
        item.setMetadata(testMetadata);
        item = itemDSpaceClient.create(testCollection.getID(),item);

        // Test read items
        List<Item> items = collectionClient.readCollectionItems(testCollection.getID(), "", 100, 0);
        assertEquals("Length of bitstreams should be 1.", 1, items.size());

        // Test delete bitstream
        collectionClient.deleteCollectionItem(testCollection.getID(), item.getID());
        items = collectionClient.readCollectionItems(testCollection.getID(), "", 100, 0);
        assertEquals("Length of deleted bitstreams should be 0.", 0, items.size());
    }
}
