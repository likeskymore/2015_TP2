package src;
public class Warehouse implements Location {
    private int id;
    private int x;
    private int y;
    private int capacity;

    // Constructor
    public Warehouse(int id, int x, int y, int capacity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCapacity() {
        return capacity;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDetails() {
        return "Warehouse{" +
                "id=" + id +
                ", coordinates=(" + x + ", " + y + ")" +
                ", capacity=" + capacity +
                '}';
    }
}
