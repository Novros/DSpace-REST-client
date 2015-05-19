package cz.novros.dspace.rest.client.implementation.resteasy.clients;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;
import cz.novros.dspace.rest.client.interfaces.client.ICommunityDSpaceClient;
import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Community;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

/**
 * Community client implementation in RESTEasy.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class CommunityClient extends DSpaceRESTEasyClient implements ICommunityDSpaceClient {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public CommunityClient(Configuration configuration, RestEasyClient client) {
        super(configuration, client);
    }

    /**
     * Create top community in DSpace.
     *
     * @param notUsedId This filed is not used in this case.
     * @param community Community which will be created in DSpace.
     * @return Returns new created community in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating community in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: server problem, access denied, bad URL and so on.
     */
    @Override
    public Community create(Integer notUsedId, Community community) throws WebApplicationException {
        log.debug("Creating top community in DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(community, MediaType.APPLICATION_JSON));
        try {
            community = extractResult(Community.class, response);
            log.info("Community (handle={},id={}) successfully created in DSpace.", community.getHandle(), community.getID());
            return community;
        } catch (WebApplicationException ex) {
            log.error("Creating community failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read community from DSpace.
     *
     * @param communityId Id of community which will be read.
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @return Returns specific community if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading community from DSpace.
     *                                 Problem can be: community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Community read(Integer communityId, String expand) throws WebApplicationException {
        log.debug("Reading community(id={}) from DSpace.", communityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + communityId + addArguments(expand));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community community = extractResult(Community.class, response);
            log.info("Community (handle={}) successfully read from DSpace(id={}).", community.getHandle(), community.getID());
            return community;
        } catch (WebApplicationException ex) {
            log.error("Reading community(id={}) failed. Response code: {}.", communityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Update community in DSpace.
     *
     * @param communityId     Id of community, which will be updated.
     * @param community Updated community. Does not matter which id will be in instance of community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating community in DSpace.
     *                                 Problem can be: community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void update(Integer communityId, Community community) throws WebApplicationException {
        log.debug("Updating community(id={}) in DSpace.", communityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + communityId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(community, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Community(id={}) successfully updated in DSpace.", community.getID());
        } catch (WebApplicationException ex) {
            log.error("Updating community(id={}) failed. Response code: {}.", communityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete community from DSpace.
     *
     * @param communityId Id of community, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting community in DSpace.
     *                                 Problem can be: community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void delete(Integer communityId) throws WebApplicationException {
        log.debug("Deleting community(id={}) in DSpace.", communityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + communityId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Community (id={}) successfully deleted in DSpace.", communityId);
        } catch (WebApplicationException ex) {
            log.error("Deleting community(id={}) failed. Response code: {}.", communityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all communities in DSpace.
     *
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @param limit  Limit of communities in List.
     * @param offset Offset for List of communities in DSpace database.
     * @return It returns filled or empty List of communities.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading communities from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Community> readAll(String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading communities from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community[] communities = extractResult(Community[].class, response);
            log.info("Communities were successfully read from DSpace. (count={})", communities.length);
            return Arrays.asList(communities);
        } catch (WebApplicationException ex) {
            log.error("Reading communities failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all top communities in DSpace.
     *
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @param limit  Limit of communities in List.
     * @param offset Offset of List of communities in DSpace database.
     * @return It returns List of all top communities in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading communities from DSpace.
     *                                             Problem can be: server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Community> readTop(String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading top communities from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/top-communities" + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community[] communities = extractResult(Community[].class, response);
            log.info("Top communities were successfully read from DSpace. (count={})", communities.length);
            return Arrays.asList(communities);
        } catch (WebApplicationException ex) {
            log.error("Reading top communities failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Creates subcommunity in community.
     *
     * @param parentCommunityId Parent community Id, in which will be community created.
     * @param subcommunity      Community, which will be created.
     * @return It returns new created subcommunity.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with creating subcommunity in community or problem with
     *                                             finding community in DSpace. Problem can be: Parent community not found, server problem, access denied, bad URL
     *                                             and so on.
     */
    @Override
    public Community createSubcommunity(Integer parentCommunityId, Community subcommunity) throws WebApplicationException {
        log.debug("Creating subcommunity in community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COMMUNITIES);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(subcommunity, MediaType.APPLICATION_JSON));
        try {
            subcommunity = extractResult(Community.class, response);
            log.info("Subcommunity (handle={},id={}) successfully created in community(id={}).", new Object[]{subcommunity.getHandle(), subcommunity.getID(), parentCommunityId});
            return subcommunity;
        } catch (WebApplicationException ex) {
            log.error("Creating subcommunity in community(id={}) failed. Response code: {}.", parentCommunityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all subcommunities from parent community.
     *
     * @param parentCommunityId Parent community id.
     * @param expand            Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *                          logo and all. It must be separated by comma.
     * @param limit             Limit of communities in List.
     * @param offset            Offset of List of communities in parent community list.
     * @return It returns List of subcommunities.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading subcommunities from community or problem
     *                                             with finding parent community. Problem can be: parent community not found, server problem, access denied,
     *                                             bad URL and so on.
     */
    @Override
    public List<Community> readSubcommunities(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading subcommunities from community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COMMUNITIES + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Community[] communities = extractResult(Community[].class, response);
            log.info("Subcommunities were successfully read from community(id={}). (count={})", parentCommunityId, communities.length);
            return Arrays.asList(communities);
        } catch (WebApplicationException ex) {
            log.error("Reading subcommunities from community(id={}) failed. Response code: {}.", parentCommunityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete subcommunity from parent community.
     *
     * @param parentCommunityId Parent community Id.
     * @param subcommunityId    Subcommunity Id of parent community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting subcommunity from community.
     *                                             Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void deleteSubcommunity(Integer parentCommunityId, Integer subcommunityId) throws WebApplicationException {
        log.debug("Deleting subcommunity(id={}) from community(id={}).", subcommunityId, parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COMMUNITIES + "/" + subcommunityId );
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Subcommunity (id={}) successfully deleted in community(id={}). ).", subcommunityId, parentCommunityId);
        } catch (WebApplicationException ex) {
            log.error("Deleting subcommunity(id={}) from community(id={}) failed. Response code: {}.", new Object[]{subcommunityId, parentCommunityId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all subcollections from parent community.
     *
     * @param parentCommunityId Parent community Id
     * @param expand            Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *                          logo and all. It must be separated by comma.
     * @param limit             Limit of subcollections in List.
     * @param offset            Offset of List of collections in parent community.
     * @return It returns List of subcollections from parent community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading subcollections from community.
     *                                             Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Collection> readSubcollections(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading subcollections from community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COLLECTIONS + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection[] collections = extractResult(Collection[].class, response);
            log.info("Subcollections were successfully read from community(id={}). (count={})", parentCommunityId, collections.length);
            return Arrays.asList(collections);
        } catch (WebApplicationException ex) {
            log.error("Reading subcollections from community(id={}) failed. Response code: {}.", parentCommunityId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete subcollection from parent community.
     *
     * @param parentCommunityId Parent community id.
     * @param subcollectionId   Subcollection Id of parent community.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting subcollection from community.
     *                                             Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void deleteSubcollection(Integer parentCommunityId, Integer subcollectionId) throws WebApplicationException {
        log.debug("Deleting subcollection(id={}) from community(id={}).", subcollectionId, parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COLLECTIONS + "/" + subcollectionId );
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("subcollection (id={}) successfully deleted in community(id={}). ).", subcollectionId, parentCommunityId);
        } catch (WebApplicationException ex) {
            log.error("Deleting subcollection(id={}) from community(id={}) failed. Response code: {}.", new Object[]{subcollectionId, parentCommunityId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }
}
