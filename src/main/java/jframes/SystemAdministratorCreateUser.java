/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframes;

import classes.Employee;
import classes.SystemAdministrator;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author STUDY MODE
 */
public class SystemAdministratorCreateUser extends javax.swing.JFrame {

    // Colors for validation
    private static final Color ERROR_COLOR = new Color(255, 200, 200);
    private static final Color OK_COLOR    = Color.WHITE;
    
    private String[] employeeData;

    // Constructor
    public SystemAdministratorCreateUser() {
        initComponents();
        setupRealTimeValidation();
        generateEmployeeNumber();    
        roleBox.setSelectedIndex(-1);
        statusBox.setSelectedIndex(-1);
        positionBox.setSelectedIndex(-1);
        supervisorBox.setSelectedIndex(-1);
        phoneAllowanceBox.setSelectedIndex(-1);
        clothingAllowanceBox.setSelectedIndex(-1);
    }
    
        public SystemAdministratorCreateUser(String[] employeeData) {
        this.employeeData = employeeData;
        initComponents();
        setupRealTimeValidation();
        generateEmployeeNumber();    
        roleBox.setSelectedIndex(-1);
        statusBox.setSelectedIndex(-1);
        positionBox.setSelectedIndex(-1);
        supervisorBox.setSelectedIndex(-1);
        phoneAllowanceBox.setSelectedIndex(-1);
        clothingAllowanceBox.setSelectedIndex(-1);
    }

    // ---------------- A) Generate Next Employee # from Employee.java ----------------
    private void generateEmployeeNumber() {
        // Assumes Employee.getNextEmployeeNumber() returns a String
        String nextNum = Employee.getNextEmployeeNumber();
        employeeNumberText.setText(nextNum);
        employeeNumberText.setEditable(false);
    }

    // ---------------- B) Setup Real-Time Validation on Key Release ----------------
    private void setupRealTimeValidation() {
        KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Object src = e.getSource();

                if      (src == lastNameText) 
                    SystemAdministrator.validateLastName(lastNameText);
                else if (src == firstNameText) 
                    SystemAdministrator.validateFirstName(firstNameText);
                // Removed usernameText and passwordText validations
                else if (src == addressText) 
                    SystemAdministrator.validateAddress(addressText);
                else if (src == phoneNumberText1) 
                    SystemAdministrator.validatePhoneNumber(phoneNumberText1);
                else if (src == sssNumberText) 
                    SystemAdministrator.validateSSSNumber(sssNumberText);
                else if (src == philhealthNumberText) 
                    SystemAdministrator.validatePhilhealth(philhealthNumberText);
                else if (src == tinNumberText) 
                    SystemAdministrator.validateTINNumber(tinNumberText);
                else if (src == pagIbigText) 
                    SystemAdministrator.validatePagIbig(pagIbigText);
                else if (src == basicSalaryText) 
                    SystemAdministrator.validateBasicSalary(basicSalaryText);
                else if (src == grossSemiMonthlyRateText) 
                    SystemAdministrator.validateGrossSemi(grossSemiMonthlyRateText);
                else if (src == riceSubsidyText) 
                    SystemAdministrator.validateRiceSubsidy(riceSubsidyText);
                else if (src == hourlyRateText) 
                    SystemAdministrator.validateHourlyRate(hourlyRateText);
            }
        };

        lastNameText.addKeyListener(adapter);
        firstNameText.addKeyListener(adapter);
        // Removed usernameText and passwordText listeners
        addressText.addKeyListener(adapter);
        phoneNumberText1.addKeyListener(adapter);
        sssNumberText.addKeyListener(adapter);
        philhealthNumberText.addKeyListener(adapter);
        tinNumberText.addKeyListener(adapter);
        pagIbigText.addKeyListener(adapter);
        basicSalaryText.addKeyListener(adapter);
        grossSemiMonthlyRateText.addKeyListener(adapter);
        riceSubsidyText.addKeyListener(adapter);
        hourlyRateText.addKeyListener(adapter);
    }

    // ------------- C) "Create" Button Handler -------------
    public void handleCreateButtonAction() {
        // Check combo box selections
        
        if (showValidationErrors()) {
            return; // Stop execution if validation fails
        }
        
        if (!isComboValid()) {
            JOptionPane.showMessageDialog(
                this,
                "Some dropdown fields are invalid or unselected.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        // Check birthday
        if (!isBirthdayValid()) {
            JOptionPane.showMessageDialog(
                this,
                "Must be 18 years old or older.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        // Check for empty fields
        if (anyEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Some required fields are empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        // Check if any field is invalid (error color)
        if (hasErrorFields()) {
            JOptionPane.showMessageDialog(
                this,
                "One or more fields are invalid. Please correct them.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Display creation confirmation
        JOptionPane.showMessageDialog(
            this,
            "User created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Build final data for creation (22 columns)
        String empNum   = employeeNumberText.getText().trim();
        String lName    = lastNameText.getText();
        String fName    = firstNameText.getText();
        String birthday = new SimpleDateFormat("MM/dd/yyyy").format(birthdayCalendar.getDate());
        String address  = addressText.getText();
        String phone    = phoneNumberText1.getText().trim();
        String sss      = sssNumberText.getText().trim();
        String phil     = philhealthNumberText.getText().trim();
        String tin      = tinNumberText.getText().trim();
        String pagibig  = pagIbigText.getText().trim();
        String basic    = basicSalaryText.getText().trim();
        String semi     = grossSemiMonthlyRateText.getText().trim();
        String rice     = riceSubsidyText.getText().trim();
        String hourRate = hourlyRateText.getText().trim();
        // Generate username and password automatically:
        String uname    = generateUsername(lName);
        String pword    = generatePassword(empNum);
        String rChoice  = roleBox.getSelectedItem().toString().trim();
        String stat     = statusBox.getSelectedItem().toString().trim();
        String pos      = positionBox.getSelectedItem().toString().trim();
        String sup      = supervisorBox.getSelectedItem().toString().trim();
        String phoneAll = phoneAllowanceBox.getSelectedItem().toString().trim();
        String clothAll = clothingAllowanceBox.getSelectedItem().toString().trim();

        String[] newUserData = {
            empNum, lName, fName, birthday, address, phone, 
            sss, phil, tin, pagibig, stat, pos, sup, basic, rice,
            phoneAll, clothAll, semi, hourRate, uname, pword, rChoice
        };

        // Use SystemAdministrator to create user
        SystemAdministrator adminObj = new SystemAdministrator(0, "", "", "");
        adminObj.createUser(newUserData);

        JOptionPane.showMessageDialog(
            this,
            "User created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

        new SystemAdministratorPage(employeeData).setVisible(true);
        dispose();
    }

    // Helper method to generate a username: "motorph" + lowercase(last name)
    private String generateUsername(String lastName) {
        return "motorph" + lastName.toLowerCase();
    }

    // Helper method to generate a default password: "motorph" + employeeNumber + "_"
    private String generatePassword(String empNumber) {
        return "Motorph" + empNumber + "_";
    }

    private boolean isComboValid() {
        if (roleBox.getSelectedIndex() < 0) return false;
        if (statusBox.getSelectedIndex() < 0) return false;
        if (positionBox.getSelectedIndex() < 0) return false;
        if (supervisorBox.getSelectedIndex() < 0) return false;
        if (phoneAllowanceBox.getSelectedIndex() < 0) return false;
        if (clothingAllowanceBox.getSelectedIndex() < 0) return false;
        return true;
    }

    private boolean isBirthdayValid() {
        Date d = birthdayCalendar.getDate();
        if (d == null) return false;
        LocalDate birth = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now   = LocalDate.now();
        return java.time.Period.between(birth, now).getYears() >= 18;
    }

    private boolean anyEmpty() {
        JTextField[] fields = {
            employeeNumberText, lastNameText, firstNameText,
            addressText, phoneNumberText1, sssNumberText, philhealthNumberText,
            tinNumberText, pagIbigText, basicSalaryText, grossSemiMonthlyRateText, riceSubsidyText, hourlyRateText
        };
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasErrorFields() {
        JTextField[] fields = {
            lastNameText, firstNameText,
            addressText, phoneNumberText1, sssNumberText, philhealthNumberText,
            tinNumberText, pagIbigText, basicSalaryText,
            grossSemiMonthlyRateText, riceSubsidyText, hourlyRateText
        };
        for (JTextField f : fields) {
            if (f.getBackground().equals(ERROR_COLOR)) {
                return true;
            }
        }
        return false;
    }

    // ------------- E) "Clear" Button Handler -------------
    public void handleClearButtonAction() {
        lastNameText.setText("");
        firstNameText.setText("");
        addressText.setText("");
        phoneNumberText1.setText("");
        sssNumberText.setText("");
        philhealthNumberText.setText("");
        tinNumberText.setText("");
        pagIbigText.setText("");
        basicSalaryText.setText("");
        riceSubsidyText.setText("1,500");
        grossSemiMonthlyRateText.setText("");
        hourlyRateText.setText("");
        // Removed usernameText and passwordText clear
        birthdayCalendar.setDate(new Date());

        roleBox.setSelectedIndex(-1);
        statusBox.setSelectedIndex(-1);
        positionBox.setSelectedIndex(-1);
        supervisorBox.setSelectedIndex(-1);
        phoneAllowanceBox.setSelectedIndex(-1);
        clothingAllowanceBox.setSelectedIndex(-1);

        JTextField[] fields = {
            lastNameText, firstNameText, addressText, phoneNumberText1,
            sssNumberText, philhealthNumberText, tinNumberText, pagIbigText,
            basicSalaryText, grossSemiMonthlyRateText, riceSubsidyText, hourlyRateText
        };

        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
        }
    }
    
    private boolean showValidationErrors() {
        StringBuilder errorMsg = new StringBuilder();

        if (lastNameText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Last Name\n");
        }
        if (firstNameText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid First Name\n");
        }
        if (addressText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Address\n");
        }
        if (phoneNumberText1.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Phone Number\n");
        }
        if (sssNumberText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid SSS Number\n");
        }
        if (philhealthNumberText.getBackground().equals(ERROR_COLOR)
            || (philhealthNumberText != null && philhealthNumberText.getBackground().equals(ERROR_COLOR))) {
            errorMsg.append("- Invalid PhilHealth Number\n");
        }
        if (tinNumberText.getBackground().equals(ERROR_COLOR)
            || (tinNumberText != null && tinNumberText.getBackground().equals(ERROR_COLOR))) {
            errorMsg.append("- Invalid TIN Number\n");
        }
        if (pagIbigText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Pag-ibig Number\n");
        }
        if (basicSalaryText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Basic Salary\n");
        }
        if (grossSemiMonthlyRateText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Gross Semi-Monthly Rate\n");
        }
        if (riceSubsidyText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Rice Subsidy\n");
        }
        if (hourlyRateText.getBackground().equals(ERROR_COLOR)) {
            errorMsg.append("- Invalid Hourly Rate\n");
        }

        // Combo box validations (optional but useful)
        if (roleBox.getSelectedIndex() == -1) {
            errorMsg.append("- Role is not selected\n");
        }
        if (statusBox.getSelectedIndex() == -1) {
            errorMsg.append("- Status is not selected\n");
        }
        if (positionBox.getSelectedIndex() == -1) {
            errorMsg.append("- Position is not selected\n");
        }
        if (supervisorBox.getSelectedIndex() == -1) {
            errorMsg.append("- Immediate Supervisor is not selected\n");
        }
        if (phoneAllowanceBox.getSelectedIndex() == -1) {
            errorMsg.append("- Phone Allowance is not selected\n");
        }
        if (clothingAllowanceBox.getSelectedIndex() == -1) {
            errorMsg.append("- Clothing Allowance is not selected\n");
        }

        // Birthday validation
        if (birthdayCalendar.getDate() == null) {
            errorMsg.append("- Birthday is not selected\n");
        } else {
            LocalDate birth = birthdayCalendar.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            int age = Period.between(birth, LocalDate.now()).getYears();
            if (age < 18) {
                errorMsg.append("- Must be at least 18 years old\n");
            }
        }

        // If there are errors, show them
        if (errorMsg.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Please fix the following issues:\n\n" + errorMsg.toString(),
                "Validation Summary", JOptionPane.ERROR_MESSAGE);
            return true; // has errors
        }

        return false; // no errors
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backButton = new buttons.whiteButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pagIbigText = new javax.swing.JTextField();
        lastNameText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        phoneAllowanceBox = new javax.swing.JComboBox<>();
        firstNameText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        birthdayCalendar = new com.toedter.calendar.JCalendar();
        jLabel8 = new javax.swing.JLabel();
        addressText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        phoneNumberText1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        sssNumberText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        philhealthNumberText = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tinNumberText = new javax.swing.JTextField();
        roleBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        positionBox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        supervisorBox = new javax.swing.JComboBox<>();
        riceSubsidyText = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        basicSalaryText = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        statusBox = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        clothingAllowanceBox = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        grossSemiMonthlyRateText = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        hourlyRateText = new javax.swing.JTextField();
        clearButton = new buttons.grayButton();
        createUserButton = new buttons.redButton();
        jLabel22 = new javax.swing.JLabel();
        employeeNumberText = new javax.swing.JTextField();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MotorPH Payroll System");
        setMaximumSize(new java.awt.Dimension(960, 540));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backButton.setText("Back");
        backButton.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        getContentPane().add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 110, 40));

        jLabel1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Phone Allowance");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 170, -1, -1));

        jLabel2.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Birthday");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, -1));

        pagIbigText.setBackground(new java.awt.Color(204, 204, 204));
        pagIbigText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        pagIbigText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagIbigTextActionPerformed(evt);
            }
        });
        getContentPane().add(pagIbigText, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 140, -1));

        lastNameText.setBackground(new java.awt.Color(204, 204, 204));
        lastNameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        lastNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastNameTextActionPerformed(evt);
            }
        });
        getContentPane().add(lastNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 140, -1));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Login Role");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        phoneAllowanceBox.setBackground(new java.awt.Color(204, 204, 204));
        phoneAllowanceBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        phoneAllowanceBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2,000", "1,000", "800", "500" }));
        getContentPane().add(phoneAllowanceBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 190, 140, -1));

        firstNameText.setBackground(new java.awt.Color(204, 204, 204));
        firstNameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        firstNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameTextActionPerformed(evt);
            }
        });
        getContentPane().add(firstNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 140, -1));

        jLabel5.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("First Name");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, -1));

        jLabel6.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Last Name");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, -1));

        birthdayCalendar.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(birthdayCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 190, 110));

        jLabel8.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Address");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, -1, -1));

        addressText.setBackground(new java.awt.Color(204, 204, 204));
        addressText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        addressText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressTextActionPerformed(evt);
            }
        });
        getContentPane().add(addressText, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, 140, -1));

        jLabel9.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Phone Number");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 170, -1, -1));

        phoneNumberText1.setBackground(new java.awt.Color(204, 204, 204));
        phoneNumberText1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        phoneNumberText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneNumberText1ActionPerformed(evt);
            }
        });
        getContentPane().add(phoneNumberText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 140, -1));

        jLabel10.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("SSS #");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 220, -1, -1));

        sssNumberText.setBackground(new java.awt.Color(204, 204, 204));
        sssNumberText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        sssNumberText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sssNumberTextActionPerformed(evt);
            }
        });
        getContentPane().add(sssNumberText, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 140, -1));

        jLabel11.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Philhealth #");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, -1, -1));

        philhealthNumberText.setBackground(new java.awt.Color(204, 204, 204));
        philhealthNumberText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        philhealthNumberText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                philhealthNumberTextActionPerformed(evt);
            }
        });
        getContentPane().add(philhealthNumberText, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, 140, -1));

        jLabel12.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("TIN #");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, -1, -1));

        tinNumberText.setBackground(new java.awt.Color(204, 204, 204));
        tinNumberText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        tinNumberText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tinNumberTextActionPerformed(evt);
            }
        });
        getContentPane().add(tinNumberText, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 340, 140, -1));

        roleBox.setBackground(new java.awt.Color(204, 204, 204));
        roleBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        roleBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Employee", "Employee|HRManager", "Employee|PayrollManager", "Employee|SystemAdministrator" }));
        roleBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleBoxActionPerformed(evt);
            }
        });
        getContentPane().add(roleBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 140, -1));

        jLabel13.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Pag-ibig #");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, -1, -1));

        jLabel14.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Position");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, -1, -1));

        positionBox.setBackground(new java.awt.Color(204, 204, 204));
        positionBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        positionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chief Executive Officer", "Chief Operating Officer", "Chief Finance Officer", "Chief Marketing Officer", "IT Operations and Systems", "HR Manager", "HR Team Leader", "HR Rank and File", "Accounting Head", "Payroll Manager", "Payroll Team Leader", "Payroll Rank and File", "Account Manager", "Account Rank and File", "Sales & Marketing", "Supply Chain and Logistics", "Customer Service and Relations" }));
        getContentPane().add(positionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 240, 140, -1));

        jLabel15.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Rice Subsidy");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, -1, -1));

        jLabel16.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Supervisor");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 270, -1, -1));

        supervisorBox.setBackground(new java.awt.Color(204, 204, 204));
        supervisorBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        supervisorBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Garcia, Manuel III", "Lim, Antonio", "Villanueva, Andrea Mae", "San, Jose Brad", "Aquino, Bianca Sofia ", "Alvaro, Roderick", "Salcedo, Anthony", "Lim, Antonio", "Romualdez, Fredrick ", "Mata, Christian", "De Leon, Selena", "Reyes, Isabella" }));
        getContentPane().add(supervisorBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 290, 140, -1));

        riceSubsidyText.setEditable(false);
        riceSubsidyText.setBackground(new java.awt.Color(204, 204, 204));
        riceSubsidyText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        riceSubsidyText.setText("1,500");
        riceSubsidyText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                riceSubsidyTextActionPerformed(evt);
            }
        });
        getContentPane().add(riceSubsidyText, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 140, 140, -1));

        jLabel17.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Basic Salary");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 320, -1, -1));

        basicSalaryText.setBackground(new java.awt.Color(204, 204, 204));
        basicSalaryText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        basicSalaryText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                basicSalaryTextActionPerformed(evt);
            }
        });
        getContentPane().add(basicSalaryText, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 340, 140, -1));

        jLabel18.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Hourly Rate");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 320, -1, -1));

        statusBox.setBackground(new java.awt.Color(204, 204, 204));
        statusBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        statusBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Regular", "Probationary" }));
        getContentPane().add(statusBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, 140, -1));

        jLabel19.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Status");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 170, -1, -1));

        clothingAllowanceBox.setBackground(new java.awt.Color(204, 204, 204));
        clothingAllowanceBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        clothingAllowanceBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1,000", "800", "500", " " }));
        getContentPane().add(clothingAllowanceBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 240, 140, -1));

        jLabel20.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Clothing Allowance");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 220, -1, -1));

        grossSemiMonthlyRateText.setBackground(new java.awt.Color(204, 204, 204));
        grossSemiMonthlyRateText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        grossSemiMonthlyRateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grossSemiMonthlyRateTextActionPerformed(evt);
            }
        });
        getContentPane().add(grossSemiMonthlyRateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 140, -1));

        jLabel21.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Semi-Monthly Rate");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, -1, -1));

        hourlyRateText.setBackground(new java.awt.Color(204, 204, 204));
        hourlyRateText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        hourlyRateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hourlyRateTextActionPerformed(evt);
            }
        });
        getContentPane().add(hourlyRateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 340, 140, -1));

        clearButton.setText("Clear");
        clearButton.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        getContentPane().add(clearButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 150, -1));

        createUserButton.setText("Create User");
        createUserButton.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        createUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createUserButtonActionPerformed(evt);
            }
        });
        getContentPane().add(createUserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 450, 150, -1));

        jLabel22.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Employee #");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 370, -1, -1));

        employeeNumberText.setBackground(new java.awt.Color(204, 204, 204));
        employeeNumberText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        employeeNumberText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeNumberTextActionPerformed(evt);
            }
        });
        getContentPane().add(employeeNumberText, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 390, 140, -1));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Admin Create User.png"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new SystemAdministratorPage(employeeData).setVisible(true);
        dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void pagIbigTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagIbigTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pagIbigTextActionPerformed

    private void lastNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameTextActionPerformed

    private void firstNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameTextActionPerformed

    private void addressTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressTextActionPerformed

    private void phoneNumberText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneNumberText1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneNumberText1ActionPerformed

    private void sssNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sssNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sssNumberTextActionPerformed

    private void philhealthNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_philhealthNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_philhealthNumberTextActionPerformed

    private void tinNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tinNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tinNumberTextActionPerformed

    private void riceSubsidyTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_riceSubsidyTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_riceSubsidyTextActionPerformed

    private void basicSalaryTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_basicSalaryTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_basicSalaryTextActionPerformed

    private void grossSemiMonthlyRateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grossSemiMonthlyRateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grossSemiMonthlyRateTextActionPerformed

    private void hourlyRateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourlyRateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hourlyRateTextActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        handleClearButtonAction();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void createUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createUserButtonActionPerformed
        // TODO add your handling code here:
        handleCreateButtonAction(); // Then do not dispose if there's an error
    }//GEN-LAST:event_createUserButtonActionPerformed

    private void employeeNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeNumberTextActionPerformed

    private void roleBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roleBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorCreateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorCreateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorCreateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SystemAdministratorCreateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SystemAdministratorCreateUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressText;
    private buttons.whiteButton backButton;
    private javax.swing.JLabel background;
    private javax.swing.JTextField basicSalaryText;
    private com.toedter.calendar.JCalendar birthdayCalendar;
    private buttons.grayButton clearButton;
    private javax.swing.JComboBox<String> clothingAllowanceBox;
    private buttons.redButton createUserButton;
    private javax.swing.JTextField employeeNumberText;
    private javax.swing.JTextField firstNameText;
    private javax.swing.JTextField grossSemiMonthlyRateText;
    private javax.swing.JTextField hourlyRateText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField lastNameText;
    private javax.swing.JTextField pagIbigText;
    private javax.swing.JTextField philhealthNumberText;
    private javax.swing.JComboBox<String> phoneAllowanceBox;
    private javax.swing.JTextField phoneNumberText1;
    private javax.swing.JComboBox<String> positionBox;
    private javax.swing.JTextField riceSubsidyText;
    private javax.swing.JComboBox<String> roleBox;
    private javax.swing.JTextField sssNumberText;
    private javax.swing.JComboBox<String> statusBox;
    private javax.swing.JComboBox<String> supervisorBox;
    private javax.swing.JTextField tinNumberText;
    // End of variables declaration//GEN-END:variables
}
