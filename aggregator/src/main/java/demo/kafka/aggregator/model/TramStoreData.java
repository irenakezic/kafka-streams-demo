package demo.kafka.aggregator.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

public class TramStoreData {

	//Tram Id
	Integer tramId; 
	
	//Tram name
	String tramName; 
	
	//Next station id
	Integer nextStationId;	
	
	//Next station name
	String nextStationName; 
	
	//Next station distance in meters
	Double nextStationDistance;
	
	//Time in minutes
	Long timeToNextStation; 
	
	//Average speed (km/h)
	Long avgSpeed;

    private TramStoreData(Integer tramId, String tramName, Integer nextStationId, String nextStationName,
            Double nextStationDistance, Long timeToNextStation, Long avgSpeed) {
        this.tramId = tramId;
        this.tramName = tramName;
        this.nextStationId = nextStationId;
        this.nextStationName = nextStationName;
        this.nextStationDistance = nextStationDistance;
        this.timeToNextStation = timeToNextStation;
        this.avgSpeed = avgSpeed;
    }

    public static TramStoreData from(TramDataAggregated aggregation) {
        return new TramStoreData(
                aggregation.getTramId(),
                aggregation.getTramName(),
                aggregation.getNextStationId(),
                aggregation.getNextStationName(),
                aggregation.getNextStationDistance(),
                aggregation.getTimeToNextStation(),
                aggregation.getAvgSpeed());
    }

	public Integer getTramId() {
		return tramId;
	}

	public void setTramId(Integer tramId) {
		this.tramId = tramId;
	}

	public String getTramName() {
		return tramName;
	}

	public void setTramName(String tramName) {
		this.tramName = tramName;
	}

	public Integer getNextStationId() {
		return nextStationId;
	}

	public void setNextStationId(Integer nextStationId) {
		this.nextStationId = nextStationId;
	}

	public String getNextStationName() {
		return nextStationName;
	}

	public void setNextStationName(String nextStationName) {
		this.nextStationName = nextStationName;
	}

	public Double getNextStationDistance() {
		return nextStationDistance;
	}

	public void setNextStationDistance(Double nextStationDistance) {
		this.nextStationDistance = nextStationDistance;
	}

	public Long getTimeToNextStation() {
		return timeToNextStation;
	}

	public void setTimeToNextStation(Long timeToNextStation) {
		this.timeToNextStation = timeToNextStation;
	}

	public Long getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(Long avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
}