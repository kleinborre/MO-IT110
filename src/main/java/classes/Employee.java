package classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Employee implements CSVHandler {
    protected int employeeNumber;
    protected String lastName;
    protected String firstName;
    protected String birthday;
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

    private static final String DATABASES_FOLDER = "databases";
    private static final String FILE_NAME = "Employee Details.csv";

    // ** Default Constructor **
    public Employee() {}

    // ** Constructor that loads employee data from CSV using Employee Number **
    public Employee(int employeeNumber) {
        this.employeeNumber = employeeNumber;
        loadEmployeeData();
    }

    // ** Retrieve Employee CSV File **
    public static File getCSVFile() {
        String userDir = System.getProperty("user.dir");
        File csvFile = new File(userDir, DATABASES_FOLDER + File.separator + FILE_NAME);

        if (!csvFile.exists()) {
            InputStream internalFile = Employee.class.getClassLoader().getResourceAsStream(DATABASES_FOLDER + "/" + FILE_NAME);
            if (internalFile != null) {
                try {
                    File directory = new File(userDir, DATABASES_FOLDER);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    Files.copy(internalFile, csvFile.toPath());
                    System.out.println(" Copied " + FILE_NAME + " from resources.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return csvFile;
    }

    // ** Load Employee Data from CSV **
    private void loadEmployeeData() {
        List<String[]> employees = readCSV(getCSVFile().getPath());

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

                    this.basicSalary = Double.parseDouble(data[13].trim().replace(",", ""));
                    this.riceSubsidy = Double.parseDouble(data[14].trim().replace(",", ""));
                    this.phoneAllowance = Double.parseDouble(data[15].trim().replace(",", ""));
                    this.clothingAllowance = Double.parseDouble(data[16].trim().replace(",", ""));
                    this.grossSemiMonthlyRate = Double.parseDouble(data[17].trim().replace(",", ""));
                    this.hourlyRate = Double.parseDouble(data[18].trim().replace(",", ""));
                } catch (NumberFormatException e) {
                    System.err.println("❌ Error parsing numeric values for Employee #" + employeeNumber);
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
    public double getTotalBenefits() { return riceSubsidy + phoneAllowance + clothingAllowance; }
    public String getBirthday() {return birthday;}

    // ** Setters that update CSV **
    public void setAddress(String address) {
        this.address = address;
        updateCSV();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateCSV();
    }

    // ** Update Employee Data in CSV **
    public void updateEmployeeInfo(String[] updatedData) {
        List<String[]> employees = readCSV(getCSVFile().getPath()); // Read all employees
        boolean isUpdated = false;

        for (int i = 0; i < employees.size(); i++) {
            String[] empData = employees.get(i);

            // Ensure the correct row is found (by employeeNumber)
            if (empData.length >= 22 && empData[0].equals(updatedData[0])) {
                employees.set(i, updatedData); // Update the correct row
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            writeCSV(getCSVFile().getPath(), employees); // Write updated data back to CSV
            System.out.println("Employee data updated successfully in CSV.");
        } else {
            System.err.println("Employee update failed: Employee not found.");
        }
    }

    private void updateCSV() {
        File csvFile = getCSVFile();
        List<String[]> employees = readCSV(csvFile.getPath());

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i)[0].equals(String.valueOf(employeeNumber))) {
                employees.get(i)[4] = this.address; // Update Address
                employees.get(i)[5] = this.phoneNumber; // Update Phone Number
                break;
            }
        }

        // ** Write updated data back to CSV **
        writeCSV(csvFile.getPath(), employees);
    }


    // ** Read CSV File **
    @Override
    public List<String[]> readCSV(String filePath) {
        List<String[]> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext();
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

    // ** Write to CSV File **
    @Override
    public void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false))) {
            String[] header = {
                "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
                "SSS #", "Philhealth #", "Pagibig #", "TIN #", "Status", "Position",
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

    // ** Get All Employees for Table Display **
    public static List<String[]> getAllEmployees() {
        Employee empInstance = new Employee();
        return empInstance.readCSV(getCSVFile().getPath());
    }
}