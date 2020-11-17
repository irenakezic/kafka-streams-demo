package demo.kafka.route.util;

/**
 * 
 * Class is used for location calculations
 *
 */
public class LocationUtil {

	/**
	 * Method calculates distance between two geographic locations on same longitude
	 * @param lat1
	 * @param lat2
	 * @return
	 */
	public static Double getDistance(Double lat1, Double lat2) {
		Double r = Double.valueOf(6371 * 1000); // Earth Radius in m
		Double deltaLat = Math.toRadians(lat1 - lat2);

		Double a = Math.asin(deltaLat / 2) * Math.sin(deltaLat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(0) * Math.sin(0);

		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return r * c;
	}
	
	/**
	 * Method calculates new latitude based on the traveled distance (longitude does not change)
	 * @param oldLat
	 * @param distance
	 * @return
	 */
	public static Double calculateLatitude(Double oldLat, Double distance) {
		Double r = Double.valueOf(6371 * 1000); // Earth Radius in m

		Double newLat = Math.asin(Math.sin(Math.toRadians(oldLat)) * Math.cos(distance / r)
				+ Math.cos(Math.toRadians(oldLat)) * Math.sin(distance / r) * Math.cos(Math.toRadians(0)));

		return Math.toDegrees(newLat);

	}
}
