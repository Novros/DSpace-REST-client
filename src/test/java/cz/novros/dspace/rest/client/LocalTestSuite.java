package cz.novros.dspace.rest.client;

import cz.novros.dspace.rest.client.local.LocalBitstreamTest;
import cz.novros.dspace.rest.client.local.LocalCollectionTest;
import cz.novros.dspace.rest.client.local.LocalCommunityTest;
import cz.novros.dspace.rest.client.local.LocalItemTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for testing all clients locally.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocalCommunityTest.class,
        LocalCollectionTest.class,
        LocalItemTest.class,
        LocalBitstreamTest.class
})
public class LocalTestSuite {
}
