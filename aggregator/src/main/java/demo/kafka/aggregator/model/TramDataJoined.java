package demo.kafka.aggregator.model;

import java.time.Instant;

/**
 * 
 * Tram data with current tram location
 *
 */
public class TramDataJoined {

	Integer tramId;
	String tramName;
	Integer routeId;
	Instant startTime;
	Instant time;
	Double latitude;
	Double longitude;
	
	public TramDataJoined(Integer tramId, String tramName, Integer routeId, Instant startTime, Instant time, Double longitude, Double latitude) {
		super();
		this.tramId = tramId;
		this.tramName = tramName;
		this.routeId = routeId;
		this.startTime = startTime;
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}
	
}
