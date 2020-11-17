package demo.kafka.producer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import demo.kafka.producer.model.Location;
import demo.kafka.producer.model.Tram;
import demo.kafka.route.util.LocationUtil;
import demo.kafka.route.util.RouteUtil;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;

@ApplicationScoped
public class TramValueGenerator {

	private static final Logger LOG = Logger.getLogger(TramValueGenerator.class);

	Random random = new Random();

	Jsonb jsonb = JsonbBuilder.create();
	
	Map<Integer, Tram> trams = new HashMap<Integer, Tram>() {{
	    put(1, new Tram(1, "Tram 11", 1, Instant.now()));
	}};
	
	Map<Integer, Location> trainLocationMap = new HashMap<>();
	
	@Incoming("tram-new-journey")
	@Outgoing("trams")
	public Flowable<KafkaRecord<Integer, Tram>> trams(Message<Tram> tramIdMsg) {
		
		Tram tram = tramIdMsg.getPayload();
		List<KafkaRecord<Integer, Tram>> tramRecords = new ArrayList<KafkaRecord<Integer,Tram>>();
		tramRecords.add(KafkaRecord.of(tram.getId(), tram));
		trainLocationMap.remove(tram.getId());
		trams.put(tram.getId(), tram);
		
		return Flowable.fromIterable(tramRecords);
	};
	
	@Outgoing("tram-location")
	public Flowable<KafkaRecord<Integer, String>> generateGPS() {

		return Flowable.interval(1, TimeUnit.SECONDS).onBackpressureDrop().map(tick -> {
			// Pick random tram from list
			Tram tram = trams.get(random.nextInt(trams.keySet().size()) + 1);

			// Generate random speed Value
			double speed = generateRandomSpeed();

			Instant now = Instant.now();

			// Calculate new location based on speed and update map
			Location location = trainLocationMap.get(tram.getId());

			if (location != null) {
				Duration timeDifference = Duration.between(location.getTime(), now);
				double distance = speed * timeDifference.getSeconds() / 3600 * 1000;
				location.setLatitude(LocationUtil.calculateLatitude(location.getLatitude(), distance));
				location.setTime(now);
			} else {
				location = new Location(now, RouteUtil.getRoute(tram.getRouteId()).get(0).getLatidue(),
						RouteUtil.getRoute(tram.getRouteId()).get(0).getLongitude());
				trainLocationMap.put(tram.getId(), location);
			}

			// Log current train location
			LOG.debugf("Train: %s sending location: %s", tram.getId(), jsonb.toJson(location));

			// Create Kafka Record - key = train id, record = time;longitude;latitude
			return KafkaRecord.of(tram.getId(), now + ";" + location.getLongitude() + ";" + location.getLatitude());
		});
	}

	/**
	 * Method generates random speed value
	 * @return
	 */
	private double generateRandomSpeed() {
		double speed = BigDecimal.valueOf(random.nextGaussian() * 20 + 30).setScale(1, RoundingMode.HALF_UP)
				.doubleValue();
		return speed < 0 ? 0 : speed;
	}
}
