package com.ibm.space.enemies;

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
public class Enemies {
	@Context ServletContext context;
	
	
    @GET
	@Path("/position")
    @Produces("application/json")
	public Response position(@Context HttpServletRequest request) throws Exception {	
    	
    	JsonArrayBuilder arr = Json.createArrayBuilder();
    	if (getValues() != null )
    	{
        	for (int i = 0; i < getValues().getTies().length; i++) {
        		JsonObjectBuilder objb = Json.createObjectBuilder();
        		objb.add("type", "enemy");
        		objb.add("id", getValues().getTies()[i].getId() );
        		objb.add("destroyed", getValues().getTies()[i].isDestroyed());
        		objb.add("x", getValues().getTies()[i].getX());
        		objb.add("y", getValues().getTies()[i].getY());
	    		objb.add("ip", "http://" + request.getLocalAddr() + ":" + request.getLocalPort());
        		JsonObject obj = objb.build();
        		arr.add(obj);
        	}    		
    	}
    	return Response.status(200).entity(arr.build()).build(); 	    
    }
    
    @GET
	@Path("/run")
    public void run() throws Exception {
    	if (getValues() != null)
    	{
        	if (getValues().isFinished()) return;
        	int x0 = getValues().getTies()[0].getX();
        	int y0 = getValues().getTies()[0].getY();
        	
        	if (x0 <= 9) incrementAllX();
        	else {
        		returnAllXtoBegin();
        		if (y0 <= 15)  incrementAllY();
        		else { // reach the end of game
        			destroyAllEnemies();
        		}
        	}
    		
    	}
    }

    @GET
    @Path("/destroy/{id}")
    public void destroy(@PathParam("id") int id) {
    	getValues().getTies()[id].setDestroyed(true);
    }
    
    @GET
	@Path("/move/{key}")
	public void move(@PathParam("key") int key) throws Exception {	
    	if (key == 48) resetValues();
    }
    
    private void destroyAllEnemies() {
		for (int i = 0; i < getValues().getTies().length; i++) {
			getValues().getTies()[i].setDestroyed(true);
		}	
	}

	@GET
	@Path("/isFinished")
    @Produces("application/json")
    public Response hasEnded() throws Exception {
    	return Response.status(200).entity(Json.createObjectBuilder().add("finished", getValues().isFinished()).build()).build();
    }
    
    private void incrementAllY() {
		for (int i = 0; i < getValues().getTies().length; i++) {
			getValues().getTies()[i].setY(getValues().getTies()[i].getY() + 1);
		}
	}

	private void returnAllXtoBegin() {
		for (int i = 0; i < getValues().getTies().length; i++) {
		getValues().getTies()[i].setX( getValues().getTies()[i].getId() % 10);
	}
}
    
	private void incrementAllX() {
		for (int i = 0; i < getValues().getTies().length; i++) {
			getValues().getTies()[i].setX( getValues().getTies()[i].getX() + 1);
		}		
    }
	
	private EnemiesValues getValues()
	{
		if (context != null) {
			if (context.getAttribute(EnemiesValues.class.getSimpleName()) == null) {
				EnemiesValues values = new EnemiesValues();
				context.setAttribute(EnemiesValues.class.getSimpleName(), values);			
			}
			return (EnemiesValues) context.getAttribute(EnemiesValues.class.getSimpleName());
		}
		else {
			return null;
		}
	}
	
	private void resetValues()
	{
		EnemiesValues values = new EnemiesValues();
		context.setAttribute(EnemiesValues.class.getSimpleName(), values);
	}
}
