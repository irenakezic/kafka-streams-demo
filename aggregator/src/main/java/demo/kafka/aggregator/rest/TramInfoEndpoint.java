package demo.kafka.aggregator.rest;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;

@ApplicationScoped
@Path("/trams")
public class TramInfoEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(TramInfoEndpoint.class);

	@Inject
    InteractiveQueries interactiveQueries;
	
    @GET
    @Path("/data/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTramData(@PathParam("id") Integer id) {
        GetTramStoreDataResult result = interactiveQueries.getTramData(id);

        if (result.getResult().isPresent()) {  
        	LOGGER.infof("Result for id %s found in local store", id);
            return Response.ok(result.getResult().get()).build();
        }
        else if (result.getHost().isPresent()) {   
        	LOGGER.infof("Result for id %s not found in local store, will call remote host %s", id, result.getHost().get());
            return callOtherInstance(result.getHost().get(), result.getPort().getAsInt(),
                    id);
        }
        else {                                                    
            return Response.status(Status.NOT_FOUND.getStatusCode(),
                    "No data found for tram " + id).build();
        }
    }

    @GET
    @Path("/meta-data")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PipelineMetadata> getMetaData() {                 
        return interactiveQueries.getMetaData();
    }
    
    private Response callOtherInstance(String host, Integer port, Integer id) {
    	URI uri;
    	try {
        	 uri = URI.create("http://" + host + ":" + port);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    	TramInfoClient client = RestClientBuilder.newBuilder().baseUri(uri).build(TramInfoClient.class);
    	return client.getTramData(id);
    }
}