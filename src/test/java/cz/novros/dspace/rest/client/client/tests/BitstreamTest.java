package cz.novros.dspace.rest.client.client.tests;

import cz.novros.dspace.rest.client.factory.IDSpaceRESTFactory;
import cz.novros.dspace.rest.client.factory.resteasy.RESTeasyBasicFactory;
import cz.novros.dspace.rest.client.interfaces.client.IBitstreamDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICollectionDspaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICommunityDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;
import cz.novros.dspace.rest.client.utils.RemoteUtil;
import cz.novros.dspace.rest.client.serverside.BitstreamResource;
import cz.novros.dspace.rest.client.serverside.CollectionResource;
import cz.novros.dspace.rest.client.serverside.CommunityResource;
import cz.novros.dspace.rest.client.serverside.ItemResource;
import org.dspace.rest.common.*;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Rostislav on 30. 4. 2015.
 */
public class BitstreamTest {
    private static int port;
    private static String baseUri;
    private static TJWSEmbeddedJaxrsServer server;
    @InjectMocks
    private final static ItemResource itemResource = new ItemResource();
    @InjectMocks
    private final static CommunityResource communityResource = new CommunityResource();
    @InjectMocks
    private final static CollectionResource collectionResource = new CollectionResource();
    @InjectMocks
    private final static BitstreamResource bitstreamResource = new BitstreamResource();

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private Bitstream testBitstream;
    private static IBitstreamDSpaceClient bitstreamClient;
    private static Item parentItem;

    @BeforeClass
    public static void beforeClass() throws Exception {
        port = RemoteUtil.findFreePort();
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(port);
        server.getDeployment().setResources((List) Arrays.asList(communityResource, collectionResource, itemResource, bitstreamResource));
        server.start();

        baseUri = "http://localhost:" + port + "/";

        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        bitstreamClient = factory.createBitstreamClient();

        createStructure();
    }

    public static void createStructure() throws Exception {
        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        ICommunityDSpaceClient communityDSpaceClient = factory.createCommunityClient();
        ICollectionDspaceClient collectionClient = factory.createCollectionClient();
        IItemDSpaceClient itemDSpaceClient = factory.createItemClient();

        Community community = new Community();
        community.setName("Community");
        communityDSpaceClient.create(0,community);

        Collection collection = new Collection();
        collection.setName("Collection");
        collection = collectionClient.create(0,collection);

        // Setup testItem
        List<MetadataEntry> testMetadata = new ArrayList<MetadataEntry>();
        testMetadata.add(new MetadataEntry("dc.title","REST práce NG","cze"));
        Item item = new Item();
        item.setMetadata(testMetadata);
        parentItem = itemDSpaceClient.create(collection.getID(),item);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    private static boolean compareBitstreams(Bitstream a, Bitstream b, boolean testId) {
        int rightCount = 0;

        if(a.getDescription() != b.getDescription()
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
        testBitstream.setName("Bitstream");
        testBitstream.setDescription("Basic description");
    }

    @Test
    public void testCRUD() throws Exception {
        // Test create
        Bitstream addedBitstream = bitstreamClient.create(parentItem.getID(), testBitstream);
        assertTrue("Added bitstream should be same as sent item.", compareBitstreams(testBitstream, addedBitstream, false));

        // Test read
        Bitstream readBitstream = bitstreamClient.read(addedBitstream.getID(), "");
        assertTrue("Read bitstream, should be same as added.", compareBitstreams(addedBitstream, readBitstream, true));

        // Test update
        addedBitstream.setName("Another name");
        addedBitstream.setDescription("Some text");
        bitstreamClient.update(addedBitstream.getID(), testBitstream);
        readBitstream = bitstreamClient.read(addedBitstream.getID(),"");
        assertTrue("Read bitstream should be same as updated bitstream.", compareBitstreams(addedBitstream, readBitstream, true));

        // Test delete
        bitstreamClient.delete(addedBitstream.getID());
        readBitstream = bitstreamClient.read(addedBitstream.getID(),"");
        assertNull("Read null bitstream, because is deleted.", readBitstream);
    }

    @Test
    public void testReadAll()  throws Exception {
        // Add some top community and some subcommunities
        bitstreamClient.create(parentItem.getID(), testBitstream);
        bitstreamClient.create(parentItem.getID(), testBitstream);
        bitstreamClient.create(parentItem.getID(), testBitstream);

        // Test read top communities.
        List<Bitstream> bitstreams = bitstreamClient.readAll("", 100, 0);
        assertEquals("Length of items should be 3.", 3, bitstreams.size());
    }

    @Ignore
    @Test
    public void testReadUpdateData() {

    }

    @Test
    public void testCreateReadDeleteResourcePolicies() {
        Bitstream addedBitstream = bitstreamClient.create(parentItem.getID(), testBitstream);

        ResourcePolicy resourcePolicy = new ResourcePolicy();
        resourcePolicy.setId(addedBitstream.getID());
        resourcePolicy.setAction(ResourcePolicy.Action.READ);
        resourcePolicy.setEpersonId(0);

        // Add resource policy
        bitstreamClient.addResourcePolicy(addedBitstream.getID(), resourcePolicy);

        // Test read subcollections
        List<ResourcePolicy> resourcePolicies = bitstreamClient.readResourcePolicies(addedBitstream.getID());
        assertEquals("Length of resource policies should be 1.", 1, resourcePolicies.size());

        // Test delete subcollections
        bitstreamClient.deleteResourcePolicy(addedBitstream.getID(), resourcePolicies.get(0).getId());
        resourcePolicies = bitstreamClient.readResourcePolicies(addedBitstream.getID());
        assertEquals("Length of deleted resource policies should be 0.", 0, resourcePolicies.size());
    }
}
