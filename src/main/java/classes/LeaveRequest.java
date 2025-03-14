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
                if (nextLine.length >= 5) {
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) { // "false" ensures overwrite
            String[] header = {"EmployeeNumber", "LeaveType", "StartDate", "EndDate", "Status"};
            writer.writeNext(header); // Write header first
            writer.writeAll(data);    // Write all records
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a new leave request and writes it to the CSV file.
     */
    public void submitLeaveRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH); // Load existing requests

        // Add new leave request
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), leaveType, 
            startDate.toString(), endDate.toString(), status
        };
        allRequests.add(newRequest);

        // Write updated leave requests back to CSV
        writeCSV(FILE_PATH, allRequests);
        System.out.println("Leave request submitted successfully.");
    }

    /**
     * Cancels a leave request by removing it from the CSV file.
     */
    public void deleteLeaveRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH);
        boolean requestFound = false;

        // Filter out the leave request to delete
        List<String[]> updatedRequests = new ArrayList<>();
        for (String[] request : allRequests) {
            if (!(request[0].equals(String.valueOf(getEmployeeNumber())) &&
                  request[1].equals(leaveType) &&
                  request[2].equals(startDate.toString()))) {
                updatedRequests.add(request); // Keep all other requests
            } else {
                requestFound = true; // Mark that we found the request to delete
            }
        }

        // Write the updated list back to CSV
        if (requestFound) {
            writeCSV(FILE_PATH, updatedRequests);
            System.out.println("Leave request deleted successfully.");
        } else {
            System.out.println("Error: Leave request not found.");
        }
    }
}