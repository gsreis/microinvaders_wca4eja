package com.ibm.space.collision;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;


@Path("/")
public class Collision {
	
	public static String[] urls = { "http://" + System.getProperty("enemyip", "127.0.0.1:9081") + "/enemy-1.0/", 
									"http://" + System.getProperty("playerip" , "127.0.0.1:9081") + "/player-1.0/",
									"http://" + System.getProperty("bombip" , "127.0.0.1:9081") + "/bomb-1.0/", 
									"http://" + System.getProperty("collisionip" , "127.0.0.1:9081") + "/collision-1.0/"};

	@GET
	@Path("/position")
    @Produces("application/json")
	public String  position() throws Exception {
    	return "[]";
    }
	
	/**
	 * Compare all bombs with all Enemies and Player's position. 
	 * If at the same position, destroy bomb and Enemies or Player
	 * Compare Player with all Enemies Position. If same position destroy Player 
	 * @throws Exception
	 */
	@GET
	@Path("/run")
	public void run() throws Exception {
	    	JsonArrayBuilder allelements = Json.createArrayBuilder();
	    	// join ll elements toguether
	    	for (int i = 0; i < urls.length; i++) {
				String json = callRest(urls[i] + "position");
				JsonArray array = Json.createReader(new StringReader(json)).readArray();
				for (int j = 0; j < array.size(); j++) {
					allelements.add(array.get(j));
				}
	    	}
	    	// if two elements match same position
	    	JsonArray array = allelements.build();
	    	boolean sprites[] = new boolean[array.size()];
	    	for (int i = 0; i < sprites.length; i++) {
				sprites[i] = ((JsonObject)array.get(i)).getBoolean("destroyed");
			}
	    	for (int i = 0; i < array.size(); i++) {
				for (int j = i+1; j < array.size(); j++) {
					JsonObject obj1 = (JsonObject)array.get(i);
					JsonObject obj2 = (JsonObject)array.get(j);
					if (obj1.getInt("x") == obj2.getInt("x") && 
						obj1.getInt("y") == obj2.getInt("y") &&
						obj1.getInt("id") != obj2.getInt("id") &&
					   !obj1.getBoolean("destroyed") && 
					   !obj2.getBoolean("destroyed") &&
					   !sprites[i] && 
					   !sprites[j] )
					{
						sprites[i] = sprites[j] = true;
						String resturl1 = findRest(obj1.getString("type"));
						if (resturl1 != null) {
							callRest(resturl1 + "destroy/" + obj1.getInt("id"));
						}			
						String resturl2 = findRest(obj2.getString("type"));
						if (resturl2 != null) {
							callRest(resturl2 + "destroy/" + obj2.getInt("id"));
						}
					}
				}
			}	
	}	
	
	@GET
	@Path("/destroy/{id}")
	public void create(@PathParam("id") int id) throws Exception {
	}
	
	
    @GET
	@Path("/move/{key}")
	public void move(@PathParam("key") String key) throws Exception {	
    }

    @GET
	@Path("/isFinished")
    @Produces("application/json")
    public String hasEnded() throws Exception {
    	return "{\"finished\" : false}";
    }

    private String findRest(String string) {
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].indexOf(string) != -1)
				return urls[i];
		}
		return null;
	}

	private String  callRest(String urlin) {
			  Client client = ClientBuilder.newClient();
			  return client.target(urlin).request(MediaType.APPLICATION_JSON).get(String.class);
	}	
}
