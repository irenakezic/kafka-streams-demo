package demo.kafka.aggregator;

import java.time.Instant;

import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.jboss.logging.Logger;

import demo.kafka.aggregator.model.Tram;
import demo.kafka.aggregator.model.TramDataAggregated;
import demo.kafka.aggregator.model.TramDataJoined;
import io.quarkus.kafka.client.serialization.JsonbSerde;

/**
 * 
 * Tram topology class implements kafka streams logic
 *
 */
public class TramTopology {
	
	private static final Logger LOGGER = Logger.getLogger(TramTopology.class);

    static final String TRAM_STORE = "tram-store";

    //Tram journey start information topic
    static final String TRAM_TOPIC = "trams";
   
    //Tram geographic location information topic
    static final String TRAM_LOCATION_TOPIC = "tram-location";
    
    //Tram aggregated information topic
    static final String TRAM_AGGREGATED_TOPIC = "tram-aggregated";
    
    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        JsonbSerde<Tram> tramSerde = new JsonbSerde<>(Tram.class);
        JsonbSerde<TramDataAggregated> aggregationSerde = new JsonbSerde<>(TramDataAggregated.class);

        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(TRAM_STORE);

        GlobalKTable<Integer, Tram> trams = builder.globalTable( 
                TRAM_TOPIC,
                Consumed.with(Serdes.Integer(), tramSerde));

        builder.stream(                                                       
                        TRAM_LOCATION_TOPIC,
                        Consumed.with(Serdes.Integer(), Serdes.String())
                )
                .join(                                                        
                        trams,
                        (tramId, position) -> tramId,
                        (position, tram) -> {
                            String[] parts = position.split(";");
                            return new TramDataJoined(tram.getId(), tram.getName(), tram.getRouteId(), tram.getTime(),
                                    Instant.parse(parts[0]), Double.valueOf(parts[1]), Double.valueOf(parts[2]));
                        }
                )
                .groupByKey()                                                 
                .aggregate(                                                   
                        TramDataAggregated::new,
                        (tramId, newValue, aggValue) -> aggValue.updateFrom(newValue),
                        Materialized.<Integer, TramDataAggregated> as(storeSupplier)
                            .withKeySerde(Serdes.Integer())
                            .withValueSerde(aggregationSerde)
                )
                .toStream()
                .to(                                                          
                		TRAM_AGGREGATED_TOPIC,
                        Produced.with(Serdes.Integer(), aggregationSerde)
                );

        Topology topology = builder.build();
        LOGGER.info(topology.describe());
        return topology;
    }
}
