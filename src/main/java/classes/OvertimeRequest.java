package classes;

public class OvertimeRequest extends Employee {
    
    private double overtimeHours;
    private double overtimePay;
    private String status;
    
    OvertimeRequest(int employeeNumber, double overtimeHours, double overtimePay, String status){
        super(employeeNumber);
        this.overtimeHours = overtimeHours;
        this.overtimePay = overtimePay;
        this.status = status;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public double getOvertimePay() {
        return overtimePay;
    }

    public String getStatus() {
        return status;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public void setOvertimePay(double overtimePay) {
        this.overtimePay = overtimePay;
    }

    public void setStatus(String status) {
        this.status = status;
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
