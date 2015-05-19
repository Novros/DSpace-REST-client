package cz.novros.dspace.rest.client.serverside;

import org.dspace.rest.common.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Rostislav on 11. 5. 2015.
 */
@Path("/")
public class RestIndex {

    String token = "nothing";

    @POST
    @Path("/login")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response login(User user) {
        return Response.ok(token, "text/plain").build();
    }
}
