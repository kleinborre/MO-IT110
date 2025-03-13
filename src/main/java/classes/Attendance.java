package classes;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance extends Employee {
    private LocalDate date;
    private LocalTime login;
    private LocalTime logout;
    
    public Attendance(int employeeNumber, LocalDate date, LocalTime login, LocalTime logout){
        super(employeeNumber);
        this.date = date;
        this.login = login;
        this.logout = logout;
    }
    
    public LocalDate getDate(){
        return date;
    }
    
    public LocalTime getLogin(){
        return login;
    }
    
    public LocalTime getLogout(){
        return logout;
    }
    
    public void setDate(LocalDate date){
        this.date = date;
    }
    
    public void setLogin(LocalTime login){
        this.login = login;
    }
    
    public void setLogout(LocalTime logout){
        this.logout = logout;
    }

//    @Override
//    public void readFromCSV(String filePath) {
//        filePath = "";
//        System.out.println(filePath);
//    }
//
//    @Override
//    public void writeToCSV(String filePath) {
//        filePath = "";
//        System.out.println(filePath);
//    }
    
    
}
