package com.ibm.space.bombs;

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
public class Bombs {
		@Context ServletContext context;
		
		@GET
		@Path("/position")
		@Produces("application/json")
		public Response position(@Context HttpServletRequest request) throws Exception {				
	    	JsonArrayBuilder arr = Json.createArrayBuilder();	    	
	    	if (getValues() != null)
	    	{
		    	for (int i = 0; i < getValues().getBombs().size(); i++) {
		    		JsonObjectBuilder objb = Json.createObjectBuilder();
		    		objb.add("type", "bomb");
		    		objb.add("x", getValues().getBombs().get(i).getX() );
		    		objb.add("y", getValues().getBombs().get(i).getY());
		    		objb.add("up", getValues().getBombs().get(i).isFromPlayer() );
		    		objb.add("id", getValues().getBombs().get(i).getId() );
		    		objb.add("destroyed", getValues().getBombs().get(i).isDestroyed());
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
				if (getValues().isHasFinished()) return;
				for (int i = 0; i < getValues().getBombs().size(); i++) {
					OneBomb b = getValues().getBombs().get(i);
					if (b.isDestroyed()) continue; // nothing to do with a finished bomb
					if (b.isFromPlayer()) { // bomb from luke
						if (b.getY() <= 0) {
							b.setDestroyed(true);
							continue;
						}
						b.setY(b.getY() - 1);
					}
					else { // bomb from dart
						if (b.getY() >= 20) {
							b.setDestroyed(true);
							continue;
						}
						b.setY(b.getY() + 1);
					}
				}				
			}

		}
		
		@GET
		@Path("/destroy/{id}")
		public void create(@PathParam("id") long id) throws Exception {
			for (int i = 0; i < getValues().getBombs().size(); i++) {
				if (getValues().getBombs().get(i).getId() == id) {
					getValues().getBombs().get(i).setDestroyed(true);
					return;
				}		
			}
		}
		
	    @GET
		@Path("/move/{key}")
		public void move(@PathParam("key") int key) throws Exception {	
	    	if (key == 48) resetValues();
	    }

		@GET
		@Path("/isFinished")
	    @Produces("application/json")
	    public String hasEnded() throws Exception {
	    	return "{\"finished\" : false}";
	    }
	    
		@GET
		@Path("/create/{x}/{y}/{fromPlayer}")
		public void create(@PathParam("x") String x, @PathParam("y") String y, @PathParam("fromPlayer") String up) throws Exception {
			int valuex = Integer.parseInt(x);
			int valuey = Integer.parseInt(y);
			boolean fromPlayer = Boolean.parseBoolean(up);
			OneBomb b = new OneBomb((int)(Math.random() * Integer.MAX_VALUE), valuex, valuey, fromPlayer); 
			if (getValues() != null)  getValues().getBombs().add(b);
		}
	    
		private BombsValues getValues()
		{
			if (context != null) {
				if (context.getAttribute(BombsValues.class.getSimpleName()) == null) {
					BombsValues values = new BombsValues();
					context.setAttribute(BombsValues.class.getSimpleName(), values);			
				}
				return (BombsValues) context.getAttribute(BombsValues.class.getSimpleName());
			}
			else {
				return null;
			}
		}
		
		private void resetValues()
		{
			BombsValues values = new BombsValues();
			context.setAttribute(BombsValues.class.getSimpleName(), values);
		}
}
