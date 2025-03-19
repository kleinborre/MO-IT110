package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages system-level user creation, deletion, updates
 * Splits data between Employee Details.csv (personal data) & User Accounts.csv (login credentials).
 */
public class SystemAdministrator extends Employee {

    public SystemAdministrator(int employeeNumber, String username, String password, String role) {
        super(employeeNumber);
        // The old "Login" reference can remain, but now we store actual credentials in User Accounts.csv
        }

    public SystemAdministrator() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
        // ** CREATE USER **
        // newUserData has 22 columns in the UI:
        //  - 0..18 => employee details
        //  - 19..21 => username, password, role
    public void createUser(String[] newUserData) {
        // 1) Insert or update Employee Details (0..18)
        createOrUpdateEmployeeDetails(newUserData);

        // 2) Insert or update User Accounts (employeeNumber, username, password, role)
        String[] userRow = new String[4];
        userRow[0] = newUserData[0];  // Employee Number
        userRow[1] = newUserData[19]; // Username
        userRow[2] = newUserData[20]; // Password
        userRow[3] = newUserData[21]; // Role

        UserAccount userAccount = new UserAccount();
        List<String[]> allUsers = userAccount.readCSV(UserAccount.getCSVFile().getPath());

        // Check if user with this employeeNumber already exists
        for (String[] row : allUsers) {
            if (row[0].equals(userRow[0])) {
                JOptionPane.showMessageDialog(null, "Employee ID already exists in User Accounts!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        allUsers.add(userRow);
        userAccount.writeCSV(UserAccount.getCSVFile().getPath(), allUsers);

        JOptionPane.showMessageDialog(null, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // ** DELETE USER ** Now removes from both Employee Details and User Accounts CSV
    public void deleteUser(String empNumber) {
        // Remove from User Accounts.csv
        UserAccount userAccount = new UserAccount();
        List<String[]> allUsers = userAccount.readCSV(UserAccount.getCSVFile().getPath());

        boolean userRemoved = allUsers.removeIf(row -> row[0].equals(empNumber));
        if (userRemoved) {
            userAccount.writeCSV(UserAccount.getCSVFile().getPath(), allUsers);
        }

        // Remove from Employee Details.csv
        List<String[]> allEmployees = readCSV(Employee.getCSVFile().getPath());

        boolean employeeRemoved = allEmployees.removeIf(row -> row[0].equals(empNumber));
        if (employeeRemoved) {
            writeCSV(Employee.getCSVFile().getPath(), allEmployees);
        }

        // Show success message if both records were removed
        if (userRemoved && employeeRemoved) {
            JOptionPane.showMessageDialog(null, "User deleted successfully from both Employee Details and User Accounts!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (userRemoved) {
            JOptionPane.showMessageDialog(null, "User deleted from User Accounts, but not found in Employee Details!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (employeeRemoved) {
            JOptionPane.showMessageDialog(null, "User deleted from Employee Details, but not found in User Accounts!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "User not found in either CSV file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ** UPDATE USER **
    // updatedData has 22 columns, but we only update columns 0..18 in Employee CSV and 19..21 in User Accounts CSV
    public void updateUser(String employeeNumber, String[] updatedData) {
        boolean employeeUpdated = false;
        boolean userUpdated = false;

        // Update Employee Details CSV
        List<String[]> employees = readCSV(Employee.getCSVFile().getPath());
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i)[0].equals(employeeNumber)) {
                // Update employee data (first 19 columns)
                for (int j = 0; j < 19; j++) {
                    employees.get(i)[j] = updatedData[j];
                }
                employeeUpdated = true;
                break;
            }
        }
        writeCSV(Employee.getCSVFile().getPath(), employees);

        // Update User Accounts CSV
        UserAccount userAccount = new UserAccount();
        List<String[]> users = userAccount.readCSV(UserAccount.getCSVFile().getPath());
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i)[0].equals(employeeNumber)) {
                // Update user data (last 3 columns: username, password, role)
                users.get(i)[1] = updatedData[19]; // Username
                users.get(i)[2] = updatedData[20]; // Password
                users.get(i)[3] = updatedData[21]; // Role
                userUpdated = true;
                break;
            }
        }
        userAccount.writeCSV(UserAccount.getCSVFile().getPath(), users);

        // Show appropriate message
        if (employeeUpdated && userUpdated) {
            JOptionPane.showMessageDialog(null, "User updated successfully in both CSV files!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (employeeUpdated) {
            JOptionPane.showMessageDialog(null, "Employee details updated, but User Account not found!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (userUpdated) {
            JOptionPane.showMessageDialog(null, "User Account updated, but Employee Details not found!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Update failed: Employee not found in either CSV file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ** CREATE OR UPDATE EMPLOYEE DETAILS.CSV (for columns 0..18) **
    private void createOrUpdateEmployeeDetails(String[] data) {
        // data has 22 columns, but columns 0..18 are for Employee
        // If the employeeNumber already exists, we update. Otherwise, we create a new row.
        List<String[]> employees = readCSV(Employee.getCSVFile().getPath());
        boolean updated = false;

        // Build employee row (19 columns)
        String[] employeeRow = new String[19];
        for (int i = 0; i < 19; i++) {
            employeeRow[i] = data[i];
        }

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).length >= 19 && employees.get(i)[0].equals(data[0])) {
                // Update existing employee
                employees.set(i, employeeRow);
                updated = true;
                break;
            }
        }

        if (!updated) {
            // Create new employee row
            employees.add(employeeRow);
        }

        // Write updated employees back
        writeCSV(Employee.getCSVFile().getPath(), employees);
    }

    // ** For the system to display "all users" in a single table (22 columns)
    //   We do an in-memory join of Employee (0..18) + User (19..21).
    public String[][] getAllUsers() {
        // 1) Load all employees
        List<String[]> empList = readCSV(Employee.getCSVFile().getPath()); // 19 columns
        // 2) Load all user accounts
        UserAccount userAccount = new UserAccount();
        List<String[]> userList = userAccount.readCSV(UserAccount.getCSVFile().getPath()); // 4 columns

        List<String[]> combined = new ArrayList<>();

        // For each employee, see if there's a matching user account
        // Then combine into a single 22-column row
        for (String[] empRow : empList) {
            if (empRow.length < 19) continue;
            String empNumber = empRow[0];

            // Default placeholders for username/password/role
            String username = "";
            String password = "";
            String role     = "";

            for (String[] userRow : userList) {
                if (userRow.length >= 4 && userRow[0].equals(empNumber)) {
                    username = userRow[1];
                    password = userRow[2];
                    role     = userRow[3];
                    break;
                }
            }
            // Build a 22-column array
            String[] combinedRow = new String[22];
            // Copy employee columns
            for (int i = 0; i < 19; i++) {
                combinedRow[i] = empRow[i];
            }
            // Fill last 3 columns
            combinedRow[19] = username;
            combinedRow[20] = password;
            combinedRow[21] = role;

            combined.add(combinedRow);
        }

        // Convert to 2D array
        String[][] result = new String[combined.size()][22];
        for (int i = 0; i < combined.size(); i++) {
            result[i] = combined.get(i);
        }
        return result;
    }

    // ** For retrieving a single "joined" user row by employeeNumber (22 columns)
    public String[] getUserByEmployeeNumber(String employeeNumber) {
        String[][] all = getAllUsers();
        for (String[] row : all) {
            if (row.length == 22 && row[0].equals(employeeNumber)) {
                return row;
            }
        }
        return null;
    }
}