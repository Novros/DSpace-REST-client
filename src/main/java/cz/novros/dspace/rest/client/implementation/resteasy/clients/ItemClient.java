package cz.novros.dspace.rest.client.implementation.resteasy.clients;

import cz.novros.dspace.rest.client.configuration.Configuration;
import cz.novros.dspace.rest.client.implementation.resteasy.DSpaceRESTEasyClient;
import cz.novros.dspace.rest.client.implementation.resteasy.resteasyclient.RestEasyClient;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;
import org.dspace.rest.common.*;
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
 * Item client implementation in RESTEasy.
 *
 * @author Rostislav Novak
 * @author Petr Karel
 * @version 1.0, $Revision: 1 $
 */
public class ItemClient extends DSpaceRESTEasyClient implements IItemDSpaceClient {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public ItemClient(Configuration configuration, RestEasyClient client) {
        super(configuration, client);
    }

    /**
     * Create item in DSpace.
     *
     * @param paretnCollectionId Id of parent collection of new Item.
     * @param item Item which will be created in collection.
     * @return Returns new created item in DSpace.
     * @throws javax.ws.rs.WebApplicationException Is thrown when was problem with creating item in DSpace.
     *                                 Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     *                                 thrown in this cases: Parent collection not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Item create(Integer paretnCollectionId, Item item) throws WebApplicationException {
        log.debug("Creating item in collection(id={}).", paretnCollectionId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + COLLECTIONS + "/" + paretnCollectionId + ITEMS);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(item, MediaType.APPLICATION_JSON));
        try {
            item = extractResult(Item.class, response);
            log.info("Item (handle={}, id={}) successfully created in collection(id={}).", new Object[]{item.getHandle(), item.getID(), paretnCollectionId});
            return item;
        } catch (WebApplicationException ex) {
            log.error("Creating item in collection(id={}) failed. Response code: {}.", paretnCollectionId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read item from DSpace.
     *
     * @param itemId Id of item which will be read.
     * @param expand Expand fields for item, which can be: metadata, parentCollection, parentCollectionList,
     *               parentCommunityList, bitstreams and all. It must be separated by comma.
     * @return Returns item if was found, otherwise it returns null.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading item from DSpace.
     *                                 Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public Item read(Integer itemId, String expand) throws WebApplicationException {
        log.debug("Reading item(id={}) from DSpace.", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + addArguments(expand));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Item item = extractResult(Item.class, response);
            log.info("Item (handle={},id={}) successfully read from DSpace.", item.getHandle(), item.getID());
            return item;
        } catch (WebApplicationException ex) {
            log.error("Reading item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Update item in collection.
     *
     * @param itemId Id of item, which will be updated.
     * @param item Updated item. Does not matter which id will be in instance of Item
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with updating item in DSpace.
     *                                 Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void update(Integer itemId, Item item) throws WebApplicationException {
        log.debug("Updating item(id={}) in DSpace.", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(item, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Item(id={}) successfully updated in DSpace.", itemId);
        } catch (WebApplicationException ex) {
            log.error("Updating item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete item from DSpace.
     *
     * @param itemId Id of item, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting item in DSpace.
     *                                 Problem can be: Item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void delete(Integer itemId) throws WebApplicationException {
        log.debug("Deleting item(id={}) in DSpace.", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Item(id={}) successfully deleted in DSpace.", itemId);
        } catch (WebApplicationException ex) {
            log.error("Deleting item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read all item.
     *
     * @param expand Expand fields for item, which can be: metadata, parentCollection, parentCollectionList,
     *               parentCommunityList, bitstreams and all. It must be separated by comma.
     * @param limit  Limit of items in List.
     * @param offset Offset of List of items in DSpace database.
     * @return It returns filled or empty List of items.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading items from DSpace.
     *                                 Problem can be: Server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Item> readAll(String expand, Integer limit, Integer offset) throws WebApplicationException {
        log.debug("Reading items from DSpace.");
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + addArguments(expand, limit, offset));
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Item[] items = extractResult(Item[].class, response);
            log.info("Items were successfully read from DSpace. (count={})", items.length);
            return Arrays.asList(items);
        } catch (WebApplicationException ex) {
            log.error("Reading items failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Find all items, which has passed metadata entry.
     *
     * @param entry MetadataEntry by which will be finding.
     * @return Returns List of items which has that metadata entry, otherwise empty list.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with searching item.
     *                                             Problem can be: server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Item> findItemsByMetadata(MetadataEntry entry) throws WebApplicationException {
        log.debug("Searching item by metadata entry from DSpace. Entry:(key={},value={},language={})",
                new Object[]{entry.getKey(), entry.getValue(), entry.getLanguage()});
        String token = login();
        Item[] items = null;

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/find-by-metadata-field");
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(entry, MediaType.APPLICATION_JSON));
        try {
            items = extractResult(Item[].class, response);
            log.info("Items were successfully searched from DSpace. (count={})", items.length);
        } catch (WebApplicationException ex) {
            log.error("Searching items failed. Response code: {}.", response.getStatus());
            throw ex;
        } finally {
            response.close();
        }

        if (items.length < 1) {
            log.info("Item not found (MetadataEntry={}/{}).", entry.getKey(), entry.getValue());
            return null;
        }

        return Arrays.asList(items);
    }

    /**
     * Read metadata from item.
     *
     * @param itemId Id of item.
     * @return It returns List of metadata.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading metadata from item.
     *                                             Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public List<MetadataEntry> readMetadata(Integer itemId) throws WebApplicationException {
        log.debug("Reading metadata from item(id={}).", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + METADATA);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            MetadataEntry[] metadata = extractResult(MetadataEntry[].class, response);
            log.info("Metadata from item(id={}) were successfully read from DSpace. (count={})", itemId, metadata.length);
            return Arrays.asList(metadata);
        } catch (WebApplicationException ex) {
            log.error("Reading metadata from item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Add List of metadata entries to item.
     *
     * @param itemId            Id of item.
     * @param metadataEntryList List of metadata, which will be added to item.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with adding metadata to item.
     *                                             Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void addMetadata(Integer itemId, List<MetadataEntry> metadataEntryList) throws WebApplicationException {
        log.debug("Adding metadata to item(id={}) in DSpace.", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + METADATA);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(metadataEntryList, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Metadata to item(id={}) successfully added in DSpace.", itemId);
        } catch (WebApplicationException ex) {
            log.error("Adding metadata to item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Update metadata in item. It delete metadata which corresponds to metadata in List and add them to Item.
     *
     * @param itemId            Id of item.
     * @param metadataEntryList List of metadata, which will be replaced.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with replacing metadata to item.
     *                                             Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void updateMetadata(Integer itemId, List<MetadataEntry> metadataEntryList) throws WebApplicationException {
        log.debug("Updating metadata in item(id={}) in DSpace.", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + METADATA);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).put(Entity.entity(metadataEntryList, MediaType.APPLICATION_JSON));
        try {
            handleErrorStatus(response);
            log.info("Metadata in item(id={}) successfully updated in DSpace.", itemId);
        } catch (WebApplicationException ex) {
            log.error("Updating metadata in item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete all metadata from item.
     *
     * @param itemId Id of metadata.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting metadata from item.
     *                                             Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void deleteMetadata(Integer itemId) throws WebApplicationException {
        log.debug("Deleting metadata in item(id={}) in DSpace.", itemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + itemId + METADATA);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Metadata in item(id={}) successfully deleted in DSpace.", itemId);
        } catch (WebApplicationException ex) {
            log.error("Deleting metadata in item(id={}) failed. Response code: {}.", itemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Read bitstreams from item.
     *
     * @param parentItemId Id of item from which will be bitstreams readed.
     * @param expand       Expand fields for bitstream, which can be: parent, policies and all. It must be separated by comma.
     * @return It returns List of bitstream from item.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with reading bitstreams from item.
     *                                             Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public List<Bitstream> readBitstreams(Integer parentItemId, String expand) throws WebApplicationException {
        log.debug("Reading bitstreams in item(id={}).", parentItemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + parentItemId + BITSTREAMS);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).get();
        try {
            Bitstream[] bitstreams = extractResult(Bitstream[].class, response);
            log.info("bitstreams from item(id={}) were successfully read from DSpace. (count={})", parentItemId, bitstreams.length);
            return Arrays.asList(bitstreams);
        } catch (WebApplicationException ex) {
            log.error("Reading bitstreams from item(id={}) failed. Response code: {}.", parentItemId, response.getStatus());
            throw ex;
        } finally {
            response.close();
        }
    }

    /**
     * Delete bitstream in item.
     *
     * @param parentItemId Id of item.
     * @param bitstreamId  Id of bitstream, which will be deleted.
     * @throws javax.ws.rs.WebApplicationException Is throw when was problem with deleting bitstream from item.
     *                                             Problem can be: item not found, bitstream not found, server problem, access denied, bad URL and so on.
     */
    @Override
    public void deleteBitstream(Integer parentItemId, Integer bitstreamId) throws WebApplicationException {
        log.debug("Deleting bitstream(id={}) in item(id={}) in DSpace.", bitstreamId, parentItemId);
        String token = login();

        ResteasyWebTarget target = client.target(ENDPOINT_URL + ITEMS + "/" + parentItemId + BITSTREAMS);
        Response response = target.request().header(headerToken, token).accept(MediaType.APPLICATION_JSON).delete();
        try {
            handleErrorStatus(response);
            log.info("Bitstream(id={}) in item(id={}) successfully deleted in DSpace.", parentItemId);
        } catch (WebApplicationException ex) {
            log.error("Deleting bitstream(id={}) in item(id={}) failed. Response code: {}.", new Object[]{bitstreamId, parentItemId, response.getStatus()});
            throw ex;
        } finally {
            response.close();
        }
    }
}
