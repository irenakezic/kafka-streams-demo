package demo.kafka.aggregator.model;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.jboss.logging.Logger;

import demo.kafka.route.model.Station;
import demo.kafka.route.util.LocationUtil;
import demo.kafka.route.util.RouteUtil;

/**
 * 
 * Class contains tram aggregated data
 *
 */
public class TramDataAggregated {

	static final Logger LOG = Logger.getLogger(TramDataAggregated.class);

	// Tram Id
	Integer tramId; 
	
	// Tram name
	String tramName; 
	
	// Journey start time
	Instant startTime; 
	
	// Last location update time
	Instant lastUpdateTime; 
	
	// Route id
	Integer routeId; 
	
	// Last station id
	Integer lastStationId; 
	
	// Last station name
	String lastStationName; 
	
	// Next station id
	Integer nextStationId; 
	
	// Next station name
	String nextStationName; 
	
	// Next station distance in meters
	Double nextStationDistance;
	
	// Time in minutes
	Long timeToNextStation; 
	
	// Max speed (km/h)
	Double maxSpeed = Double.valueOf(0);
	
	// Current speed (km/h)
	Long speed;
	
	// Average speed (km/h)
	Long avgSpeed;
	
	// Current longitude
	Double longitude; 
	
	 // Current latitude
	Double latitude;
	
	// Total distance
	Double totalDistance; 
	
	/**
	 * Method determines if the new record is start of a new tram journey
	 * @param startTime
	 * @return
	 */
	private boolean isNewJounrey(Instant startTime) {
		if(this.startTime == null || this.startTime.isBefore(startTime)) {
			return true;
		}
		
		return false;
	}

	
	/**
	 * Updates the aggregated data with new record values
	 * @param tramData
	 * @return
	 */
	public TramDataAggregated updateFrom(TramDataJoined tramData) {
		this.tramId = tramData.tramId;
		this.tramName = tramData.tramName;
		this.routeId = tramData.routeId;
		Double distance = isNewJounrey(tramData.startTime) ? 0 : LocationUtil.getDistance(tramData.latitude, this.latitude) / 1000;
		this.totalDistance = isNewJounrey(tramData.startTime) ? distance : this.totalDistance + distance;
		Long duration = isNewJounrey(tramData.startTime) ? 0 : Duration.between(this.lastUpdateTime, tramData.time).getSeconds();
		this.startTime = tramData.startTime;
		this.lastUpdateTime = tramData.time;
		this.speed = duration > 0 ? Math.round(distance / duration * 3600) : 0;
		this.maxSpeed = Math.max(this.maxSpeed, this.speed);
		Long totalDuration = Duration.between(this.startTime, tramData.time).getSeconds();
		this.avgSpeed = totalDuration > 0 ? Math.round(this.totalDistance.doubleValue() / totalDuration.longValue() * 3600) : 0;
		setStationInfo(tramData);
		this.longitude = tramData.longitude;
		this.latitude = tramData.latitude;

		return this;
	}

	/**
	 * Update tram station related data
	 * @param tramData
	 */
	private void setStationInfo(TramDataJoined tramData) {

		List<Station> stationList = RouteUtil.getRoute(tramData.routeId);

		for (Integer i = 1; i < stationList.size(); i++) {

			Station prevStation = stationList.get(i - 1);
			Station station = stationList.get(i);

			if ((prevStation.getLatidue() < station.getLatidue() && prevStation.getLatidue() <= tramData.latitude
					&& station.getLatidue() > tramData.latitude)
					|| (prevStation.getLatidue() > station.getLatidue() && prevStation.getLatidue() >= tramData.latitude
							&& station.getLatidue() < tramData.latitude)) {
				this.lastStationId = prevStation.getId();
				this.lastStationName = prevStation.getName();
				this.nextStationId = station.getId();
				this.nextStationName = station.getName();
				this.nextStationDistance = LocationUtil.getDistance(station.getLatidue(), tramData.latitude);
				this.timeToNextStation = Math.round(this.nextStationDistance / this.avgSpeed * 60 / 1000);
				this.timeToNextStation = this.timeToNextStation > 30 ? null : this.timeToNextStation;
				break;
			}
		}
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
