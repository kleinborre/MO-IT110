package classes;

public class SystemAdministrator extends Employee implements CSVHandler {
    
    private Login login;
    
    SystemAdministrator(int employeeNumber, String username, String password, String role){
        super(employeeNumber);
        this.login = new Login(username, password, role);
    }
    
    public boolean authenticate(String username, String password) {
        return login.login(username, password);
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
    public void readFromCSV(String filePath) {
        System.out.println("Reading user data from CSV file: " + filePath);
    }

    @Override
    public void writeToCSV(String filePath) {
        System.out.println("Writing user data to CSV file: " + filePath);
    }
    
}
