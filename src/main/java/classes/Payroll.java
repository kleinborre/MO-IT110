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
 * Payroll class that manages employee payslips and payroll records.
 */
public class Payroll implements CSVHandler {
    private String month;
    private int year;
    private List<Payslip> payslips;

    private static final String FILE_PATH = "src/main/java/databases/Payroll Records.csv";

    /**
     * Constructor for Payroll.
     */
    public Payroll(String month, int year) {
        this.month = month;
        this.year = year;
        this.payslips = new ArrayList<>();
    }

    /** Getters */
    public String getMonth() { return month; }
    public int getYear() { return year; }
    public List<Payslip> getPayslips() { return payslips; }

    /** Setters */
    public void setMonth(String month) { this.month = month; }
    public void setYear(int year) { this.year = year; }
    public void setPayslips(List<Payslip> payslips) { this.payslips = payslips; }

    /**
     * Adds a payslip to the payroll.
     */
    public void addPayslip(Payslip payslip) {
        payslips.add(payslip);
    }

    /**
     * Calculates total gross salaries for all payslips.
     */
    public double getTotalGrossSalaries() {
        return payslips.stream().mapToDouble(Payslip::calculateGrossSalary).sum();
    }

    /**
     * Calculates total deductions for all payslips.
     */
    public double getTotalDeductions() {
        return payslips.stream().mapToDouble(Payslip::calculateDeductions).sum();
    }

    /**
     * Calculates total net salaries for all payslips.
     */
    public double getTotalNetSalaries() {
        return payslips.stream().mapToDouble(Payslip::calculateNetSalary).sum();
    }

    /**
     * Reads payroll records from CSV.
     */
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> payrollRecords = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                payrollRecords.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return payrollRecords;
    }

    /**
     * Writes payroll records to CSV.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = {"Month", "Year", "TotalGrossSalary", "TotalDeductions", "TotalNetSalary"};
            writer.writeNext(header); // Write header first
            writer.writeAll(data);    // Write actual data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves payroll summary to CSV.
     */
    public void savePayroll() {
        List<String[]> allPayrolls = readCSV(FILE_PATH);

        // Remove any existing payroll entry for the same month & year
        allPayrolls.removeIf(record -> record[0].equals(month) && record[1].equals(String.valueOf(year)));

        // Add the new payroll record
        String[] newPayroll = {
            month,
            String.valueOf(year),
            String.format("%.2f", getTotalGrossSalaries()),
            String.format("%.2f", getTotalDeductions()),
            String.format("%.2f", getTotalNetSalaries())
        };
        allPayrolls.add(newPayroll);

        // Write updated records back to CSV
        writeCSV(FILE_PATH, allPayrolls);
        System.out.println("Payroll saved successfully.");
    }
}