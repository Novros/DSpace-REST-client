package cz.novros.dspace.rest.client.interfaces.client;

import cz.novros.dspace.rest.client.interfaces.IDSpaceClient;
import org.dspace.rest.common.Bitstream;
import org.dspace.rest.common.Item;
import org.dspace.rest.common.MetadataEntry;

import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Interface for all REST api implementations. It contains additional items operations.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public interface IItemDSpaceClient extends IDSpaceClient<Item> {


    /**
     * Find all items, which has passed metadata entry.
     *
     * @param entry MetadataEntry by which will be finding.
     * @return Returns List of items which has that metadata entry, otherwise empty list.
     * @throws WebApplicationException Is throw when was problem with searching item.
     * Problem can be: server problem, access denied, bad URL and so on.
     */
    public List<Item> findItemsByMetadata(MetadataEntry entry) throws WebApplicationException;

    /**
     * Read metadata from item.
     *
     * @param itemId Id of item.
     * @return It returns List of metadata.
     * @throws WebApplicationException Is throw when was problem with reading metadata from item.
     * Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    public List<MetadataEntry> readMetadata(Integer itemId) throws WebApplicationException;

    /**
     * Read bitstream from item.
     *
     * @param parentItemId Id of item from which will be bitstreams readed.
     * @param expand Expand fields for bitstream, which can be: parent, policies and all. It must be separated by comma.
     * @return It returns List of bitstream from item.
     * @throws WebApplicationException Is throw when was problem with reading bitstreams from item.
     * Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    public List<Bitstream> readBitstreams(Integer parentItemId, String expand) throws WebApplicationException;

    /**
     * Add List of metadata entries to item.
     *
     * @param itemId Id of item.
     * @param metadataEntryList List of metadata, which will be added to item.
     * @throws WebApplicationException Is throw when was problem with adding metadata to item.
     * Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    public void addMetadata(Integer itemId, List<MetadataEntry> metadataEntryList) throws WebApplicationException;

    /**
     * Update metadata in item. It delete metadata which corresponds to metadata in List and add them to Item.
     *
     * @param itemId Id of item.
     * @param metadataEntryList List of metadata, which will be replaced.
     * @throws WebApplicationException Is throw when was problem with replacing metadata to item.
     * Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    public void updateMetadata(Integer itemId, List<MetadataEntry> metadataEntryList) throws WebApplicationException;

    /**
     * Delete all metadata from item.
     *
     * @param itemId Id of metadata.
     * @throws WebApplicationException Is throw when was problem with deleting metadata from item.
     * Problem can be: item not found, server problem, access denied, bad URL and so on.
     */
    public void deleteMetadata(Integer itemId) throws WebApplicationException;

    /**
     * Delete bitstream in item.
     *
     * @param parentItemId Id of item.
     * @param bitstreamId Id of bitstream, which will be deleted.
     * @throws WebApplicationException Is throw when was problem with deleting bitstream from item.
     * Problem can be: item not found, bitstream not found, server problem, access denied, bad URL and so on.
     */
    public void deleteBitstream(Integer parentItemId, Integer bitstreamId) throws WebApplicationException;

}
