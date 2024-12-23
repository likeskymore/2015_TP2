package src;

import java.util.*;
import PQueue.*;
import Graph.*;

public class EmergencySupplyNetwork {
    private AdjacencyMapGraph<Location, Integer> costGraph = new AdjacencyMapGraph<>(false);
    private Map<Warehouse, Integer> remainingStock = new HashMap<>();

    // Method to calculate the cost (Euclidean distance) between cities and warehouses
    public Map<City, Map<Warehouse, Double>> costGraphBuilder(List<City> cities, List<Warehouse> warehouses) {
        Map<City, Map<Warehouse, Double>> costMatrix = new HashMap<>();

        // Insert vertices for cities into the graph
        for (City city : cities) {
            Vertex<Location> cityVertex = new LocationVertex(city);  // Wrap the city in LocationVertex
            costGraph.insertVertex(cityVertex.getElement());  // Insert the city vertex into the graph
        }

        // Insert vertices for warehouses into the graph
        for (Warehouse warehouse : warehouses) {
            Vertex<Location> warehouseVertex = new LocationVertex(warehouse);  // Wrap the warehouse in LocationVertex
            costGraph.insertVertex(warehouseVertex.getElement());  // Insert the warehouse vertex into the graph
        }
        // Now, calculate the cost matrix
        for (City city : cities) {
            Map<Warehouse, Double> warehouseCostMap = new HashMap<>();
            for (Warehouse warehouse : warehouses) {
                // Find the vertex for the city and warehouse
                Vertex<Location> cityVertex = findVertex(city);
                Vertex<Location> warehouseVertex = findVertex(warehouse);

                // Calculate the cost (Euclidean distance)
                double cost = calculateCost(city, warehouse);

                // Add edge between the city and warehouse with the calculated cost
                costGraph.insertEdge(cityVertex, warehouseVertex, (int) cost);  // Cast to int if needed

                // Store the cost for this city-warehouse pair
                warehouseCostMap.put(warehouse, cost);
            }
            costMatrix.put(city, warehouseCostMap);
        }
        return costMatrix;
    }

    public Map<City, Map<Warehouse, Integer>> allocateResources(List<City> cities, List<Warehouse> warehouses) {
        // Priority queue for cities based on priority (High -> 1, Medium -> 2, Low -> 3)
        SortedPriorityQueue<Integer, City> cityQueue = new SortedPriorityQueue<>();
        Map<City, Map<Warehouse, Integer>> allocationLog = new HashMap<>();
        
        // Initialize the remaining stock map with the warehouse capacities (assuming initial stock is same as capacity)
        for (Warehouse warehouse : warehouses) {
            remainingStock.put(warehouse, warehouse.getCapacity());  // Assuming initial stock = capacity
        }

        // Insert cities into the priority queue, with priority as the key
        for (City city : cities) {
            int priorityValue = priorityToInteger(city.getPriority()); // Map priority to integer value
            cityQueue.insert(priorityValue, city);
        }

        // Process cities in priority order (Low priority city will be processed last)
        while (!cityQueue.isEmpty()) {
            City city = cityQueue.removeMin().getValue();

            Map<Warehouse, Integer> allocatedWarehouses = new HashMap<>();
            boolean fulfilled = false;

            for (Warehouse warehouse : findWarehousesInCostOrder(city, warehouses)) {
                int availableStock = remainingStock.get(warehouse);  // Get the remaining stock for the warehouse
                if (availableStock > 0) {  // Check if warehouse has remaining stock
                    int allocatedUnits = Math.min(availableStock, city.getDemand());  // Allocate units based on available stock
                    remainingStock.put(warehouse, availableStock - allocatedUnits);  // Decrease stock after allocation
                    city.setDemand(city.getDemand() - allocatedUnits);  // Update city demand

                    // Update allocation log with the allocated units
                    allocatedWarehouses.put(warehouse, allocatedWarehouses.getOrDefault(warehouse, 0) + allocatedUnits);
                    
                    if (city.getDemand() == 0) {
                        fulfilled = true;
                        break;
                    }
                }
            }
            allocationLog.put(city, allocatedWarehouses);
        }
        return allocationLog;
    }

    // Method to get the remaining stock (read-only access)
    public Map<Warehouse, Integer> getRemainingStock() {
        return new HashMap<>(remainingStock);  // Return a copy to avoid external modifications
    }

    // Convert the priority string to an integer value
    private int priorityToInteger(String priority) {
        switch (priority) {
            case "High":
                return 1;
            case "Medium":
                return 2;
            case "Low":
                return 3;
            default:
                throw new IllegalArgumentException("Invalid priority: " + priority);
        }
    }

    // Find warehouses sorted by transportation cost from the given city
    private List<Warehouse> findWarehousesInCostOrder(City city, List<Warehouse> warehouses) {
        return warehouses.stream()
            .sorted(Comparator.comparingDouble(w -> calculateCost(city, w)))
            .toList();
    }

    // Method to find the Vertex for a given Location object
    private Vertex<Location> findVertex(Location location) {
        // Search for the vertex corresponding to the given Location object
        for (Vertex<Location> vertex : costGraph.vertices()) {
            if (vertex.getElement().equals(location)) {
                return vertex;
            }
        }
        return null;  // If not found, return null
    }

    // Method to calculate the Euclidean distance between a city and a warehouse
    private double calculateCost(City city, Warehouse warehouse) {
        int xCity = city.getX();
        int yCity = city.getY();
        int xWarehouse = warehouse.getX();
        int yWarehouse = warehouse.getY();

        // Calculate Euclidean distance
        double distance = Math.sqrt(Math.pow(xCity - xWarehouse, 2) + Math.pow(yCity - yWarehouse, 2));

        if (distance > 10 && distance <= 20) {
            return (Math.round(distance * 100.0) / 100.0) * 2; // use truck
        } else if (distance > 20){
            return (Math.round(distance * 100.0) / 100.0) * 3; // use rail
        } else {
            return Math.round(distance * 100.0) / 100.0;       // use drone
        }
    }
}
