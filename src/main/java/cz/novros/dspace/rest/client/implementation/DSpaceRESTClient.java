package cz.novros.dspace.rest.client.implementation;

import cz.novros.dspace.rest.client.configuration.Configuration;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Class with basic information for REST api. This is core class from which must inherit all DSpaceObject clients.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public abstract class DSpaceRESTClient {

    protected final Configuration configuration;
    protected final String ENDPOINT_URL;

    protected static final String COMMUNITIES = "/communities";
    protected static final String COLLECTIONS = "/collections";
    protected static final String ITEMS = "/items";
    protected static final String METADATA = "/metadata";
    protected static final String BITSTREAMS = "/bitstreams";

    protected static final String headerToken = "rest-dspace-token";

    public DSpaceRESTClient(Configuration configuration) {
        this.configuration = configuration;
        if (configuration.getEndpointURL().endsWith("/")) {
            ENDPOINT_URL = configuration.getEndpointURL().substring(0, configuration.getEndpointURL().length()-1);
        } else {
            ENDPOINT_URL = configuration.getEndpointURL();
        }
    }

    /**
     * Extract object from result.
     *
     * @param responseType Type of result.
     * @param response Actual result
     * @param <T> Type of result. In this case often Community, Collection, etc.
     * @return Returns extracted object from response if all was ok. Otherwise throws response code.
     * @throws WebApplicationException Throws exception if response code was not from interval <200-300).
     */
    public static <T> T extractResult(Class<T> responseType, Response response) throws WebApplicationException {
        int status = response.getStatus();
        if (status >= 200 && status < 300) {
            return response.readEntity(responseType);
        } else {
            handleErrorStatus(response);
        }
        return null;
    }

    /**
     * Handle error code from response.
     *
     * @param response Response in which should be some error.
     * @throws WebApplicationException Children of WebApplicationException are thrown.
     */
    public static void handleErrorStatus(Response response) throws WebApplicationException {
        final int status = response.getStatus();
        if (status >= 200 && status < 300) {
            return;
        } else if (status == 400) {
            throw new BadRequestException(response);
        } else if (status == 401) {
            throw new NotAuthorizedException(response);
        } else if (status == 404) {
            throw new NotFoundException(response);
        } else if (status == 500) {
            throw new InternalServerErrorException(response);
        } else if (status == 503) {
            throw new ServiceUnavailableException(response);
        } else if (status >= 500) {
            throw new ServerErrorException(response);
        } else if (status >= 400) {
            throw new ClientErrorException(response);
        } else if (status >= 300) {
            throw new RedirectionException(response);
        } else {
            throw new WebApplicationException(response);
        }
    }

    public static String addArguments(String expand) {
        String arguments = "";
        if( !expand.isEmpty() || !expand.equals("")) {
            arguments += "?expand=" + expand;
        }
        return arguments;
    }

    public static String addArguments(String expand, Integer limit, Integer offset) {
        String arguments = "";
        if( !expand.isEmpty() || !expand.equals("")) {
            arguments += "expand=" + expand;
        }

        if(arguments.length() > 0) {
            arguments += "&";
        }

        if( limit != null) {
            arguments += "limit=" + limit;
        }

        if(arguments.length() > 0) {
            arguments += "&";
        }

        if( offset != null) {
            arguments += "offset=" + offset;
        }

        if(arguments.length() > 0) {
            arguments = "?" + arguments;
        }

        return arguments;
    }

    /**
     * Login REST api client into DSpace REST api.
     *
     * @return Return generated code from DSpace REST api. Use this code to communicate with DSpace REST api.
     * @throws WebApplicationException This exception is thrown when was problem with communication of DSpace REST api
     * or bad credentials.
     */
    public abstract String login() throws WebApplicationException;
}
