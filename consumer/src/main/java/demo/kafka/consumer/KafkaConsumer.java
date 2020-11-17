package demo.kafka.consumer;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import demo.kafka.consumer.model.TramDataAggregated;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class KafkaConsumer {

	@Incoming("tram-aggregated")
	@Outgoing("tram-stats")
	@Broadcast
	public TramDataAggregated getTramInformation(JsonObject json) {
		return json.mapTo(TramDataAggregated.class);
	}
}
