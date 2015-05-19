package cz.novros.dspace.rest.client.factory;

import cz.novros.dspace.rest.client.factory.resteasy.RESTeasyBasicFactory;
import cz.novros.dspace.rest.client.factory.resteasy.RESTeasyPooledFactory;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;
import org.junit.Test;

/**
 * Created by Rostislav on 7. 5. 2015.
 */
public class RESTeasyFactoryTest {

    private static final String endpointURL = "http://dspace.test.cz/rest";
    private static final String username = "dspace";
    private static final String password = "dspace";

    @Test
    public void testCreatingBasicRESTeasyClient() {
        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(endpointURL, username, password);
        IItemDSpaceClient client = factory.createItemClient();
    }

    @Test
    public void testCreatingPooledRESTeasyClient() {
        IDSpaceRESTFactory factory = new RESTeasyPooledFactory(endpointURL, username, password);
        IItemDSpaceClient client = factory.createItemClient();
    }

}
