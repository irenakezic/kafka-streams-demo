package demo.kafka.route.model;

public class Station {

	Integer id;
	String name;
	Double longitude;
	Double latidue;

	public Station(Integer id, String name, Double latidue, Double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latidue = latidue;
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

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatidue() {
		return latidue;
	}

	public void setLatidue(Double latidue) {
		this.latidue = latidue;
	}
}
