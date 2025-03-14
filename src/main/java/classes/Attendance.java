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
    public List<String[]> readCSV(String filePath) {
        List<String[]> attendanceRecords = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) { // Ensure correct data format
                    attendanceRecords.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return attendanceRecords;
    }

    /**
     * Writes attendance records to CSV file.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) { // Overwrite file
            // Define headers for CSV
            String[] header = { "Employee #", "Date", "Login Time", "Logout Time" };
            writer.writeNext(header);

            // Write attendance data
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs attendance data (adds a new attendance record).
     */
    public void logAttendance() {
        List<String[]> allRecords = readCSV(FILE_PATH); // Load existing records

        // Add new attendance record
        String[] newRecord = {
            String.valueOf(getEmployeeNumber()), 
            date.toString(), 
            login.toString(), 
            (logout != null) ? logout.toString() : "N/A"
        };
        allRecords.add(newRecord);

        // Write updated records back to CSV
        writeCSV(FILE_PATH, allRecords);
        System.out.println("Attendance logged successfully.");
    }
}