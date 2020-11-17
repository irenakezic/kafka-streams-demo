package demo.kafka.demo.metrics;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.kafka.common.Metric;
import org.apache.kafka.streams.KafkaStreams;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class MetricsHandler {

	@Inject
	MetricRegistry metricRegistry;
	
	@Inject
	KafkaStreams kafkaStreams;
	
	private static final Logger LOGGER = Logger.getLogger(MetricsHandler.class);
	
	private ExecutorService executorService;
	
	void onStart(@Observes StartupEvent ev) {
		executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> {
			try {
				while(true) {
					if(kafkaStreams.state().isRunningOrRebalancing()) {
						break;
					}
					Thread.sleep(1000);
				}
			} catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			exportMetrics();
		});
	}
	 
	void onStop(@Observes ShutdownEvent ev) {
		executorService.shutdown();
	}
	
	public void exportMetrics() {
		Set<String> processed = new HashSet<>();
		
		//Loop through Kafka Streams metrics and register in Metric Registry
		for(Metric metric : kafkaStreams.metrics().values()) {
			String name = metric.metricName().group() + ":" + metric.metricName().name();
			
			if(processed.contains(name)) {
				continue;
			}
			
			if(name.contentEquals("stream-metrics:topology-description") ||
					name.contentEquals("stream-metrics:version") ||
					name.contentEquals("stream-metrics:state") ||
					name.contentEquals("stream-metrics:application-id") ||
					name.contentEquals("stream-metrics:commit-id") ||
					name.contentEquals("app-info:version") ||
					name.contentEquals("app-info:start-time-ms") ||
					name.contentEquals("app-info:commit-id") ||
					name.contentEquals("consumer-fetch-manager-metrics:preferred-read-replica")) {
				continue;
			} else if( name.endsWith("count") || name.endsWith("total")) {
				registerCounter(metric, name);
			} else {
				registerGauge(metric, name);
			}

			processed.add(name);
		}
	}
	
	private void registerGauge(Metric metric, String name) {
		Metadata metadata = Metadata.builder()
				.withName(name)
				.withType(MetricType.GAUGE)
				.withDescription(metric.metricName().description())
				.build();
		
		metricRegistry.register(metadata, new Gauge<Double>() {
			
			@Override
			public Double getValue() {
				return (Double) metric.metricValue();
			}
		});
	}
	
	private void registerCounter(Metric metric, String name) {
		Metadata metadata = Metadata.builder()
				.withName(name)
				.withType(MetricType.COUNTER)
				.withDescription(metric.metricName().description())
				.build();
		
		metricRegistry.register(metadata, new Counter() {
			
			@Override
			public void inc(long n) {
				
			}
			
			@Override
			public void inc() {
				
			}
			
			@Override
			public long getCount() {
				return ((Double)metric.metricValue()).longValue();
			}
		});
	}
}
