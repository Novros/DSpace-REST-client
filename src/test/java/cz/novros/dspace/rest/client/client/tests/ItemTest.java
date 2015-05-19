package cz.novros.dspace.rest.client.client.tests;

import cz.novros.dspace.rest.client.interfaces.client.IBitstreamDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;
import org.dspace.rest.common.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Rostislav on 30. 4. 2015.
 */
public abstract class ItemTest {
    private Item testItem;
    protected static IItemDSpaceClient itemClient;
    protected static IBitstreamDSpaceClient bitstreamDSpaceClient;
    protected static Collection parentCollection;

    /*public static void createStructure() throws Exception {
        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        ICommunityDSpaceClient communityDSpaceClient = factory.createCommunityClient();
        ICollectionDspaceClient collectionClient = factory.createCollectionClient();

        Community community = new Community();
        community.setName("Community");
        communityDSpaceClient.create(0,community);

        Collection collection = new Collection();
        collection.setName("Collection");
        parentCollection = collectionClient.create(0,collection);

        ((DSpaceRESTEasyClient)communityDSpaceClient).close();
        ((DSpaceRESTEasyClient)collectionClient).close();
    } */

    private static boolean compareItems(Item a, Item b, boolean testId) {
        int rightCount = 0;

        for (MetadataEntry aMetadataEntry : a.getMetadata()) {
            for (MetadataEntry bMetadataEntry : b.getMetadata()) {
                if(bMetadataEntry.getKey().equals(aMetadataEntry.getKey())
                        && bMetadataEntry.getLanguage().equals(aMetadataEntry.getLanguage())) {
                    if(bMetadataEntry.getValue() == aMetadataEntry.getValue()) {
                        rightCount++;
                    }
                }
            }
        }

        if(testId) {
            if(a.getID() != b.getID()) {
                return false;
            }
        }

        if(rightCount != a.getMetadata().size()) {
            return false;
        }

        return true;
    }

    @Before
    public void beforeTest() {
        // Setup testItem
        List<MetadataEntry> testMetadata = new ArrayList<MetadataEntry>();
        testMetadata.add(new MetadataEntry("dc.contributor.author","REST api",null));
        testMetadata.add(new MetadataEntry("dc.title","REST práce NG","cze"));
        testMetadata.add(new MetadataEntry("dc.title.alternative","Toto je prace pres REST","cze"));
        testMetadata.add(new MetadataEntry("dc.title","REST work NG","en"));
        testMetadata.add(new MetadataEntry("dc.title.alternative","This is work over REST","en"));
        testItem = new Item();
        testItem.setMetadata(testMetadata);
    }

    @Test
    public void testCRUD() throws Exception {
        // Test create
        int notUsedId = 0;
        Item addedItem = itemClient.create(parentCollection.getID(), testItem);
        assertTrue("Added item should be same as sent item.", compareItems(testItem, addedItem, false));

        // Test read
        Item readItem = itemClient.read(addedItem.getID(), "metadata");
        assertTrue("Read Item, should be same as added.", compareItems(addedItem, readItem, true));

        // Test update
        List<MetadataEntry> testMetadata = testItem.getMetadata();
        testMetadata.get(0).setValue("Another REST"); // should be autor
        testMetadata.get(1).setValue("Jiny nazev"); // Should be name
        testItem.setMetadata(testMetadata);
        itemClient.update(addedItem.getID(), testItem);
        readItem = itemClient.read(addedItem.getID(),"metadata");
        assertTrue("Read item should be same as updated item.", compareItems(testItem, readItem, false));

        // Test delete
        itemClient.delete(addedItem.getID());
        readItem = itemClient.read(addedItem.getID(),"metadata");
        assertNull("Read null item, because is deleted.", readItem);
    }

    @Test
    public void testReadAll()  throws Exception {
        // Add some top community and some subcommunities
        itemClient.create(parentCollection.getID(), testItem);
        itemClient.create(parentCollection.getID(), testItem);
        itemClient.create(parentCollection.getID(), testItem);

        // Test read top communities.
        List<Item> items = itemClient.readAll("", 100, 0);
        assertEquals("Length of items should be 3.", 3, items.size());
    }

    @Test
    public void testCRUDMetadata() {
        // Setup item
        testItem = itemClient.create(parentCollection.getID(), testItem);
        testItem = itemClient.read(testItem.getID(), "metadata");

        // Add metadata
        List<MetadataEntry> metadata = new ArrayList<>();
        metadata.add(new MetadataEntry("dc.contributor.referee","Nothing man", null));
        itemClient.addMetadata(testItem.getID(), metadata);

        // Test read metadata
        List<MetadataEntry> readMetadata = itemClient.readMetadata(testItem.getID());
        assertEquals("Size of list of metadata size should be testItem+1.", testItem.getMetadata().size()+1, readMetadata.size());

        // Test Update all metadata
        List<MetadataEntry> testMetadata = testItem.getMetadata();
        testMetadata.get(0).setValue("Another REST"); // should be autor
        testMetadata.get(1).setValue("Jiny nazev"); // Should be name
        testMetadata.remove(testMetadata.size()-1);
        testMetadata.remove(testMetadata.size()-1);
        itemClient.updateMetadata(testItem.getID(), testMetadata);
        readMetadata = itemClient.readMetadata(testItem.getID());
        assertEquals("Size of list of metadata should be same.", testMetadata.size(), readMetadata.size());

        // Test delete metadata
        itemClient.deleteMetadata(testItem.getID());
        readMetadata = itemClient.readMetadata(testItem.getID());
        assertEquals("Size of list size should be minimal(4).", 4, readMetadata.size());
    }

    @Test
    public void testReadDeleteBitstreams() {
        testItem = itemClient.create(parentCollection.getID(), testItem);

        // Setup bitstream
        Bitstream bitstream = new Bitstream();
        bitstream.setName("Test bitstream");
        Bitstream addedBitstream = bitstreamDSpaceClient.create(testItem.getID(), bitstream);

        // Test read bitstreams
        List<Bitstream> bitstreams = itemClient.readBitstreams(testItem.getID(),"");
        assertEquals("Length of bitstreams should be 1.", 1, bitstreams.size());

        // Test delete bitstream
        itemClient.deleteBitstream(testItem.getID(), addedBitstream.getID());
        bitstreams = itemClient.readBitstreams(testItem.getID(), "");
        assertEquals("Length of deleted bitstreams should be 0.", 0, bitstreams.size());
    }
}
