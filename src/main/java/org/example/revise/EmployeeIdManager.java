package org.example.revise;

import java.util.ArrayList;
import java.util.List;

public class EmployeeIdManager {

    public static void main(String[] args) {
        EmployeeIdManager manager = new EmployeeIdManager();
        manager.registerEmployee(50000, 75000);
        manager.registerEmployee(30000, 65000);
        manager.registerEmployee(70000, 80000);
        manager.registerEmployee(20000, 55000);
        manager.registerEmployee(40000, 70000);

        manager.findEmployee(30000);
        manager.findInRange(25000, 40000);
        manager.height();
        manager.pathToEmployee(20000);
        manager.displayEmployees();
    }

    private class Employee {
        int id;
        double salary;
        Employee leftEmployee;
        Employee rightEmployee;
        public Employee(int id, double salary) {
            this.id = id;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "ID=" + this.id + ", Salary=" + this.salary;
        }
    }

    private Employee root;

    public EmployeeIdManager() {
        this.root = null;
    }

    public void registerEmployee(int id, double salary) {
        var newNode = new Employee(id, salary);
        root = add(root, newNode);
        System.out.println("Registered: " + newNode);
    }

    private Employee add(Employee root, Employee key) {
        if(root == null) {
            return key;
        }

        if(key.id < root.id)  {
            root.leftEmployee = add(root.leftEmployee, key);
        } else if (key.id > root.id) {
            root.rightEmployee = add(root.rightEmployee, key);
        } else {
            root.salary = key.salary;
        }

        return root;
    }

    public void findEmployee(int id) {
        if (root == null) System.out.println("No employees found");
        var foundNode = dfs(root, id);
        if(foundNode != null) System.out.println("Found: " + foundNode);
    }

    private Employee dfs(Employee root, int id) {
        if(root == null) return null;

        if(root.id == id) return root;
        else if(id < root.id) return dfs(root.leftEmployee, id);
        else return dfs(root.rightEmployee, id);
    }

    public void findInRange(int low, int high) {
        List<Employee> output = new ArrayList<>();
        inOrder(root, low, high, output);
        String outputString = String.format("RANGE [%d-%d]: ", low, high);
        System.out.println(outputString + output);
    }

    private void inOrder(Employee root, int low, int high, List<Employee> output) {
        if(root == null) return;

        inOrder(root.leftEmployee, low, high, output);
        if(root.id >= low && root.id <= high) output.add(root);
        inOrder(root.rightEmployee, low, high, output);
    }

    public void height() {
        System.out.println("Tree height: " + calculateHeight(root));
    }

    private int calculateHeight(Employee root) {
        if (root == null) return -1;
        return 1 + Math.max(calculateHeight(root.leftEmployee), calculateHeight(root.rightEmployee));
    }

    public void displayEmployees() {
        List<Integer> output = new ArrayList<>();
        inOrder(root, output);
        System.out.println("All employees: " + output);
    }

    private void inOrder(Employee root, List<Integer> output) {
        if(root == null) return;
        inOrder(root.leftEmployee, output);
        output.add(root.id);
        inOrder(root.rightEmployee, output);
    }


    public void pathToEmployee(int id) {
        List<Integer> output = new ArrayList<>();
        preOrderSearch(root, id, output);

        int idPosition = output.indexOf(id);
        System.out.println("Path to " + id + " " + output.subList(0, idPosition + 1));
    }

    private void preOrderSearch(Employee root, int id, List<Integer> output) {
        if(root == null) return;

        output.add(root.id);
        preOrderSearch(root.leftEmployee, id, output);
        preOrderSearch(root.rightEmployee, id, output);
    }
}
