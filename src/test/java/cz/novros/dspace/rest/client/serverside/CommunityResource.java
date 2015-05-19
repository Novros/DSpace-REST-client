package cz.novros.dspace.rest.client.serverside;

import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Community;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by Rostislav on 8. 5. 2015.
 */
@Path("/communities")
public class CommunityResource {

    private Integer communityMapId = 0;
    private Integer collectionId = 0;
    private Map<Integer,Community> communityMap = new HashMap<>();

    @GET
    @Path("/test")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String test (){
        return "hello world";
    }

    @GET
    @Path("/{community_id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Community getCommunity(@PathParam("community_id") Integer communityId, @QueryParam("expand") String expand, @Context HttpHeaders headers) throws WebApplicationException {
        return findCommunity(communityId);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Community[] getCommunities(@QueryParam("expand") String expand, @QueryParam("limit") @DefaultValue("100") Integer limit,
                                      @QueryParam("offset") @DefaultValue("0") Integer offset, @Context HttpHeaders headers)
            throws WebApplicationException {
        //@TODO limit offset
        List<Community> communities = new ArrayList<>();
        for (Map.Entry<Integer, Community> entry : communityMap.entrySet()) {
            communities.add(entry.getValue());
            for(Community subcommunity : entry.getValue().getSubCommunities()) {
                communities.add(subcommunity);
            }
        }

        return communities.toArray(new Community[0]);
    }

    @GET
    @Path("/top-communities")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Community[] getTopCommunities(@QueryParam("expand") String expand,
                                         @QueryParam("limit") @DefaultValue("20") Integer limit, @QueryParam("offset") @DefaultValue("0") Integer offset,
                                         @Context HttpHeaders headers) throws WebApplicationException {
        //@TODO limit offset
        List<Community> communities = new ArrayList<>();
        for (Map.Entry<Integer, Community> entry : communityMap.entrySet()) {
            communities.add(entry.getValue());
        }

        return communities.toArray(new Community[0]);
    }

    @GET
    @Path("/{community_id}/collections")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Collection[] getCommunityCollections(@PathParam("community_id") Integer communityId,
                                                @QueryParam("expand") String expand, @QueryParam("limit") @DefaultValue("100") Integer limit,
                                                @QueryParam("offset") @DefaultValue("0") Integer offset, @Context HttpHeaders headers)
            throws WebApplicationException {
        //@TODO limit offset
        Community community = findCommunity(communityId);
        List<Collection> collections = new ArrayList<>();
        for (Collection collection : collections) {
            collections.add(collection);
        }
        return collections.toArray(new Collection[0]);
    }

    @GET
    @Path("/{community_id}/communities")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Community[] getCommunityCommunities(@PathParam("community_id") Integer communityId,
                                               @QueryParam("expand") String expand, @QueryParam("limit") @DefaultValue("20") Integer limit,
                                               @QueryParam("offset") @DefaultValue("0") Integer offset,
                                               @Context HttpHeaders headers) throws WebApplicationException {
        //@TODO limit offset
        Community community = findCommunity(communityId);
        List<Community> communities = new ArrayList<>();
        for(Community subcommunity : community.getSubCommunities()) {
            communities.add(subcommunity);
        }

        return communities.toArray(new Community[0]);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Community createCommunity(Community community, @Context HttpHeaders headers) throws WebApplicationException {
        community.setID(communityMapId++);
        communityMap.put(community.getID(), community);
        return community;
    }

    @POST
    @Path("/{community_id}/collections")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Collection addCommunityCollection(@PathParam("community_id") Integer communityId, Collection collection, @Context HttpHeaders headers)
            throws WebApplicationException {
        Community dspaceCommunity = findCommunity(communityId);
        List<Collection> subCollections = dspaceCommunity.getCollections();
        collection.setID(collectionId++);
        subCollections.add(collection);
        dspaceCommunity.setCollections(subCollections);
        return collection;
    }

    @POST
    @Path("/{community_id}/communities")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Community addCommunityCommunity(@PathParam("community_id") Integer communityId, Community community,
                                            @Context HttpHeaders headers) throws WebApplicationException {
        Community dspaceCommunity = findCommunity(communityId);
        List<Community> subCommunities = dspaceCommunity.getSubCommunities();
        community.setID(communityMapId++);
        subCommunities.add(community);
        dspaceCommunity.setSubCommunities(subCommunities);
        return community;
    }

    @PUT
    @Path("/{community_id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response updateCommunity(@PathParam("community_id") Integer communityId, Community community, @Context HttpHeaders headers)
            throws WebApplicationException {
        Community dspaceCommunity = findCommunity(communityId);
        communityMap.remove(dspaceCommunity);

        community.setID(dspaceCommunity.getID());
        communityMap.put(communityId,community);

        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{community_id}")
    public Response deleteCommunity(@PathParam("community_id") Integer communityId, @Context HttpHeaders headers) throws WebApplicationException  {
        Community community = findCommunity(communityId);
        communityMap.remove(communityId);
        return Response.status(Response.Status.OK).build();
    }


    @DELETE
    @Path("/{community_id}/collections/{collection_id}")
    public Response deleteCommunityCollection(@PathParam("community_id") Integer communityId, @PathParam("collection_id") Integer collectionId,
                                              @Context HttpHeaders headers) throws WebApplicationException {
        Community community = findCommunity(communityId);
        List<Collection> subcollections = community.getCollections();

        boolean deleted = false;
        for (Iterator<Collection> iterator = subcollections.iterator(); iterator.hasNext();) {
            Collection subcollection = iterator.next();
            if(subcollection.getID() == collectionId) {
                iterator.remove();
                deleted = true;
                break;
            }
        }
        community.setCollections(subcollections);
        if(deleted) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{community_id}/communities/{community_id2}")
    public Response deleteCommunityCommunity(@PathParam("community_id") Integer parentCommunityId, @PathParam("community_id2") Integer subcommunityId,
                                             @Context HttpHeaders headers) throws WebApplicationException {
        Community community = findCommunity(parentCommunityId);
        List<Community> subcommunities = community.getSubCommunities();

        boolean deleted = false;
        for (Iterator<Community> iterator = subcommunities.iterator(); iterator.hasNext();) {
            Community subcommunity = iterator.next();
            if(subcommunity.getID() == subcommunityId) {
                iterator.remove();
                deleted = true;
                break;
            }
        }
        community.setSubCommunities(subcommunities);
        if(deleted) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private Community findCommunity(int id) throws WebApplicationException {
        Community community = communityMap.get(id);
        if(community == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return community;
    }
}
