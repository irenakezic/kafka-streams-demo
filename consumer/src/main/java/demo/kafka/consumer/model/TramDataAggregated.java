package demo.kafka.consumer.model;

import java.time.Instant;

import org.jboss.logging.Logger;

public class TramDataAggregated {

	static final Logger LOG = Logger.getLogger(TramDataAggregated.class);

	Integer tramId; 
	String tramName; 
	Instant startTime; 
	Instant lastUpdateTime;
	Integer routeId; 
	Integer lastStationId; 
	String lastStationName; 
	Integer nextStationId; 
	String nextStationName;
	Double nextStationDistance; 
	Long timeToNextStation;
	Double maxSpeed; 
	Long speed; 
	Long avgSpeed;
	Double longitude; 
	Double latitude; 
	Double totalDistance; 

	public TramDataAggregated() {
		super();
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

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Instant lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getLastStationId() {
		return lastStationId;
	}

	public void setLastStationId(Integer lastStationId) {
		this.lastStationId = lastStationId;
	}

	public String getLastStationName() {
		return lastStationName;
	}

	public void setLastStationName(String lastStationName) {
		this.lastStationName = lastStationName;
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

	public Double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

	public Long getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(Long avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}

}
