package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.file.Files;
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

    private static final String DATABASES_FOLDER = "databases";
    private static final String FILE_NAME = "Overtime Requests.csv";

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

    public static File getCSVFile() {
        String userDir = System.getProperty("user.dir"); 
        File csvFile;

        // 1. Check target/databases/ (for NetBeans execution)
        File targetFile = new File(userDir, "target" + File.separator + DATABASES_FOLDER + File.separator + FILE_NAME);
        if (targetFile.exists()) {
            return targetFile;
        }

        // 2. Check databases/ (for JAR execution)
        File externalFile = new File(userDir, DATABASES_FOLDER + File.separator + FILE_NAME);
        if (externalFile.exists()) {
            return externalFile;
        }

        // 3. If missing, copy from resources
        InputStream internalFile = OvertimeRequest.class.getClassLoader().getResourceAsStream(DATABASES_FOLDER + "/" + FILE_NAME);
        if (internalFile != null) {
            try {
                File directory = new File(userDir, DATABASES_FOLDER);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                Files.copy(internalFile, externalFile.toPath());
                System.out.println("Copied " + FILE_NAME + " from resources to external directory.");
                return externalFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("CSV file not found anywhere: " + FILE_NAME);
        return null;
    }

    /** 
     * Reads all overtime requests from the CSV file.
     */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> overtimeRequests = new ArrayList<>();
        File csvFile = getCSVFile();

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
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
        File csvFile = getCSVFile();

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false))) {
            String[] header = {"EmployeeNumber", "Name", "Date", "OvertimeHours", "OvertimePay", "Status"};
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
        List<String[]> allRequests = readCSV(getCSVFile().getPath());
        boolean isUpdated = false;

        // Get full name of the employee
        String fullName = getFullName(); 

        // Create new overtime request entry with full name at index 1
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), 
            fullName,  // Insert full name here
            date, 
            String.valueOf(overtimeHours), 
            String.valueOf(overtimePay), 
            status
        };

        // Check if a request for the same employee on the same date exists (update instead of duplicate)
        for (int i = 0; i < allRequests.size(); i++) {
            String[] request = allRequests.get(i);
            if (request[0].equals(String.valueOf(getEmployeeNumber())) && request[2].equals(date)) {
                allRequests.set(i, newRequest); // Update existing request
                isUpdated = true;
                break;
            }
        }
        
        if (!isUpdated) {
            allRequests.add(newRequest); // Add new request if it doesn’t exist
        }

        writeCSV(getCSVFile().getPath(), allRequests); // Overwrite file with updated list
        System.out.println("Overtime request " + (isUpdated ? "updated" : "submitted") + " successfully.");
    }

    /**
     * Cancels an overtime request by removing it from the CSV file.
     */
    public void cancelOvertimeRequest() {
        List<String[]> allRequests = readCSV(getCSVFile().getPath());
        List<String[]> updatedRequests = new ArrayList<>();
        boolean isDeleted = false;

        for (String[] request : allRequests) {
            if (request.length >= 6 && request[0].equals(String.valueOf(getEmployeeNumber())) && request[2].equals(date)) {
                isDeleted = true; // Found and removed this request
                continue;
            }
            updatedRequests.add(request);
        }

        writeCSV(getCSVFile().getPath(), updatedRequests); // Overwrite file with updated data

        if (isDeleted) {
            System.out.println("Overtime request cancelled successfully.");
        } else {
            System.out.println("No matching overtime request found.");
        }
    }
    
    /**
    * Retrieves all overtime requests for HR Manager to review.
    */
    public static List<String[]> getAllOvertimeRequests() {
        OvertimeRequest overtimeRequestInstance = new OvertimeRequest(0, "", 0.0, 0.0, ""); // Dummy instance
        return overtimeRequestInstance.readCSV(getCSVFile().getPath()); // Use instance method
    }

    /**
    * Updates the status of an overtime request (Approve/Reject)
    */
    public static void updateOvertimeStatus(int employeeNumber, String date, String status) {
        OvertimeRequest overtimeRequestInstance = new OvertimeRequest(0, "", 0.0, 0.0, ""); // Dummy instance
        List<String[]> allRequests = overtimeRequestInstance.readCSV(getCSVFile().getPath());
        boolean updated = false;

        for (String[] request : allRequests) {
            if (request.length >= 6 && 
                request[0].equals(String.valueOf(employeeNumber)) &&
                request[2].equals(date)) {

                request[5] = status; // Update Status
                updated = true;
                break;
            }
        }

        if (updated) {
            overtimeRequestInstance.writeCSV(getCSVFile().getPath(), allRequests);
            System.out.println("Overtime request status updated to: " + status);
        } else {
            System.out.println("No matching overtime request found.");
        }
    }
}