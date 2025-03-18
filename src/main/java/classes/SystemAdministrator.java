package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import javax.swing.JOptionPane;

public class SystemAdministrator extends Employee implements CSVHandler {

    private Login login;
    private static final String DATABASES_FOLDER = "databases";
    private static final String FILE_NAME = "Employee Details.csv";

    // ** Constructor **
    public SystemAdministrator(int employeeNumber, String username, String password, String role) {
        super(employeeNumber);
        this.login = new Login(username, password, role);
    }

    // ** Get Role **
    public String getRole() {
        return login.getRole();
    }

    public static File getCSVFile() {
        return new File("databases/Employee Details.csv");
    }

    // ** Authenticate User (Reuses readCSV Result) **
    public boolean authenticate(String inputUsername, String inputPassword, String selectedRole) {
        List<String[]> allUsers = readCSV(getCSVFile().getPath());
        
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
        List<String[]> allUsers = readCSV(getCSVFile().getPath());
        
        if (userExists(newUser[0], allUsers)) {
            JOptionPane.showMessageDialog(null, "Employee ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        allUsers.add(newUser);
        writeCSV(getCSVFile().getPath(), allUsers);
        System.out.println("User " + newUser[0] + " created successfully.");
    }

    // ** Delete User (Uses Encapsulated Methods) **
    public void deleteUser(String empNumber) {
        List<String[]> allUsers = readCSV(getCSVFile().getPath());
        int index = findUserIndex(empNumber, allUsers);

        if (index != -1) {
            allUsers.remove(index);
            writeCSV(getCSVFile().getPath(), allUsers);
            JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ** Update User (Uses Encapsulated Methods) **
    public void updateUser(String employeeNumber, String[] updatedData) {
        List<String[]> employees = readCSV(Employee.getCSVFile().getPath()); // Read all employees
        boolean isUpdated = false;

        for (int i = 0; i < employees.size(); i++) {
            String[] empData = employees.get(i);

            // Find the correct employee record by employeeNumber
            if (empData.length >= 22 && empData[0].equals(employeeNumber)) {
                employees.set(i, updatedData); // Update the row in the list
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            writeCSV(Employee.getCSVFile().getPath(), employees); // Write updated list back to CSV
            System.out.println("Employee data updated successfully in CSV.");
        } else {
            System.err.println("Employee update failed: Employee not found.");
        }
    }

    // ** Write CSV (Prevents Empty Files) **
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        if (data.isEmpty()) {
            System.err.println("No data to write!");
            return;
        }

        File csvFile = getCSVFile();
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false))) { // "false" ensures overwrite
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
        List<String[]> usersList = readCSV(getCSVFile().getPath());
        return usersList.toArray(new String[0][0]);
    }
    
    // ** Fetch user data by employee number **
    public String[] getUserByEmployeeNumber(String employeeNumber) {
        List<String[]> employees = readCSV(getCSVFile().getPath());
    
        for (String[] empData : employees) {
            if (empData.length >= 22 && empData[0].equals(employeeNumber)) {
                return empData; // Return the employee's data
            }
        }
        return null; // Not found
    }

}