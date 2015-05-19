package cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient;

import cz.novros.dspace.rest.client.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * Child of RestEasyClient and override methods for creating and closing basic connection.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class BasicRestEasyClient extends RestEasyClient {

    /**
     * Method for preparing ResteasyClient. For example basic or pooled connection.
     *
     * @param configuration Configuration for REST client.
     */
    @Override
    public void create(Configuration configuration) {
        ResteasyClientBuilder builder = new ResteasyClientBuilder();
        builder.disableTrustManager();
        client = builder.build();
    }

    /**
     * Method for close ResteasyClient connection.
     */
    @Override
    public void close() {
        client.close();
    }
}
