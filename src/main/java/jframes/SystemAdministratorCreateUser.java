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

    // Constructor (without initComponents block)
    public SystemAdministratorCreateUser() {
        // Normally NetBeans auto-calls initComponents(), but we omit it here
        initComponents();            // <--- NetBeans typically inserts this
        setupRealTimeValidation();
        generateEmployeeNumber();    // auto-generate new employee #
        // Optionally set combos to -1 so they're unselected
        roleBox.setSelectedIndex(-1);
        statusBox.setSelectedIndex(-1);
        positionBox.setSelectedIndex(-1);
        supervisorBox.setSelectedIndex(-1);
        phoneAllowanceBox.setSelectedIndex(-1);
        clothingAllowanceBox.setSelectedIndex(-1);
    }

    // ---------------- A) Generate Next Employee # from Employee.java ----------------
    private void generateEmployeeNumber() {
        // Ensure your Employee class has a static method: getNextEmployeeNumber()
        String nextNum = Employee.getNextEmployeeNumber();
        employeeNumberText.setText(nextNum);
        employeeNumberText.setEditable(false); // So user can't change
    }

    // ---------------- B) Setup Real-Time Validation on Key Release ----------------
    private void setupRealTimeValidation() {
        KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Object src = e.getSource();
                if      (src == lastNameText)             validateLastName();
                else if (src == firstNameText)            validateFirstName();
                else if (src == usernameText)             validateUsername();
                else if (src == passwordText)             validatePassword();
                else if (src == addressText)              validateAddress();
                else if (src == phoneNumberText1)         validatePhoneNumber();
                else if (src == sssNumberText)            formatSSSNumber(); //validateSSS();
                else if (src == philhealthNumberText)     validatePhilhealth();
                else if (src == tinNumberText)            formatTINNumber(); //validateTIN();
                else if (src == pagIbigText)              validatePagIbig();
                else if (src == basicSalaryText)          validateBasicSalary();
                else if (src == grossSemiMonthlyRateText) validateGrossSemi();
                else if (src == riceSubsidyText)          validateRiceSubsidy();
                else if (src == hourlyRateText)           validateHourlyRate();
            }
        };
        // Add adapter to each text field
        lastNameText.addKeyListener(adapter);
        firstNameText.addKeyListener(adapter);
        usernameText.addKeyListener(adapter);
        passwordText.addKeyListener(adapter);
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

    // ---------------- C) Validation Methods ----------------

    private void validateLastName() {
        // letters only, min 2, max 35
        String text = lastNameText.getText().trim();
        if (text.matches("^[A-Za-z]{2,35}$")) {
            lastNameText.setBackground(OK_COLOR);
        } else {
            lastNameText.setBackground(ERROR_COLOR);
        }
    }

    private void validateFirstName() {
        // letters only, min 2, max 35
        String text = firstNameText.getText().trim();
        if (text.matches("^[A-Za-z]{2,35}$")) {
            firstNameText.setBackground(OK_COLOR);
        } else {
            firstNameText.setBackground(ERROR_COLOR);
        }
    }

    private void validateUsername() {
        String text = usernameText.getText().trim();

        // 1) length 10..20
        boolean lengthOk = (text.length() >= 10 && text.length() <= 20);

        // 2) not all digits => at least one non-digit
        boolean notAllDigits = !text.matches("\\d+");

        // 3) no consecutive '.' or '_'
        boolean noConsecutiveDot = !text.contains("..");
        boolean noConsecutiveUnderscore = !text.contains("__");

        // 4) only alphanumeric or '.' or '_'
        boolean validChars = text.matches("^[A-Za-z0-9._]+$");

        if (lengthOk && notAllDigits && noConsecutiveDot && noConsecutiveUnderscore && validChars) {
            usernameText.setBackground(OK_COLOR);
        } else {
            usernameText.setBackground(ERROR_COLOR);
        }
    }


    private void validatePassword() {
        // 9-50 chars, must contain letter, digit, '.' or '_'
        // and only letters, digits, '.' or '_'
        String text = passwordText.getText().trim();
        boolean lengthOk = (text.length() >= 9 && text.length() <= 50);
        boolean hasLetter = text.matches(".*[A-Za-z].*");
        boolean hasDigit  = text.matches(".*\\d.*");
        boolean hasSymbol = text.matches(".*[._].*");
        boolean onlyAllowed = text.matches("^[A-Za-z0-9._]+$");
        if (lengthOk && hasLetter && hasDigit && hasSymbol && onlyAllowed) {
            passwordText.setBackground(OK_COLOR);
        } else {
            passwordText.setBackground(ERROR_COLOR);
        }
    }

    private void validateAddress() {
        // Alphanumeric + commas + periods + spaces, length 12-100
        // No double commas or double periods
        String text = addressText.getText().trim();
        if (text.length() < 12 || text.length() > 100) {
            addressText.setBackground(ERROR_COLOR);
            return;
        }
        if (!text.matches("^[A-Za-z0-9,\\.\\s]+$")) {
            addressText.setBackground(ERROR_COLOR);
            return;
        }
        if (text.contains(",,") || text.contains("..")) {
            addressText.setBackground(ERROR_COLOR);
            return;
        }
        addressText.setBackground(OK_COLOR);
    }

    private void validatePhoneNumber() {
        String text = phoneNumberText1.getText().replaceAll("[^0-9]", ""); // Remove non-numeric chars

        if (text.length() > 11) {
            text = text.substring(0, 11); // Ensure max length
        }

        if (text.matches("^09\\d{9}$") || text.matches("^639\\d{9}$")) {
            phoneNumberText1.setBackground(OK_COLOR);
        } else {
            phoneNumberText1.setBackground(ERROR_COLOR);
        }

        // Apply formatting 09XX-XXX-XXXX or 639XX-XXX-XXXX
        if (text.length() >= 11) {
            phoneNumberText1.setText(text.replaceAll("(\\d{4})(\\d{3})(\\d{4})", "$1-$2-$3"));
        }
    }
    
    private void formatSSSNumber() {
        String text = sssNumberText.getText().replaceAll("[^0-9]", ""); // Remove non-numeric chars

        if (text.length() > 10) {
            text = text.substring(0, 10); // Ensure max length
        }

        if (text.length() == 10) {
            sssNumberText.setText(text.replaceAll("(\\d{2})(\\d{7})(\\d{1})", "$1-$2-$3"));
        }
    }
    
    private void formatTINNumber() {
        String text = tinNumberText.getText().replaceAll("[^0-9]", ""); // Remove non-numeric chars

        if (text.length() > 12) {
            text = text.substring(0, 12); // Ensure max length
        }

        if (text.length() == 12) {
            tinNumberText.setText(text.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{3})", "$1-$2-$3-$4"));
        }
    }

    private void validatePhilhealth() {
        // 12 digits
        String text = philhealthNumberText.getText().trim();
        if (text.matches("\\d{12}")) {
            philhealthNumberText.setBackground(OK_COLOR);
        } else {
            philhealthNumberText.setBackground(ERROR_COLOR);
        }
    }

    private void validatePagIbig() {
        // 12 digits
        String text = pagIbigText.getText().trim();
        if (text.matches("\\d{12}")) {
            pagIbigText.setBackground(OK_COLOR);
        } else {
            pagIbigText.setBackground(ERROR_COLOR);
        }
    }

    // Formatter for numerical values with commas
    private static final DecimalFormat formatter = new DecimalFormat("#,##0");

    private void validateBasicSalary() {
        String text = basicSalaryText.getText().replace(",", "").trim(); // Remove existing commas before parsing
        if (text.matches("\\d+")) {
            String formattedText = formatter.format(Long.parseLong(text)); // Correctly format with commas
            basicSalaryText.setText(formattedText);
            basicSalaryText.setBackground(OK_COLOR);
        } else {
            basicSalaryText.setBackground(ERROR_COLOR);
        }
    }

    private void validateGrossSemi() {
        String text = grossSemiMonthlyRateText.getText().replace(",", "").trim(); // Remove existing commas before parsing
        if (text.matches("\\d+")) {
            String formattedText = formatter.format(Long.parseLong(text)); // Correctly format with commas
            grossSemiMonthlyRateText.setText(formattedText);
            grossSemiMonthlyRateText.setBackground(OK_COLOR);
        } else {
            grossSemiMonthlyRateText.setBackground(ERROR_COLOR);
        }
    }

    private void validateRiceSubsidy() {
        String text = riceSubsidyText.getText().replace(",", "").trim(); // Remove existing commas before parsing
        if (text.matches("\\d+") && Integer.parseInt(text) == 1500) {
            riceSubsidyText.setText("1,500"); // Always set this fixed format
            riceSubsidyText.setBackground(OK_COLOR);
        } else {
            riceSubsidyText.setBackground(ERROR_COLOR);
        }
    }


    private void validateHourlyRate() {
        // double only
        String text = hourlyRateText.getText().trim();
        if (text.matches("\\d+(\\.\\d+)?")) {
            hourlyRateText.setBackground(OK_COLOR);
        } else {
            hourlyRateText.setBackground(ERROR_COLOR);
        }
    }

    // ------------- D) Final "Create" Button Handler -------------
    public void handleCreateButtonAction() {
        // Check combos
        if (!isComboValid()) {
            JOptionPane.showMessageDialog(
                this,
                "Some dropdown fields are invalid or unselected.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return; // do NOT close
        }
        // Check birthday
        if (!isBirthdayValid()) {
            JOptionPane.showMessageDialog(
                this,
                "Must be 18 years old or older.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return; // do NOT close
        }
        // Check empties
        if (anyEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Some required fields are empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return; // do NOT close
        }
        // Check if any field is error color
        if (hasErrorFields()) {
            JOptionPane.showMessageDialog(
                this,
                "One or more fields are invalid. Please correct them.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return; // do NOT close
        }

        // If we reach here => all validations passed => do the creation
        JOptionPane.showMessageDialog(
            this,
            "User created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // 4) If we reach here => all validations passed => do the creation
        // Build or collect the final data for creation:
        String empNum   = employeeNumberText.getText().trim();
        String lName    = lastNameText.getText().trim();
        String fName    = firstNameText.getText().trim();
        String birthday = new SimpleDateFormat("MM/dd/yyyy").format(birthdayCalendar.getDate());
        String address  = addressText.getText().trim();
        String phone    = phoneNumberText1.getText().trim();
        String sss      = sssNumberText.getText().trim();
        String phil     = philhealthNumberText.getText().trim();
        String tin      = tinNumberText.getText().trim();
        String pagibig  = pagIbigText.getText().trim();
        String basic    = basicSalaryText.getText().trim();
        String semi     = grossSemiMonthlyRateText.getText().trim();
        String rice     = riceSubsidyText.getText().trim();
        String hourRate = hourlyRateText.getText().trim();
        String uname    = usernameText.getText().trim();
        String pword    = passwordText.getText().trim();
        String rChoice  = roleBox.getSelectedItem().toString().trim();
        String stat     = statusBox.getSelectedItem().toString().trim();
        String pos      = positionBox.getSelectedItem().toString().trim();
        String sup      = supervisorBox.getSelectedItem().toString().trim();
        String phoneAll = phoneAllowanceBox.getSelectedItem().toString().trim();
        String clothAll = clothingAllowanceBox.getSelectedItem().toString().trim();

        // Create your 22-column array or however you store data:
        String[] newUserData = {
            empNum, 
            lName,
            fName,
            birthday,
            address,
            phone,
            sss,
            phil,
            pagibig,
            tin,
            stat,
            pos,
            sup,
            basic,
            rice,
            phoneAll,
            clothAll,
            semi,
            hourRate,
            uname,
            pword,
            rChoice
        };

        // Now call your SystemAdministrator to create the user:
        SystemAdministrator admin = new SystemAdministrator(0, "", "", "");
        admin.createUser(newUserData);

        // If creation is successful, show success message and dispose:
        JOptionPane.showMessageDialog(
            this,
            "User created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Now we can close
        new SystemAdministratorPage().setVisible(true);
        dispose();
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
        if (d == null) return false; // not selected
        LocalDate birth = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now   = LocalDate.now();
        return Period.between(birth, now).getYears() >= 18;
    }

    private boolean anyEmpty() {
        JTextField[] fields = {
            employeeNumberText, lastNameText, firstNameText,
            usernameText, passwordText, addressText, phoneNumberText1,
            sssNumberText, philhealthNumberText, tinNumberText, pagIbigText,
            basicSalaryText, grossSemiMonthlyRateText, riceSubsidyText, hourlyRateText
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
            lastNameText, firstNameText, usernameText, passwordText,
            addressText, phoneNumberText1, sssNumberText, philhealthNumberText,
            tinNumberText, pagIbigText, basicSalaryText,
            grossSemiMonthlyRateText, riceSubsidyText, hourlyRateText
        };
        for (JTextField f : fields) {
            if (f.getBackground() == ERROR_COLOR) {
                return true;
            }
        }
        return false;
    }

    // ------------- E) "Clear" Button Handler -------------
    public void handleClearButtonAction() {
        // Reset text fields
        lastNameText.setText("");
        firstNameText.setText("");
        addressText.setText("");
        phoneNumberText1.setText("");
        sssNumberText.setText("");
        philhealthNumberText.setText("");
        tinNumberText.setText("");
        pagIbigText.setText("");
        basicSalaryText.setText("");
        riceSubsidyText.setText("1,500"); // Default value
        grossSemiMonthlyRateText.setText("");
        hourlyRateText.setText("");
        usernameText.setText("");
        passwordText.setText("");
        birthdayCalendar.setDate(new Date()); // Sets to today's date

        // Reset dropdown selections
        roleBox.setSelectedIndex(-1);
        statusBox.setSelectedIndex(-1);
        positionBox.setSelectedIndex(-1);
        supervisorBox.setSelectedIndex(-1);
        phoneAllowanceBox.setSelectedIndex(-1);
        clothingAllowanceBox.setSelectedIndex(-1);

        // Reset background colors (Validation Reset)
            JTextField[] fields = {
            lastNameText, firstNameText, usernameText, passwordText, 
            addressText, phoneNumberText1, sssNumberText, philhealthNumberText,
            tinNumberText, pagIbigText, basicSalaryText, grossSemiMonthlyRateText, 
            riceSubsidyText, hourlyRateText
        };

        for (JTextField field : fields) {
            field.setBackground(Color.WHITE); // Reset to default white
        }
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
        jLabel4 = new javax.swing.JLabel();
        passwordText = new javax.swing.JTextField();
        firstNameText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        birthdayCalendar = new com.toedter.calendar.JCalendar();
        jLabel7 = new javax.swing.JLabel();
        usernameText = new javax.swing.JTextField();
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
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, -1, -1));

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
        getContentPane().add(lastNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 140, -1));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Login Role");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, -1));

        phoneAllowanceBox.setBackground(new java.awt.Color(204, 204, 204));
        phoneAllowanceBox.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        phoneAllowanceBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2,000", "1,000", "800", "500" }));
        getContentPane().add(phoneAllowanceBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 190, 140, -1));

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

        firstNameText.setBackground(new java.awt.Color(204, 204, 204));
        firstNameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        firstNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameTextActionPerformed(evt);
            }
        });
        getContentPane().add(firstNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 140, -1));

        jLabel5.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("First Name");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, -1));

        jLabel6.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Last Name");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, -1, -1));

        birthdayCalendar.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(birthdayCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 390, 190, 110));

        jLabel7.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Username");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        usernameText.setBackground(new java.awt.Color(204, 204, 204));
        usernameText.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        usernameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameTextActionPerformed(evt);
            }
        });
        getContentPane().add(usernameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 140, -1));

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
        getContentPane().add(roleBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 140, -1));

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
        clothingAllowanceBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Regular", "Probationary" }));
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
        new SystemAdministratorPage().setVisible(true);
        dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void pagIbigTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagIbigTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pagIbigTextActionPerformed

    private void lastNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameTextActionPerformed

    private void passwordTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordTextActionPerformed

    private void firstNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameTextActionPerformed

    private void usernameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameTextActionPerformed

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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField lastNameText;
    private javax.swing.JTextField pagIbigText;
    private javax.swing.JTextField passwordText;
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
    private javax.swing.JTextField usernameText;
    // End of variables declaration//GEN-END:variables
}
