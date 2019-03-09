import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AddFireIncidents {

	
	public static void main(String[] args) throws IOException {
		String listingFile = "C:\\learning\\MLCert\\capstone\\output\\detail.csv";
		
		String incidentFile ="C:\\learning\\MLCert\\capstone\\output\\FireIncidents.csv";
		
		List<Listing1> listings = loadListings(listingFile);
		
		List<Point> points = loadPoints(incidentFile);
		System.out.println("--- total points: " + points.size());
		
		
		for (int i = 0; i < points.size(); i++) {
			System.out.println(i);
			Point p = points.get(i);
			for (int j = 0; j < listings.size(); j++) {
				Listing1 listing = listings.get(j);
				double dist = distance(p.lat, p.lng, listing.lat, listing.lng);
				listing.riskLevel += getRiskLevel(dist);
			}
		}
		
		String outputFile = "C:\\learning\\MLCert\\capstone\\output\\detailWithFireIncidents.csv";
		PrintWriter printWriter = new PrintWriter(outputFile);
	    for (int i = 0; i < listings.size(); i++) {
	        printWriter.println(listings.get(i).toCSV());
	    }
	    printWriter.close();
	}
	
	private static double getRiskLevel(double distance) {
		if (distance <= 100) {
			return 1;
		} else if (distance <= 250) {
			return 0.5;
		} else if (distance <= 500) {
			return 0.25;
		} else 
			return 0;
	}
	
	private static List<Listing1> loadListings(String file) {
		List<Listing1> listings = new ArrayList<Listing1>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				listings.add(Listing1.read(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listings;
	}
	
	private static List<Point> loadPoints(String file) {
		List<Point> points = new ArrayList<Point>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				try {
					String[] parts = line.split(",");
					double lat = Double.parseDouble(parts[8]); // latitude
					double lng = Double.parseDouble(parts[9]); // longitude
					points.add(new Point(lng, lat));
				} catch (NumberFormatException nfe) {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return points;
	}
	
	public static double distance(double lat1, double lon1,double lat2, 
	        double lon2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    distance = Math.pow(distance, 2);

	    return Math.sqrt(distance);
	}
}
