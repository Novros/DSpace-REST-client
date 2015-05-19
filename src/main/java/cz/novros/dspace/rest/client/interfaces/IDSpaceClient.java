package cz.novros.dspace.rest.client.interfaces;

import org.dspace.rest.common.DSpaceObject;

import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Interface for all REST api implementations. It contains basic CRUD operations over DSpaceObjects.
 *
 * @param <T> It must be child of DSpaceObject, so, it can be Community, Collection, Item or Bitstream.
 *
 * @author Rostislav Novak
 * @version 1.0, $Revision: 1 $
 */
public interface IDSpaceClient <T extends DSpaceObject> {
    /**
     * Create specific DSpaceObject in DSpace.
     *
     * @param parentId Id of parent DSpaceObject of new DSpaceObject.
     * @param dSpaceObject Object which will be created in DSpace.
     * @return Returns new created DSpaceObject in DSpace.
     * @throws WebApplicationException Is thrown when was problem with creating DSpaceObject in DSpace.
     * Exception can any child class of WebApplicationException, which corresponds to HTTP code. It is
     * thrown in this cases: Parent DSpaceObject not found, server problem, access denied, bad URL and so on.
     */
    public T create(Integer parentId, T dSpaceObject) throws WebApplicationException;

    /**
     * Read DSpaceObject from DSpace.
     *
     * @param objectId Id of DSpaceObject which will be read.
     * @param expand Expand fields for specific DSpaceObject.
     * @return Returns specific DSpaceObject if was found, otherwise it returns null.
     * @throws WebApplicationException Is throw when was problem with reading DSpaceObject from DSpace.
     * Problem can be: DSpaceObject not found, server problem, access denied, bad URL and so on.
     */
    public T read(Integer objectId, String expand) throws WebApplicationException;

    /**
     * Update DSpaceObject in DSpace.
     *
     * @param objectId Id of DSpaceObject, which will be updated.
     * @param dSpaceObject Specific updated DSpaceObject. Does not matter which id will be in instance of DSpaceObject
     * @throws WebApplicationException Is throw when was problem with updating DSpaceObject in DSpace.
     * Problem can be: DSpaceObject not found, server problem, access denied, bad URL and so on.
     */
    public void update(Integer objectId, T dSpaceObject) throws WebApplicationException;

    /**
     * Delete DSpaceObject from Dspace.
     *
     * @param objectId Id of DSpaceObject, which will be deleted.
     * @throws WebApplicationException Is throw when was problem with deleting DSpaceObject in DSpace.
     * Problem can be: DSpaceObject not found, server problem, access denied, bad URL and so on.
     */
    public void delete(Integer objectId) throws WebApplicationException;

    /**
     * Read all DSpaceObjects of specific type.
     *
     * @param expand Expand fields for specific DSpaceObject.
     * @param limit Limit of DSpaceObjects in List.
     * @param offset Offset of List of specific DSpaceObjects in DSpace database.
     * @return It returns filled or empty List of specific DSpaceObjects.
     * @throws WebApplicationException Is throw when was problem with reading DSpaceObjects from DSpace.
     * Problem can be: Server problem, access denied, bad URL and so on.
     */
    public List<T> readAll(String expand, Integer limit, Integer offset) throws WebApplicationException;
}
