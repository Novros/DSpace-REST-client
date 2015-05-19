package cz.novros.dspace.rest.client.implementation.resteasy.clients;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;
import cz.novros.dspace.rest.client.interfaces.client.IBitstreamDSpaceClient;
import org.dspace.rest.common.Bitstream;
import org.dspace.rest.common.ResourcePolicy;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Bitstream client implementation in RESTEasy.
 *
 * @author Rostislav Novak
 * @author Petr Karel
 * @version 1.0, $Revision: 1 $
 */
public class BitstreamClient extends DSpaceRESTEasyClient implements IBitstreamDSpaceClient {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public BitstreamClient(Configuration configuration, RestEasyClient client) {
        super(configuration, client);
    }

    /**
     * Not used. Never use this method.
     * @param notUsed
     * @param notUsedBitstream
     * @return
     */
    @Deprecated
    public Bitstream create(Integer notUsed, Bitstream notUsedBitstream) {
        throw new ForbiddenException();
    }

    /**
     * Create bitstream in DSpace.
     *
     * @param parentItemId  Id of parent item of new bitstream.
     * @param bitstream Bitstream which will be created in item.
     * @param inputStream Data which will be in created bitstream.
     * @return Returns new created bitstream in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating bitstream in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Bitstream create(Integer parentItemId, Bitstream bitstream, InputStream inputStream) throws WebApplicationException {
        log.debug("Creating bitstream in item(id={}).", parentItemId);
        String token = login();

        UriBuilder uriBuilder = UriBuilder.fromPath(ENDPOINT_URL + ITEMS + "/" + parentItemId + BITSTREAMS);
        uriBuilder.queryParam("name", bitstream.getName() != null ? bitstream.getName() : "");
        uriBuilder.queryParam("description", bitstream.getDescription() != null ? bitstream.getDescription() : "");
        if (bitstream.getPolicies() != null && bitstream.getPolicies().length > 0) {
            ResourcePolicy defaultPolicy = bitstream.getPolicies()[0];
            uriBuilder.queryParam("groupId", defaultPolicy.getGroupId());
            if (defaultPolicy.getStartDate() != null) {
                LocalDate startDate = new LocalDate(defaultPolicy.getStartDate());
                uriBuilder.queryParam("year", startDate.getYear());
                uriBuilder.queryParam("month", startDate.getMonthOfYear());
                uriBuilder.queryParam("day", startDate.getDayOfMonth());
            }
        }

        ResteasyWebTarget target = client.target(uriBuilder.build().toString());
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(inputStream, MediaType.APPLICATION_JSON));
        try {
            bitstream = extractResult(Bitstream.class, response);
            log.info("Bitstream (handle={}, id={}) successfully created in item(id={}).", new Object[]{bitstream.getHandle(), bitstream.getID(), parentItemId});
            return bitstream;
        } catch (WebApplicationException ex) {
            log.error("Creating bitstream failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read bitstream from DSpace.
     *
     * @param bitstreamId Id of bitstream which will be read.
     * @param expand Expand fields for bitstream, which can be: parent, policies and all. It must be separated by comma.
     * @return Returns bitstream if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading bitstream from DSpace.
     *                                 Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Bitstream read(Integer bitstreamId, String expand) throws WebApplicationException {
        log.debug("Reading bitstream(id={}) from DSpace.", bitstreamId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + addArguments(expand));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Bitstream bitstream = extractResult(Bitstream.class, response);
            log.info("Bitstream(handle={},id={}) successfully read from DSpace.", bitstream.getHandle(), bitstream.getID());
            return bitstream;
        } catch (WebApplicationException ex) {
            log.error("Reading bitstream(id={}) failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Update bitstream in DSpace. Beware, it only updated bitstream information not data.
     *
     * @param bitstreamId Id of bitstream, which will be updated.
     * @param bitstream Updated bitstream. Does not matter which id will be in instance of Bitstream
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating bitstream in DSpace.
     *                                 Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void update(Integer bitstreamId, Bitstream bitstream) throws WebApplicationException {
        log.debug("Updating bitstream(id={}) in DSpace.", bitstreamId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(bitstream, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Bitstream(id={}) successfully updated in DSpace.", bitstreamId);
        } catch (WebApplicationException ex) {
            log.error("Updating bitstream(id={}) failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete Bitstream from Dspace.
     *
     * @param bitstreamId Id of Bitstream, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting Bitstream in DSpace.
     *                                 Problem can be: Bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void delete(Integer bitstreamId) throws WebApplicationException {
        log.debug("Deleting bitstream in DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Bitstream(id={}) successfully deleted in DSpace.", bitstreamId);
        } catch (WebApplicationException ex) {
            log.error("Deleting bitstream(id={}) failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all bitstreams.
     *
     * @param expand Expand fields for bitstream, which can be: parent, policies and all. It must be separated by comma.
     * @param limit  Limit of bitstream in List.
     * @param offset Offset of List bitstream in DSpace database.
     * @return It returns filled or empty List of specific Bitstreams.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading bitstream from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Bitstream> readAll(String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading bitstreams from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Bitstream[] bitstreams = extractResult(Bitstream[].class, response);
            log.info("Bitstreams were successfully read from DSpace. (count={})", bitstreams.length);
            return Arrays.asList(bitstreams);
        } catch (WebApplicationException ex) {
            log.error("Reading bitstreams failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read bitstream data.
     *
     * @param bitstreamId Id of bitstream.
     * @return Returns InputStream filled with data of bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading data of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public InputStream readData(Integer bitstreamId) throws WebApplicationException {
        String token = login();
        log.info("Reading bitstream's (id={}) binary data.", bitstreamId);

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/retrieve");
        Response response = target.request().header(headerToken, token).accept(MediaType.WILDCARD).get();
        try {
            InputStream inputStream = extractResult(InputStream.class, response);
            log.info("Bitstream's (id={}) binary data read successfully.", bitstreamId);
            return inputStream;
        } catch (WebApplicationException ex) {
            log.error("Reading bitstream's (id={}) binary data failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Update data of bitstream.
     *
     * @param bitstreamId Id of bitstream.
     * @param inputStream InputStream filled with new data, which will be replaced in bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating data of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void updateData(Integer bitstreamId, InputStream inputStream) throws WebApplicationException {
        String token = login();
        log.info("Updating bitstream's (id={}) binary data.", bitstreamId);

        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/data");
        Response response = target.request().header(headerToken, token).accept(MediaType.WILDCARD).put(Entity.entity(inputStream, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Bitstream's (id={}) binary data updated successfully.", bitstreamId);
        } catch (WebApplicationException ex) {
            log.error("Updating bitstream's (id={}) binary data failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all bitstream resource policies.
     *
     * @param bitstreamId Id of bitstream.
     * @return It returns List of resource policies of bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading resource policies of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public List<ResourcePolicy> readResourcePolicies(Integer bitstreamId) throws WebApplicationException {
        log.debug("Getting all bitstream's (id={}) policies.", bitstreamId);
        ResourcePolicy[] resourcePolicies;
        String token = login();
        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/policy");
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            resourcePolicies = extractResult(ResourcePolicy[].class, response);
            return Arrays.asList(resourcePolicies);
        } catch (WebApplicationException ex) {
            log.error("Getting bitstream's (id={}) policies failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Add resource policy to bitstream.
     *
     * @param bitstreamId Id of bitstream
     * @param policy      Resource policy which will be added to bitstream.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with adding resource policy of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void addResourcePolicy(Integer bitstreamId, ResourcePolicy policy) throws WebApplicationException {
        log.info("Adding a new policy to bitstream (id={}).", bitstreamId);
        String token = login();
        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/policy");
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(policy, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Policy(id={}) added to bitstream (id={}) successfully.", policy, bitstreamId);
        } catch (WebApplicationException ex) {
            log.error("Adding policy to bitstream(id={}) failed. Response code: {}.", bitstreamId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete resource policy in bitstream.
     *
     * @param bitstreamId      Id of bitstream.
     * @param resourcePolicyId Id of resource policy which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting resource policy of bitstream.
     *                                             Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void deleteResourcePolicy(Integer bitstreamId, Integer resourcePolicyId) throws WebApplicationException {
        log.info("Deleting resource policy(id={}) from bitstream (id={}).", resourcePolicyId, bitstreamId);
        String token = login();
        ResteasyWebTarget target = client.target(ENDPOINT_URL + BITSTREAMS + "/" + bitstreamId + "/policy/" + resourcePolicyId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Bitstream's(id={}) policy (id={}) deleted successfully.", bitstreamId, resourcePolicyId);
        } catch (WebApplicationException ex) {
            log.error("Deleting policy(id={}) from bitstream (id={}) failed. Response code: {}.", new Object[]{resourcePolicyId, bitstreamId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }
}
