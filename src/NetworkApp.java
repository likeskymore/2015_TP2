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
        supplySystem.costGraphBuilder(cities, warehouses);
        Map<City, List<Warehouse>> allocationLog = supplySystem.allocateResources(cities, warehouses);

        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();
        resourceRedistribution.distributeResources(warehouses);

        DynamicResourceSharing citiesClusters = new DynamicResourceSharing(cities);
        citiesClusters.displayClusters();
        citiesClusters.citiesUnion(allocationLog);
        citiesClusters.queryCities();

        // Créer une map pour stocker le résultat JSON
        Map<String, Object> jsonOutput = new HashMap<>();

        // Task 1 and 2
        Map<String, Object> task1and2 = new HashMap<>();
        
        // Cost Matrix
        Map<String, Object> costMatrix = new HashMap<>();
        List<Map<String, Object>> costMatrixList = new ArrayList<>();
        // Remplir le costMatrixList avec vos données calculées pour les villes et entrepôts (exemple)
        for (City city : cities) {
            Map<String, Object> cityCost = new HashMap<>();
            cityCost.put("City", "City " + city.getId());  // Assurez-vous que la méthode getId() retourne l'identifiant
            for (Warehouse warehouse : warehouses) {
                cityCost.put("Warehouse " + warehouse.getId(), Math.random() * 100);  // Remplacez par le calcul réel
            }
            costMatrixList.add(cityCost);
        }
        costMatrix.put("Cost Matrix", costMatrixList);
        task1and2.put("Graph Representation", costMatrix);
        
        // Resource Allocation
        List<Map<String, Object>> allocationList = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> allocation = new HashMap<>();
            allocation.put("City", "City " + city.getId());  
            allocation.put("Priority", "High");  
            allocation.put("Allocated", 50);  
            allocation.put("Warehouse", "Warehouse 101");  
            allocationList.add(allocation);
        }
        task1and2.put("Resource Allocation", allocationList);

        // Remaining Capacities
        Map<String, Object> remainingCapacities = new HashMap<>();
        remainingCapacities.put("Warehouse 101", 0);
        remainingCapacities.put("Warehouse 102", 20);
        remainingCapacities.put("Warehouse 103", 110);
        task1and2.put("Remaining Capacities", remainingCapacities);

        // Ajouter Task 1 and 2 au JSON final
        jsonOutput.put("Task 1 and 2", task1and2);

        // Task 3
        Map<String, Object> task3 = new HashMap<>();
        Map<String, Object> resourceRedistributio = new HashMap<>();
        
        // Transfers
        List<Map<String, Object>> transfersList = new ArrayList<>();
        Map<String, Object> transfer1 = new HashMap<>();
        transfer1.put("From", "Warehouse Z");
        transfer1.put("To", "Warehouse X");
        transfer1.put("Units", 50);
        transfersList.add(transfer1);

        Map<String, Object> transfer2 = new HashMap<>();
        transfer2.put("From", "Warehouse Z");
        transfer2.put("To", "Warehouse Y");
        transfer2.put("Units", 10);
        transfersList.add(transfer2);

        resourceRedistributio.put("Transfers", transfersList);
        
        // Final Resource Levels
        Map<String, Object> finalResourceLevels = new HashMap<>();
        finalResourceLevels.put("Warehouse 101", 50);
        finalResourceLevels.put("Warehouse 102", 30);
        finalResourceLevels.put("Warehouse 103", 50);
        resourceRedistributio.put("Final Resource Levels", finalResourceLevels);

        task3.put("Resource Redistribution", resourceRedistributio);
        jsonOutput.put("Task 3", task3);

        // Task 4
        Map<String, Object> task4 = new HashMap<>();
        Map<String, Object> dynamicResourceSharing = new HashMap<>();
        
        // Initial Clusters
        Map<String, String> initialClusters = new HashMap<>();
        initialClusters.put("City A", "Cluster 1");
        initialClusters.put("City B", "Cluster 2");
        initialClusters.put("City C", "Cluster 3");
        dynamicResourceSharing.put("Initial Clusters", initialClusters);

        // Merging Steps
        List<Map<String, Object>> mergingSteps = new ArrayList<>();
        Map<String, Object> mergeStep = new HashMap<>();
        mergeStep.put("Action", "Merge");
        mergeStep.put("Cities", Arrays.asList("City A", "City B"));
        mergeStep.put("Cluster After Merge", "Cluster 1");
        mergingSteps.add(mergeStep);

        dynamicResourceSharing.put("Merging Steps", mergingSteps);

        // Cluster Membership After Merging
        Map<String, String> clusterMembership = new HashMap<>();
        clusterMembership.put("City A", "Cluster 1");
        clusterMembership.put("City B", "Cluster 1");
        clusterMembership.put("City C", "Cluster 3");
        dynamicResourceSharing.put("Cluster Membership After Merging", clusterMembership);

        // Queries
        List<Map<String, Object>> queries = new ArrayList<>();
        Map<String, Object> query1 = new HashMap<>();
        query1.put("Query", "Are City A and City C in the same cluster?");
        query1.put("Result", "No");
        queries.add(query1);

        Map<String, Object> query2 = new HashMap<>();
        query2.put("Query", "Are City A and City B in the same cluster?");
        query2.put("Result", "Yes");
        queries.add(query2);

        Map<String, Object> query3 = new HashMap<>();
        query3.put("Query", "Are City B and City C in the same cluster?");
        query3.put("Result", "No");
        queries.add(query3);

        dynamicResourceSharing.put("Queries", queries);

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
