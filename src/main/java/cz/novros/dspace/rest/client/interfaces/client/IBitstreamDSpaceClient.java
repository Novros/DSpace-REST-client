package cz.novros.dspace.rest.client.interfaces.client;

import cz.novros.dspace.rest.client.interfaces.IDSpaceClient;
import org.dspace.rest.common.Bitstream;
import org.dspace.rest.common.ResourcePolicy;

import javax.ws.rs.WebApplicationException;
import java.io.InputStream;
import java.util.List;

/**
 * Interface for all REST api implementations. It contains additional bitstream operations.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public interface IBitstreamDSpaceClient extends IDSpaceClient<Bitstream> {

    /**
     * Not used. Never use this method.
     * @param notUsed
     * @param notUsedBitstream
     * @return
     */
    @Deprecated
    public Bitstream create(Integer notUsed, Bitstream notUsedBitstream) throws WebApplicationException;

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
    public Bitstream create(Integer parentItemId, Bitstream bitstream, InputStream inputStream) throws WebApplicationException;

    /**
     * Read bitstream data.
     *
     * @param bitstreamId Id of bitstream.
     * @return Returns InputStream filled with data of bitstream.
     * @throws WebApplicationException Is throw when was problem with reading data of bitstream.
     * Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    public InputStream readData(Integer bitstreamId) throws WebApplicationException;

    /**
     * Update data of bitstream.
     *
     * @param bitstreamId Id of bitstream.
     * @param inputStream InputStream filled with new data, which will be replaced in bitstream.
     * @throws WebApplicationException Is throw when was problem with updating data of bitstream.
     * Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    public void updateData(Integer bitstreamId, InputStream inputStream) throws WebApplicationException;

    /**
     * Read all bitstream resource policies.
     *
     * @param bitstreamId Id of bitstream.
     * @return It returns List of resource policies of bitstream.
     * @throws WebApplicationException Is throw when was problem with reading resource policies of bitstream.
     * Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    public List<ResourcePolicy> readResourcePolicies(Integer bitstreamId) throws WebApplicationException;

    /**
     * Add resource policy to bitstream.
     *
     * @param bitstreamId Id of bitstream
     * @param policy Resource policy which will be added to bitstream.
     * @throws WebApplicationException Is throw when was problem with adding resource policy of bitstream.
     * Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    public void addResourcePolicy(Integer bitstreamId, ResourcePolicy policy) throws WebApplicationException;

    /**
     * Delete resource policy in bitstream.
     *
     * @param bitstreamId Id of bitstream.
     * @param resourcePolicyId Id of resource policy which will be deleted.
     * @throws WebApplicationException Is throw when was problem with deleting resource policy of bitstream.
     * Problem can be: bitstream not found, server problem, access denied, bad URL and so on.
     */
    public void deleteResourcePolicy(Integer bitstreamId, Integer resourcePolicyId) throws WebApplicationException;


}
