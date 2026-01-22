package org.example.revise;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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

        manager.deleteEmployee(20000);

        manager.displayEmployeesAtSameLevel();
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
        if(root == null) return key;

        if(key.id < root.id) root.leftEmployee = add(root.leftEmployee, key);
        else if (key.id > root.id) root.rightEmployee = add(root.rightEmployee, key);
        else root.salary = key.salary;

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

    public void deleteEmployee(int id) {
        delete(root, id);
        System.out.println("Deleted Employee: " + id);
        displayEmployees();
    }

    public Employee delete(Employee root, int id) {
        if(root == null) return null;

        if(id < root.id)
            root.leftEmployee = delete(root.leftEmployee, id);
        else if (id > root.id) {
            root.rightEmployee = delete(root.rightEmployee, id);
        } else {
            // Case 1: No children
            if(root.leftEmployee == null && root.rightEmployee == null) return null;

            // Case 2: One child
            if(root.leftEmployee == null) return root.rightEmployee;
            if(root.rightEmployee == null) return root.leftEmployee;

            // Case 3: Both children
            // Find the minimum in the right tree and replace with key then make successor null;
            Employee successor = findMinimum(root.rightEmployee);
            root.id = successor.id;
            root.rightEmployee = delete(root.rightEmployee, successor.id);
        }

        return root;
    }

    public Employee findMinimum(Employee root) {
        Employee currentNode = root;
        while (currentNode.leftEmployee != null) {
            currentNode = currentNode.leftEmployee;
        }
        return currentNode;
    }

    public void displayEmployeesAtSameLevel() {
        List<List<Employee>> output = new ArrayList<>();
        levelOrderTraversal(root, output);
        System.out.println("Employees at same level: " + output);
    }

    private void levelOrderTraversal(Employee root, List<List<Employee>> output) {
        if(root == null) return;

        Queue<Employee> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Employee> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                Employee currentEmployee = queue.remove();
                level.add(currentEmployee);

                if (currentEmployee.rightEmployee != null) queue.add(currentEmployee.rightEmployee);
                if(currentEmployee.leftEmployee != null) queue.add(currentEmployee.leftEmployee);
            }
            output.add(level);
        }
    }

    public void addEmployeePractice(int id, double salary) {
        var newEmployee = new Employee(id, salary);
        root = addPractice(root, newEmployee);
    }

    private Employee addPractice(Employee root, Employee newNode) {
        if(root == null) {
            return newNode;
        }

        if(newNode.id < root.id) root.leftEmployee = addPractice(root.leftEmployee, newNode);
        else if(newNode.id > root.id) root.rightEmployee = addPractice(root.rightEmployee, newNode);
        else root.salary = newNode.salary;

        return root;
    }
}
