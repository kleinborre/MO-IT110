package classes;

import java.time.LocalDate;

public class LeaveRequest extends Employee implements CSVHandler {
    
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    
    LeaveRequest(int employeeNumber, String leaveType, 
            LocalDate startDate, LocalDate endDate, String status){
        super(employeeNumber);
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    
    public String getLeaveType(){
        return leaveType;
    }
    public LocalDate getStartDate(){
        return startDate;
    }
    
    public LocalDate getEndDate(){
        return endDate;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setLeaveType(String leaveType){
        this.leaveType = leaveType;
    }
    
    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }
    
    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    @Override
    public void readFromCSV(String filePath) {
        filePath = "";
        System.out.println(filePath);
    }

    @Override
    public void writeToCSV(String filePath) {
        filePath = "";
        System.out.println(filePath);
    }
}
