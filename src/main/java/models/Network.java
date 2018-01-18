package models;


public class Network {
    private int id;
    private String name;
    private String availability;

    public Network(String name, String availability) {
        this.name = name;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Network network = (Network) o;

        if (id != network.id) return false;
        if (!name.equals(network.name)) return false;
        return availability != null ? availability.equals(network.availability) : network.availability == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (availability != null ? availability.hashCode() : 0);
        return result;
    }
}
