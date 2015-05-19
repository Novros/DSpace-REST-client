package cz.novros.dspace.rest.client.serverside;

import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Item;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Rostislav on 8. 5. 2015.
 */
public class CollectionResource {
    /*@GET
    @Path("/{collection_id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public org.dspace.rest.common.Collection getCollection(@PathParam("collection_id") Integer collectionId,
                                                           @QueryParam("expand") String expand, @QueryParam("limit") @DefaultValue("100") Integer limit,
                                                           @QueryParam("offset") @DefaultValue("0") Integer offset) throws WebApplicationException {

    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public org.dspace.rest.common.Collection[] getCollections(@QueryParam("expand") String expand,
                                                              @QueryParam("limit") @DefaultValue("100") Integer limit, @QueryParam("offset") @DefaultValue("0") Integer offset,
                                                              @QueryParam("userIP") String user_ip, @QueryParam("userAgent") String user_agent,
                                                              @QueryParam("xforwardedfor") String xforwardedfor, @Context HttpHeaders headers, @Context HttpServletRequest request)
            throws WebApplicationException
    {

    }

    @GET
    @Path("/{collection_id}/items")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public org.dspace.rest.common.Item[] getCollectionItems(@PathParam("collection_id") Integer collectionId,
                                                            @QueryParam("expand") String expand, @QueryParam("limit") @DefaultValue("100") Integer limit,
                                                            @QueryParam("offset") @DefaultValue("0") Integer offset, @QueryParam("userIP") String user_ip,
                                                            @QueryParam("userAgent") String user_agent, @QueryParam("xforwardedfor") String xforwardedfor,
                                                            @Context HttpHeaders headers, @Context HttpServletRequest request) throws WebApplicationException
    {

    }

    @POST
    @Path("/{collection_id}/items")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Item addCollectionItem(@PathParam("collection_id") Integer collectionId, Item item,
                                  @QueryParam("userIP") String user_ip, @QueryParam("userAgent") String user_agent,
                                  @QueryParam("xforwardedfor") String xforwardedfor, @Context HttpHeaders headers, @Context HttpServletRequest request)
            throws WebApplicationException
    {

    }

    @PUT
    @Path("/{collection_id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response updateCollection(@PathParam("collection_id") Integer collectionId,
                                     org.dspace.rest.common.Collection collection, @QueryParam("userIP") String user_ip,
                                     @QueryParam("userAgent") String user_agent, @QueryParam("xforwardedfor") String xforwardedfor,
                                     @Context HttpHeaders headers, @Context HttpServletRequest request) throws WebApplicationException
    {

    }

    @DELETE
    @Path("/{collection_id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response deleteCollection(@PathParam("collection_id") Integer collectionId, @QueryParam("userIP") String user_ip,
                                     @QueryParam("userAgent") String user_agent, @QueryParam("xforwardedfor") String xforwardedfor,
                                     @Context HttpHeaders headers, @Context HttpServletRequest request) throws WebApplicationException

    }

    @DELETE
    @Path("/{collection_id}/items/{item_id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response deleteCollectionItem(@PathParam("collection_id") Integer collectionId, @PathParam("item_id") Integer itemId,
                                         @QueryParam("userIP") String user_ip, @QueryParam("userAgent") String user_agent,
                                         @QueryParam("xforwardedfor") String xforwardedfor, @Context HttpHeaders headers, @Context HttpServletRequest request)
            throws WebApplicationException
    {

    }


    @POST
    @Path("/find-collection")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Collection findCollectionByName(String name, @Context HttpHeaders headers) throws WebApplicationException
    {

    }

    private Collection findCollection(int id) throws WebApplicationException {
    }*/
}
