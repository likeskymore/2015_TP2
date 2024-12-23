package src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> distributeResources(List<Warehouse> warehouses) {
        List<Map<String, Object>> transfers = new ArrayList<>();  // Store transfer records

        if (warehouses == null || warehouses.isEmpty()) {
            return transfers;  // Return empty list if no warehouses
        }

        // Populate the heaps based on warehouse capacity
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getCapacity() > 50) {
                surplusHeap.insert(warehouse.getCapacity(), warehouse);
            } else if (warehouse.getCapacity() < 50) {
                needHeap.insert(warehouse.getCapacity(), warehouse);
            }
        }

        // Perform resource redistribution
        while (!surplusHeap.isEmpty() && !needHeap.isEmpty()) {
            // Get warehouse with the highest surplus and the most urgent need
            Warehouse surplusWarehouse = surplusHeap.removeMin().getValue();
            Warehouse needWarehouse = needHeap.removeMin().getValue();

            int surplus = surplusWarehouse.getCapacity() - 50;  // Surplus over the threshold
            int need = 50 - needWarehouse.getCapacity();  // Needed to reach the threshold

            // Allocate resources
            int transferAmount = Math.min(surplus, need);
            surplusWarehouse.setCapacity(surplusWarehouse.getCapacity() - transferAmount);
            needWarehouse.setCapacity(needWarehouse.getCapacity() + transferAmount);

            // Prepare transfer record
            Map<String, Object> transferRecord = new HashMap<>();
            transferRecord.put("From", "Warehouse " + surplusWarehouse.getId());
            transferRecord.put("To", "Warehouse " + needWarehouse.getId());
            transferRecord.put("Units", transferAmount);
            transfers.add(transferRecord);  // Add transfer record to the list

            // Reinsert updated warehouses into the heaps if needed
            if (surplusWarehouse.getCapacity() > 50) {
                surplusHeap.insert(surplusWarehouse.getCapacity(), surplusWarehouse);
            }
            if (needWarehouse.getCapacity() < 50) {
                needHeap.insert(needWarehouse.getCapacity(), needWarehouse);
            }
        }
        return transfers;  // Return the list of transfer records
    }
}
