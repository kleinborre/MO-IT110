package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PayrollManager extends Payslip {
    
    private static final String ATTENDANCE_FILE = "src/main/java/databases/Attendance Records.csv";
    private static final String EMPLOYEE_FILE = "src/main/java/databases/Employee Details.csv";

    private int month;
    private int year;

    public PayrollManager(int employeeNumber, int month, int year) {
        super(employeeNumber, month, year);
        this.month = month;
        this.year = year;
    }

    /**
     *  Reads a CSV file and returns a list of string arrays (each row in the file).
     */
    public List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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
            System.err.println(" Error reading file: " + filePath);
        }

        return records;
    }

    /**
     *  Fetches the full name of an employee based on their employee number.
     */
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

    /**
     *  Fetch all employees who have attendance for the given month & year.
     */
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
                    System.err.println(" Error processing employee number: " + record[0]);
                }
            }
        }
        return employeeNumbers;
    }

    /**
     *  Process attendance & calculate payroll for each employee.
     */
    public List<String[]> processPayrollAttendance() {
        List<String[]> payrollSummary = new ArrayList<>();
        List<Integer> employeeNumbers = getAllEmployeeNumbers();

        if (employeeNumbers.isEmpty()) {
            System.err.println("No employee records found for " + month + "/" + year);
            return payrollSummary;
        }

        HashMap<Integer, Double> totalWorkedHoursMap = calculateTotalWorkedHours();

        for (int empNumber : employeeNumbers) {
            String fullName = getEmployeeFullName(empNumber);
            Employee employee = new Employee(empNumber); // Fetch employee data only once

            double totalWorkedHours = totalWorkedHoursMap.getOrDefault(empNumber, 0.0);
            double hourlyRate = employee.getHourlyRate();
            double grossSalary = totalWorkedHours * hourlyRate;

            double sssDeduction = calculateSSSDeduction(grossSalary);
            double philhealthDeduction = calculatePhilHealthDeduction(grossSalary);
            double pagibigDeduction = calculatePagibigDeduction(grossSalary);
            double preTaxDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;
            double withholdingTax = calculateWithholdingTax(grossSalary, preTaxDeductions);

            double totalDeductions = preTaxDeductions + withholdingTax;
            double totalBenefits = employee.getTotalBenefits();
            double netSalary = (grossSalary - totalDeductions) + totalBenefits;

            String[] row = {
                String.valueOf(empNumber),
                fullName,
                String.format("%.2f", grossSalary),
                String.format("%.2f", totalDeductions),
                String.format("%.2f", netSalary)
            };
            payrollSummary.add(row);
        }

        return payrollSummary;
    }

    /**
     * Calculate total worked hours per employee using **attendance records**.
     */
    private HashMap<Integer, Double> calculateTotalWorkedHours() {
        List<String[]> records = readCSV(ATTENDANCE_FILE);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        HashMap<Integer, Double> workedHoursMap = new HashMap<>();
        HashSet<String> processedDates = new HashSet<>();

        for (String[] record : records) {
            if (record.length >= 6) {
                try {
                    int empNumber = Integer.parseInt(record[0].trim());
                    String[] dateParts = record[3].trim().split("/");
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int recordYear = Integer.parseInt(dateParts[2]);

                    if (recordMonth == this.month && recordYear == this.year) {
                        String loginTimeStr = record[4].trim();
                        String logoutTimeStr = record[5].trim();

                        if (loginTimeStr.isEmpty() || logoutTimeStr.isEmpty()) {
                            continue;
                        }

                        LocalTime loginTime = LocalTime.parse(loginTimeStr, timeFormatter);
                        LocalTime logoutTime = LocalTime.parse(logoutTimeStr, timeFormatter);

                        if (logoutTime.isBefore(loginTime)) {
                            continue;
                        }

                        double hoursWorked = Duration.between(loginTime, logoutTime).toMinutes() / 60.0;
                        String uniqueKey = empNumber + "-" + record[3];

                        if (!processedDates.contains(uniqueKey)) {
                            workedHoursMap.put(empNumber, workedHoursMap.getOrDefault(empNumber, 0.0) + hoursWorked);
                            processedDates.add(uniqueKey);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error processing worked hours for Employee #" + record[0]);
                }
            }
        }
        return workedHoursMap;
    }

    /**
     * Returns total **gross salary** for all employees
     */
    public double getTotalGrossSalary() {
        double total = 0.0;
        HashMap<Integer, Double> totalWorkedHoursMap = calculateTotalWorkedHours();

        for (int empNum : getAllEmployeeNumbers()) {
            double hourlyRate = new Employee(empNum).getHourlyRate();
            double grossSalary = totalWorkedHoursMap.getOrDefault(empNum, 0.0) * hourlyRate;
            total += grossSalary;
        }
        return total;
    }


    /**
     *  Returns total **deductions** for all employees
     */
    public double getTotalDeductions() {
        double total = 0.0;
        HashMap<Integer, Double> totalWorkedHoursMap = calculateTotalWorkedHours();

        for (int empNum : getAllEmployeeNumbers()) {
            double hourlyRate = new Employee(empNum).getHourlyRate();
            double grossSalary = totalWorkedHoursMap.getOrDefault(empNum, 0.0) * hourlyRate;

            double sssDeduction = calculateSSSDeduction(grossSalary);
            double philhealthDeduction = calculatePhilHealthDeduction(grossSalary);
            double pagibigDeduction = calculatePagibigDeduction(grossSalary);
            double preTaxDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;
            double withholdingTax = calculateWithholdingTax(grossSalary, preTaxDeductions);

            total += preTaxDeductions + withholdingTax;
        }
        return total;
    }


    public double getTotalNetSalary() {
        double total = 0.0;
        HashMap<Integer, Double> totalWorkedHoursMap = calculateTotalWorkedHours();

        for (int empNum : getAllEmployeeNumbers()) {
            double hourlyRate = new Employee(empNum).getHourlyRate();
            double grossSalary = totalWorkedHoursMap.getOrDefault(empNum, 0.0) * hourlyRate;

            double sssDeduction = calculateSSSDeduction(grossSalary);
            double philhealthDeduction = calculatePhilHealthDeduction(grossSalary);
            double pagibigDeduction = calculatePagibigDeduction(grossSalary);
            double preTaxDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;
            double withholdingTax = calculateWithholdingTax(grossSalary, preTaxDeductions);

            double totalDeductions = preTaxDeductions + withholdingTax;

            // Get non-taxable benefits from Employee
            double totalBenefits = new Employee(empNum).getTotalBenefits();

            // Ensure net salary includes benefits
            double netSalary = (grossSalary - totalDeductions) + totalBenefits;
        
            total += netSalary;
        }
    
        return total;
    }

    private double calculateSSSDeduction(double grossSalary) {
        double[][] sssBrackets = {
            {3250, 135}, {3750, 157.50}, {4250, 180}, {4750, 202.50}, {5250, 225},
            {5750, 247.50}, {6250, 270}, {6750, 292.50}, {7250, 315}, {7750, 337.50},
            {8250, 360}, {8750, 382.50}, {9250, 405}, {9750, 427.50}, {10250, 450},
            {10750, 472.50}, {11250, 495}, {11750, 517.50}, {12250, 540}, {12750, 562.50},
            {Double.MAX_VALUE, 1125}
        };
        for (double[] bracket : sssBrackets) {
            if (grossSalary <= bracket[0]) {
                return bracket[1];
            }
        }
        return 0;
    }

    private double calculatePhilHealthDeduction(double grossSalary) {
        return Math.min(grossSalary * 0.03 / 2, 900);
    }

    private double calculatePagibigDeduction(double grossSalary) {
        return Math.min(grossSalary * 0.02, 100);
    }

    private double calculateWithholdingTax(double grossSalary, double totalDeductions) {
        double taxableIncome = grossSalary - totalDeductions;
        if (taxableIncome <= 20832) return 0;
        else if (taxableIncome <= 33333) return (taxableIncome - 20833) * 0.20;
        else if (taxableIncome <= 66667) return 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome <= 166667) return 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome <= 666667) return 40833.33 + (taxableIncome - 166667) * 0.32;
        else return 200833.33 + (taxableIncome - 666667) * 0.35;
    }
}