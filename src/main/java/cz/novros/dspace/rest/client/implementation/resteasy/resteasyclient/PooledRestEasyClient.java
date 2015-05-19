package cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient;

import cz.novros.dspace.rest.client.configuration.Configuration;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

/**
 * Child of RestEasyClient and override methods for creating and closing pooled connection.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class PooledRestEasyClient extends RestEasyClient {

    private PoolingClientConnectionManager connectionManager;
    private HttpClient httpClient;
    private ApacheHttpClient4Engine engine;

    /**
     * Method for preparing ResteasyClient. For example basic or pooled connection.
     *
     * @param configuration Configuration for REST client.
     */
    @Override
    public void create(Configuration configuration) {
        connectionManager = new PoolingClientConnectionManager();
        if (configuration.getMaxTotal() != null) {
            connectionManager.setMaxTotal(configuration.getMaxTotal());
        }
        if (configuration.getMaxPerRoute() != null) {
            connectionManager.setDefaultMaxPerRoute(configuration.getMaxPerRoute());
        }
        httpClient = new DefaultHttpClient(connectionManager);
        engine = new ApacheHttpClient4Engine(httpClient);
        client = new ResteasyClientBuilder().httpEngine(engine).disableTrustManager().build();
    }

    /**
     * Method for close ResteasyClient connection.
     */
    @Override
    public void close() {
        try {
            //client.close();
            //engine.finalize();
            connectionManager.shutdown();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
