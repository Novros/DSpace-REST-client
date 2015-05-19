package cz.novros.dspace.rest.client.factory.resteasy;

import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.BasicRestEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;

import java.util.Properties;

/**
 * Factory for creating basic RESTeasy clients.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class RESTeasyBasicFactory extends  RESTeasyFactory {

    public RESTeasyBasicFactory(String endpointURL, String username, String password) {
        super(endpointURL, username, password);
    }

    public RESTeasyBasicFactory(String endpointURL, String username, String password, Integer maxTotal, Integer maxPerRoute) {
        super(endpointURL, username, password, maxTotal, maxPerRoute);
    }

    public RESTeasyBasicFactory(Properties properties) {
        super(properties);
    }

    /**
     * Method for creating type of RESTeasy cover client.
     *
     * @return It returns RESTeasy client.
     */
    @Override
    protected RestEasyClient getRESTeasyClient() {
        return clientFactory.getRESTeasyClient(BasicRestEasyClient.class);
    }
}
