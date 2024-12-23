package src;
public class City implements Location{
    private int id;
    private int x;
    private int y;
    private int demand;
    private String priority;

    // Constructor
    public City(int id, int x, int y, int demand, String priority) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.priority = priority;
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

    public int getDemand() {
        return demand;
    }

    public String getPriority() {
        return priority;
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

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDetails() {
        return "City{" +
                "id=" + id +
                ", coordinates=(" + x + ", " + y + ")" +
                ", demand=" + demand +
                ", priority='" + priority + '\'' +
                '}';
    }
}