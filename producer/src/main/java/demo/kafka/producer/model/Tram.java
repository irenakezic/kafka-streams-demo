package demo.kafka.producer.model;

import java.time.Instant;

/**
 * 
 * Class contains tram information created when tram starts the journey
 *
 */
public class Tram {
	
	Integer id;
	String name;
	Integer routeId;
	Instant time;
	
	public Tram() {
		super();
	}

	public Tram(Integer id, String name, Integer routeId, Instant time) {
		super();
		this.id = id;
		this.name = name;
		this.routeId = routeId;
		this.time = time;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
