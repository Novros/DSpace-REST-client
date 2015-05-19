package cz.novros.dspace.rest.client.client.tests;

import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.interfaces.client.ICollectionDspaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICommunityDSpaceClient;
import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Community;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class for test of community client.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public abstract class CommunityTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private Community testCommunity;
    protected static ICommunityDSpaceClient communityClient;
    protected static ICollectionDspaceClient collectionClient; // Only for last test.

    private static boolean compareCommunities(Community a, Community b, boolean testId) {
        if(!a.getCopyrightText().equals(b.getCopyrightText())
                || !a.getIntroductoryText().equals(b.getIntroductoryText())
                || !a.getShortDescription().equals(b.getShortDescription())
                || !a.getSidebarText().equals(b.getSidebarText())
                || !a.getName().equals(b.getName())) {
            return false;
        }

        if(testId) {
            if(!a.getID().equals(b.getID())) {
                return false;
            }
        }
        return true;
    }

    @Before
    public void beforeTest() {
        // Setup testCommunity
        testCommunity = new Community();
        testCommunity.setName("Top community");
        testCommunity.setCopyrightText("Copyright text");
        testCommunity.setIntroductoryText("Introductory text");
        testCommunity.setShortDescription("Short description");
        testCommunity.setSidebarText("Sidebar text");
        testCommunity.setID(0);
    }

    @Test
    public void testCRUD() throws Exception {
        // Test create
        int notUsedId = 0;
        Community addedCommunity = communityClient.create(notUsedId, testCommunity);
        assertTrue("Added community should be same as sent community.", compareCommunities(testCommunity, addedCommunity, false));

        // Test read
        Community readCommunity = communityClient.read(addedCommunity.getID(), "");
        assertTrue("Read community, should be same as added.", compareCommunities(addedCommunity,readCommunity, true));

        // Test update
        testCommunity.setIntroductoryText("text");
        testCommunity.setSidebarText("sidebar");
        communityClient.update(addedCommunity.getID(), testCommunity);
        readCommunity = communityClient.read(addedCommunity.getID(),"");
        assertTrue("Read community should be same as updated community.", compareCommunities(testCommunity, readCommunity, false));

        // Test delete
        communityClient.delete(addedCommunity.getID());
        thrown.expect(NotFoundException.class);
        communityClient.read(addedCommunity.getID(),"");
        //assertNull("Read null community, because is deleted.", readCommunity);
    }

    @Test
    public void testReadAllAndTopCommunities()  throws Exception {
        // Add some top community and some subcommunities
        int notUsedId = 0;
        communityClient.create(notUsedId, testCommunity);
        testCommunity.setName("Second top community");
        Community addedCommunity = communityClient.create(notUsedId, testCommunity);
        testCommunity.setName("Subcommunity");
        List<Community> oneSubcommunity = new ArrayList<>();
        oneSubcommunity.add(new Community());
        testCommunity.setSubCommunities(oneSubcommunity);
        communityClient.createSubcommunity(addedCommunity.getID(),testCommunity);

        // Test read top communities.
        List<Community> topCommunities = communityClient.readTop("",100,0);
        List<Community> communities = communityClient.readAll("", 100, 0);
        assertTrue("Length of top communities should be lower than all communities.", communities.size() > topCommunities.size() );
    }

    @Test
    public void testCreateReadDeleteSubcommunity()  throws Exception {
        // Setup structure
        int notUsedId = 0;
        testCommunity = communityClient.create(notUsedId, testCommunity);
        Community subcommunity = testCommunity;
        subcommunity.setName("Subcommunity");
        subcommunity.setShortDescription("Short");

        // Test create subcommunity
        Community addedSubcommunity = communityClient.createSubcommunity(testCommunity.getID(), subcommunity);
        assertTrue("Added subcommunity should be same.", compareCommunities(subcommunity, addedSubcommunity, false));

        // Test read subcommunities
        List<Community> subcommunities = communityClient.readSubcommunities(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of subcommunities should be 1.", 1, subcommunities.size());
        assertTrue("Subcommunities(0) should be same.", compareCommunities(subcommunity, subcommunities.get(0), false));

        // Test delete subcommunities
        thrown.expect(NotFoundException.class);
        communityClient.delete(addedSubcommunity.getID()); // Should not delete
        communityClient.deleteSubcommunity(testCommunity.getID(), addedSubcommunity.getID());
        subcommunities = communityClient.readSubcommunities(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of deleted subcommunities should be 0.", 0, subcommunities.size());
    }

    @Test
    public void testCreateReadDeleteSubcollection()  throws Exception {
        // Setup structure
        int notUsedId = 0;
        testCommunity = communityClient.create(notUsedId, testCommunity);
        Collection subcollection = new Collection();
        subcollection.setName("Subcollection");

        // Setup subcollection
        Collection addedSubcollection = collectionClient.create(testCommunity.getID(), subcollection);

        // Test read subcollections
        List<Collection> subcollections = communityClient.readSubcollections(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of subcollections should be 1.", 1, subcollections.size());

        // Test delete subcollections
        communityClient.deleteSubcollection(testCommunity.getID(), addedSubcollection.getID());
        subcollections = communityClient.readSubcollections(testCommunity.getID(), "", 100, 0);
        assertEquals("Length of deleted subcommunities should be 0.", 0, subcollections.size());

        ((DSpaceRESTEasyClient)collectionClient).close();
    }

}
