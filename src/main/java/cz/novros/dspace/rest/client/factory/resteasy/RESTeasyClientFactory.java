package cz.novros.dspace.rest.client.factory.resteasy;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.BasicRestEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.PooledRestEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;

/**
 * This class create client for RESTeasy REST client. It can create basic client or pooled client.
 *
 * @author Rostislav Novak
 * @version 1.0 $Revision: 1 $
 */
public class RESTeasyClientFactory {

    private final Configuration configuration;

    /**
     * Create RESTeasyClientFactory to create types of clients.
     *
     * @param configuration Configuration which will be used to create ResteasyClient client class.
     */
    public RESTeasyClientFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Create RestEasyClient client, which is cover of RESTeasy client.
     *
     * @param type Type of RESTeasy client. It can be basic or pooled.
     * @throws IllegalArgumentException If argument type is bad.
     * @return Returns created RestEasyClient client or throw IllegalArgumentException exception.
     */
    public RestEasyClient getRESTeasyClient (Class<? extends RestEasyClient> type) throws IllegalArgumentException {
        if (BasicRestEasyClient.class.isAssignableFrom(type)) {
            BasicRestEasyClient client = new BasicRestEasyClient();
            client.create(configuration);
            return client;
        } else if (PooledRestEasyClient.class.isAssignableFrom(type)) {
            PooledRestEasyClient client = new PooledRestEasyClient();
            client.create(configuration);
            return client;
        } else {
            throw new IllegalArgumentException(type + " is not supported. Use BasicClient.class or PooledClient.class.");
        }
    }
}
