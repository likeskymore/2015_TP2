package DisjointSets;

// A Java program to implement Disjoint Set Data Structure.
public class DisjointUnionSets { 
    private int[] rank;     // Ranks of each set (used for union by rank)
    private int[] parent;   // Parent array for the disjoint set
    private int n;          // Number of elements in the sets
  
    // Constructor to initialize the disjoint set with n elements
    public DisjointUnionSets(int n) { 
        if (n <= 0) {
            throw new IllegalArgumentException("Number of elements must be positive.");
        }
        this.n = n; 
        rank = new int[n]; 
        parent = new int[n]; 
        makeSet(); 
    } 
  
    // Initializes each element to be in its own set
    private void makeSet() { 
        for (int i = 0; i < n; i++) { 
            parent[i] = i; // Each element is its own parent (root)
        } 
    } 

    // Validates if an element index is within bounds
    private void validate(int x) {
        if (x < 0 || x >= n) {
            throw new IllegalArgumentException("Element out of bounds: " + x);
        }
    }
    
    // Finds the representative (root) of the set containing element x
    // Implements path compression for efficiency
    public int find(int x) {
        validate(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }
    
    // Unites the sets containing elements x and y
    // Uses union by rank to maintain balanced trees
    public void union(int x, int y) {
        validate(x);
        validate(y);
        int xRoot = find(x);
        int yRoot = find(y);

        // If both elements are already in the same set, do nothing
        if (xRoot == yRoot) {
            return;
        }
    
        // Union by rank: attach the smaller tree under the larger tree
        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
        } else if (rank[yRoot] < rank[xRoot]) {
            parent[yRoot] = xRoot;
        } else {
            parent[yRoot] = xRoot;
            rank[xRoot]++;
        }
    }

    // Returns the total number of elements in the disjoint set
    public int size() {
        return n;
    }

    // Checks if two elements belong to the same set
    public boolean isConnected(int x, int y) {
        validate(x);
        validate(y);
        return find(x) == find(y);
    }
}