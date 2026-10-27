
package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/jdbc";
        String user = "root";
        String password = "root";

        try (
                Connection con = DriverManager.getConnection(
                        url, user, password);
                Scanner sc = new Scanner(System.in)
        ) {
            System.out.println("Database connected!");

            Statement st = con.createStatement();

            while (true) {
                System.out.println("\n===== STUDENT MANAGEMENT =====");
                System.out.println("1. Add Student");
                System.out.println("2. Get All Students");
                System.out.println("3. Get Student By ID");
                System.out.println("4. Update Student");
                System.out.println("5. Delete Student");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    // CREATE
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter age: ");
                        int age = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter gender: ");
                        String gender = sc.nextLine();

                        System.out.print("Enter Aadhar (12 digits): ");
                        String aadhar = sc.nextLine();

                        if (!aadhar.matches("\\d{12}")) {
                            System.out.println(
                                    "Aadhar must contain 12 digits.");
                            break;
                        }

                        String insert = "INSERT INTO student " +
                                "(name, age, gender, aadhar) " +
                                "VALUES (?, ?, ?, ?)";

                        try (PreparedStatement ps =
                                     con.prepareStatement(insert)) {
                            ps.setString(1, name);
                            ps.setInt(2, age);
                            ps.setString(3, gender);
                            ps.setString(4, aadhar);

                            ps.executeUpdate();
                            System.out.println("Student added!");
                        }
                        break;

                    // READ ALL
                    case 2:
                        ResultSet rs = st.executeQuery(
                                "SELECT * FROM student");

                        while (rs.next()) {
                            System.out.println(
                                    rs.getInt("id") + " | " +
                                            rs.getString("name") + " | " +
                                            rs.getInt("age") + " | " +
                                            rs.getString("gender"));
                        }
                        rs.close();
                        break;

                    // READ BY ID
                    case 3:
                        System.out.print("Enter student ID: ");
                        int id = Integer.parseInt(sc.nextLine());

                        try (PreparedStatement ps =
                                     con.prepareStatement(
                                             "SELECT * FROM student WHERE id = ?")) {
                            ps.setInt(1, id);

                            try (ResultSet result = ps.executeQuery()) {
                                if (result.next()) {
                                    System.out.println(
                                            "ID: " + result.getInt("id"));
                                    System.out.println(
                                            "Name: " + result.getString("name"));
                                    System.out.println(
                                            "Age: " + result.getInt("age"));
                                    System.out.println(
                                            "Gender: " + result.getString("gender"));
                                } else {
                                    System.out.println("Student not found!");
                                }
                            }
                        }
                        break;

                    // UPDATE
                    case 4:
                        System.out.print("Enter ID to update: ");
                        int updateId =
                                Integer.parseInt(sc.nextLine());

                        System.out.print("Enter new name: ");
                        String newName = sc.nextLine();

                        System.out.print("Enter new age: ");
                        int newAge = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter new gender: ");
                        String newGender = sc.nextLine();

                        System.out.print("Enter new Aadhar: ");
                        String newAadhar = sc.nextLine();

                        if (!newAadhar.matches("\\d{12}")) {
                            System.out.println(
                                    "Aadhar must contain 12 digits.");
                            break;
                        }

                        try (PreparedStatement ps =
                                     con.prepareStatement(
                                             "UPDATE student SET name=?, age=?, " +
                                                     "gender=?, aadhar=? WHERE id=?")) {
                            ps.setString(1, newName);
                            ps.setInt(2, newAge);
                            ps.setString(3, newGender);
                            ps.setString(4, newAadhar);
                            ps.setInt(5, updateId);

                            int rows = ps.executeUpdate();

                            System.out.println(rows > 0
                                    ? "Student updated!"
                                    : "Student ID not found.");
                        }
                        break;

                    // DELETE
                    case 5:
                        System.out.print("Enter ID to delete: ");
                        int deleteId =
                                Integer.parseInt(sc.nextLine());

                        try (PreparedStatement ps =
                                     con.prepareStatement(
                                             "DELETE FROM student WHERE id = ?")) {
                            ps.setInt(1, deleteId);

                            int rows = ps.executeUpdate();

                            System.out.println(rows > 0
                                    ? "Student deleted!"
                                    : "Student ID not found.");
                        }
                        break;

                    case 6:
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
}
