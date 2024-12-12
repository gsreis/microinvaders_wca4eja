package com.ibm.space.space;

import java.io.StringReader;
import java.util.Random;


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
import javax.ws.rs.core.Response;

@Path("/")
public class Space {
	
	// this will be moved to a registry pattern
	public static String[] urls = { "http://" + System.getProperty("enemyip", "127.0.0.1:9081") + "/enemy-1.0/", 
            						"http://" + System.getProperty("playerip" , "127.0.0.1:9081") + "/player-1.0/",
            						"http://" + System.getProperty("bombip" , "127.0.0.1:9081") + "/bomb-1.0/", 
            						"http://" + System.getProperty("collisionip" , "127.0.0.1:9081") + "/collision-1.0/"};

    @GET
	@Path("/position")
    @Produces("application/json")
	public Response  position() throws Exception {
    	JsonArrayBuilder allelements = Json.createArrayBuilder();
    	for (int i = 0; i < urls.length; i++) {
			String json = callRest(urls[i] + "position");
			JsonArray array = Json.createReader(new StringReader(json)).readArray();
			for (int j = 0; j < array.size(); j++) {
				allelements.add(array.get(j));
			}
		}
    	return Response.status(200).entity(allelements.build()).header("Access-Control-Allow-Headers", "Content-Type").
                header("Access-Control-Allow-Methods", "GET, POST, OPTIONS").
                header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
	@Path("/run")
    public void run() throws Exception {  
    	
    	// redirect commands to other Microservices
    	for (int i = 0; i < urls.length; i++)
        	callRest(urls[i] + "run");
    	
    	// create bombs from enemies
		String json = callRest(getURLFromName("enemy") + "position");
		JsonArray array = Json.createReader(new StringReader(json)).readArray();
		int biggerX = 0;
		int lowerX = 0;
		int biggerY = 0;
		for (int j = 0; j < array.size(); j++) {
			int objectX = ((JsonObject)array.get(j)).getInt("x");
			int objectY = ((JsonObject)array.get(j)).getInt("y");
			if (objectX > biggerX) biggerX = objectX;
			if (objectX < lowerX) lowerX = objectX;
			if (objectY > biggerY) biggerY = objectY;	
		}
		// 30% of chance to generate a bomb from enemy
		//Calendar c = Calendar.getInstance();
		Random r = new Random();
		if (r.nextInt(100) < 30) {
			int posx = lowerX + r.nextInt(biggerX - lowerX);
			int posy = biggerY+1;
			callRest(getURLFromName("bomb") + "create/" + posx + "/" + posy + "/false" );
		}
    }

	@GET
	@Path("/destroy/{id}")
	public void create(@PathParam("id") int id) throws Exception {
    	for (int i = 0; i < urls.length; i++)
        	callRest(urls[i] + "destroy/" + id);
	}

    @GET
	@Path("/move/{key}")
	public void move(@PathParam("key") String key) throws Exception {
       	for (int i = 0; i < urls.length; i++)
        	callRest(urls[i] + "move/" + key);   	
    }
    
    @GET
	@Path("/create/{x}/{y}/{fromPlayer}")
	public void create(@PathParam("x") String x, @PathParam("y") String y, @PathParam("fromPlayer") String up) throws Exception {
		 callRest(getURLFromName("bomb") + "create/" + x + "/" + y + "/" + up);
    }   
    
    @GET
	@Path("/isFinished")
    @Produces("application/json")
	public Response isFinished() throws Exception {	
    	boolean finished = false;
    	for (int i = 0; i < urls.length; i++) {
        	String json = callRest(urls[i] + "isFinished"); 
        	JsonObject jobj = Json.createReader(new StringReader(json)).readObject();
        	if (jobj != null && jobj.getBoolean("finished")) {
        		finished = true;
        	}
    	}
    	return Response.status(200).entity(Json.createObjectBuilder().add("finished", finished).build()).
    			header("Access-Control-Allow-Headers", "Content-Type").
                header("Access-Control-Allow-Methods", "GET, POST, OPTIONS").
                header("Access-Control-Allow-Origin", "*").build();
    }   
    
	private String  callRest(String urlin) {
		try {
		  Client client = ClientBuilder.newClient();
		  return client.target(urlin).request(MediaType.APPLICATION_JSON).get(String.class);
		}catch(Exception e) {}
		return "[]";
	}
	
	private String getURLFromName(String name) {
		for (int j = 0; j < urls.length; j++) {
			if (urls[j].indexOf(name) != -1)
				return urls[j];
		}
		return null;
	}
    
}

