package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemAdministrator extends Employee implements CSVHandler {
    
    private Login login;
    private static final String FILE_PATH = "src/main/java/databases/Employee Details.csv";
    
    public SystemAdministrator(int employeeNumber, String username, String password, String role){
        super(employeeNumber);
        this.login = new Login(username, password, role);
    }
    
    public boolean authenticate(String inputUsername, String inputPassword, String selectedRole) {
        Map<String, String[]> users = readCSV(FILE_PATH);

        if (users.containsKey(inputUsername)) {
            String[] userData = users.get(inputUsername);
            String storedPassword = userData[1].trim();
            String[] storedRoles = userData[2].trim().toLowerCase().split("\\|"); // Split multiple roles

            if (storedPassword.equals(inputPassword)) {
                for (String role : storedRoles) {
                    if (role.equals(selectedRole.trim().toLowerCase())) {
                        return true; // Valid role match
                    }
                }
            }
        }
        return false;
    }


    
    public String getRole() {
        return login.getRole();
    }
    
    public void createUser(String newUsername, String newPassword, String newRole) {
        System.out.println("User created: " + newUsername + " with role " + newRole);
    }

    public void deleteUser(String username) {
        System.out.println("User deleted: " + username);
    }

    public void updateUser(String username, String newRole) {
        System.out.println("Updated user: " + username + " to role " + newRole);
    }

    public void assignRole(String username, String newRole) {
        System.out.println("Assigned new role to " + username + ": " + newRole);
    }
    
    @Override
    public Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> userMap = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Read header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 22) { // Ensure all required columns exist
                    String empNumber = nextLine[0].trim();
                    String lastName = nextLine[1].trim();
                    String firstName = nextLine[2].trim();
                    String birthday = nextLine[3].trim();
                    String address = nextLine[4].trim();
                    String phoneNumber = nextLine[5].trim();
                    String sssNumber = nextLine[6].trim();
                    String philHealthNumber = nextLine[7].trim();
                    String tinNumber = nextLine[8].trim();
                    String pagIbigNumber = nextLine[9].trim();
                    String status = nextLine[10].trim();
                    String position = nextLine[11].trim();
                    String immediateSupervisor = nextLine[12].trim();
                    String basicSalary = nextLine[13].trim();
                    String riceSubsidy = nextLine[14].trim();
                    String phoneAllowance = nextLine[15].trim();
                    String clothingAllowance = nextLine[16].trim();
                    String grossSemiMonthlyRate = nextLine[17].trim();
                    String hourlyRate = nextLine[18].trim();
                    String username = nextLine[19].trim();
                    String password = nextLine[20].trim();
                    String role = nextLine[21].trim();

                    // Store full row data for CRUD operations
                    userMap.put(empNumber, nextLine); 
                    userMap.put(username, nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            Logger.getLogger(SystemAdministrator.class.getName()).log(Level.SEVERE, null, e);
        }
        return userMap;
    }

    
    @Override
    public void writeCSV(String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            writer.writeNext(new String[]{"Employee #", "Username", "Password", "Role"});

            // Example of writing a new user (Can be modified for actual CRUD operations)
            writer.writeNext(new String[]{"10035", "employee35", "password", "employee"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
