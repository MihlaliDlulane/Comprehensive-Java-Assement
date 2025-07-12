package Department;

public class Department {
    private String name;
    private String location;

    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return "Department{name='" + name + "', location='" + location + "'}";
    }

    // Clone method for deep cloning
    public Department clone() {
        return new Department(this.name, this.location);
    }
}