package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Attendance extends Employee implements CSVHandler {
    private LocalDate date;
    private String login;
    private String logout;

    private static final String DATABASE_FOLDER = "databases"; // Updated folder name to match pom.xml
    private static final String FILE_NAME = "Attendance Records.csv";
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

    public static File getCSVFile() {
        String userDir = System.getProperty("user.dir"); 
        File csvFile;

        // 1. Check inside target/databases/ (for NetBeans execution)
        File targetFile = new File(userDir, "target" + File.separator + DATABASE_FOLDER + File.separator + FILE_NAME);
        if (targetFile.exists()) {
            return targetFile;
        }

        // 2. Check inside databases/ (for JAR execution)
        File externalFile = new File(userDir, DATABASE_FOLDER + File.separator + FILE_NAME);
        if (externalFile.exists()) {
            return externalFile;
        }

        // 3. If not found, try copying from inside JAR
        InputStream internalFile = Attendance.class.getClassLoader().getResourceAsStream(DATABASE_FOLDER + "/" + FILE_NAME);
        if (internalFile != null) {
            try {
                File directory = new File(userDir, DATABASE_FOLDER);
                if (!directory.exists()) {
                    directory.mkdirs(); // Create databases folder only in the current working directory
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

    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        File csvFile = getCSVFile();

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
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
        File csvFile = getCSVFile();

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false))) {
            String[] header = { "Employee #", "Last Name", "First Name", "Date", "Login Time", "Logout Time" };
            writer.writeNext(header);
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getAllAttendanceRecords() {
        Attendance attendanceInstance = new Attendance();
        return attendanceInstance.readCSV(getCSVFile().getPath());
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
        synchronized (Attendance.class) { 
            File csvFile = getCSVFile();
            List<String[]> allRecords = getAllAttendanceRecords();
        boolean updated = false;
        
        for (String[] row : allRecords) {
            if (row[0].equals(record[0]) && row[3].equals(record[3]) && row[5].equals("N/A")) { 
                row[5] = record[5]; // Update the logout time
                updated = true;
                break;
            }
        }

        if (!updated) {
            allRecords.add(record); // If no update was made, add a new entry
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false))) {
            String[] header = { "Employee #", "Last Name", "First Name", "Date", "Login Time", "Logout Time" };
            writer.writeNext(header);
            writer.writeAll(allRecords);
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
                boolean matches = record[0].equalsIgnoreCase(searchTerm) ||  
                                record[1].toLowerCase().contains(searchTerm.toLowerCase()) || 
                                record[2].toLowerCase().contains(searchTerm.toLowerCase()); 

                if (matches) {
                    filteredRecords.add(record);
                }
            }
        }
        return filteredRecords;
    }
}