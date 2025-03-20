/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframes;

import classes.SystemAdministrator;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 *
 * @author STUDY MODE
 */
public class EmployeeProfileInformationUpdateInfo extends javax.swing.JFrame {
    
    private String[] employeeData; // 22 columns (joined)
    private static final Color ERROR_COLOR = new Color(255, 200, 200); // Light Red
    private static final Color OK_COLOR = Color.WHITE; // Default
    private long lastTypedTime = 0;

    public EmployeeProfileInformationUpdateInfo(String[] employeeData) {
        this.employeeData = employeeData;
        initComponents();
        populateProfileInfo();
        setupRealTimeValidation();
    }

    public EmployeeProfileInformationUpdateInfo() {
        initComponents();
        setupRealTimeValidation();
    }

    /**
     * Fill user details in UI fields.
     */
    private void populateProfileInfo() {
        if (employeeData == null || employeeData.length != 22) {
            System.err.println("Error: Employee data is missing or incorrect.");
            return;
        }

        SystemAdministrator admin = new SystemAdministrator(0, "", "", "");
        String[] updatedData = admin.getUserByEmployeeNumber(employeeData[0]);

        if (updatedData == null) {
            System.err.println("Error: Employee not found in joined data.");
            return;
        }

        // Fill the text fields
        employeeNumberLabel.setText(updatedData[0]);
        fullNameLabel.setText(updatedData[2] + " " + updatedData[1]);
        positionLabel.setText(updatedData[11]);
        supervisorLabel.setText(updatedData[12]);
        statusLabel.setText(updatedData[10]);
        birthdayLabel.setText(updatedData[3]);
        sssNumberLabel.setText(updatedData[6]);
        philhealthNumberLabel.setText(updatedData[7]);
        pagibigNumberLabel.setText(updatedData[8]);
        tinNumberLabel.setText(updatedData[9]);
        basicSalaryLabel.setText("₱ " + updatedData[13]);
        riceSubsidyLabel.setText("₱ " + updatedData[14]);
        phoneAllowanceLabel.setText("₱ " + updatedData[15]);
        clothingAllowanceLabel.setText("₱ " + updatedData[16]);
        grossSemiMonthlyRateLabel.setText("₱ " + updatedData[17]);
        hourlyRateLabel.setText("₱ " + updatedData[18]);

        phoneNumberLabel.setText(updatedData[5]);
        addressLabel.setText(updatedData[4]);
    }

    /**
     * Enable real-time validation for phone number and address.
     */
    private void setupRealTimeValidation() {
        phoneNumberLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                lastTypedTime = System.currentTimeMillis();
                new Thread(() -> {
                    try {
                        Thread.sleep(1000); // Wait for 1 second after typing stops
                        if (System.currentTimeMillis() - lastTypedTime >= 1000) {
                            SwingUtilities.invokeLater(() -> formatAndValidatePhoneNumber());
                        }
                    } catch (InterruptedException ignored) {}
                }).start();
            }
        });

        addressLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateAddress(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateAddress(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateAddress(); }
        });
    }

    /**
     * Formats and Validates Phone Number when user stops typing
     */
    private void formatAndValidatePhoneNumber() {
        String text = phoneNumberLabel.getText().replaceAll("[^0-9]", ""); // Remove non-digits

        // If number starts with "09" and has 11 digits, trim to last 9
        if (text.startsWith("09") && text.length() == 11) {
            text = text.substring(2); // Remove "09"
        } else if (text.startsWith("639") && text.length() == 12) {
            text = text.substring(3); // Remove "639"
        }

        // Ensure exactly 9 digits remain, then apply XXX-XXX-XXX format
        if (text.length() == 9) {
            text = text.replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1-$2-$3");
            phoneNumberLabel.setText(text);
        }

        // Validate formatted number
        isPhoneNumberValid(phoneNumberLabel.getText());
    }

    /**
     * Validate Address (must be 12-100 characters, no ".." or ",,")
     */
    private void validateAddress() {
        String text = addressLabel.getText().trim();

        if (text.length() < 12 || text.length() > 100 || text.contains(",,") || text.contains("..")) {
            addressLabel.setBackground(ERROR_COLOR);
        } else {
            addressLabel.setBackground(OK_COLOR);
        }
    }

    /**
     * Save updated info to CSV (Only updates Phone & Address)
     */
/**
 * Save updated info (Only updates Phone & Address) and ensures one success dialog.
 */
    private void updateEmployeeInfoInCSV() {
        String empNumber = employeeNumberLabel.getText().trim();
        String originalInput = phoneNumberLabel.getText().trim(); // Store raw user input
        String updatedAddr = addressLabel.getText().trim();

        if (empNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Employee number is missing.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // **Detect if the input had illegal characters before formatting**
        boolean hadIllegalChars = !originalInput.matches("^[0-9\\-]+$"); // Allow only digits and '-'

        // **Force formatting & validate**
        formatAndValidatePhoneNumber();
        String updatedPhone = phoneNumberLabel.getText().trim(); // Get corrected version
        boolean isValid = isPhoneNumberValid(updatedPhone);

        // **Detect if auto-correction occurred**
        boolean wasCorrected = hadIllegalChars || !originalInput.equals(updatedPhone);

        if (!isValid) {
            JOptionPane.showMessageDialog(this, "Invalid phone number format! Please correct it before saving.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SystemAdministrator admin = new SystemAdministrator(0, "", "", "");
        String[] existing = admin.getUserByEmployeeNumber(empNumber);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Employee not found in system!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // **Ensure auto-corrections are treated as actual changes**
        boolean phoneUpdated = !existing[5].equals(updatedPhone) || wasCorrected;
        boolean addressUpdated = !existing[4].equals(updatedAddr);

        if (!phoneUpdated && !addressUpdated) {
            JOptionPane.showMessageDialog(
                this, 
                "No changes detected or input was auto-corrected.", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // **Update CSV Data**
        existing[4] = updatedAddr;
        existing[5] = updatedPhone;
        admin.updateUser(empNumber, existing);

        // **Show correct message**
        if (wasCorrected) {
            JOptionPane.showMessageDialog(this, "Phone Number was automatically corrected and updated successfully.", "Auto-Corrected & Saved", JOptionPane.INFORMATION_MESSAGE);
        } else if (phoneUpdated && addressUpdated) {
            JOptionPane.showMessageDialog(this, "Phone Number and Address updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (phoneUpdated) {
            JOptionPane.showMessageDialog(this, "Phone Number updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Address updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Supporting method of phone number validation
    private boolean isPhoneNumberValid(String text) {
        // Remove all non-numeric characters (keeps only digits)
        String cleanedText = text.replaceAll("[^0-9]", ""); 

        // Check if the cleaned number is valid (09XXXXXXXXX or 639XXXXXXXXX)
        if (cleanedText.matches("^09\\d{9}$") || cleanedText.matches("^639\\d{9}$")) {
            phoneNumberLabel.setBackground(OK_COLOR);
            return true;
        }

        // Check if the formatted number follows the pattern XXX-XXX-XXX
        if (text.matches("^\\d{3}-\\d{3}-\\d{3}$")) {
            phoneNumberLabel.setBackground(OK_COLOR);
            return true;
        }

        // Invalid format
        phoneNumberLabel.setBackground(ERROR_COLOR);
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
        jLabel2 = new javax.swing.JLabel();
        fullNameLabel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        employeeNumberLabel = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        positionLabel = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        supervisorLabel = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        basicSalaryLabel = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        grossSemiMonthlyRateLabel = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        hourlyRateLabel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        sssNumberLabel = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        philhealthNumberLabel = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        pagibigNumberLabel = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        tinNumberLabel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        phoneNumberLabel = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        addressLabel = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        birthdayLabel = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        riceSubsidyLabel = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        phoneAllowanceLabel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        clothingAllowanceLabel = new javax.swing.JTextField();
        backButton1 = new buttons.grayButton();
        doneButton = new buttons.redButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MotorPH Payroll System");
        setMaximumSize(new java.awt.Dimension(960, 540));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backButton.setText("Back");
        backButton.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        getContentPane().add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 110, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Name");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, -1, 20));

        fullNameLabel.setEditable(false);
        fullNameLabel.setBackground(new java.awt.Color(204, 204, 204));
        fullNameLabel.setForeground(new java.awt.Color(102, 102, 102));
        fullNameLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameLabelActionPerformed(evt);
            }
        });
        getContentPane().add(fullNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 200, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Employee No.");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, -1, 20));

        employeeNumberLabel.setEditable(false);
        employeeNumberLabel.setBackground(new java.awt.Color(204, 204, 204));
        employeeNumberLabel.setForeground(new java.awt.Color(102, 102, 102));
        employeeNumberLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeNumberLabelActionPerformed(evt);
            }
        });
        getContentPane().add(employeeNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 200, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Position");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, -1, 20));

        positionLabel.setEditable(false);
        positionLabel.setBackground(new java.awt.Color(204, 204, 204));
        positionLabel.setForeground(new java.awt.Color(102, 102, 102));
        positionLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionLabelActionPerformed(evt);
            }
        });
        getContentPane().add(positionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 200, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Status");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 280, -1, 20));

        statusLabel.setEditable(false);
        statusLabel.setBackground(new java.awt.Color(204, 204, 204));
        statusLabel.setForeground(new java.awt.Color(102, 102, 102));
        statusLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusLabelActionPerformed(evt);
            }
        });
        getContentPane().add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, 200, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Supervisor");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, -1, 20));

        supervisorLabel.setEditable(false);
        supervisorLabel.setBackground(new java.awt.Color(204, 204, 204));
        supervisorLabel.setForeground(new java.awt.Color(102, 102, 102));
        supervisorLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supervisorLabelActionPerformed(evt);
            }
        });
        getContentPane().add(supervisorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 200, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Basic Salary");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, -1, 20));

        basicSalaryLabel.setEditable(false);
        basicSalaryLabel.setBackground(new java.awt.Color(204, 204, 204));
        basicSalaryLabel.setForeground(new java.awt.Color(102, 102, 102));
        basicSalaryLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                basicSalaryLabelActionPerformed(evt);
            }
        });
        getContentPane().add(basicSalaryLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 400, 200, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Gross Semi-Monthly Rate");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, -1, 20));

        grossSemiMonthlyRateLabel.setEditable(false);
        grossSemiMonthlyRateLabel.setBackground(new java.awt.Color(204, 204, 204));
        grossSemiMonthlyRateLabel.setForeground(new java.awt.Color(102, 102, 102));
        grossSemiMonthlyRateLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grossSemiMonthlyRateLabelActionPerformed(evt);
            }
        });
        getContentPane().add(grossSemiMonthlyRateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, 200, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Hourly Rate");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 180, -1, 20));

        hourlyRateLabel.setEditable(false);
        hourlyRateLabel.setBackground(new java.awt.Color(204, 204, 204));
        hourlyRateLabel.setForeground(new java.awt.Color(102, 102, 102));
        hourlyRateLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hourlyRateLabelActionPerformed(evt);
            }
        });
        getContentPane().add(hourlyRateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 200, 200, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("SSS No.");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, -1, 20));

        sssNumberLabel.setEditable(false);
        sssNumberLabel.setBackground(new java.awt.Color(204, 204, 204));
        sssNumberLabel.setForeground(new java.awt.Color(102, 102, 102));
        sssNumberLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sssNumberLabelActionPerformed(evt);
            }
        });
        getContentPane().add(sssNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 200, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Philhealth No.");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 280, -1, 20));

        philhealthNumberLabel.setEditable(false);
        philhealthNumberLabel.setBackground(new java.awt.Color(204, 204, 204));
        philhealthNumberLabel.setForeground(new java.awt.Color(102, 102, 102));
        philhealthNumberLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                philhealthNumberLabelActionPerformed(evt);
            }
        });
        getContentPane().add(philhealthNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 200, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Pagibig No.");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 330, -1, 20));

        pagibigNumberLabel.setEditable(false);
        pagibigNumberLabel.setBackground(new java.awt.Color(204, 204, 204));
        pagibigNumberLabel.setForeground(new java.awt.Color(102, 102, 102));
        pagibigNumberLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagibigNumberLabelActionPerformed(evt);
            }
        });
        getContentPane().add(pagibigNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 350, 200, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("TIN No.");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 380, -1, 20));

        tinNumberLabel.setEditable(false);
        tinNumberLabel.setBackground(new java.awt.Color(204, 204, 204));
        tinNumberLabel.setForeground(new java.awt.Color(102, 102, 102));
        tinNumberLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tinNumberLabelActionPerformed(evt);
            }
        });
        getContentPane().add(tinNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, 200, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Contact No.");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 130, -1, 20));

        phoneNumberLabel.setBackground(new java.awt.Color(204, 204, 204));
        phoneNumberLabel.setForeground(new java.awt.Color(102, 102, 102));
        phoneNumberLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneNumberLabelActionPerformed(evt);
            }
        });
        getContentPane().add(phoneNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 150, 200, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Address");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 180, -1, 20));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);

        addressLabel.setBackground(new java.awt.Color(204, 204, 204));
        addressLabel.setColumns(20);
        addressLabel.setForeground(new java.awt.Color(102, 102, 102));
        addressLabel.setRows(5);
        jScrollPane1.setViewportView(addressLabel);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 200, 200, 70));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Birthday");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 280, -1, 20));

        birthdayLabel.setEditable(false);
        birthdayLabel.setBackground(new java.awt.Color(204, 204, 204));
        birthdayLabel.setForeground(new java.awt.Color(102, 102, 102));
        birthdayLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                birthdayLabelActionPerformed(evt);
            }
        });
        getContentPane().add(birthdayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 300, 200, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Rice Subsidy");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 330, -1, 20));

        riceSubsidyLabel.setEditable(false);
        riceSubsidyLabel.setBackground(new java.awt.Color(204, 204, 204));
        riceSubsidyLabel.setForeground(new java.awt.Color(102, 102, 102));
        riceSubsidyLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                riceSubsidyLabelActionPerformed(evt);
            }
        });
        getContentPane().add(riceSubsidyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 350, 200, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Phone Allowance");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 380, -1, 20));

        phoneAllowanceLabel.setEditable(false);
        phoneAllowanceLabel.setBackground(new java.awt.Color(204, 204, 204));
        phoneAllowanceLabel.setForeground(new java.awt.Color(102, 102, 102));
        phoneAllowanceLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneAllowanceLabelActionPerformed(evt);
            }
        });
        getContentPane().add(phoneAllowanceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 400, 200, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Clothing Allowance");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 430, -1, 20));

        clothingAllowanceLabel.setEditable(false);
        clothingAllowanceLabel.setBackground(new java.awt.Color(204, 204, 204));
        clothingAllowanceLabel.setForeground(new java.awt.Color(102, 102, 102));
        clothingAllowanceLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clothingAllowanceLabelActionPerformed(evt);
            }
        });
        getContentPane().add(clothingAllowanceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 450, 200, -1));

        backButton1.setText("Back");
        backButton1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        backButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(backButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 130, -1));

        doneButton.setText("Done");
        doneButton.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });
        getContentPane().add(doneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 130, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Profile Information.png"))); // NOI18N
        jLabel1.setToolTipText("");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new EmployeeProfileInformation(employeeData).setVisible(true);
        dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void fullNameLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullNameLabelActionPerformed

    private void employeeNumberLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeNumberLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeNumberLabelActionPerformed

    private void positionLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_positionLabelActionPerformed

    private void statusLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusLabelActionPerformed

    private void supervisorLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supervisorLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supervisorLabelActionPerformed

    private void basicSalaryLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_basicSalaryLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_basicSalaryLabelActionPerformed

    private void grossSemiMonthlyRateLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grossSemiMonthlyRateLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grossSemiMonthlyRateLabelActionPerformed

    private void hourlyRateLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourlyRateLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hourlyRateLabelActionPerformed

    private void sssNumberLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sssNumberLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sssNumberLabelActionPerformed

    private void philhealthNumberLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_philhealthNumberLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_philhealthNumberLabelActionPerformed

    private void pagibigNumberLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagibigNumberLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pagibigNumberLabelActionPerformed

    private void tinNumberLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tinNumberLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tinNumberLabelActionPerformed

    private void phoneNumberLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneNumberLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneNumberLabelActionPerformed

    private void birthdayLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_birthdayLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_birthdayLabelActionPerformed

    private void riceSubsidyLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_riceSubsidyLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_riceSubsidyLabelActionPerformed

    private void phoneAllowanceLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneAllowanceLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneAllowanceLabelActionPerformed

    private void clothingAllowanceLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clothingAllowanceLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clothingAllowanceLabelActionPerformed

    private void backButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton1ActionPerformed
        // TODO add your handling code here:
        new EmployeeProfileInformation(this.employeeData).setVisible(true);
        dispose();
    }//GEN-LAST:event_backButton1ActionPerformed

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        // **Force formatting before validation**
        formatAndValidatePhoneNumber();

        // **Check if it's valid**
        if (!isPhoneNumberValid(phoneNumberLabel.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid phone number format! Please correct it before saving.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop saving process
        }

        // If valid, proceed with saving
        updateEmployeeInfoInCSV();
        new EmployeeProfileInformation(employeeData).setVisible(true);
        dispose();
    }//GEN-LAST:event_doneButtonActionPerformed

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
            java.util.logging.Logger.getLogger(EmployeeProfileInformationUpdateInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileInformationUpdateInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileInformationUpdateInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileInformationUpdateInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeProfileInformationUpdateInfo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addressLabel;
    private buttons.whiteButton backButton;
    private buttons.grayButton backButton1;
    private javax.swing.JTextField basicSalaryLabel;
    private javax.swing.JTextField birthdayLabel;
    private javax.swing.JTextField clothingAllowanceLabel;
    private buttons.redButton doneButton;
    private javax.swing.JTextField employeeNumberLabel;
    private javax.swing.JTextField fullNameLabel;
    private javax.swing.JTextField grossSemiMonthlyRateLabel;
    private javax.swing.JTextField hourlyRateLabel;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField pagibigNumberLabel;
    private javax.swing.JTextField philhealthNumberLabel;
    private javax.swing.JTextField phoneAllowanceLabel;
    private javax.swing.JTextField phoneNumberLabel;
    private javax.swing.JTextField positionLabel;
    private javax.swing.JTextField riceSubsidyLabel;
    private javax.swing.JTextField sssNumberLabel;
    private javax.swing.JTextField statusLabel;
    private javax.swing.JTextField supervisorLabel;
    private javax.swing.JTextField tinNumberLabel;
    // End of variables declaration//GEN-END:variables
}
