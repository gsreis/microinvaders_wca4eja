package com.ibm.space.player;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@Path("/")
public class Player {
	@Context ServletContext context;
 
    @GET
	@Path("/position")
    @Produces("application/json")
	public Response  position(@Context HttpServletRequest request) throws Exception {
    	JsonArrayBuilder arr = Json.createArrayBuilder();
    	if (getValues() != null)
    	{
        	JsonObjectBuilder object = Json.createObjectBuilder();
        	object.add("type", "player");
        	object.add("x", getValues().getX());
        	object.add("y", getValues().getY());
        	object.add("destroyed", getValues().isDestroyed());
        	object.add("id", 0);
        	object.add("ip", "http://" + request.getLocalAddr() + ":" + request.getLocalPort());
        	JsonObject obj = object.build();
        	arr.add(obj);    		
    	}
    	return Response.status(200).entity(arr.build()).build() ;
    	
    }
    
    @GET
	@Path("/run")
    public void run() throws Exception {
    }

    
    @GET
    @Path("/destroy/0")
    public void destroy() {
    	getValues().setDestroyed(true);
    }
    
    @GET
	@Path("/move/{key}")
	public void move(@PathParam("key") int key) throws Exception {
    	if (key == 48) resetValues();
    	if (key == 111)
    		if (getValues().getX() > 0) getValues().setX(getValues().getX()-1);
    	if (key== 112)
    		if (getValues().getX() < 19) getValues().setX(getValues().getX()+1);
	}   
 
    @GET
	@Path("/isFinished")
    @Produces("application/json")
    public Response hasEnded() throws Exception {
    	return Response.status(200).entity(Json.createObjectBuilder().add("finished", getValues().isDestroyed()).build()).build();
    }
   
	private PlayerValues getValues()
	{
		if (context != null) {
			if (context.getAttribute(PlayerValues.class.getSimpleName()) == null) {
				PlayerValues values = new PlayerValues();
				context.setAttribute(PlayerValues.class.getSimpleName(), values);			
			}
			return (PlayerValues) context.getAttribute(PlayerValues.class.getSimpleName());
		}
		else {
			return null;
		}
	}
	private void resetValues()
	{
		PlayerValues values = new PlayerValues();
		context.setAttribute(PlayerValues.class.getSimpleName(), values);
	}
	
}
