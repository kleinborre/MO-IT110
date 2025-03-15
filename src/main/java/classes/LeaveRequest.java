package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LeaveRequest class that manages employee leave requests and handles CSV operations.
 */
public class LeaveRequest extends Employee implements CSVHandler {
    
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    
    private static final String FILE_PATH = "src/main/java/databases/Leave Requests.csv";

    /**
     * Constructor for LeaveRequest.
     */
    public LeaveRequest(int employeeNumber, String leaveType, 
                        LocalDate startDate, LocalDate endDate, String status) {
        super(employeeNumber);
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    /** Getters */
    public String getLeaveType() {
        return leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    /** Setters */
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Reads all leave requests from the CSV file.
     */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> leaveRequests = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 6) { // Ensure correct column count
                    leaveRequests.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return leaveRequests;
    }

    /**
     * Writes all leave requests back to the CSV file.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = {"EmployeeNumber", "Name", "LeaveType", "StartDate", "EndDate", "Status"};
            writer.writeNext(header); // Write header only once
            writer.writeAll(data);    // Write all records
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a new leave request and writes it to the CSV file.
     */
    public void submitLeaveRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH);

        // Get employee's full name
        String employeeName = getFullName();

        // Create new leave request entry
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), 
            employeeName, 
            leaveType, 
            startDate.toString(), 
            endDate.toString(), 
            status
        };

        allRequests.add(newRequest); // Add new request

        writeCSV(FILE_PATH, allRequests); // Overwrite file with updated list
        System.out.println("Leave request submitted successfully.");
    }

    /**
     * Cancels a leave request by removing it from the CSV file.
     */
    public void deleteLeaveRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH);
        List<String[]> updatedRequests = new ArrayList<>();
        boolean isDeleted = false;

        for (String[] request : allRequests) {
            if (request[0].equals(String.valueOf(getEmployeeNumber())) && request[2].equals(leaveType) && request[3].equals(startDate.toString())) {
                isDeleted = true; // Found and removed this request
                continue;
            }
            updatedRequests.add(request);
        }

        writeCSV(FILE_PATH, updatedRequests); // Overwrite file with updated data

        if (isDeleted) {
            System.out.println("Leave request cancelled successfully.");
        } else {
            System.out.println("No matching leave request found.");
        }
    }
    
    /**
    * Retrieves all leave requests for HR Manager to review.
    */
    public static List<String[]> getAllLeaveRequests() {
        LeaveRequest leaveRequestInstance = new LeaveRequest(0, "", null, null, ""); // Dummy instance
        return leaveRequestInstance.readCSV(FILE_PATH); // Use instance method
    }
    
    /**
    * Updates the status of a leave request (Approve/Reject)
    */
    public static void updateLeaveStatus(int employeeNumber, String leaveType, String startDate, String status) {
        LeaveRequest leaveRequestInstance = new LeaveRequest(0, "", null, null, ""); // Dummy instance to use readCSV & writeCSV
        List<String[]> allRequests = leaveRequestInstance.readCSV(FILE_PATH); // Use instance method
        boolean updated = false;

        for (String[] request : allRequests) {
            if (request.length >= 6 && 
                request[0].equals(String.valueOf(employeeNumber)) &&
                request[2].equals(leaveType) &&
                request[3].equals(startDate)) {
            
                request[5] = status; // Update Status
                updated = true;
                break;
            }
        }

        if (updated) {
            leaveRequestInstance.writeCSV(FILE_PATH, allRequests); // Use instance method
            System.out.println("Leave request status updated to: " + status);
        } else {
            System.out.println("No matching leave request found.");
        }
    }
}