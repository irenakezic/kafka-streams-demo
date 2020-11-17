package demo.kafka.aggregator.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import demo.kafka.aggregator.model.TramDataAggregated;
import demo.kafka.aggregator.model.TramStoreData;

@ApplicationScoped
public class InteractiveQueries {
	
	private static final Logger LOG = Logger.getLogger(InteractiveQueries.class);
	
	static final String TRAM_STORE = "tram-store";

    @Inject
    KafkaStreams streams;
    
    @ConfigProperty(name = "myhost")
    String host;

    private ReadOnlyKeyValueStore<Integer, TramDataAggregated> getTramInfoStore() {
        while (true) {
            try {
                return streams.store(TRAM_STORE, QueryableStoreTypes.keyValueStore());
            } catch (InvalidStateStoreException e) {
                // ignore, store not ready yet
            }
        }
    }
    
    public GetTramStoreDataResult getTramData(Integer id) {
        StreamsMetadata metadata = streams.metadataForKey(                  
                TRAM_STORE,
                id,
                Serdes.Integer().serializer()
        );

        if (metadata == null || metadata == StreamsMetadata.NOT_AVAILABLE) {
            LOG.warnf("Found no metadata for key {}", id);
            return GetTramStoreDataResult.notFound();
        }
        else if (metadata.host().contentEquals(host)) {   
            LOG.infof("Found data for key %s locally", id);
            TramDataAggregated result = getTramInfoStore().get(id);

            if (result != null) {
                return GetTramStoreDataResult.found(TramStoreData.from(result));
            }
            else {
                return GetTramStoreDataResult.notFound();
            }
        }
        else {          
            LOG.infof("Found data for key %s on remote host %s:%s", id.toString(), metadata.host(), metadata.port()
            );
            return GetTramStoreDataResult.foundRemotely(metadata.host(), metadata.port());
        }
    }

    public List<PipelineMetadata> getMetaData() {                           
        return streams.allMetadataForStore(TRAM_STORE)
                .stream()
                .map(m -> new PipelineMetadata(
                        m.hostInfo().host(),
                        m.topicPartitions()
                            .stream()
                            .map(TopicPartition::toString)
                            .collect(Collectors.toSet()))
                )
                .collect(Collectors.toList());
    }
}
