package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Attendance extends Employee implements CSVHandler {
    private LocalDate date;
    private String login;
    private String logout;

    private static final String FILE_PATH = "src/main/java/databases/Attendance Records.csv";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Attendance(int employeeNumber, LocalDate date, String login, String logout) {
        super(employeeNumber);
        this.date = date;
        this.login = login;
        this.logout = logout;
    }

    public Attendance() {}

    public LocalDate getDate() { return date; }
    public String getLogin() { return login; }
    public String getLogout() { return logout; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setLogin(String login) { this.login = login; }
    public void setLogout(String logout) { this.logout = logout; }

    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            reader.readNext(); // Skip header row
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 6) {
                    records.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = { "Employee #", "Last Name", "First Name", "Date", "Login Time", "Logout Time" };
            writer.writeNext(header);
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getAllAttendanceRecords() {
        Attendance attendanceInstance = new Attendance();
        return attendanceInstance.readCSV(FILE_PATH);
    }

    public static List<String[]> getEmployeeAttendance(int employeeNumber, int month, int year) {
        List<String[]> allRecords = getAllAttendanceRecords();
        List<String[]> filteredRecords = new ArrayList<>();

        for (String[] record : allRecords) {
            try {
                LocalDate recordDate = LocalDate.parse(record[3], DATE_FORMAT);
                boolean matchesMonth = (month == -1 || recordDate.getMonthValue() == month);
                boolean matchesYear = (year == -1 || recordDate.getYear() == year);

                if (Integer.parseInt(record[0]) == employeeNumber && matchesMonth && matchesYear) {
                    filteredRecords.add(record);
                }
            } catch (Exception e) {
                System.out.println("Invalid date format in CSV: " + record[3]);
            }
        }
        return filteredRecords;
    }

    public static void appendToCSV(String[] record) {
        synchronized (Attendance.class) { // Prevents duplicate writes
            try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
                writer.writeNext(record);
                writer.flush(); // Ensure immediate write to disk
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static List<String[]> searchAttendanceRecords(String searchTerm) {
        List<String[]> allRecords = getAllAttendanceRecords();
        List<String[]> filteredRecords = new ArrayList<>();

        for (String[] record : allRecords) {
            if (record.length >= 6) {
                boolean matches = record[0].equalsIgnoreCase(searchTerm) ||  // Employee Number
                                record[1].toLowerCase().contains(searchTerm.toLowerCase()) || // Last Name
                                record[2].toLowerCase().contains(searchTerm.toLowerCase()); // First Name

                if (matches) {
                    filteredRecords.add(record);
                }
            }
        }
        return filteredRecords;
    }
}