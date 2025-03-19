package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.file.Files;
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

    private static final String DATABASES_FOLDER = "databases";
    private static final String FILE_NAME = "Leave Requests.csv";

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

    public LeaveRequest() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        InputStream internalFile = LeaveRequest.class.getClassLoader().getResourceAsStream(DATABASES_FOLDER + "/" + FILE_NAME);
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
     * Reads all leave requests from the CSV file.
     */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> leaveRequests = new ArrayList<>();
        File csvFile = getCSVFile();

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
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
        File csvFile = getCSVFile();

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false))) {
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
        List<String[]> allRequests = readCSV(getCSVFile().getPath());

        // Ensure full name is retrieved before saving
        Employee employee = new Employee(getEmployeeNumber());
        String fullName = employee.getLastName() + ", " + employee.getFirstName();

        // Create a new leave request entry with the correct name
        String[] newRequest = {
            String.valueOf(getEmployeeNumber()), 
            fullName, // Ensure name is correctly saved
            leaveType, 
            startDate.toString(), 
            endDate.toString(), 
            status
        };

        allRequests.add(newRequest); // Add new request
        writeCSV(getCSVFile().getPath(), allRequests); // Overwrite file with updated list
        System.out.println("Leave request submitted successfully.");
    }

    /**
     * Cancels a leave request by removing it from the CSV file.
     */
    public void deleteLeaveRequest() {
        List<String[]> allRequests = readCSV(getCSVFile().getPath());
        List<String[]> updatedRequests = new ArrayList<>();
        boolean isDeleted = false;

        for (String[] request : allRequests) {
            if (request[0].equals(String.valueOf(getEmployeeNumber())) && 
                request[2].equals(leaveType) && 
                request[3].equals(startDate.toString())) {
                isDeleted = true; // Found and removed this request
                continue;
            }
            updatedRequests.add(request);
        }

        writeCSV(getCSVFile().getPath(), updatedRequests); // Overwrite file with updated data

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
        return leaveRequestInstance.readCSV(getCSVFile().getPath()); // Use instance method
    }
    
    /**
    * Updates the status of a leave request (Approve/Reject)
    */
    public static void updateLeaveStatus(int employeeNumber, String leaveType, String startDate, String status) {
        LeaveRequest leaveRequestInstance = new LeaveRequest(0, "", null, null, ""); // Dummy instance
        List<String[]> allRequests = leaveRequestInstance.readCSV(getCSVFile().getPath());
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
            leaveRequestInstance.writeCSV(getCSVFile().getPath(), allRequests);
            System.out.println("Leave request status updated to: " + status);
        } else {
            System.out.println("No matching leave request found.");
        }
    }
    
     /**
     * Converts LeaveRequest object to CSV row format.
     */
    public String[] toCSVArray() {
        Employee employee = new Employee(getEmployeeNumber());
        String fullName = employee.getLastName() + ", " + employee.getFirstName();

        return new String[]{
            String.valueOf(getEmployeeNumber()),
            fullName,
            leaveType,
            startDate.toString(),
            endDate.toString(),
            status
        };
    }
}