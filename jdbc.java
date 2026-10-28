package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

    static final String URL =
            "jdbc:mysql://localhost:3306/curd";

    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) {

        try (Connection cn = DriverManager.getConnection(
                URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Database connected successfully!");

            while (true) {

                System.out.println("\n===== EMPLOYEE CRUD OPERATIONS =====");
                System.out.println("1. Insert Employee");
                System.out.println("2. Display Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        insertEmployee(cn, sc);
                        break;

                    case 2:
                        displayEmployees(cn);
                        break;

                    case 3:
                        updateEmployee(cn, sc);
                        break;

                    case 4:
                        deleteEmployee(cn, sc);
                        break;

                    case 5:
                        System.out.println("Program closed.");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CREATE: Insert an employee
    static void insertEmployee(Connection cn, Scanner sc)
            throws Exception {

        System.out.print("Enter employee name: ");
        String name = sc.nextLine();

        System.out.print("Enter employee salary: ");
        int salary = sc.nextInt();
        sc.nextLine();

        String sql =
                "INSERT INTO employee (name, salary) VALUES (?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, salary);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee inserted successfully!");
            }
        }
    }

    // READ: Display all employees
    static void displayEmployees(Connection cn)
            throws Exception {

        String sql = "SELECT * FROM employee";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nID\tNAME\t\tSALARY");
            System.out.println("--------------------------------");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        rs.getInt("id") + "\t" +
                                rs.getString("name") + "\t\t" +
                                rs.getInt("salary")
                );
            }

            if (!found) {
                System.out.println("No employee records found.");
            }
        }
    }

    // UPDATE: Modify an employee
    static void updateEmployee(Connection cn, Scanner sc)
            throws Exception {

        System.out.print("Enter employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new employee name: ");
        String name = sc.nextLine();

        System.out.print("Enter new salary: ");
        int salary = sc.nextInt();
        sc.nextLine();

        String sql =
                "UPDATE employee SET name = ?, salary = ? WHERE id = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, salary);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        }
    }

    // DELETE: Remove an employee
    static void deleteEmployee(Connection cn, Scanner sc)
            throws Exception {

        System.out.print("Enter employee ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM employee WHERE id = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        }
    }
}