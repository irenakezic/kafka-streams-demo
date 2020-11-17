package demo.kafka.aggregator.rest;

import java.util.Set;

public class PipelineMetadata {

	String host;
    Set<String> partitions;

    public PipelineMetadata(String host, Set<String> partitions) {
        this.host = host;
        this.partitions = partitions;
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Set<String> getPartitions() {
		return partitions;
	}

	public void setPartitions(Set<String> partitions) {
		this.partitions = partitions;
	}
    
    
}
