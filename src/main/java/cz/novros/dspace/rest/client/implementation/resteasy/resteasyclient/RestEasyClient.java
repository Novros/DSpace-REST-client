package cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient;

import cz.novros.dspace.rest.client.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.net.URI;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

/**
 * Class which cover ResteasyClient. Use inherited class BasicRestEasyClient for basic client or
 * PooledRestEasyClient or pooled connection.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public abstract class RestEasyClient {

    protected ResteasyClient client;

    /**
     * Method for preparing ResteasyClient. For example basic or pooled connection.
     *
     * @param configuration Configuration for REST client.
     */
    public abstract void create(Configuration configuration);

    /**
     * Method for close ResteasyClient connection.
     */
    public abstract void close();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(!client.isClosed()) {
            throw new Exception("Unclosed RESTEasy client!!!");
        }
    }

    public ResteasyWebTarget target(String uri) {
        return client.target(uri);
    }

    public ResteasyWebTarget target(URI uri) {
        return client.target(uri);
    }

    public ResteasyWebTarget target(UriBuilder uriBuilder) {
        return client.target(uriBuilder);
    }

    public ResteasyWebTarget target(Link link) {
        return client.target(link);
    }

    public boolean isClosed() {
        return client.isClosed();
    }
}
