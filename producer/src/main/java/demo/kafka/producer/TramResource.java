package demo.kafka.producer;

import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import demo.kafka.producer.model.Tram;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecordMetadata;


@ApplicationScoped
@Path("/newJourney")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TramResource {
	
	private static final Logger LOG = Logger.getLogger(TramResource.class);
	
	@Inject @Channel("tram-new-journey") 
	Emitter<Tram> priceEmitter;
	
	
	@Inject
	TramValueGenerator valueGenerator;
	
	@POST
	public Response newJourney(Tram data) {
		data.setTime(Instant.now());
		priceEmitter.send(data);
		LOG.infof("Tram %s journey started on route %s", data.getId(), data.getRouteId());
		return Response.ok().build();
	}
}
