/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframes;

import classes.SystemAdministrator;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author STUDY MODE
 */
public class SystemAdministratorUpdateUser extends javax.swing.JFrame {

    private String[] selectedUser; // 22 columns
    private static final DecimalFormat formatter = new DecimalFormat("#,##0");

    // Validation Colors (still used for local references, but real checks are in SystemAdministrator)
    private static final Color ERROR_COLOR = new Color(255, 200, 200);
    private static final Color OK_COLOR    = Color.WHITE;

    public SystemAdministratorUpdateUser(String[] selectedUser) {
        this.selectedUser = selectedUser;
        initComponents();
        populateFields(selectedUser);
        setupRealTimeValidation();
    }

    /**
     * Fill the form fields with the user data.
     */
    private void populateFields(String[] userData) {
        if (userData == null || userData.length != 22) {
            System.out.println("Error: userData must be 22 columns, got " 
                               + (userData != null ? userData.length : 0));
            return;
        }

        employeeNumberText.setText(userData[0]);
        employeeNumberText.setEditable(false);
        lastNameText.setText(userData[1]);
        firstNameText.setText(userData[2]);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormat.parse(userData[3]);
            birthdayCalendar.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addressText.setText(userData[4]);
        phoneNumberText1.setText(userData[5]);
        sssNumberText.setText(userData[6]);
        philhealthNumberText1.setText(userData[7]);
        tinNumberText1.setText(userData[8]);
        pagIbigText.setText(userData[9]);
        statusBox.setSelectedItem(userData[10]);
        positionBox.setSelectedItem(userData[11]);
        supervisorBox.setSelectedItem(userData[12]);

        basicSalaryText.setText(formatNumber(userData[13]));
        riceSubsidyText.setText(formatNumber(userData[14]));
        phoneAllowanceBox.setSelectedItem(formatNumber(userData[15]));
        clothingAllowanceBox.setSelectedItem(formatNumber(userData[16]));
        grossSemiMonthlyRateText.setText(formatNumber(userData[17]));
        hourlyRateText.setText(formatNumber(userData[18]));

        usernameText.setText(userData[19]);
        passwordText.setText(userData[20]);
        roleBox.setSelectedItem(userData[21]);
    }

    /**
     * Utility for formatting numeric strings with commas.
     */
    private String formatNumber(String number) {
        try {
            return formatter.format(Long.parseLong(number.replace(",", "").trim()));
        } catch (NumberFormatException e) {
            return number; // If not purely numeric, just return as-is
        }
    }

    /**
     * Sets up real-time validation by attaching a KeyAdapter 
     * that calls the static methods in SystemAdministrator.
     */
    private void setupRealTimeValidation() {
        KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Object src = e.getSource();

                if      (src == lastNameText) 
                    SystemAdministrator.validateLastName(lastNameText);
                else if (src == firstNameText) 
                    SystemAdministrator.validateFirstName(firstNameText);
                else if (src == usernameText) 
                    SystemAdministrator.validateUsername(usernameText);
                else if (src == passwordText) 
                    SystemAdministrator.validatePassword(passwordText);
                else if (src == addressText) 
                    SystemAdministrator.validateAddress(addressText);
                else if (src == phoneNumberText1) 
                    SystemAdministrator.validatePhoneNumber(phoneNumberText1);
                else if (src == sssNumberText) 
                    SystemAdministrator.validateSSSNumber(sssNumberText);
                else if (src == tinNumberText1) 
                    SystemAdministrator.validateTINNumber(tinNumberText1);
                else if (src == philhealthNumberText1) 
                    SystemAdministrator.validatePhilhealth(philhealthNumberText1);
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
        usernameText.addKeyListener(adapter);
        passwordText.addKeyListener(adapter);
        addressText.addKeyListener(adapter);
        phoneNumberText1.addKeyListener(adapter);
        sssNumberText.addKeyListener(adapter);
        tinNumberText1.addKeyListener(adapter);
        philhealthNumberText1.addKeyListener(adapter);
        pagIbigText.addKeyListener(adapter);
        basicSalaryText.addKeyListener(adapter);
        grossSemiMonthlyRateText.addKeyListener(adapter);
        riceSubsidyText.addKeyListener(adapter);
        hourlyRateText.addKeyListener(adapter);
    }
    
    /**
     * Resets field background colors to white (called after clearing).
     */
    private void resetValidationColors() {
        JTextField[] fields = {
            lastNameText, firstNameText, usernameText, passwordText, 
            addressText, phoneNumberText1, sssNumberText, philhealthNumberText1,
            tinNumberText1, pagIbigText, basicSalaryText, grossSemiMonthlyRateText, 
            riceSubsidyText, hourlyRateText
        };
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
        }
    }
    
    /**
     * Checks if any required field is empty.
     */
    private boolean anyEmpty() {
        String[] fields = {
            lastNameText.getText(),
            firstNameText.getText(),
            addressText.getText(),
            phoneNumberText1.getText().trim(),
            sssNumberText.getText().trim(),
            philhealthNumberText1.getText().trim(),
            tinNumberText1.getText().trim(),
            pagIbigText.getText().trim(),
            basicSalaryText.getText().trim(),
            riceSubsidyText.getText().trim(),
            grossSemiMonthlyRateText.getText().trim(),
            hourlyRateText.getText().trim(),
            usernameText.getText().trim(),
            passwordText.getText().trim()
        };
        for (String field : fields) {
            if (field.isEmpty()) {
                return true;
            }
        }
        return false;
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
        jLabel7 = new javax.swing.JLabel();
        usernameText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        passwordText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        roleBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        firstNameText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lastNameText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        birthdayCalendar = new com.toedter.calendar.JCalendar();
        jLabel8 = new javax.swing.JLabel();
        addressText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        phoneNumberText1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        sssNumberText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        philhealthNumberText1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tinNumberText1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        pagIbigText = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        statusBox = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        positionBox = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        supervisorBox = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        basicSalaryText = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        riceSubsidyText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        phoneAllowanceBox = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        clothingAllowanceBox = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        grossSemiMonthlyRateText = new javax.swing.JTextField();
        clearButton = new buttons.grayButton();
        UpdateUserButton = new buttons.redButton();
        jLabel18 = new javax.swing.JLabel();
        hourlyRateText = new javax.swing.JTextField();
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

        jLabel7.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Employee #");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 370, -1, -1));

        usernameText.setBackground(new java.awt.Color(204, 204, 204));
        usernameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        usernameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameTextActionPerformed(evt);
            }
        });
        getContentPane().add(usernameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 140, -1));

        jLabel4.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Password");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, -1));

        passwordText.setBackground(new java.awt.Color(204, 204, 204));
        passwordText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        passwordText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordTextActionPerformed(evt);
            }
        });
        getContentPane().add(passwordText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 140, -1));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Login Role");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, -1));

        roleBox.setBackground(new java.awt.Color(204, 204, 204));
        roleBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        roleBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Employee", "Employee|HRManager", "Employee|PayrollManager", "Employee|SystemAdministrator" }));
        getContentPane().add(roleBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 140, -1));

        jLabel5.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("First Name");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, -1));

        firstNameText.setBackground(new java.awt.Color(204, 204, 204));
        firstNameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        firstNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameTextActionPerformed(evt);
            }
        });
        getContentPane().add(firstNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 140, -1));

        jLabel6.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Last Name");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, -1, -1));

        lastNameText.setBackground(new java.awt.Color(204, 204, 204));
        lastNameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        lastNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastNameTextActionPerformed(evt);
            }
        });
        getContentPane().add(lastNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 140, -1));

        jLabel2.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Birthday");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, -1, -1));

        birthdayCalendar.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(birthdayCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 390, 190, 110));

        jLabel8.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Address");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, -1, -1));

        addressText.setBackground(new java.awt.Color(204, 204, 204));
        addressText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        addressText.setText("N/A");
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
        phoneNumberText1.setText("N/A");
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
        sssNumberText.setText("N/A");
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

        philhealthNumberText1.setBackground(new java.awt.Color(204, 204, 204));
        philhealthNumberText1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        philhealthNumberText1.setText("N/A");
        philhealthNumberText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                philhealthNumberText1ActionPerformed(evt);
            }
        });
        getContentPane().add(philhealthNumberText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, 140, -1));

        jLabel12.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("TIN #");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, -1, -1));

        tinNumberText1.setBackground(new java.awt.Color(204, 204, 204));
        tinNumberText1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        tinNumberText1.setText("N/A");
        tinNumberText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tinNumberText1ActionPerformed(evt);
            }
        });
        getContentPane().add(tinNumberText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 340, 140, -1));

        jLabel13.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Pag-ibig #");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, -1, -1));

        pagIbigText.setBackground(new java.awt.Color(204, 204, 204));
        pagIbigText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        pagIbigText.setText("N/A");
        pagIbigText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagIbigTextActionPerformed(evt);
            }
        });
        getContentPane().add(pagIbigText, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 140, -1));

        jLabel19.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Status");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 170, -1, -1));

        statusBox.setBackground(new java.awt.Color(204, 204, 204));
        statusBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        statusBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Regular", "Probationary" }));
        getContentPane().add(statusBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, 140, -1));

        jLabel14.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Position");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, -1, -1));

        positionBox.setBackground(new java.awt.Color(204, 204, 204));
        positionBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        positionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chief Executive Officer", "Chief Operating Officer", "Chief Finance Officer", "Chief Marketing Officer", "IT Operations and Systems", "HR Manager", "HR Team Leader", "HR Rank and File", "Accounting Head", "Payroll Manager", "Payroll Team Leader", "Payroll Rank and File", "Account Manager", "Account Rank and File", "Sales & Marketing", "Supply Chain and Logistics", "Customer Service and Relations" }));
        getContentPane().add(positionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 240, 140, -1));

        jLabel16.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Supervisor");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 270, -1, -1));

        supervisorBox.setBackground(new java.awt.Color(204, 204, 204));
        supervisorBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        supervisorBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Garcia, Manuel III", "Lim, Antonio", "Villanueva, Andrea Mae", "San, Jose Brad", "Aquino, Bianca Sofia ", "Alvaro, Roderick", "Salcedo, Anthony", "Lim, Antonio", "Romualdez, Fredrick ", "Mata, Christian", "De Leon, Selena", "Reyes, Isabella" }));
        getContentPane().add(supervisorBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 290, 140, -1));

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

        jLabel15.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Rice Subsidy");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, -1, -1));

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

        jLabel1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Phone Allowance");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 170, -1, -1));

        phoneAllowanceBox.setBackground(new java.awt.Color(204, 204, 204));
        phoneAllowanceBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        phoneAllowanceBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2,000", "1,000", "800", "500" }));
        phoneAllowanceBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneAllowanceBoxActionPerformed(evt);
            }
        });
        getContentPane().add(phoneAllowanceBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 190, 140, -1));

        jLabel20.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Clothing Allowance");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 220, -1, -1));

        clothingAllowanceBox.setBackground(new java.awt.Color(204, 204, 204));
        clothingAllowanceBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        clothingAllowanceBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1,000", "800", "500" }));
        getContentPane().add(clothingAllowanceBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 240, 140, -1));

        jLabel21.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Semi-Monthly Rate");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, -1, -1));

        grossSemiMonthlyRateText.setBackground(new java.awt.Color(204, 204, 204));
        grossSemiMonthlyRateText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        grossSemiMonthlyRateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grossSemiMonthlyRateTextActionPerformed(evt);
            }
        });
        getContentPane().add(grossSemiMonthlyRateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 140, -1));

        clearButton.setText("Clear");
        clearButton.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        getContentPane().add(clearButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 150, -1));

        UpdateUserButton.setText("Update User");
        UpdateUserButton.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        UpdateUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateUserButtonActionPerformed(evt);
            }
        });
        getContentPane().add(UpdateUserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 450, 150, -1));

        jLabel18.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Hourly Rate");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 320, -1, -1));

        hourlyRateText.setBackground(new java.awt.Color(204, 204, 204));
        hourlyRateText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        hourlyRateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hourlyRateTextActionPerformed(evt);
            }
        });
        getContentPane().add(hourlyRateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 340, 140, -1));

        jLabel22.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Username");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        employeeNumberText.setBackground(new java.awt.Color(204, 204, 204));
        employeeNumberText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        employeeNumberText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeNumberTextActionPerformed(evt);
            }
        });
        getContentPane().add(employeeNumberText, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 390, 140, -1));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Admin Update User.png"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        this.dispose(); 
    }//GEN-LAST:event_backButtonActionPerformed

    private void usernameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameTextActionPerformed

    private void passwordTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordTextActionPerformed

    private void firstNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameTextActionPerformed

    private void lastNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameTextActionPerformed

    private void addressTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressTextActionPerformed

    private void phoneNumberText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneNumberText1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneNumberText1ActionPerformed

    private void sssNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sssNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sssNumberTextActionPerformed

    private void philhealthNumberText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_philhealthNumberText1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_philhealthNumberText1ActionPerformed

    private void tinNumberText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tinNumberText1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tinNumberText1ActionPerformed

    private void pagIbigTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagIbigTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pagIbigTextActionPerformed

    private void basicSalaryTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_basicSalaryTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_basicSalaryTextActionPerformed

    private void riceSubsidyTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_riceSubsidyTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_riceSubsidyTextActionPerformed

    private void grossSemiMonthlyRateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grossSemiMonthlyRateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grossSemiMonthlyRateTextActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // Reset text fields
        lastNameText.setText("");
        firstNameText.setText("");
        addressText.setText("");
        phoneNumberText1.setText("");
        sssNumberText.setText("");
        philhealthNumberText1.setText("");
        tinNumberText1.setText("");
        pagIbigText.setText("");
        basicSalaryText.setText("");
        riceSubsidyText.setText("1,500"); // Default
        grossSemiMonthlyRateText.setText("");
        hourlyRateText.setText("");
        usernameText.setText("");
        passwordText.setText("");
        birthdayCalendar.setDate(new Date()); // Set to today

        // Reset dropdown selections
        roleBox.setSelectedIndex(-1);
        statusBox.setSelectedIndex(-1);
        positionBox.setSelectedIndex(-1);
        supervisorBox.setSelectedIndex(-1);
        phoneAllowanceBox.setSelectedIndex(-1);
        clothingAllowanceBox.setSelectedIndex(-1);

        // Reset background colors
        resetValidationColors();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void UpdateUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateUserButtonActionPerformed
        // Validate required fields
        if (anyEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", 
                                          "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String birthday = dateFormat.format(birthdayCalendar.getDate());

        // Build updated user data array (22 fields)
        String[] updatedUser = new String[22];
        updatedUser[0]  = employeeNumberText.getText().trim();
        updatedUser[1]  = lastNameText.getText();
        updatedUser[2]  = firstNameText.getText();
        updatedUser[3]  = birthday;
        updatedUser[4]  = addressText.getText();
        updatedUser[5]  = phoneNumberText1.getText().trim();
        updatedUser[6]  = sssNumberText.getText().trim();
        updatedUser[7]  = philhealthNumberText1.getText().trim();
        updatedUser[8]  = tinNumberText1.getText().trim();  // Notice order fix if needed
        updatedUser[9]  = pagIbigText.getText().trim();     //  ...
        updatedUser[10] = statusBox.getSelectedItem().toString().trim();
        updatedUser[11] = positionBox.getSelectedItem().toString().trim();
        updatedUser[12] = supervisorBox.getSelectedItem().toString().trim();
        updatedUser[13] = basicSalaryText.getText().trim();
        updatedUser[14] = riceSubsidyText.getText().trim();
        updatedUser[15] = phoneAllowanceBox.getSelectedItem().toString();
        updatedUser[16] = clothingAllowanceBox.getSelectedItem().toString();
        updatedUser[17] = grossSemiMonthlyRateText.getText().trim();
        updatedUser[18] = hourlyRateText.getText().trim();
        updatedUser[19] = usernameText.getText().trim();
        updatedUser[20] = passwordText.getText().trim();
        updatedUser[21] = roleBox.getSelectedItem().toString().trim();

        // Update user via SystemAdministrator
        SystemAdministrator admin = new SystemAdministrator(0, "", "", "");
        admin.updateUser(updatedUser[0], updatedUser);

        JOptionPane.showMessageDialog(this, "User updated successfully!", 
                                      "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh the main admin page if open
        for (java.awt.Window window : java.awt.Window.getWindows()) {
            if (window instanceof SystemAdministratorPage) {
                ((SystemAdministratorPage) window).loadUserData(); 
                window.setVisible(true);
                break;
            }
        }
        this.dispose();
    }//GEN-LAST:event_UpdateUserButtonActionPerformed

    private void hourlyRateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourlyRateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hourlyRateTextActionPerformed

    private void employeeNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeNumberTextActionPerformed

    private void phoneAllowanceBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneAllowanceBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneAllowanceBoxActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(SystemAdministratorUpdateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    /* Create and display the form with test data */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Provide test data to avoid constructor error
            String[] testUser = {
                "10001", "Garcia", "Manuel III", "10/11/1983", "Valero Carpark Building",
                "966-860-270", "44-4506057-3", "820126853951", "442-605-657-000", "691295330870",
                "Regular", "Chief Executive Officer", "N/A", "90000", "1500", "2000",
                "1000", "45000", "535.71", "employee1", "password", "employee"
            };
            new SystemAdministratorUpdateUser(testUser).setVisible(true);
        }
    });
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private buttons.redButton UpdateUserButton;
    private javax.swing.JTextField addressText;
    private buttons.whiteButton backButton;
    private javax.swing.JLabel background;
    private javax.swing.JTextField basicSalaryText;
    private com.toedter.calendar.JCalendar birthdayCalendar;
    private buttons.grayButton clearButton;
    private javax.swing.JComboBox<String> clothingAllowanceBox;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField lastNameText;
    private javax.swing.JTextField pagIbigText;
    private javax.swing.JTextField passwordText;
    private javax.swing.JTextField philhealthNumberText1;
    private javax.swing.JComboBox<String> phoneAllowanceBox;
    private javax.swing.JTextField phoneNumberText1;
    private javax.swing.JComboBox<String> positionBox;
    private javax.swing.JTextField riceSubsidyText;
    private javax.swing.JComboBox<String> roleBox;
    private javax.swing.JTextField sssNumberText;
    private javax.swing.JComboBox<String> statusBox;
    private javax.swing.JComboBox<String> supervisorBox;
    private javax.swing.JTextField tinNumberText1;
    private javax.swing.JTextField usernameText;
    // End of variables declaration//GEN-END:variables
}
