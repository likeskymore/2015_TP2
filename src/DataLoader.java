package src;
import java.io.*;
import java.util.*;

public class DataLoader {
    List<City> cities = new ArrayList<>();
    List<Warehouse> warehouses = new ArrayList<>();

    // Method to load data from file
    public void loadData(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        boolean readingCities = false;
        boolean readingWarehouses = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("Cities:")) {
                readingCities = true;
                readingWarehouses = false;
                continue;
            }

            if (line.startsWith("Warehouses:")) {
                readingWarehouses = true;
                readingCities = false;
                continue;
            }

            // Parse Cities
            if (readingCities) {
                cities.add(parseCity(line));
            } 
            // Parse Warehouses
            else if (readingWarehouses) {
                warehouses.add(parseWarehouse(line));
            }
        }

        br.close();
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    // Method to parse City
    private static City parseCity(String line) {
        try {
            // Example: "City A: ID = 1, Coordinates = (2, 7), Demand = 70 units, Priority = Medium"
            
            // Remove "City A:" part
            String cityDetails = line.split(":")[1].trim();
            
            // Regex pattern to extract ID, Coordinates, Demand, and Priority
            String regex = "ID = (\\d+), Coordinates = \\((\\d+), (\\d+)\\), Demand = (\\d+) units, Priority = (\\w+)";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(cityDetails);

            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                int x = Integer.parseInt(matcher.group(2));
                int y = Integer.parseInt(matcher.group(3));
                int demand = Integer.parseInt(matcher.group(4));
                String priority = matcher.group(5);

                return new City(id, x, y, demand, priority);
            } else {
                throw new IllegalArgumentException("Invalid city format: " + line);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing city line: " + line, e);
        }
    }
    // Method to parse Warehouse
    private static Warehouse parseWarehouse(String line) {
        try {
            // Example: "Warehouse X: ID = 101, Coordinates = (0, 10), Capacity = 100 units"
            
            // Remove "Warehouse X:" part
            String warehouseDetails = line.split(":")[1].trim();
            
            // Regex pattern to extract ID, Coordinates, and Capacity
            String regex = "ID = (\\d+), Coordinates = \\((\\d+), (\\d+)\\), Capacity = (\\d+) units";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(warehouseDetails);

            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                int x = Integer.parseInt(matcher.group(2));
                int y = Integer.parseInt(matcher.group(3));
                int capacity = Integer.parseInt(matcher.group(4));

                return new Warehouse(id, x, y, capacity);
            } else {
                throw new IllegalArgumentException("Invalid warehouse format: " + line);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing warehouse line: " + line, e);
        }
    }
}