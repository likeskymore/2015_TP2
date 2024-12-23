package src;

import java.util.Comparator;
import java.util.List;

import MinHeap.HeapPriorityQueue;

public class ResourceRedistribution {
    private HeapPriorityQueue<Integer, Warehouse> surplusHeap;
    private HeapPriorityQueue<Integer, Warehouse> needHeap;

    // Constructor initializes the heaps
    public ResourceRedistribution() {
        // SurplusHeap: Max-Heap (uses reverse order comparator for Integer keys)
        surplusHeap = new HeapPriorityQueue<Integer, Warehouse>(Comparator.reverseOrder());
        // NeedHeap: Min-Heap (uses natural order comparator for Integer keys)
        needHeap = new HeapPriorityQueue<Integer, Warehouse>(Comparator.naturalOrder());
    }

    /**
     * Distribute surplus resources to warehouses in need.
     */
    public void distributeResources(List<Warehouse> warehouses) {

        if (warehouses == null || warehouses.isEmpty()) {
            System.out.println("No warehouses to process.");
            return;
        }
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getCapacity() > 50) {
                // Insert into surplusHeap with max-heap behavior
                surplusHeap.insert(warehouse.getCapacity(), warehouse);
            } else if (warehouse.getCapacity() < 50) {
                // Insert into needHeap with min-heap behavior
                needHeap.insert(warehouse.getCapacity(), warehouse);
            }
        }

        while (!surplusHeap.isEmpty() && !needHeap.isEmpty()) {
            // Get the warehouse with the highest surplus
            Warehouse surplusWarehouse = surplusHeap.removeMin().getValue();
            // Get the warehouse with the most urgent need
            Warehouse needWarehouse = needHeap.removeMin().getValue();

            int surplus = surplusWarehouse.getCapacity() - 50; // Surplus over the threshold
            int need = 50 - needWarehouse.getCapacity(); // Needed to reach the threshold

            // Allocate resources
            int transferAmount = Math.min(surplus, need);
            surplusWarehouse.setCapacity(surplusWarehouse.getCapacity() - transferAmount);
            needWarehouse.setCapacity(needWarehouse.getCapacity() + transferAmount);

            System.out.println("Transferred " + transferAmount + " units from Warehouse " +
                    surplusWarehouse.getId() + " to Warehouse " + needWarehouse.getId());

            // Reinsert updated warehouses into appropriate heaps
            if (surplusWarehouse.getCapacity() > 50) {
                surplusHeap.insert(surplusWarehouse.getCapacity(), surplusWarehouse);
            }
            if (needWarehouse.getCapacity() < 50) {
                needHeap.insert(needWarehouse.getCapacity(), needWarehouse);
            }
        }

        System.out.println("Final Resource Levels:");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Warehouse " + warehouse.getId() + ": " + warehouse.getCapacity() + " units");
        }

    }
}
