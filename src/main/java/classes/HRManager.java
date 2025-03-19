package classes;

public class HRManager extends Employee {
    
    public HRManager(int employeeNumber) {
        super(employeeNumber);
    }
    
    public static void approveLeaveRequest(int employeeNumber, String leaveType, String startDate) {
        LeaveRequest.updateLeaveStatus(employeeNumber, leaveType, startDate, "Approved");
    }
    
    public static void rejectLeaveRequest(int employeeNumber, String leaveType, String startDate) {
        LeaveRequest.updateLeaveStatus(employeeNumber, leaveType, startDate, "Rejected");
    }
    
    public static void approveOvertimeRequest(int employeeNumber, String date) {
        OvertimeRequest.updateOvertimeStatus(employeeNumber, date, "Approved");
    }
    
    public static void rejectOvertimeRequest(int employeeNumber, String date) {
        OvertimeRequest.updateOvertimeStatus(employeeNumber, date, "Rejected");
    }
}
