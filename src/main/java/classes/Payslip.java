package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Payslip class that calculates salaries and manages payslip records.
 */
public class Payslip extends Employee implements PayrollCalculator, CSVHandler {

    private double deductions;
    private double grossSalary;
    private double netSalary;
    private double sssDeduction;
    private double philhealthDeduction;
    private double pagibigDeduction;
    private double withholdingTax;

    private static final String FILE_PATH = "src/main/java/databases/Payslip Records.csv";

    /**
     * Constructor for Payslip.
     */
    public Payslip(int employeeNumber) {
        super(employeeNumber);
        this.deductions = 0;
        this.grossSalary = 0;
        this.netSalary = 0;
        this.sssDeduction = 0;
        this.philhealthDeduction = 0;
        this.pagibigDeduction = 0;
        this.withholdingTax = 0;
    }

    /** Getters */
    public double getDeductions() {
        return deductions;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public double getSssDeduction() {
        return sssDeduction;
    }

    public double getPhilhealthDeduction() {
        return philhealthDeduction;
    }

    public double getPagibigDeduction() {
        return pagibigDeduction;
    }

    public double getWithholdingTax() {
        return withholdingTax;
    }

    /** Setters */
    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public void setSssDeduction(double sssDeduction) {
        this.sssDeduction = sssDeduction;
    }

    public void setPhilhealthDeduction(double philhealthDeduction) {
        this.philhealthDeduction = philhealthDeduction;
    }

    public void setPagibigDeduction(double pagibigDeduction) {
        this.pagibigDeduction = pagibigDeduction;
    }

    public void setWithholdingTax(double withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    /**
     * Calculates the gross salary based on employee details.
     */
    @Override
    public double calculateGrossSalary() {
        this.grossSalary = getBasicSalary() + (getHourlyRate() * 160);
        return this.grossSalary;
    }

    /**
     * Calculates deductions from gross salary.
     */
    @Override
    public double calculateDeductions() {
        this.sssDeduction = grossSalary * 0.045;
        this.philhealthDeduction = grossSalary * 0.03;
        this.pagibigDeduction = 200;
        this.withholdingTax = grossSalary * 0.12;

        this.deductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;
        return this.deductions;
    }

    /**
     * Calculates the net salary after deductions.
     */
    @Override
    public double calculateNetSalary() {
        this.netSalary = grossSalary - deductions;
        return this.netSalary;
    }

    /**
     * Reads all payslip records from CSV.
     */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> payslipRecords = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 8) { // Ensure correct column count
                    payslipRecords.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return payslipRecords;
    }

    /**
     * Writes all payslip records back to the CSV file.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) { // "false" ensures overwrite
            String[] header = {"EmployeeNumber", "GrossSalary", "Deductions", "NetSalary", "SSS", "PhilHealth", "Pagibig", "WithholdingTax"};
            writer.writeNext(header); // Write header first
            writer.writeAll(data);    // Write all records
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates and saves a payslip record to CSV.
     */
    public void generatePayslip() {
        calculateGrossSalary();
        calculateDeductions();
        calculateNetSalary();

        List<String[]> allPayslips = readCSV(FILE_PATH);

        // Remove previous payslip if it exists
        allPayslips.removeIf(payslip -> payslip[0].equals(String.valueOf(getEmployeeNumber())));

        // Add new payslip record
        String[] newPayslip = {
            String.valueOf(getEmployeeNumber()),
            String.format("%.2f", grossSalary),
            String.format("%.2f", deductions),
            String.format("%.2f", netSalary),
            String.format("%.2f", sssDeduction),
            String.format("%.2f", philhealthDeduction),
            String.format("%.2f", pagibigDeduction),
            String.format("%.2f", withholdingTax)
        };
        allPayslips.add(newPayslip);

        // Write updated records back to CSV
        writeCSV(FILE_PATH, allPayslips);
        System.out.println("Payslip generated successfully.");
    }
}