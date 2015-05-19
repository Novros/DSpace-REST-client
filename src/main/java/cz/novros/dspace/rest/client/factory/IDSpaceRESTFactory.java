package cz.novros.dspace.rest.client.factory;

import cz.novros.dspace.rest.client.interfaces.client.IBitstreamDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICollectionDspaceClient;
import cz.novros.dspace.rest.client.interfaces.client.ICommunityDSpaceClient;
import cz.novros.dspace.rest.client.interfaces.client.IItemDSpaceClient;

/**
 * Interface for factory for creating REST api clients.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public interface IDSpaceRESTFactory {

    /**
     * Create community client for REST api.
     *
     * @return Returns new instance of community client.
     */
    public ICommunityDSpaceClient createCommunityClient();

    /**
     * Create collection client for REST api.
     *
     * @return Returns new instance of collection client.
     */
    public ICollectionDspaceClient createCollectionClient();

    /**
     * Create item client for REST api.
     *
     * @return Returns new instance of item client.
     */
    public IItemDSpaceClient createItemClient();

    /**
     * Create bitstream client for REST api.
     *
     * @return Returns new instance of bitstream client.
     */
    public IBitstreamDSpaceClient createBitstreamClient();
}
