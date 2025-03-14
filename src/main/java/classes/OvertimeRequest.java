package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
     * Reads all overtime requests from the CSV file and returns them as a map.
     * Key: Employee Number, Value: Overtime Request details.
     */
    @Override
    public Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> overtimeRequests = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) { // Ensure correct column count
                    overtimeRequests.put(nextLine[0], nextLine);
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"EmployeeNumber", "OvertimeHours", "OvertimePay", "Status"};
            writer.writeNext(header); // Write header first

            for (String[] row : data) {
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a new overtime request and writes it to the CSV file.
     */
    public void submitOvertimeRequest() {
        List<String[]> allRequests = new ArrayList<>();

        // Read existing overtime requests
        Map<String, String[]> existingRequests = readCSV(FILE_PATH);
        allRequests.addAll(existingRequests.values()); // Convert Map to List

        // Add new overtime request
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), 
            String.valueOf(overtimeHours), 
            String.valueOf(overtimePay), 
            status
        };
        allRequests.add(newRequest);

        // Write updated overtime requests back to CSV
        writeCSV(FILE_PATH, allRequests);
        System.out.println("Overtime request submitted successfully.");
    }

    /**
     * Cancels an overtime request by removing it from the CSV file.
     */
    public void cancelOvertimeRequest() {
        List<String[]> allRequests = new ArrayList<>();
        boolean requestFound = false;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] headers = reader.readNext();
            allRequests.add(headers); // Keep header row

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (!nextLine[0].equals(String.valueOf(getEmployeeNumber())) || 
                    !nextLine[1].equals(String.valueOf(overtimeHours))) {
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
            System.out.println("Overtime request cancelled successfully.");
        } else {
            System.out.println("Error: Overtime request not found.");
        }
    }
}