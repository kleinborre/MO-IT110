package classes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

/**
 * Represents the payslip calculations for a single employee for a given month/year.
 */
public class Payslip extends Employee {

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

    public Payslip(int employeeNumber, int month, int year) {
        super(employeeNumber);
        this.month = month;
        this.year = year;
        resetCalculations();
    }

    public void updatePayslip(int newMonth, int newYear) {
        this.month = newMonth;
        this.year = newYear;
        resetCalculations();
    }

    private void resetCalculations() {
        totalWorkedHours = 0.0;
        overtimeHours = 0.0;
        overtimePay = 0.0;
        grossSalary = 0.0;
        deductions = 0.0;
        netSalary = 0.0;
        sssDeduction = 0.0;
        philhealthDeduction = 0.0;
        pagibigDeduction = 0.0;
        withholdingTax = 0.0;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void processAttendance() {
        totalWorkedHours = 0.0; 
        HashSet<LocalDate> processedDays = new HashSet<>();

        List<String[]> records = Attendance.getAllAttendanceRecords();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        for (String[] row : records) {
            if (row.length >= 6 && row[0].trim().equals(String.valueOf(employeeNumber))) {
                try {
                    LocalDate date = LocalDate.parse(row[3].trim(), dateFormatter);
                    if (date.getMonthValue() == month && date.getYear() == year && !processedDays.contains(date)) {
                        String loginStr = row[4].trim();
                        String logoutStr = row[5].trim();

                        if (!loginStr.isEmpty() && !logoutStr.isEmpty()) {
                            LocalTime loginTime = LocalTime.parse(loginStr, timeFormatter);
                            LocalTime logoutTime = LocalTime.parse(logoutStr, timeFormatter);

                            if (logoutTime.isAfter(loginTime)) {
                                double hoursWorked = Duration.between(loginTime, logoutTime).toMinutes() / 60.0;
                                totalWorkedHours += hoursWorked;
                                processedDays.add(date);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing attendance for Employee #" + employeeNumber + ": " + e.getMessage());
                }
            }
        }
    }

    public void processOvertime() {
        overtimeHours = 0.0;
        overtimePay = 0.0;

        List<String[]> otRecords = OvertimeRequest.getAllOvertimeRequests();
        DateTimeFormatter csvDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Correct CSV format

        for (String[] record : otRecords) {
            if (record.length >= 6 && record[0].equals(String.valueOf(employeeNumber))) {
                try {
                    LocalDate overtimeDate = LocalDate.parse(record[2].trim(), csvDateFormatter);

                    // Ensure overtime is counted only for the selected month & year
                    if (overtimeDate.getMonthValue() == month && overtimeDate.getYear() == year) {
                        double oh = Double.parseDouble(record[3].trim()); // Overtime hours
                        double op = Double.parseDouble(record[4].trim()); // Overtime pay
                        String status = record[5].trim().replace("\"", ""); // Remove "" if exists

                        // Only add to the payslip if the status is "Approved"
                        if (status.equalsIgnoreCase("Approved")) {
                            overtimeHours += oh;
                            overtimePay += op;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing overtime for Employee #" + employeeNumber + " on " + record[2] + ": " + e.getMessage());
                }
            }
        }
    }

    public void calculateGrossSalary() {
        // Ensures correct addition of overtime pay
        grossSalary = (totalWorkedHours * getHourlyRate()) + overtimePay;
    }

    public void calculateDeductions() {
        if (grossSalary == 0) {
            sssDeduction = 0;
            philhealthDeduction = 0;
            pagibigDeduction = 0;
            withholdingTax = 0;
        } else {
            calculateSSS();
            calculatePhilhealth();
            calculatePagibig();
            calculateWithholdingTax();
        }
        deductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;
    }


    public void calculateNetSalary() {
        if (grossSalary == 0 && deductions == 0) {
            netSalary = 0; // Ensures net salary is 0 if there's no gross salary or deductions
        } else {
            netSalary = grossSalary - deductions + getTotalBenefits(); // Proper calculation
        }
    }

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
                return;
            }
        }
    }
    
    public double getComputedBasicSalary() {
        return totalWorkedHours * getHourlyRate();
    }

    private void calculatePhilhealth() {
        philhealthDeduction = Math.min(grossSalary * 0.03 / 2, 900);
    }

    private void calculatePagibig() {
        pagibigDeduction = Math.min(grossSalary * 0.02, 100);
    }

    private void calculateWithholdingTax() {
        double taxableIncome = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction);
        if (taxableIncome <= 20832) {
            withholdingTax = 0;
        } else if (taxableIncome <= 33333) {
            withholdingTax = (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome <= 66667) {
            withholdingTax = 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome <= 166667) {
            withholdingTax = 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666667) {
            withholdingTax = 40833.33 + (taxableIncome - 166667) * 0.32;
        } else {
            withholdingTax = 200833.33 + (taxableIncome - 666667) * 0.35;
        }
    }

    public String getStartDate() {
        return findAttendanceDate(true);
    }

    public String getEndDate() {
        return findAttendanceDate(false);
    }

    private String findAttendanceDate(boolean earliest) {
        List<String[]> records = Attendance.getAllAttendanceRecords();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate targetDate = null;

        for (String[] row : records) {
            if (row.length >= 6 && row[0].trim().equals(String.valueOf(employeeNumber))) {
                try {
                    LocalDate date = LocalDate.parse(row[3].trim(), dateFormatter);
                    if (date.getMonthValue() == month && date.getYear() == year) {
                        if (earliest && (targetDate == null || date.isBefore(targetDate))) {
                            targetDate = date;
                        } else if (!earliest && (targetDate == null || date.isAfter(targetDate))) {
                            targetDate = date;
                        }
                    }
                } catch (Exception e) {
                    // Ignore parse errors
                }
            }
        }
        return (targetDate != null) ? targetDate.format(dateFormatter) : "No Records";
    }

    public double getSssDeduction() { return sssDeduction; }
    public double getPhilhealthDeduction() { return philhealthDeduction; }
    public double getPagibigDeduction() { return pagibigDeduction; }
    public double getWithholdingTax() { return withholdingTax; }

    public double getTotalWorkedHours() { return totalWorkedHours; }
    public double getOvertimeHours() { return overtimeHours; }
    public double getOvertimePay() { return overtimePay; }
    public double getGrossSalary() { return grossSalary; }
    public double getNetSalary() { return netSalary; }
    public double getTotalDeductions() { return deductions; }
}
