package cz.novros.dspace.rest.client;

import cz.novros.dspace.rest.client.dspace.DSpaceBitstreamTest;
import cz.novros.dspace.rest.client.dspace.DSpaceCollectionTest;
import cz.novros.dspace.rest.client.dspace.DSpaceCommunityTest;
import cz.novros.dspace.rest.client.dspace.DSpaceItemTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for testing clients against DSpace instance.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DSpaceCommunityTest.class,
        DSpaceCollectionTest.class,
        DSpaceItemTest.class,
        DSpaceBitstreamTest.class
})
public class DSpaceTestSuite {
}
