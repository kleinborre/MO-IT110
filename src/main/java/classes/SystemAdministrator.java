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

    // ** Constructor **
    public SystemAdministrator(int employeeNumber, String username, String password, String role) {
        super(employeeNumber);
        this.login = new Login(username, password, role);
    }

    // ** Get Role **
    public String getRole() {
        return login.getRole();
    }

    // ** Authenticate User (Reuses readCSV Result) **
    public boolean authenticate(String inputUsername, String inputPassword, String selectedRole) {
        List<String[]> allUsers = readCSV(FILE_PATH);
        
        for (String[] userData : allUsers) {
            if (userData.length >= 22) {
                String storedUsername = userData[19].trim();
                String storedPassword = userData[20].trim();
                String[] storedRoles = userData[21].trim().toLowerCase().split("\\|");

                if (storedUsername.equals(inputUsername) && storedPassword.equals(inputPassword)) {
                    return Arrays.asList(storedRoles).contains(selectedRole.trim().toLowerCase());
                }
            }
        }
        return false;
    }

    // ** Check if Employee Exists **
    private boolean userExists(String empNumber, List<String[]> allUsers) {
        return allUsers.stream().anyMatch(user -> user[0].equals(empNumber));
    }

    // ** Find Employee Index in List **
    private int findUserIndex(String empNumber, List<String[]> allUsers) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i)[0].equals(empNumber)) return i;
        }
        return -1; // Not found
    }

    // ** Create User (Encapsulated Logic) **
    public void createUser(String[] newUser) {
        List<String[]> allUsers = readCSV(FILE_PATH);
        
        if (userExists(newUser[0], allUsers)) {
            JOptionPane.showMessageDialog(null, "Employee ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        allUsers.add(newUser);
        writeCSV(FILE_PATH, allUsers);
        System.out.println("User " + newUser[0] + " created successfully.");
    }

    // ** Delete User (Uses Encapsulated Methods) **
    public void deleteUser(String empNumber) {
        List<String[]> allUsers = readCSV(FILE_PATH);
        int index = findUserIndex(empNumber, allUsers);

        if (index != -1) {
            allUsers.remove(index);
            writeCSV(FILE_PATH, allUsers);
            JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ** Update User (Uses Encapsulated Methods) **
    public void updateUser(String employeeID, String[] updatedUser) {
        List<String[]> allUsers = readCSV(FILE_PATH);
        int index = findUserIndex(employeeID, allUsers);

        if (index != -1) {
            allUsers.set(index, updatedUser); // Update matching row
            writeCSV(FILE_PATH, allUsers);
            System.out.println("User " + employeeID + " updated successfully.");
        } else {
            System.out.println("Error: Employee ID " + employeeID + " not found!");
        }
    }

    // ** Read CSV (Handles Empty File) **
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> usersList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext(); // Skip header
            if (header == null) return usersList; // If file is empty

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                usersList.add(nextLine);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    // ** Write CSV (Prevents Empty Files) **
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        if (data.isEmpty()) {
            System.err.println("No data to write!");
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) { // "false" ensures overwrite
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

    // ** Get All Users for Table Display **
    public String[][] getAllUsers() {
        List<String[]> usersList = readCSV(FILE_PATH);
        return usersList.toArray(new String[0][0]);
    }
}