package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Attendance class that records employee attendance and manages CSV data.
 */
public class Attendance extends Employee implements CSVHandler {
    private LocalDate date;
    private LocalTime login;
    private LocalTime logout;
    private static final String FILE_PATH = "src/main/java/databases/Attendance Records.csv";

    /**
     * Constructor for Attendance.
     */
    public Attendance(int employeeNumber, LocalDate date, LocalTime login, LocalTime logout) {
        super(employeeNumber);
        this.date = date;
        this.login = login;
        this.logout = logout;
    }

    /**
     * Getters & Setters
     */
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getLogin() {
        return login;
    }

    public LocalTime getLogout() {
        return logout;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLogin(LocalTime login) {
        this.login = login;
    }

    public void setLogout(LocalTime logout) {
        this.logout = logout;
    }

    /**
     * Reads attendance records from CSV file.
     */
    @Override
    public Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> attendanceMap = new HashMap<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Read header row
            String[] nextLine;
            
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) { // Ensure correct data format
                    String empNumber = nextLine[0].trim();
                    attendanceMap.put(empNumber, nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return attendanceMap;
    }

    /**
     * Writes attendance records to CSV file.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Define headers for CSV
            String[] header = { "Employee #", "Date", "Login Time", "Logout Time" };
            writer.writeNext(header);

            // Write attendance data
            for (String[] row : data) {
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs attendance data (adds a new attendance record).
     */
    public void logAttendance() {
        List<String[]> allRecords = new ArrayList<>();

        // Read existing attendance records
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] headers = reader.readNext(); // Read header row
            allRecords.add(headers); // Preserve headers
            
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                allRecords.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        // Add new attendance record
        String[] newRecord = {
            String.valueOf(getEmployeeNumber()), 
            date.toString(), 
            login.toString(), 
            (logout != null) ? logout.toString() : "N/A"
        };
        allRecords.add(newRecord);

        // Write back to CSV
        writeCSV(FILE_PATH, allRecords);
    }
}