package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PayrollManager {

    private static final String DATABASES_FOLDER = "databases";
    private static final String ATTENDANCE_FILE = "Attendance Records.csv";
    private static final String EMPLOYEE_FILE = "Employee Details.csv";

    private int month;
    private int year;

    public PayrollManager(int month, int year) {
        this.month = month;
        this.year = year;
    }

    private static File getCSVFile(String fileName) {
        String userDir = System.getProperty("user.dir"); 
        File csvFile;

        // 1. Check target/databases/ (for NetBeans execution)
        File targetFile = new File(userDir, "target" + File.separator + DATABASES_FOLDER + File.separator + fileName);
        if (targetFile.exists()) {
            return targetFile;
        }

        // 2. Check databases/ (for JAR execution)
        File externalFile = new File(userDir, DATABASES_FOLDER + File.separator + fileName);
        if (externalFile.exists()) {
            return externalFile;
        }

        // 3. If missing, copy from resources
        InputStream internalFile = PayrollManager.class.getClassLoader().getResourceAsStream(DATABASES_FOLDER + "/" + fileName);
        if (internalFile != null) {
            try {
                File directory = new File(userDir, DATABASES_FOLDER);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                Files.copy(internalFile, externalFile.toPath());
                System.out.println("Copied " + fileName + " from resources to external directory.");
                return externalFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("CSV file not found anywhere: " + fileName);
        return null;
    }

    public List<String[]> readCSV(String fileName) {
        List<String[]> records = new ArrayList<>();
        File csvFile = getCSVFile(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean firstRow = true;

            while ((line = br.readLine()) != null) {
                if (firstRow) { 
                    firstRow = false;
                    continue;
                }

                line = line.replaceAll("^\"|\"$", "");
                String[] values = line.split(",");

                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim().replaceAll("^\"|\"$", "");
                }

                records.add(values);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
        }

        return records;
    }

    private String getEmployeeFullName(int employeeNumber) {
        List<String[]> employeeRecords = readCSV(EMPLOYEE_FILE);

        for (String[] record : employeeRecords) {
            if (record.length >= 3) {
                try {
                    int empNum = Integer.parseInt(record[0].trim());
                    if (empNum == employeeNumber) {
                        return record[2].trim() + " " + record[1].trim();
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing employee number: " + record[0]);
                }
            }
        }
        return "Unknown Employee";
    }

    public List<Integer> getAllEmployeeNumbers() {
        List<String[]> attendanceRecords = readCSV(ATTENDANCE_FILE);
        List<Integer> employeeNumbers = new ArrayList<>();

        for (String[] record : attendanceRecords) {
            if (record.length >= 6) {
                try {
                    int empNumber = Integer.parseInt(record[0].trim());
                    String[] dateParts = record[3].trim().split("/");
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int recordYear = Integer.parseInt(dateParts[2]);

                    if (recordMonth == this.month && recordYear == this.year) {
                        if (!employeeNumbers.contains(empNumber)) {
                            employeeNumbers.add(empNumber);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing employee number: " + record[0]);
                }
            }
        }
        return employeeNumbers;
    }

    public List<String[]> processPayrollAttendance() {
        List<String[]> payrollSummary = new ArrayList<>();
        List<Integer> employeeNumbers = getAllEmployeeNumbers();

        if (employeeNumbers.isEmpty()) {
            System.err.println("No employee records found for " + month + "/" + year);
            return payrollSummary;
        }

        for (int empNumber : employeeNumbers) {
            Payslip payslip = new Payslip(empNumber, month, year);
            payslip.processAttendance();
            payslip.processOvertime();
            payslip.calculateGrossSalary();
            payslip.calculateDeductions();
            payslip.calculateNetSalary();

            String fullName = getEmployeeFullName(empNumber);
            String[] row = {
                String.valueOf(empNumber),
                fullName,
                String.format("%.2f", payslip.getGrossSalary()),
                String.format("%.2f", payslip.getTotalDeductions()),
                String.format("%.2f", payslip.getNetSalary())
            };
            payrollSummary.add(row);
        }

        return payrollSummary;
    }
}
