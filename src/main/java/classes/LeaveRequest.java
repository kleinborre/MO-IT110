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
import java.util.Map;
import java.util.HashMap;

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
     * Reads all leave requests from the CSV file and returns them as a map.
     * Key: Employee Number, Value: Leave Request details.
     */
    @Override
    public Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> leaveRequests = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 5) {
                    leaveRequests.put(nextLine[0], nextLine);
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"EmployeeNumber", "LeaveType", "StartDate", "EndDate", "Status"};
            writer.writeNext(header); // Write header first

            for (String[] row : data) {
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a new leave request and writes it to the CSV file.
     */
    public void submitLeaveRequest() {
        List<String[]> allRequests = new ArrayList<>();

        // Read existing leave requests
        Map<String, String[]> existingRequests = readCSV(FILE_PATH);
        allRequests.addAll(existingRequests.values()); // Convert Map to List

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
    public void cancelLeaveRequest() {
        List<String[]> allRequests = new ArrayList<>();
        boolean requestFound = false;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] headers = reader.readNext();
            allRequests.add(headers); // Keep header row

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (!nextLine[0].equals(String.valueOf(getEmployeeNumber())) || 
                    !nextLine[1].equals(leaveType) || 
                    !nextLine[2].equals(startDate.toString())) {
                    allRequests.add(nextLine); // Keep other records
                } else {
                    requestFound = true;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        if (requestFound) {
            // Write updated list back to CSV
            writeCSV(FILE_PATH, allRequests);
            System.out.println("Leave request cancelled successfully.");
        } else {
            System.out.println("Error: Leave request not found.");
        }
    }
}