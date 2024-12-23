package src;

import java.util.*;
import DisjointSets.*;

public class DynamicResourceSharing {
    private DisjointUnionSets cityClusters;
    private Map<City, Integer> cityIndexMap; // Maps City objects to indices
    private List<City> citiesList;           // Stores City objects by index

    // Constructor initializes the disjoint set and city mapping
    public DynamicResourceSharing(List<City> cities) {
        int cityCount = cities.size();
        cityClusters = new DisjointUnionSets(cityCount);
        cityIndexMap = new HashMap<>();
        citiesList = new ArrayList<>(cities);

        // Map city objects to their indices
        for (int i = 0; i < cityCount; i++) {
            cityIndexMap.put(cities.get(i), i);
        }
    }

    // Merges the clusters containing two cities
    public void mergeClusters(City city1, City city2) {
        int index1 = cityIndexMap.get(city1);
        int index2 = cityIndexMap.get(city2);
        cityClusters.union(index1, index2);
    }

    // Checks if two cities belong to the same cluster
    public boolean areInSameCluster(City city1, City city2) {
        int index1 = cityIndexMap.get(city1);
        int index2 = cityIndexMap.get(city2);
        return cityClusters.isConnected(index1, index2);
    }

    // Displays the current cluster of each city
    public void displayClusters() {
        System.out.println("Initial Clusters:");
        for (City city : citiesList) {
            int index = cityIndexMap.get(city);
            int cluster = cityClusters.find(index) + 1; // Adjust cluster index to start from 1
            System.out.println("City " + city.getId() + " belongs to cluster: " + cluster);
        }
    }

    // Performs union of cities based on shared warehouses
    public void citiesUnion(Map<City, List<Warehouse>> allocationLog) {
        System.out.println("Merging clusters...");
    
        // Iterate through the allocation log to merge cities sharing warehouses
        for (Map.Entry<City, List<Warehouse>> entry : allocationLog.entrySet()) {
            City city = entry.getKey();
            List<Warehouse> warehouses = entry.getValue();
    
            // Union cities that share the same warehouse
            for (Warehouse warehouse : warehouses) {
                for (City otherCity : allocationLog.keySet()) {
                    // Avoid merging the same city with itself and ensure each pair is only merged once
                    if (otherCity != city && allocationLog.get(otherCity).contains(warehouse) && cityIndexMap.get(city) < cityIndexMap.get(otherCity)) {
                        System.out.println("Merging clusters of City " + city.getId() + " and City " + otherCity.getId() + "...");
                        mergeClusters(city, otherCity);
                    }
                }
            }
        }
    
        // After merging, display the final clusters
        displayClusters();
    }

    // Query cities to check if they belong to the same cluster
    public void queryCities() {
        System.out.println("Querying cities:");
        for (int i = 0; i < citiesList.size(); i++) {
            for (int j = i + 1; j < citiesList.size(); j++) {
                City city1 = citiesList.get(i);
                City city2 = citiesList.get(j);
                boolean sameCluster = areInSameCluster(city1, city2);
                System.out.println("Query: Are City " + city1.getId() + " and City " + city2.getId() + " in the same cluster?");
                System.out.println(sameCluster ? "Yes" : "No");
            }
        }
    }
}