package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class NetworkApp {
    public static void main(String[] args) throws IOException {
        // Créer un Gson avec une indentation (pretty-print)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        DataLoader dataLoader = new DataLoader();
        String filePath = "Tests/TestCase1.txt";

        // Load data from file
        dataLoader.loadData(filePath);

        // Retrieve cities and warehouses
        List<City> cities = dataLoader.getCities();
        List<Warehouse> warehouses = dataLoader.getWarehouses();

        EmergencySupplyNetwork supplySystem = new EmergencySupplyNetwork();
        Map<City, Map<Warehouse, Double>> costMatrix = supplySystem.costGraphBuilder(cities, warehouses);
        Map<City, Map<Warehouse, Integer>> allocationLog = supplySystem.allocateResources(cities, warehouses);
        Map<Warehouse, Integer> remainingStock = supplySystem.getRemainingStock();

        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();
        List<Map<String, Object>> transferList = resourceRedistribution.distributeResources(warehouses);

        DynamicResourceSharing citiesClusters = new DynamicResourceSharing(cities);
        Map<Integer, List<City>> clusterMap = citiesClusters.citiesUnion(allocationLog);
        List<Map<String, Object>> queryList = citiesClusters.queryCities();  // Get queries from the cities' clusters

        // Créer une map pour stocker le résultat JSON
        Map<String, Object> jsonOutput = new HashMap<>();

        // Task 1 and 2
        Map<String, Object> task1and2 = new HashMap<>();
        
        // Cost Matrix - Use data dynamically from costMatrix
        Map<String, Object> graphRepresentation = new HashMap<>();
        List<Map<String, Object>> costMatrixList = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> cityCost = new LinkedHashMap<>();
            cityCost.put("City", "City " + city.getId());
            Map<Warehouse, Double> warehouseCosts = costMatrix.get(city);
            for (Warehouse warehouse : warehouses) {
                cityCost.put("Warehouse " + warehouse.getId(), warehouseCosts.get(warehouse));
            }
            costMatrixList.add(cityCost);
        }
        graphRepresentation.put("Cost Matrix", costMatrixList);
        task1and2.put("Graph Representation", graphRepresentation);
        
        // Resource Allocation - Use data dynamically from allocationLog
        List<Map<String, Object>> allocationList = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> allocation = new HashMap<>();
            allocation.put("City", "City " + city.getId());
            allocation.put("Priority", city.getPriority());
            allocation.put("Allocated", allocationLog.get(city).values().stream().mapToInt(Integer::intValue).sum()); // Dynamic allocation
            allocation.put("Warehouse", allocationLog.get(city).keySet().stream().map(warehouse -> "Warehouse " + warehouse.getId()).findFirst().orElse("N/A"));
            allocationList.add(allocation);
        }
        task1and2.put("Resource Allocation", allocationList);

        // Ajouter remaining resources
        Map<String, Object> remainingCapacities = new HashMap<>();
        for (Warehouse warehouse : warehouses) {
            // Fetch remaining stock for each warehouse from the remainingStock map
            remainingCapacities.put("Warehouse " + warehouse.getId(), remainingStock.get(warehouse));
        }
        task1and2.put("Remaining Capacities", remainingCapacities);

        // Ajouter Task 1 and 2 au JSON final
        jsonOutput.put("Task 1 and 2", task1and2);

        // Task 3: Resource Redistribution
        Map<String, Object> task3 = new HashMap<>();
        Map<String, Object> resourceRedistributio = new HashMap<>();
        
        // Transfers from Resource Redistribution (populate from transferList)
        resourceRedistributio.put("Transfers", transferList);
        
        // Final Resource Levels (Assuming you want the final values for warehouses after redistribution)
        Map<String, Object> finalResourceLevels = new HashMap<>();
        for (Warehouse warehouse : warehouses) {
            finalResourceLevels.put("Warehouse " + warehouse.getId(), warehouse.getCapacity());
        }
        resourceRedistributio.put("Final Resource Levels", finalResourceLevels);

        task3.put("Resource Redistribution", resourceRedistributio);
        jsonOutput.put("Task 3", task3);

        // Task 4: Dynamic Resource Sharing
        Map<String, Object> task4 = new HashMap<>();
        Map<String, Object> dynamicResourceSharing = new HashMap<>();
        
        // Initial Clusters - Dynamically created from clusterMap
        Map<String, String> initialClusters = new HashMap<>();
        for (Map.Entry<Integer, List<City>> entry : clusterMap.entrySet()) {
            for (City city : entry.getValue()) {
                initialClusters.put("City " + city.getId(), "Cluster " + entry.getKey());
            }
        }
        dynamicResourceSharing.put("Initial Clusters", initialClusters);

        // Merging Steps - Use the citiesClusters merging steps dynamically
        List<Map<String, Object>> mergingSteps = new ArrayList<>();
        List<Map<String, Object>> mergeSteps = citiesClusters.getMergingSteps();
        mergingSteps.addAll(mergeSteps);
        dynamicResourceSharing.put("Merging Steps", mergingSteps);
        
        // Cluster Membership After Merging - Dynamically generated using clusterMap
        Map<String, String> clusterMembership = new HashMap<>();
        for (Map.Entry<Integer, List<City>> entry : clusterMap.entrySet()) {
            for (City city : entry.getValue()) {
                clusterMembership.put("City " + city.getId(), "Cluster " + entry.getKey());
            }
        }
        dynamicResourceSharing.put("Cluster Membership After Merging", clusterMembership);

        // Queries from DynamicResourceSharing - Using dynamically generated query list
        dynamicResourceSharing.put("Queries", queryList);

        task4.put("Dynamic Resource Sharing", dynamicResourceSharing);
        jsonOutput.put("Task 4", task4);

        // Écrire le résultat JSON dans un fichier avec indentation
        try (Writer writer = new FileWriter("output.json")) {
            gson.toJson(jsonOutput, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
