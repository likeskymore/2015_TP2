package src;

import java.util.*;
import PQueue.*;
import Graph.*;

public class EmergencySupplyNetwork {
    private AdjacencyMapGraph<Location, Integer> costGraph = new AdjacencyMapGraph<>(false);

    // Method to calculate the cost (Euclidean distance) between cities and warehouses
    public void costGraphBuilder(List<City> cities, List<Warehouse> warehouses) {
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

        // Print the header for the cost matrix
        System.out.println("Graph Representation (Cost Matrix):");
        System.out.println("----------------------------------------------------------------");

        // Print the first row (cities label) and align the warehouses properly
        System.out.print(String.format("%-10s", "cities |"));  // Formatting for 'cities' label
        for (Warehouse warehouse : warehouses) {
            System.out.print(String.format("%-15s", "Warehouse " + warehouse.getId())+" | ");  // Align warehouse columns
        }
        System.out.println();
        System.out.println("----------------------------------------------------------------");

        // Now, calculate and print the cost matrix
        for (City city : cities) {
            System.out.print(String.format("%-10s", "City " + city.getId() + " |"));  // Formatting for city label
            for (Warehouse warehouse : warehouses) {
                // Find the vertex for the city and warehouse
                Vertex<Location> cityVertex = findVertex(city);
                Vertex<Location> warehouseVertex = findVertex(warehouse);

                // Calculate the cost (Euclidean distance)
                double cost = calculateCost(city, warehouse);

                // Add edge between the city and warehouse with the calculated cost
                costGraph.insertEdge(cityVertex, warehouseVertex, (int) cost);  // Cast to int if needed

                // Print the cost for the specific city and warehouse with two decimal places, ensuring alignment
                System.out.print(String.format("%-15.2f", cost)+" | ");  // Formatting for costs
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------");
    }

    public Map<City,List<Warehouse>> allocateResources(List<City> cities, List<Warehouse> warehouses) {
                // Priority queue for cities based on priority (High -> 1, Medium -> 2, Low -> 3)
        SortedPriorityQueue<Integer, City> cityQueue = new SortedPriorityQueue<>();
        Map<City, List<Warehouse>> allocationLog = new HashMap<>();

        // Insert cities into the priority queue, with priority as the key
        for (City city : cities) {
            int priorityValue = priorityToInteger(city.getPriority());  // Map priority to integer value
            cityQueue.insert(priorityValue, city);
        }

        // Process cities in priority order (Low priority city will be processed last)
        System.out.println("Allocating resources:");
        while (!cityQueue.isEmpty()) {
            City city = cityQueue.removeMin().getValue();
            System.out.println("Allocating resources for City " + city.getId() + " (Priority: " + city.getPriority() + ")");

            List<Warehouse> allocatedWarehouses = new ArrayList<>();
            boolean fulfilled = false;

            for (Warehouse warehouse : findWarehousesInCostOrder(city, warehouses)) {
                if (warehouse.getCapacity() > 0) {
                    int allocatedUnits = Math.min(warehouse.getCapacity(), city.getDemand());
                    warehouse.setCapacity(warehouse.getCapacity() - allocatedUnits);
                    city.setDemand(city.getDemand() - allocatedUnits);

                    // Add the warehouse to the allocation log
                    if (!allocatedWarehouses.contains(warehouse)) {
                        allocatedWarehouses.add(warehouse);
                    }
                    System.out.println("Allocated " + allocatedUnits + " units from Warehouse " + warehouse.getId());

                    if (city.getDemand() == 0) {
                        fulfilled = true;
                        break;
                    }
                }
            }

            if (!fulfilled) {
                System.out.println("Unable to fully fulfill demand for City " + city.getId());
            }

            allocationLog.put(city, allocatedWarehouses);
        }

        // Print remaining warehouse capacities
        System.out.println("Remaining Warehouse Capacities:");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Warehouse " + warehouse.getId() + ": " + warehouse.getCapacity());
        }

        return allocationLog;
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
