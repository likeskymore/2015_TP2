package src;

import java.util.*;
import DisjointSets.*;

public class DynamicResourceSharing {
    private DisjointUnionSets cityClusters;
    private Map<City, Integer> cityIndexMap; // Maps City objects to indices
    private List<City> citiesList;           // Stores City objects by index
    private List<Map<String, Object>> mergingSteps; // Stores merging steps

    // Constructor initializes the disjoint set and city mapping
    public DynamicResourceSharing(List<City> cities) {
        int cityCount = cities.size();
        cityClusters = new DisjointUnionSets(cityCount);
        cityIndexMap = new HashMap<>();
        citiesList = new ArrayList<>(cities);
        mergingSteps = new ArrayList<>();  // Initialize merging steps list

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

        // Log the merging step
        Map<String, Object> mergeStep = new HashMap<>();
        mergeStep.put("Action", "Merge");
        mergeStep.put("Cities", Arrays.asList("City " + city1.getId(), "City " + city2.getId()));
        mergeStep.put("Cluster After Merge", "Cluster " + (cityClusters.find(index1) + 1));
        mergingSteps.add(mergeStep);  // Add the merge step to the list
    }

    // Checks if two cities belong to the same cluster
    public boolean areInSameCluster(City city1, City city2) {
        int index1 = cityIndexMap.get(city1);
        int index2 = cityIndexMap.get(city2);
        return cityClusters.isConnected(index1, index2);
    }

    // Performs union of cities based on shared warehouses
    public Map<Integer, List<City>> citiesUnion(Map<City, Map<Warehouse, Integer>> allocationLog) {

        // Map to track which warehouses are associated with which cities
        Map<Warehouse, List<City>> warehouseToCitiesMap = new HashMap<>();

        // Build the warehouse-to-cities map from the allocation log
        for (Map.Entry<City, Map<Warehouse, Integer>> entry : allocationLog.entrySet()) {
            City city = entry.getKey();
            Map<Warehouse, Integer> allocations = entry.getValue();

            for (Warehouse warehouse : allocations.keySet()) {
                warehouseToCitiesMap.putIfAbsent(warehouse, new ArrayList<>());
                warehouseToCitiesMap.get(warehouse).add(city);
            }
        }

        // Iterate over the warehouse-to-cities map to merge clusters of cities sharing the same warehouse
        for (Map.Entry<Warehouse, List<City>> entry : warehouseToCitiesMap.entrySet()) {
            List<City> citiesSharingWarehouse = entry.getValue();

            // Merge all cities sharing this warehouse into the same cluster
            for (int i = 0; i < citiesSharingWarehouse.size(); i++) {
                City city1 = citiesSharingWarehouse.get(i);
                for (int j = i + 1; j < citiesSharingWarehouse.size(); j++) {
                    City city2 = citiesSharingWarehouse.get(j);

                    // Ensure City 1 has the smaller ID for consistent output
                    City smallerCity = city1.getId() < city2.getId() ? city1 : city2;
                    City largerCity = city1.getId() < city2.getId() ? city2 : city1;

                    mergeClusters(smallerCity, largerCity);
                }
            }
        }

        // Construct a map of clusters
        Map<Integer, List<City>> clusters = new HashMap<>();
        for (City city : citiesList) {
            int clusterId = cityClusters.find(cityIndexMap.get(city)) + 1; // Adjust to start clusters at 1
            clusters.putIfAbsent(clusterId, new ArrayList<>());
            clusters.get(clusterId).add(city);
        }
        return clusters;
    }

    // Query cities to check if they belong to the same cluster 
    public List<Map<String, Object>> queryCities() {
        List<Map<String, Object>> queries = new ArrayList<>();

        for (int i = 0; i < citiesList.size(); i++) {
            for (int j = i + 1; j < citiesList.size(); j++) {
                City city1 = citiesList.get(i);
                City city2 = citiesList.get(j);
                boolean sameCluster = areInSameCluster(city1, city2);

                // Create a map for this query
                Map<String, Object> queryResult = new LinkedHashMap<>();
                queryResult.put("Query", "Are City " + city1.getId() + " and City " + city2.getId() + " in the same cluster?");
                queryResult.put("Result", sameCluster ? "Yes" : "No");

                // Add to the result list
                queries.add(queryResult);
            }
        }

        return queries;
    }

    // Get the merging steps that have been recorded
    public List<Map<String, Object>> getMergingSteps() {
        return mergingSteps;
    }
}
