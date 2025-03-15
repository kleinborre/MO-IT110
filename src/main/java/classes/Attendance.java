package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Attendance extends Employee implements CSVHandler {
    private LocalDate date;
    private LocalTime login;
    private LocalTime logout;
    private static final String FILE_PATH = "src/main/java/databases/Attendance Records.csv";

    private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter ALT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Fix for existing errors
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Attendance(int employeeNumber, LocalDate date, LocalTime login, LocalTime logout) {
        super(employeeNumber);
        this.date = date;
        this.login = login;
        this.logout = logout;
    }

    public Attendance() {}

    public LocalDate getDate() { return date; }
    public LocalTime getLogin() { return login; }
    public LocalTime getLogout() { return logout; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setLogin(LocalTime login) { this.login = login; }
    public void setLogout(LocalTime logout) { this.logout = logout; }

    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> attendanceRecords = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null || header.length < 6 || !header[0].contains("Employee")) {
                System.out.println("Invalid or missing header in CSV file!");
                return attendanceRecords;
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 6 && !nextLine[0].equalsIgnoreCase("Employee #")) { 
                    // Convert incorrect date format (YYYY-MM-DD) to MM/DD/YYYY
                    try {
                        LocalDate parsedDate;
                        if (nextLine[3].contains("-")) { // If it's in YYYY-MM-DD format
                            parsedDate = LocalDate.parse(nextLine[3], ALT_DATE_FORMAT);
                            nextLine[3] = parsedDate.format(CSV_DATE_FORMAT); // Convert to MM/DD/YYYY
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format in CSV: " + nextLine[3]);
                    }
                    attendanceRecords.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return attendanceRecords;
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

    public void logAttendance() {
        List<String[]> allRecords = readCSV(FILE_PATH);
        String formattedDate = date.format(CSV_DATE_FORMAT);
        String formattedLogin = login.format(TIME_FORMATTER);

        for (String[] record : allRecords) {
            if (Integer.parseInt(record[0]) == getEmployeeNumber() && record[3].equals(formattedDate)) {
                System.out.println("Clock-In already exists!");
                return;
            }
        }

        String[] newRecord = { 
            String.valueOf(getEmployeeNumber()), 
            getLastName(), 
            getFirstName(), 
            formattedDate, 
            formattedLogin, 
            "N/A"
        };

        appendToCSV(FILE_PATH, newRecord);
    }

    public void updateClockOut() {
        List<String[]> allRecords = readCSV(FILE_PATH);
        boolean updated = false;
        String formattedDate = date.format(CSV_DATE_FORMAT);
        String formattedLogout = logout.format(TIME_FORMATTER);

        for (String[] record : allRecords) {
            if (Integer.parseInt(record[0]) == getEmployeeNumber() && record[3].equals(formattedDate)) {
                if (record[5].equals("N/A")) {  
                    record[5] = formattedLogout;
                    updated = true;
                } else {
                    System.out.println("Clock-Out already recorded.");
                    return;
                }
            }
        }

        if (!updated) {
            System.out.println("Clock-Out failed: No matching Clock-In found.");
            return;
        }

        writeCSV(FILE_PATH, allRecords);
    }

    public List<String[]> getEmployeeAttendance(int employeeNumber, int month, int year) {
        List<String[]> allRecords = readCSV(FILE_PATH);
        List<String[]> filteredRecords = new ArrayList<>();

        for (String[] record : allRecords) {
            try {
                LocalDate recordDate = LocalDate.parse(record[3], CSV_DATE_FORMAT);
                boolean matchesMonth = (month == -1 || recordDate.getMonthValue() == month);
                boolean matchesYear = (year == -1 || recordDate.getYear() == year);

                if (Integer.parseInt(record[0]) == employeeNumber && matchesMonth && matchesYear) {
                    filteredRecords.add(new String[]{record[3], record[4], record[5]});
                }
            } catch (Exception e) {
                System.out.println("Invalid date format in CSV: " + record[3]);
            }
        }
        return filteredRecords;
    }


    public void appendToCSV(String filePath, String[] record) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeNext(record);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
