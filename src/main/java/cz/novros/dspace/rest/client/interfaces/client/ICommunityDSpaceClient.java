package cz.novros.dspace.rest.client.interfaces.client;

import cz.novros.dspace.rest.client.interfaces.IDSpaceClient;
import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Community;

import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Interface for all REST api implementations. It contains additional community operations.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public interface ICommunityDSpaceClient extends IDSpaceClient<Community> {

    /**
     * Read all top communities in DSpace.
     *
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @param limit Limit of communities in List.
     * @param offset Offset of List of communities in DSpace database.
     * @return It returns List of all top communities in DSpace.
     * @throws WebApplicationException Is throw when was problem with reading communities from DSpace.
     * Problem can be: server problem, access denied, bad URL and so on.
     */
    public List<Community> readTop(String expand, Integer limit, Integer offset) throws WebApplicationException;

    /**
     * Creates subcommunity in community.
     *
     * @param parentCommunityId Parent community Id, in which will be community created.
     * @param subcommunity Community, which will be created.
     * @return It returns new created subcommunity.
     * @throws WebApplicationException Is throw when was problem with creating subcommunity in community or problem with
     * finding community in DSpace. Problem can be: Parent community not found, server problem, access denied, bad URL
     * and so on.
     */
    public Community createSubcommunity(Integer parentCommunityId, Community subcommunity) throws WebApplicationException;

    /**
     * Read all subcommunities from parent community.
     *
     * @param parentCommunityId Parent community id.
     * @param expand Expand fields for community, which can be: parentCommunity, subCommunities, collections,
     *               logo and all. It must be separated by comma.
     * @param limit Limit of communities in List.
     * @param offset Offset of List of communities in parent community list.
     * @return It returns List of subcommunities.
     * @throws WebApplicationException Is throw when was problem with reading subcommunities from community or problem
     * with finding parent community. Problem can be: parent community not found, server problem, access denied,
     * bad URL and so on.
     */
    public List<Community> readSubcommunities(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws WebApplicationException;

    /**
     * Delete subcommunity from parent community.
     *
     * @param parentCommunityId Parent community Id.
     * @param subcommunityId Subcommunity Id of parent community.
     * @throws WebApplicationException Is throw when was problem with deleting subcommunity from community.
     * Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     */
    public void deleteSubcommunity(Integer parentCommunityId, Integer subcommunityId) throws WebApplicationException;

    /**
     * Read all subcollections from parent community.
     *
     * @param parentCommunityId Parent community Id
     * @param expand Expand fields for collection, which can be: parentCommunityList, parentCommunity, items, license
     *               logo and all. It must be separated by comma.
     * @param limit Limit of subcollections in List.
     * @param offset Offset of List of collections in parent community.
     * @return It returns List of subcollections from parent community.
     * @throws WebApplicationException Is throw when was problem with reading subcollections from community.
     * Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     */
    public List<Collection> readSubcollections(Integer parentCommunityId, String expand, Integer limit, Integer offset) throws WebApplicationException;

    /**
     * Delete subcollection from parent community.
     *
     * @param parentCommunityId Parent community id.
     * @param subcollectionId Subcollection Id of parent community.
     * @throws WebApplicationException Is throw when was problem with deleting subcollection from community.
     * Problem can be: parent community not found, server problem, access denied, bad URL and so on.
     */
    public void deleteSubcollection(Integer parentCommunityId, Integer subcollectionId) throws WebApplicationException;
}
