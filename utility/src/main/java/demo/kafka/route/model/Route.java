package demo.kafka.route.model;

import java.util.List;

public class Route {
	
	Integer id;
	List<Station> stations;
	
	public Route(Integer id, List<Station> stations) {
		super();
		this.id = id;
		this.stations = stations;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

}
