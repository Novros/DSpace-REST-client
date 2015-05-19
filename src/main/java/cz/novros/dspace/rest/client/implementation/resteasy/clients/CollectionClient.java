package cz.novros.dspace.rest.client.implementation.resteasy.clients;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;
import cz.novros.dspace.rest.client.interfaces.client.ICollectionDspaceClient;
import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Item;
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
 * Collection client implementation in RESTEasy.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public class CollectionClient extends DSpaceRESTEasyClient implements ICollectionDspaceClient {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public CollectionClient(Configuration configuration, RestEasyClient client) {
        super(configuration, client);
    }

    /**
     * Create collection in DSpace.
     *
     * @param parentCommunityId    Id of parent community of new collection.
     * @param collection Collection which will be created in community.
     * @return Returns new created collection in community.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating collection in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent community not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Collection create(Integer parentCommunityId, Collection collection) throws WebApplicationException {
        log.debug("Creating collection in community(id={}).", parentCommunityId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COMMUNITIES + "/" + parentCommunityId + COLLECTIONS);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(collection, MediaType.APPLICATION_JSON));
        try {
            collection = extractResult(Collection.class, response);
            log.info("Collection (handle={}, id={}) successfully created in community(id={}).", new Object[]{collection.getHandle(), collection.getID(), parentCommunityId});
            return collection;
        } catch (WebApplicationException ex) {
            log.error("Creating collection failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read collection from DSpace.
     *
     * @param collectionId Id of Collection which will be read.
     * @param expand Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *               logo and all. It must be separated by comma.
     * @return Returns collection if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading collection from DSpace.
     *                                 Problem can be: collection  not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Collection read(Integer collectionId, String expand) throws WebApplicationException {
        log.debug("Reading collection(id={}) from DSpace.", collectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId + addArguments(expand));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection collection = extractResult(Collection.class, response);
            log.info("Collection (handle={},id={}) successfully read from DSpace.", collection.getHandle(), collection.getID());
            return collection;
        } catch (WebApplicationException ex) {
            log.error("Reading collection(id={}) failed. Response code: {}.", collectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Update collection in DSpace.
     *
     * @param collectionId Id of Collection, which will be updated.
     * @param collection Updated collection. Does not matter which id will be in instance of Collection
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating collection in DSpace.
     *                                 Problem can be: collection not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void update(Integer collectionId, Collection collection) throws WebApplicationException {
        log.debug("Updating collection(id={}) in DSpace.", collectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(collection, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Collection (id={}) successfully updated in DSpace.", collection.getID());
        } catch (WebApplicationException ex) {
            log.error("Updating collection(id={}) failed. Response code: {}.", collectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }

    }

    /**
     * Delete collection from DSpace.
     *
     * @param collectionId Id of collection, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting collection in DSpace.
     *                                 Problem can be: collection not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void delete(Integer collectionId) throws WebApplicationException {
        log.debug("Deleting collection(id={}) in DSpace.", collectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + collectionId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Collection(id={}) successfully deleted in DSpace.", collectionId);
        } catch (WebApplicationException ex) {
            log.error("Deleting collection(id={}) failed. Response code: {}.", collectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all collections.
     *
     * @param expand Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *               logo and all. It must be separated by comma.
     * @param limit Limit of collections in List.
     * @param offset Offset for List of collection in DSpace database.
     * @return It returns filled or empty List of collections.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading collections from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Collection> readAll(String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading collections from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection[] collections = extractResult(Collection[].class, response);
            log.info("Collections were successfully read from DSpace. (count={})", collections.length);
            return Arrays.asList(collections);
        } catch (WebApplicationException ex) {
            log.error("Reading collections failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Look for collection in DSpace by name.
     *
     * @param name Name of collection.
     * @return Returns first collection with that name.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with searching collection DSpace.
     *                                             Problem can be: collection not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Collection findCollectionByName(String name) throws WebApplicationException {
        log.debug("Searching for collection(name={}) from DSpace.", name);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/find-by-name");
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Collection collection = extractResult(Collection.class, response);
            log.info("Collection (handle={},id={}) successfully found in DSpace.", collection.getHandle(), collection.getID());
            return collection;
        } catch (WebApplicationException ex) {
            log.error("Searching for collection(name={}) failed. Response code: {}.", name, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read items from collection.
     *
     * @param parentCollectionId Parent collection id.
     * @param expand             Expand fields for item, which can be: metadata, parentCollection, parentCollectionList,
     *                           parentCommunityList, bitstreams and all. It must be separated by comma.
     * @param limit              Limit of items from parent collection.
     * @param offset             Offset of List of items in parent collection list.
     * @return It returns List of items from parent collection.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading items from collection or problem
     *                                             with finding parent collection. Problem can be: parent collection not found, server problem, access denied,
     *                                             bad URL and so on.
     */
    @Override
    public List<Item> readCollectionItems(Integer parentCollectionId, String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading items from collection(id={}).", parentCollectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + parentCollectionId + ITEMS + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Item[] items = extractResult(Item[].class, response);
            log.info("Items were successfully read from collection(id={}). (count={})", parentCollectionId, items.length);
            return Arrays.asList(items);
        } catch (WebApplicationException ex) {
            log.error("Reading items from collection(id={}) failed. Response code: {}.", parentCollectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete item from collection.
     *
     * @param parentCollectionId Parent collection id.
     * @param itemId             Id of item from parent collection.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting item from collection.
     *                                             Problem can be: parent collection not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void deleteCollectionItem(Integer parentCollectionId, Integer itemId) throws WebApplicationException {
        log.debug("Deleting item(id={}) in collection(id={}).", itemId, parentCollectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + parentCollectionId + ITEMS + "/" + itemId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Item(id={}) in collection(id={}) was successfully deleted in DSpace.", itemId, parentCollectionId);
        } catch (WebApplicationException ex) {
            log.error("Deleting item(id={}) in collection(id={}) failed. Response code: {}.", new Object[]{itemId, parentCollectionId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }
}
