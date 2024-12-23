package Graph;
import src.Location;

public class LocationVertex implements Vertex<Location> {
    private Location location;

    public LocationVertex(Location location) {
        this.location = location;
    }

    @Override
    public Location getElement() {
        return location;
    }
}