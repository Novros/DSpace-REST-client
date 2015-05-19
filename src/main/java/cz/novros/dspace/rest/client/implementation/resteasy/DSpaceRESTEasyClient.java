package cz.novros.dspace.rest.client.implementation.resteasy;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.implementation.DSpaceRESTClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;
import org.dspace.rest.common.User;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Basic class for RESTEasy client.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class DSpaceRESTEasyClient extends DSpaceRESTClient {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected RestEasyClient client;

    public DSpaceRESTEasyClient(Configuration configuration, RestEasyClient client) {
        super(configuration);
        this.client = client;
    }

    /**
     * Login REST api client into DSpace REST api.
     *
     * @return Return generated code from DSpace REST api. Use this code to communicate with DSpace REST api.
     * @throws javax.ws.rs.WebApplicationException This exception is thrown when was problem with communication of DSpace REST api
     *                                             or bad credentials.
     */
    @Override
    public String login() throws WebApplicationException {
        log.debug("Requesting authentication token [username={}, password={}].", configuration.getUsername(), "***");
        ResteasyWebTarget target = client.target(ENDPOINT_URL + "/login");
        Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(new User(configuration.getUsername(), configuration.getPassword()), MediaType.APPLICATION_JSON));
        try {
            return extractResult(String.class, response);
        } catch (WebApplicationException ex) {
            log.error("Requesting authentication token failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Close RESTEasy client. This must be done.
     */
    public void close() {
        client.close();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(!client.isClosed()) {
            throw new Exception("Unclosed RESTEasy client!!!");
        }
    }
}
