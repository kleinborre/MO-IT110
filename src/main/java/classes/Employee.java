package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.*;

public class Employee implements CSVHandler {
    protected int employeeNumber;
    protected String lastName;
    protected String firstName;
    protected String address;
    protected String phoneNumber;
    protected String sssNumber;
    protected String philhealthNumber;
    protected String pagibigNumber;
    protected String tinNumber;
    protected String status;
    protected String position;
    protected String immediateSupervisor;
    protected double basicSalary;
    protected double riceSubsidy;
    protected double phoneAllowance;
    protected double clothingAllowance;
    protected double grossSemiMonthlyRate;
    protected double hourlyRate;
    protected String username;
    protected String password;
    protected String role;

    private static final String FILE_PATH = "src/main/java/databases/Employee Details.csv";

    // ** Default Constructor **
    public Employee() {}

    // ** Constructor that loads data from CSV using Employee Number **
    public Employee(int employeeNumber) {
        this.employeeNumber = employeeNumber;
        loadEmployeeData();
    }

    /**
     * **Load Employee Data from CSV**
     * - Finds the employee in CSV and assigns values to attributes.
     */
    private void loadEmployeeData() {
        List<String[]> employees = readCSV(FILE_PATH);

        for (String[] data : employees) {
            if (data.length >= 22 && data[0].equals(String.valueOf(employeeNumber))) {
                try {
                    this.lastName = data[1];
                    this.firstName = data[2];
                    this.address = data[4];
                    this.phoneNumber = data[5];
                    this.sssNumber = data[6];
                    this.philhealthNumber = data[7];
                    this.pagibigNumber = data[8];
                    this.tinNumber = data[9];
                    this.status = data[10];
                    this.position = data[11];
                    this.immediateSupervisor = data[12];
                    
                    // **Trim and clean all double values**
                    this.basicSalary = Double.parseDouble(data[13].trim().replace(",", ""));
                    this.riceSubsidy = Double.parseDouble(data[14].trim().replace(",", ""));
                    this.phoneAllowance = Double.parseDouble(data[15].trim().replace(",", ""));
                    this.clothingAllowance = Double.parseDouble(data[16].trim().replace(",", ""));
                    this.grossSemiMonthlyRate = Double.parseDouble(data[17].trim().replace(",", ""));
                    this.hourlyRate = Double.parseDouble(data[18].trim().replace(",", ""));
                    
                    this.username = data[19];
                    this.password = data[20];
                    this.role = data[21];
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing numeric values for Employee #" + employeeNumber);
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // ** Getters **
    public int getEmployeeNumber() { return employeeNumber; }
    public String getFullName() { return lastName + ", " + firstName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // ** Setters (With CSV Update) **
    public void setLastName(String lastName) { this.lastName = lastName; updateCSV(); }
    public void setFirstName(String firstName) { this.firstName = firstName; updateCSV(); }
    public void setAddress(String address) { this.address = address; updateCSV(); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; updateCSV(); }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; updateCSV(); }
    public void setPhilhealthNumber(String philhealthNumber) { this.philhealthNumber = philhealthNumber; updateCSV(); }
    public void setPagibigNumber(String pagibigNumber) { this.pagibigNumber = pagibigNumber; updateCSV(); }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; updateCSV(); }
    public void setStatus(String status) { this.status = status; updateCSV(); }
    public void setPosition(String position) { this.position = position; updateCSV(); }
    public void setImmediateSupervisor(String immediateSupervisor) { this.immediateSupervisor = immediateSupervisor; updateCSV(); }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; updateCSV(); }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; updateCSV(); }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; updateCSV(); }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; updateCSV(); }
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate; updateCSV(); }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; updateCSV(); }
    public void setUsername(String username) { this.username = username; updateCSV(); }
    public void setPassword(String password) { this.password = password; updateCSV(); }
    public void setRole(String role) { this.role = role; updateCSV(); }

    /**
     * **Updates Employee Data in CSV File**
     * - Finds and updates the correct row in the CSV file.
     */
    private void updateCSV() {
        List<String[]> employees = readCSV(FILE_PATH);

        for (String[] data : employees) {
            if (data[0].equals(String.valueOf(employeeNumber))) {
                data[1] = lastName;
                data[2] = firstName;
                data[4] = address;
                data[5] = phoneNumber;
                data[6] = sssNumber;
                data[7] = philhealthNumber;
                data[8] = pagibigNumber;
                data[9] = tinNumber;
                data[10] = status;
                data[11] = position;
                data[12] = immediateSupervisor;
                data[13] = String.valueOf(basicSalary);
                data[14] = String.valueOf(riceSubsidy);
                data[15] = String.valueOf(phoneAllowance);
                data[16] = String.valueOf(clothingAllowance);
                data[17] = String.valueOf(grossSemiMonthlyRate);
                data[18] = String.valueOf(hourlyRate);
                data[19] = username;
                data[20] = password;
                data[21] = role;
                break;
            }
        }

        writeCSV(FILE_PATH, employees);
    }

    // CSV Read Method
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // Skip header
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 22) {
                    employees.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // CSV Write Method
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = {
                "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
                "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position",
                "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate",
                "Username", "Password", "Role"
            };
            writer.writeNext(header);
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}