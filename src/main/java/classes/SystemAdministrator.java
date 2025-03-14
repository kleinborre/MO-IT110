package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class SystemAdministrator extends Employee implements CSVHandler {
    
    private Login login;
    private static final String FILE_PATH = "src/main/java/databases/Employee Details.csv";

    // Constructor
    public SystemAdministrator(int employeeNumber, String username, String password, String role) {
        super(employeeNumber);  // Inherit from Employee
        this.login = new Login(username, password, role);
    }

    public String getRole() {
        return login.getRole();
    }

    // **🔹 Authenticate User**
    public boolean authenticate(String inputUsername, String inputPassword, String selectedRole) {
        Map<String, String[]> users = readCSV(FILE_PATH);
        if (users.containsKey(inputUsername)) {
            String[] userData = users.get(inputUsername);
            String storedPassword = userData[20].trim();
            String[] storedRoles = userData[21].trim().toLowerCase().split("\\|");

            if (storedPassword.equals(inputPassword)) {
                for (String role : storedRoles) {
                    if (role.equals(selectedRole.trim().toLowerCase())) {
                        return true; // Role match
                    }
                }
            }
        }
        return false;
    }

    // **🔹 Create User**
    public void createUser(String[] newUser) {
        List<String[]> allUsers = readAllUsers();

        // Check if Employee ID already exists
        for (String[] user : allUsers) {
            if (user[0].equals(newUser[0])) {
                JOptionPane.showMessageDialog(null, "Employee ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        allUsers.add(newUser);
        writeCSV(FILE_PATH, allUsers);
        System.out.println("User " + newUser[0] + " created successfully.");
    }

    // **🔹 Delete User**
    public void deleteUser(String empNumber) {
        List<String[]> allUsers = readAllUsers();
        boolean userFound = false;

        Iterator<String[]> iterator = allUsers.iterator();
        while (iterator.hasNext()) {
            String[] user = iterator.next();
            if (user[0].equals(empNumber)) {
                iterator.remove();
                userFound = true;
                break;
            }
        }

        if (userFound) {
            writeCSV(FILE_PATH, allUsers);
            JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // **🔹 Update User**
    public void updateUser(String employeeID, String[] updatedUser) {
        List<String[]> allUsers = readAllUsers();
        boolean found = false;

        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i)[0].equals(employeeID)) {
                allUsers.set(i, updatedUser); // Update matching row
                found = true;
                break;
            }
        }

        if (found) {
            writeCSV(FILE_PATH, allUsers);
            System.out.println("User " + employeeID + " updated successfully.");
        } else {
            System.out.println("Error: Employee ID " + employeeID + " not found!");
        }
    }

    // **🔹 Assign Role**
    public void assignRole(String username, String newRole) {
        System.out.println("Assigned new role to " + username + ": " + newRole);
    }

    // **🔹 Read CSV and Return Map**
    @Override
    public Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> userMap = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 22) { // Ensure all columns exist
                    userMap.put(nextLine[0], nextLine); // Store by Employee Number
                    userMap.put(nextLine[19], nextLine); // Store by Username
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return userMap;
    }

    // **🔹 Write Data to CSV**
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {
                "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
                "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position",
                "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate",
                "Username", "Password", "Role"
            };
            writer.writeNext(header);
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // **🔹 Read All Users from CSV**
    public List<String[]> readAllUsers() {
        List<String[]> usersList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            reader.readNext(); // Skip header
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                usersList.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    // **🔹 Get All Users as Table Data**
    public String[][] getAllUsers() {
        List<String[]> usersList = readAllUsers();
        return usersList.toArray(new String[0][0]);
    }
}