package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * OvertimeRequest class that manages overtime requests and handles CSV operations.
 */
public class OvertimeRequest extends Employee implements CSVHandler {

    private String date;
    private double overtimeHours;
    private double overtimePay;
    private String status;

    private static final String FILE_PATH = "src/main/java/databases/Overtime Requests.csv";

    /**
     * Constructor for OvertimeRequest.
     */
    public OvertimeRequest(int employeeNumber, String date, double overtimeHours, double overtimePay, String status) {
        super(employeeNumber);
        this.date = date;
        this.overtimeHours = overtimeHours;
        this.overtimePay = overtimePay;
        this.status = status;
    }

    /** Getters */
    public String getDate() {
        return date;
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

    /** Setters */
    public void setDate(String date) {
        this.date = date;
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

    /** 
     * Reads all overtime requests from the CSV file.
     */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> overtimeRequests = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 5) { // Ensure correct column count
                    overtimeRequests.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return overtimeRequests;
    }

    /** 
     * Writes all overtime requests back to the CSV file.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = {"EmployeeNumber", "Date", "OvertimeHours", "OvertimePay", "Status"};
            writer.writeNext(header); // Write header only once
            writer.writeAll(data);    // Write all records
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a new overtime request and writes it to the CSV file.
     */
    public void submitOvertimeRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH);
        boolean isUpdated = false;

        // Create new overtime request entry
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), 
            date, 
            String.valueOf(overtimeHours), 
            String.valueOf(overtimePay), 
            status
        };

        // Check if a request for the same employee on the same date exists (update instead of duplicate)
        for (int i = 0; i < allRequests.size(); i++) {
            String[] request = allRequests.get(i);
            if (request[0].equals(String.valueOf(getEmployeeNumber())) && request[1].equals(date)) {
                allRequests.set(i, newRequest); // Update existing request
                isUpdated = true;
                break;
            }
        }

        if (!isUpdated) {
            allRequests.add(newRequest); // Add new request if it doesn’t exist
        }

        writeCSV(FILE_PATH, allRequests); // Overwrite file with updated list
        System.out.println("Overtime request " + (isUpdated ? "updated" : "submitted") + " successfully.");
    }

    /**
     * Cancels an overtime request by removing it from the CSV file.
     */
    public void cancelOvertimeRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH);
        List<String[]> updatedRequests = new ArrayList<>();
        boolean isDeleted = false;

        for (String[] request : allRequests) {
            if (request[0].equals(String.valueOf(getEmployeeNumber())) && request[1].equals(date)) {
                isDeleted = true; // Found and removed this request
                continue;
            }
            updatedRequests.add(request);
        }

        writeCSV(FILE_PATH, updatedRequests); // Overwrite file with updated data

        if (isDeleted) {
            System.out.println("Overtime request cancelled successfully.");
        } else {
            System.out.println("No matching overtime request found.");
        }
    }
}
