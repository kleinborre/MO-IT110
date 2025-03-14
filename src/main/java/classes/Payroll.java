package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Payroll class that manages employee payslips.
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
    public String getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public List<Payslip> getPayslips() {
        return payslips;
    }

    /** Setters */
    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPayslips(List<Payslip> payslips) {
        this.payslips = payslips;
    }

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
        double total = 0;
        for (Payslip payslip : payslips) {
            total += payslip.calculateGrossSalary();
        }
        return total;
    }

    /**
     * Calculates total deductions for all payslips.
     */
    public double getTotalDeductions() {
        double total = 0;
        for (Payslip payslip : payslips) {
            total += payslip.calculateDeductions();
        }
        return total;
    }

    /**
     * Calculates total net salaries for all payslips.
     */
    public double getTotalNetSalaries() {
        double total = 0;
        for (Payslip payslip : payslips) {
            total += payslip.calculateNetSalary();
        }
        return total;
    }

    /**
     * Reads payroll records from CSV.
     */
    @Override
    public Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> payrollRecords = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Skip header row
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) { // Ensure correct column count
                    payrollRecords.put(nextLine[0], nextLine);
                }
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"Month", "Year", "TotalGrossSalary", "TotalDeductions", "TotalNetSalary"};
            writer.writeNext(header); // Write header first

            for (String[] row : data) {
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves payroll summary to CSV.
     */
    public void savePayroll() {
        List<String[]> allPayrolls = new ArrayList<>();

        // Read existing payroll records
        Map<String, String[]> existingPayrolls = readCSV(FILE_PATH);
        allPayrolls.addAll(existingPayrolls.values()); // Convert Map to List

        // Add new payroll record
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