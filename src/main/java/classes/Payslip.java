package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Payslip extends Employee implements CSVHandler {
    private double totalWorkedHours;
    private double overtimeHours;
    private double overtimePay;
    private double grossSalary;
    private double deductions;
    private double netSalary;
    private double sssDeduction;
    private double philhealthDeduction;
    private double pagibigDeduction;
    private double withholdingTax;

    private int month;
    private int year;

    private static final String ATTENDANCE_FILE = "src/main/java/databases/Attendance Records.csv";
    private static final String OVERTIME_FILE = "src/main/java/databases/Overtime Requests.csv";
    private static final String PAYSLIP_FILE = "src/main/java/databases/Payslip Records.csv";

    public Payslip(int employeeNumber, int month, int year) {
        super(employeeNumber);
        this.month = month;
        this.year = year;
        this.totalWorkedHours = 0.0;
        this.overtimeHours = 0.0;
        this.overtimePay = 0.0;
        this.grossSalary = 0.0;
        this.deductions = 0.0;
        this.netSalary = 0.0;
        processAttendance();
        processOvertime();
        calculateGrossSalary();
        calculateDeductions();
        calculateNetSalary();
    }

    private void processAttendance() {
    List<String[]> records = readCSV(ATTENDANCE_FILE);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    totalWorkedHours = 0.0; // Reset before calculation
    HashSet<LocalDate> processedDates = new HashSet<>(); // To track unique days

    for (int i = 1; i < records.size(); i++) { // Skip headers
        String[] record = records.get(i);

        if (record.length >= 6 && record[0].equals(String.valueOf(employeeNumber))) {
            try {
                LocalDate date = LocalDate.parse(record[3], dateFormatter);

                // **Ensure we only process each date once**
                if (date.getMonthValue() == month && date.getYear() == year && !processedDates.contains(date)) {
                    String loginStr = record[4].trim();
                    String logoutStr = record[5].trim();

                    // **Fix: Skip if login/logout are empty**
                    if (loginStr.isEmpty() || logoutStr.isEmpty()) {
                        System.err.println(" Skipping empty login/logout for Employee #" + employeeNumber + " on " + date);
                        continue;
                    }

                    // **Fix: Normalize time values to HH:mm format**
                    String[] loginParts = loginStr.split(":");
                    String[] logoutParts = logoutStr.split(":");

                    if (loginParts.length < 2 || logoutParts.length < 2) {
                        System.err.println(" Skipping invalid time format for Employee #" + employeeNumber + " on " + date);
                        continue;
                    }

                    String normalizedLogin = String.format("%02d:%s", Integer.parseInt(loginParts[0]), loginParts[1]);
                    String normalizedLogout = String.format("%02d:%s", Integer.parseInt(logoutParts[0]), logoutParts[1]);

                    LocalTime loginTime = LocalTime.parse(normalizedLogin, timeFormatter);
                    LocalTime logoutTime = LocalTime.parse(normalizedLogout, timeFormatter);

                    // **Fix: Prevent processing if login & logout times are identical**
                    if (logoutTime.equals(loginTime)) {
                        System.err.println(" Skipping invalid time (Same login/logout): " + loginStr + " - " + logoutStr + " for Employee #" + employeeNumber);
                        continue;
                    }

                    // **Fix: Prevent negative worked hours (logout before login)**
                    if (logoutTime.isBefore(loginTime)) {
                        System.err.println(" Skipping invalid time (Logout before Login): " + loginStr + " - " + logoutStr + " for Employee #" + employeeNumber);
                        continue;
                    }

                    double hoursWorked = Duration.between(loginTime, logoutTime).toMinutes() / 60.0;
                    totalWorkedHours += hoursWorked;

                    // **Track this date to prevent duplicate processing**
                    processedDates.add(date);

                    System.out.println(" Processed: " + date + " | " + normalizedLogin + " - " + normalizedLogout + " | Hours: " + hoursWorked);
                }
            } catch (Exception e) {
                System.err.println(" Error processing attendance record for Employee #" + employeeNumber + " | " + record[3]);
                e.printStackTrace();
                }
            }
        }

        System.out.println(" Total Worked Hours for Employee #" + employeeNumber + " in " + month + "/" + year + ": " + totalWorkedHours);
    }


    //  Process Overtime: Read overtime hours and pay from CSV
    private void processOvertime() {
        List<String[]> records = readCSV(OVERTIME_FILE);
        for (String[] record : records) {
            if (record.length >= 5 && record[0].equals(String.valueOf(employeeNumber))) {
                try {
                    overtimeHours += Double.parseDouble(record[3]); // Overtime Hours
                    overtimePay += Double.parseDouble(record[4]); // Overtime Pay
                } catch (Exception e) {
                    System.err.println("Error processing overtime record for Employee #" + employeeNumber);
                }
            }
        }
    }

    //  Calculate Gross Salary
    private void calculateGrossSalary() {
        grossSalary = (totalWorkedHours * getHourlyRate()) + overtimePay;
    }

    //  Calculate SSS Deduction based on Salary Bracket
    private void calculateSSS() {
        double[][] sssBrackets = {
            {3250, 135}, {3750, 157.50}, {4250, 180}, {4750, 202.50}, {5250, 225},
            {5750, 247.50}, {6250, 270}, {6750, 292.50}, {7250, 315}, {7750, 337.50},
            {8250, 360}, {8750, 382.50}, {9250, 405}, {9750, 427.50}, {10250, 450},
            {10750, 472.50}, {11250, 495}, {11750, 517.50}, {12250, 540}, {12750, 562.50},
            {Double.MAX_VALUE, 1125}
        };
        for (double[] bracket : sssBrackets) {
            if (grossSalary <= bracket[0]) {
                sssDeduction = bracket[1];
                break;
            }
        }
    }

    //  Calculate PhilHealth and Pag-IBIG deductions
    private void calculatePhilHealthAndPagibig() {
        philhealthDeduction = Math.min(grossSalary * 0.03 / 2, 900);
        pagibigDeduction = Math.min(grossSalary * 0.02, 100);
    }

    //  Calculate Withholding Tax
    private void calculateWithholdingTax() {
        double taxableIncome = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction);
        if (taxableIncome <= 20832) withholdingTax = 0;
        else if (taxableIncome <= 33333) withholdingTax = (taxableIncome - 20833) * 0.20;
        else if (taxableIncome <= 66667) withholdingTax = 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome <= 166667) withholdingTax = 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome <= 666667) withholdingTax = 40833.33 + (taxableIncome - 166667) * 0.32;
        else withholdingTax = 200833.33 + (taxableIncome - 666667) * 0.35;
    }

    //  Calculate Total Deductions
    private void calculateDeductions() {
        calculateSSS();
        calculatePhilHealthAndPagibig();
        calculateWithholdingTax();
        deductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;
    }

    private void calculateNetSalary() {
    netSalary = grossSalary - deductions;

    // If Overtime Pay is from previous years (before 2025), subtract from Net Salary
    if (year < 2025) {
        netSalary -= overtimePay;
        System.out.println("Overtime Pay deducted as it's not from 2025: -" + overtimePay);
    }
    }

    //  Getter Methods
    public double getTotalWorkedHours() { return totalWorkedHours; }
    public double getOvertimeHours() { return overtimeHours; }
    public double getOvertimePay() { return overtimePay; }
    public double getGrossSalary() { return grossSalary; }
    public double getDeductions() { return deductions; }
    public double getNetSalary() { return netSalary; }
    public double getSssDeduction() { return sssDeduction; }
    public double getPhilhealthDeduction() { return philhealthDeduction; }
    public double getPagibigDeduction() { return pagibigDeduction; }
    public double getWithholdingTax() { return withholdingTax; }
    public double getTotalDeductions() {return sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;}



    /** CSV Handling */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                data.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}