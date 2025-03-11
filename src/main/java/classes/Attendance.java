package classes;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private LocalDate date;
    private LocalTime login;
    private LocalTime logout;
    
    public Attendance(LocalDate date, LocalTime login, LocalTime logout){
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
}
