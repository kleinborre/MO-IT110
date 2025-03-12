package classes;

public class Login {
    
    private String username;
    private String password;
    private String role;
    
    Login(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public boolean login (String username, String password){
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public void logout(){
        System.out.println(username + " has logged out.");
    }
    
    public String getPermissions(){
        return "Permission for role: " + role;
    }
    
    public String getRole(){
        return role;
    }
    
}
