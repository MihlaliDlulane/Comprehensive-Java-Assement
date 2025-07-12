package Employee;
import java.util.*;

import Department.Department;

public class Employee implements Comparable<Employee>, Cloneable {
    private String id;
    private String name;
    private double salary;
    private Department department;
    private List<String> skills;

    // Constructor
    public Employee(String id, String name, double salary, Department department) {
        this.id = null;
        this.name = null;
        this.salary = 0;
        this.department = null;
        this.skills = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public List<String> getSkills() { return skills; }
    public void addSkill(String skill) { this.skills.add(skill); }

    // TODO: Implement equals() method
    @Override
    public boolean equals(Object obj) {
        // Implementation needed
       return false;
    }

    // TODO: Implement hashCode() method
    @Override
    public int hashCode() {

        return 0;
    }

    // TODO: Implement compareTo() method (compare by salary)
    @Override
    public int compareTo(Employee other) {
        // Implementation needed
        return 0;
    }

    // TODO: Implement toString() method
    @Override
    public String toString() {
        // Implementation needed
      return "";
    }

    // TODO: Implement shallow clone
    @Override
    public Employee clone() throws CloneNotSupportedException {
        // Implementation needed
        return null;
    }

    // TODO: Implement deep clone
    public Employee deepClone() {
        // Implementation needed
      return null;
    }
}