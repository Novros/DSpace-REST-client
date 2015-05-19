package cz.novros.dspace.rest.client.factory.resteasy;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.factory.IDSpaceRESTFactory;
import cz.novros.dspace.rest.client.implementation.resteasy.clients.BitstreamClient;
import cz.novros.dspace.rest.client.implementation.resteasy.clients.CollectionClient;
import cz.novros.dspace.rest.client.implementation.resteasy.clients.CommunityClient;
import cz.novros.dspace.rest.client.implementation.resteasy.clients.ItemClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;
import cz.novros.dspace.rest.client.interfaces.client.IBitstreamDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICollectionDspaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICommunityDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;

import java.util.Properties;

/**
 * Base class of RESTeasy factory for creating rest easy REST DSpace clients.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public abstract class RESTeasyFactory implements IDSpaceRESTFactory {

    private Configuration configuration;
    protected RESTeasyClientFactory clientFactory;

    public RESTeasyFactory(String endpointURL, String username, String password) {
        configuration = new Configuration(endpointURL, username, password, null, null);
        clientFactory = new RESTeasyClientFactory(configuration);
    }

    public RESTeasyFactory(String endpointURL, String username, String password,
                           Integer maxTotal, Integer maxPerRoute) {
        configuration = new Configuration(endpointURL, username, password, maxTotal, maxPerRoute);
        clientFactory = new RESTeasyClientFactory(configuration);
    }

    public RESTeasyFactory(Properties properties) {
        configuration = new Configuration(properties.getProperty("endpointURL"),
                properties.getProperty("username"),
                properties.getProperty("password"),
                properties.getProperty("maxTotal") != null ? Integer.valueOf(properties.getProperty("maxTotal")) : null,
                properties.getProperty("maxPerRoute") != null ? Integer.valueOf(properties.getProperty("maxPerRoute")) : null);
        clientFactory = new RESTeasyClientFactory(configuration);
    }

    /**
     * Create community client for REST api.
     *
     * @return Returns new instance of community client.
     */
    @Override
    public ICommunityDSpaceClient createCommunityClient() {
        return new CommunityClient(configuration, getRESTeasyClient());
    }

    /**
     * Create collection client for REST api.
     *
     * @return Returns new instance of collection client.
     */
    @Override
    public ICollectionDspaceClient createCollectionClient() {
        return new CollectionClient(configuration, getRESTeasyClient());
    }

    /**
     * Create item client for REST api.
     *
     * @return Returns new instance of item client.
     */
    @Override
    public IItemDSpaceClient createItemClient() {
        return new ItemClient(configuration, getRESTeasyClient());
    }

    /**
     * Create bitstream client for REST api.
     *
     * @return Returns new instance of bitstream client.
     */
    @Override
    public IBitstreamDSpaceClient createBitstreamClient() {
        return new BitstreamClient(configuration, getRESTeasyClient());
    }

    /**
     * Method for creating type of RESTeasy cover client.
     *
     * @return It returns RESTeasy client.
     */
    protected abstract RestEasyClient getRESTeasyClient();
}
