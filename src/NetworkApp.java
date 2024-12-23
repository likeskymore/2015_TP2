package src;
import java.io.*;
import java.util.*;

public class NetworkApp {
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader();
        String filePath = "Tests/TestCase1.txt";

        // Load data from file
        dataLoader.loadData(filePath);

        // Retrieve cities and warehouses
        List<City> cities = dataLoader.getCities();
        List<Warehouse> warehouses = dataLoader.getWarehouses();

        EmergencySupplyNetwork supplySystem = new EmergencySupplyNetwork();
        supplySystem.costGraphBuilder(cities, warehouses);
        Map<City,List<Warehouse>> allocationLog = supplySystem.allocateResources(cities, warehouses);
        
        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();
        resourceRedistribution.distributeResources(warehouses);

        DynamicResourceSharing citiesClusters = new DynamicResourceSharing(cities);
        citiesClusters.displayClusters();
        citiesClusters.citiesUnion(allocationLog);
        citiesClusters.queryCities();
    }
}