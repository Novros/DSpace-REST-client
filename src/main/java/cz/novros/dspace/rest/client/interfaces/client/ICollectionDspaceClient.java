package cz.novros.dspace.rest.client.interfaces.client;

import cz.novros.dspace.rest.client.interfaces.IDSpaceClient;
import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Item;

import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Interface for all REST api implementations. It contains additional collection operations.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public interface ICollectionDspaceClient extends IDSpaceClient<Collection> {

    /**
     * Look for collection in DSpace by name.
     *
     * @param name Name of collection.
     * @return Returns first collection with that name.
     * @throws WebApplicationException Is throw when was problem with searching collection DSpace.
     * Problem can be: collection not found, server problem, access denied, bad URL and so on.
     */
    public Collection findCollectionByName(String name) throws WebApplicationException;

    /**
     * Read items from collection.
     *
     * @param parentCollectionId Parent collection id.
     * @param expand Expand fields for item, which can be: metadata, parentCollection, parentCollectionList,
     *               parentCommunityList, bitstreams and all. It must be separated by comma.
     * @param limit Limit of items from parent collection.
     * @param offset Offset of List of items in parent collection list.
     * @return It returns List of items from parent collection.
     * @throws WebApplicationException Is throw when was problem with reading items from collection or problem
     * with finding parent collection. Problem can be: parent collection not found, server problem, access denied,
     * bad URL and so on.
     */
    public List<Item> readCollectionItems(Integer parentCollectionId, String expand, Integer limit, Integer offset) throws WebApplicationException;

    /**
     * Delete item from collection.
     *
     * @param parentCollectionId Parent collection id.
     * @param itemId Id of item from parent collection.
     * @throws WebApplicationException Is throw when was problem with deleting item from collection.
     * Problem can be: parent collection not found, server problem, access denied, bad URL and so on.
     */
    public void deleteCollectionItem(Integer parentCollectionId, Integer itemId) throws WebApplicationException;
}
