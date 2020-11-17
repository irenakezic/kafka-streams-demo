package demo.kafka.producer.model;

import java.time.Instant;

/**
 * Tram Location data contains geographic coordinates at certain point in time
 *
 */
public class Location {

	Instant time;
	Double latitude;
	Double longitude;
	
	public Location(Instant time, Double latitude, Double longitude) {
		super();
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
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
	
	
}
