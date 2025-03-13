package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Payslip extends Employee implements PayrollCalculator{
    private double deductions;
    private double grossSalary;
    private double netSalary;
    private double sssDeduction;
    private double philhealthDeduction;
    private double pagibigDeduction;
    private double withholdingTax;

    public Payslip(int employeeNumber, 
                   double deductions, double grossSalary, double netSalary, double sssDeduction,
                   double philhealthDeduction, double pagibigDeduction, double witholdingTax) {
        super(employeeNumber);
        this.deductions = deductions;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
        this.sssDeduction = sssDeduction;
        this.philhealthDeduction = philhealthDeduction;
        this.pagibigDeduction = pagibigDeduction;
        this.withholdingTax = witholdingTax;
    }
    
    @Override
    public double calculateGrossSalary() {
        grossSalary = getBasicSalary() + (getHourlyRate() * 160);
        return grossSalary;
    }
    
    @Override
    public double calculateDeductions() {
        sssDeduction = grossSalary * 0.045;
        philhealthDeduction = grossSalary * 0.03;
        pagibigDeduction = 200;
        withholdingTax = grossSalary * 0.12;

        deductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;
        return deductions;
    }
    
    @Override
    public double calculateNetSalary() {
        netSalary = grossSalary - deductions;
        return netSalary;
    }
    
    

//    @Override
//    public void readFromCSV(String filePath) {
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println("Reading: " + line);
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading CSV: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void writeToCSV(String filePath) {
//        try (FileWriter writer = new FileWriter(filePath, true)) {
//            writer.write(employeeNumber + "," + grossSalary + "," + deductions + "," + netSalary + "\n");
//            System.out.println("Payslip saved to CSV.");
//        } catch (IOException e) {
//            System.err.println("Error writing CSV: " + e.getMessage());
//        }
//    }

    public void printPayslip() {}
    
    
}
 