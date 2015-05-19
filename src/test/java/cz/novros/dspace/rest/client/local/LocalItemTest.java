package cz.novros.dspace.rest.client.local;

import cz.novros.dspace.rest.client.client.tests.ItemTest;
import cz.novros.dspace.rest.client.factory.IDSpaceRESTFactory;
import cz.novros.dspace.rest.client.factory.resteasy.RESTeasyBasicFactory;
import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.serverside.ItemResource;
import cz.novros.dspace.rest.client.serverside.RestIndex;
import cz.novros.dspace.rest.client.utils.RemoteUtil;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Rostislav on 11. 5. 2015.
 */
public class LocalItemTest extends ItemTest {
    private static String baseUri;
    private static TJWSEmbeddedJaxrsServer server;
    @InjectMocks
    private final static ItemResource itemResource = new ItemResource();
    private final static RestIndex restIndex = new RestIndex();

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @BeforeClass
    public static void beforeClass() throws Exception {
        int port = RemoteUtil.findFreePort();
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(port);
        server.getDeployment().setResources(Arrays.asList(itemResource, restIndex));
        server.start();

        baseUri = "http://localhost:" + port + "/";

        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        itemClient = factory.createItemClient();
        bitstreamDSpaceClient = factory.createBitstreamClient();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
        ((DSpaceRESTEasyClient)itemClient).close();
    }

    @Test
    public void test() throws Exception {
        ClientRequest request = new ClientRequest(baseUri + "nothing");
        ClientResponse<String> response = request.get(String.class);
        assertEquals("Nothing", response.getEntity());
    }
}
