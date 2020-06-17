package com.sdx.platform.quartz.service.impl;

/*import static com.mongodb.client.model.Filters.eq;*/

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


import org.json.JSONObject;

/*import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;*/
import com.sdx.platform.config.Memory;
import com.sdx.platform.domain.ResponseDomainUtil;
import com.sdx.platform.domain.User;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@Path("/sdx/cds/users")
public class UserResource
{
    private static Map<String, User> DB = new HashMap<>();
     
    @GET
    @Produces("application/json")
    public String getAllUsers() {
    	
    	/*log.info("I'm hitting the right place");
		MongoCollection<Document> coll = Memory.getSdxBaseDB().getCollection("sdx-platform-user");
		FindIterable<Document> docs = coll.find();
		
		String output = StreamSupport.stream(coll.find().spliterator(), false)
        .map(Document::toJson)
        .collect(Collectors.joining(", ", "[", "]"));
		
		//output = "{ \"draw\": 1,\"recordsTotal\": 11,\"recordsFiltered\": 11,\"data\":"+output+"}";
        log.info("output "+output);
        return output;*/
    	return "";
    }
     
    @POST
    @Consumes("application/json")
    public Response createUser(User user) throws URISyntaxException
    {
        if(user.getFName() == null || user.getLName() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        //Mongo Insert
        return Response.status(201).build();
    }
 
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") String id) throws URISyntaxException {
        
       /* MongoCollection<Document> coll = Memory.getSdxBaseDB().getCollection("sdx-platform-user");
        Document document = coll.find(eq("_id", id)).first();
        if (document == null) {
        	return Response
	                .status(404)
	                .entity("error")
	                .build();
        } else {
            log.info("I Got you "+document);
            try {
				JSONObject outUser = ResponseDomainUtil.buildUserEditDetails(document);

				log.info("SINGLE user output ::::: "+outUser.toString());
				return Response
		                .status(200)
		                .entity(outUser.toString())
		                .build();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }*/
        return Response
                .status(404)
                .entity("error")
                .build();
        
    }
 
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateUser(@PathParam("id") int id, User user) throws URISyntaxException
    {
        User temp = DB.get(id);
        if(user == null) {
            return Response.status(404).build();
        }
        temp.setFName(user.getFName());
        temp.setLName(user.getLName());
        DB.put(temp.get_id(), temp);
        return Response.status(200).entity(temp).build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) throws URISyntaxException {
        User user = DB.get(id);
        if(user != null) {
            DB.remove(user.get_id());
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
   
}
