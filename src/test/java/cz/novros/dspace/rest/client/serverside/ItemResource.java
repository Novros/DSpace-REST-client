package cz.novros.dspace.rest.client.serverside;

import javax.ws.rs.Path;

/**
 * Created by Rostislav on 8. 5. 2015.
 */
@Path("/items")
public class ItemResource {
    @Path("../nothing")
    public String test() {
        return "Nothing";
    }
}
