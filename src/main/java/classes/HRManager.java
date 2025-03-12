package classes;

public class HRManager extends Employee {
    
    HRManager(int employeeNumber) {
        super(employeeNumber);
    }
    
    public void approveLeaveRequest(int employeeNumber){
        System.out.println("Leave request approved for Employee #" + employeeNumber);
    }
    
    public void rejectLeaveRequest(int employeeNumber){
        System.out.println("Leave request rejected for Employee #" + employeeNumber);
    }
    
    public void approveOvertimeRequest(int employeeNumber){
        System.out.println("Overtime request approved for Employee #" + employeeNumber);
    }
    
    public void rejectOvertimeRequest(int employeeNumber){
        System.out.println("Overtime request rejected for Employee #" + employeeNumber);
    }
    
}