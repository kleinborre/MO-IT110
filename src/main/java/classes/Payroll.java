package classes;

import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private String month;
    private int year;
    private List<Payslip> payslips;

    public Payroll(String month, int year) {
        this.month = month;
        this.year = year;
        this.payslips = new ArrayList<>();
    }

    public String getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public List<Payslip> getPayslips() {
        return payslips;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPayslips(List<Payslip> payslips) {
        this.payslips = payslips;
    }
    
    

    public void addPayslip(Payslip payslip) {
        payslips.add(payslip);
    }

    public double getTotalGrossSalaries() {
        double total = 0;
        for (Payslip payslip : payslips) {
            total += payslip.calculateGrossSalary();
        }
        return total;
    }

    public double getTotalDeductions() {
        double total = 0;
        for (Payslip payslip : payslips) {
            total += payslip.calculateDeductions();
        }
        return total;
    }

    public double getTotalNetSalaries() {
        double total = 0;
        for (Payslip payslip : payslips) {
            total += payslip.calculateNetSalary();
        }
        return total;
    }

    public void printPayrollSummary() {
//        System.out.println("\n==== Payroll Summary for " + month + " " + year + " ====");
//        for (Payslip payslip : payslips) {
//            payslip.printPayslip();
//        }
//        System.out.println("Total Gross Salaries: " + getTotalGrossSalaries());
//        System.out.println("Total Deductions: " + getTotalDeductions());
//        System.out.println("Total Net Salaries: " + getTotalNetSalaries());
//        System.out.println("====================================\n");
    }
}