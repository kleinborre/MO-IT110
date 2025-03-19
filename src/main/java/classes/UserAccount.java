package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles read/write for User Accounts.csv
 * CSV Columns:
 *   0 - Employee Number
 *   1 - Username
 *   2 - Password
 *   3 - Role
 */
public class UserAccount implements CSVHandler {

    private static final String DATABASES_FOLDER = "databases";
    private static final String FILE_NAME = "User Accounts.csv";

    // Basic attributes for a single user
    private int employeeNumber;
    private String username;
    private String password;
    private String role;

    public UserAccount() {}

    public UserAccount(int employeeNumber) {
        this.employeeNumber = employeeNumber;
        loadUserData();
    }

    // ** Load from CSV using employeeNumber
    private void loadUserData() {
        List<String[]> allUsers = readCSV(getCSVFile().getPath());
        for (String[] user : allUsers) {
            if (user.length >= 4 && user[0].equals(String.valueOf(employeeNumber))) {
                this.username = user[1];
                this.password = user[2];
                this.role     = user[3];
                break;
            }
        }
    }

    /**
     * Authenticate by checking if input matches EITHER:
     *   - user[0] => Employee Number
     *   - user[1] => Username
     * Then compare password & role.
     */
    public static boolean authenticate(String input, String inputPassword, String selectedRole) {
        UserAccount userAccount = new UserAccount();
        List<String[]> allUsers = userAccount.readCSV(getCSVFile().getPath());

        for (String[] user : allUsers) {
            if (user.length >= 4) {
                String storedEmpNumber = user[0].trim();
                String storedUsername  = user[1].trim();
                String storedPassword  = user[2].trim();
                String[] storedRoles   = user[3].trim().toLowerCase().split("\\|");

                // Compare input to either employee number OR username
                boolean matchesIDOrUsername =
                    storedEmpNumber.equalsIgnoreCase(input) ||
                    storedUsername.equalsIgnoreCase(input);

                if (matchesIDOrUsername && storedPassword.equals(inputPassword)) {
                    // Next, check role
                    for (String role : storedRoles) {
                        if (role.equalsIgnoreCase(selectedRole)) {
                            return true; // Auth success
                        }
                    }
                }
            }
        }
        return false; // Auth fail
    }


    // ** CSV read/write **

    public static File getCSVFile() {
        return new File(DATABASES_FOLDER + File.separator + FILE_NAME);
    }

    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> users = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 4) {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException ex) {
            Logger.getLogger(UserAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = {"Employee Number", "Username", "Password", "Role"};
            writer.writeNext(header);
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ** Getters **
    public int getEmployeeNumber()  { return employeeNumber; }
    public String getUsername()     { return username; }
    public String getPassword()     { return password; }
    public String getRole()         { return role; }
}