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

    private double overtimeHours;
    private double overtimePay;
    private String status;

    private static final String FILE_PATH = "src/main/java/databases/Overtime Requests.csv";

    /**
     * Constructor for OvertimeRequest.
     */
    public OvertimeRequest(int employeeNumber, double overtimeHours, double overtimePay, String status) {
        super(employeeNumber);
        this.overtimeHours = overtimeHours;
        this.overtimePay = overtimePay;
        this.status = status;
    }

    /** Getters */
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
                if (nextLine.length >= 4) { // Ensure correct column count
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) { // "false" ensures overwrite
            String[] header = {"EmployeeNumber", "OvertimeHours", "OvertimePay", "Status"};
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

        // Remove previous request for the same employee and hours (if it exists)
        allRequests.removeIf(request -> 
            request[0].equals(String.valueOf(getEmployeeNumber())) && 
            request[1].equals(String.valueOf(overtimeHours))
        );

        // Add the new request
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), 
            String.valueOf(overtimeHours), 
            String.valueOf(overtimePay), 
            status
        };
        allRequests.add(newRequest);

        // Write all requests back to CSV
        writeCSV(FILE_PATH, allRequests);
        System.out.println("Overtime request submitted successfully.");
    }

    /**
     * Cancels an overtime request by removing it from the CSV file.
     */
    public void cancelOvertimeRequest() {
        List<String[]> allRequests = readCSV(FILE_PATH);
        boolean requestFound = false;

        // Filter out the request to delete
        List<String[]> updatedRequests = new ArrayList<>();
        for (String[] request : allRequests) {
            if (!(request[0].equals(String.valueOf(getEmployeeNumber())) &&
                  request[1].equals(String.valueOf(overtimeHours)) &&
                  request[2].equals(String.valueOf(overtimePay)))) {
                updatedRequests.add(request); // Keep all other requests
            } else {
                requestFound = true; // Mark that we found the request to delete
            }
        }

        // Write the updated list back to CSV
        if (requestFound) {
            writeCSV(FILE_PATH, updatedRequests);
            System.out.println("Overtime request cancelled successfully.");
        } else {
            System.out.println("Error: Overtime request not found.");
        }
    }
}