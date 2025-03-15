package classes;

interface UserAuthentication {
    boolean login(String username, String password, String role);
    void logout();
    String getPermissions();
}
