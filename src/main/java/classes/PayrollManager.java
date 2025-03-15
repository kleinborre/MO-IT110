package classes;

import java.util.List;

/**
 * PayrollManager class that manages payroll operations.
 */
public class PayrollManager extends Employee {
    
    /**
     * Constructor for PayrollManager.
     */
    public PayrollManager(int employeeNumber) {
        super(employeeNumber);
    }

    /**
     * Creates a new payroll for a given month and year.
     */
    public Payroll createPayroll(String month, int year) {
        return new Payroll(month, year);
    }

    /**
     * Adds a payslip to the payroll.
     */
    public void addPayslipToPayroll(Payroll payroll, Payslip payslip) {
        payroll.addPayslip(payslip);
    }

    /**
     * Calculates and finalizes payroll.
     */
    public void finalizePayroll(Payroll payroll) {
        System.out.println("Finalizing payroll for " + payroll.getMonth() + " " + payroll.getYear());
        System.out.println("Total Gross Salary: " + payroll.getTotalGrossSalaries());
        System.out.println("Total Deductions: " + payroll.getTotalDeductions());
        System.out.println("Total Net Salary: " + payroll.getTotalNetSalaries());

        payroll.savePayroll(); // Saves payroll to CSV
        System.out.println("Payroll finalized and saved.");
    }

    /**
     * Views payroll summary.
     */
    public void viewPayrollSummary(Payroll payroll) {
        System.out.println("\n=== Payroll Summary for " + payroll.getMonth() + " " + payroll.getYear() + " ===");
        System.out.println("Total Gross Salaries: " + payroll.getTotalGrossSalaries());
        System.out.println("Total Deductions: " + payroll.getTotalDeductions());
        System.out.println("Total Net Salaries: " + payroll.getTotalNetSalaries());

        List<Payslip> payslips = payroll.getPayslips();
        for (Payslip payslip : payslips) {
            System.out.println("Employee #" + payslip.getEmployeeNumber() + " - Net Salary: " + payslip.calculateNetSalary());
        }
        System.out.println("==========================================\n");
    }
}