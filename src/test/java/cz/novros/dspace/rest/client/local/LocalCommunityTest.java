package cz.novros.dspace.rest.client.local;

import cz.novros.dspace.rest.client.utils.RemoteUtil;
import cz.novros.dspace.rest.client.client.tests.CommunityTest;
import cz.novros.dspace.rest.client.factory.IDSpaceRESTFactory;
import cz.novros.dspace.rest.client.factory.resteasy.RESTeasyBasicFactory;
import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.serverside.CommunityResource;
import cz.novros.dspace.rest.client.serverside.RestIndex;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.InjectMocks;

import java.util.Arrays;

/**
 * Class for local test of community client.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class LocalCommunityTest extends CommunityTest {

    private static String baseUri;
    private static TJWSEmbeddedJaxrsServer server;
    @InjectMocks
    private final static CommunityResource communityResource = new CommunityResource();
    private final static RestIndex restIndex = new RestIndex();

    @BeforeClass
    public static void beforeClass() throws Exception {
        int port = RemoteUtil.findFreePort();
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(port);
        server.getDeployment().setResources(Arrays.asList(communityResource, restIndex));
        server.start();

        baseUri = "http://localhost:" + port + "/";

        IDSpaceRESTFactory factory = new RESTeasyBasicFactory(baseUri,"username","password");
        communityClient = factory.createCommunityClient();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
        ((DSpaceRESTEasyClient)communityClient).close();
    }
}
