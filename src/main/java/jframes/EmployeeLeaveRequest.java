/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframes;

import classes.LeaveRequest;
import com.toedter.calendar.JDateChooser;
import java.io.File;
import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
/**
 *
 * @author STUDY MODE
 */
public class EmployeeLeaveRequest extends javax.swing.JFrame {
    
    private String[] employeeData;
    private LeaveRequest existingRequest = null;

    public EmployeeLeaveRequest(String[] employeeData) {
        this.employeeData = employeeData;
        initComponents();
        setupRealTimeValidation();
    }

    public EmployeeLeaveRequest() {
        initComponents();
        setupRealTimeValidation();
    }

    public EmployeeLeaveRequest(String[] employeeData, LeaveRequest existingRequest) {
        this.employeeData = employeeData;
        this.existingRequest = existingRequest;
        initComponents();
        setupRealTimeValidation();
        populateExistingRequest();
    }

    private void populateExistingRequest() {
        if (existingRequest != null) {
            leaveTypeCombo.setSelectedItem(existingRequest.getLeaveType());
            startDateChooser.setDate(java.util.Date.from(
                existingRequest.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            endDateChooser.setDate(java.util.Date.from(
                existingRequest.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
        }
    }

    private void setupRealTimeValidation() {
        endDateChooser.setEnabled(false);
        
        startDateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
            validateStartDate();
            if (startDateChooser.getDate() != null) {
                endDateChooser.setEnabled(true);
            } else {
                endDateChooser.setEnabled(false);
            }
        });

        endDateChooser.getDateEditor().addPropertyChangeListener("date", evt -> validateEndDate());
    }

    private LocalDate convertToLocalDate(JDateChooser dateChooser) {
        if (dateChooser.getDate() != null) {
            return dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    /**
     * Validates Start Date.
     */
    private void validateStartDate() {
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = convertToLocalDate(startDateChooser);

        if (selectedDate == null) return;

        String leaveType = leaveTypeCombo.getSelectedItem().toString();
        LocalDate oneYearFromToday = today.plusYears(1);
        LocalDate twoYearsFromToday = today.plusYears(2);

        if (!selectedDate.isAfter(today)) {
            JOptionPane.showMessageDialog(this, "Cannot choose past or current dates for leave.", "Error", JOptionPane.ERROR_MESSAGE);
            startDateChooser.setDate(null);
        } else if (selectedDate.isAfter(oneYearFromToday) && !leaveType.equals("Vacation")) {
            JOptionPane.showMessageDialog(this, "Cannot choose leave 1 year in advance unless it’s Vacation Leave.", "Error", JOptionPane.ERROR_MESSAGE);
            startDateChooser.setDate(null);
        } else if (selectedDate.isAfter(twoYearsFromToday)) {
            JOptionPane.showMessageDialog(this, "Cannot choose leave more than 2 years in advance.", "Error", JOptionPane.ERROR_MESSAGE);
            startDateChooser.setDate(null);
        }
    }

    /**
     * Validates End Date.
     */
    private void validateEndDate() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = convertToLocalDate(startDateChooser);
        LocalDate endDate = convertToLocalDate(endDateChooser);

        if (endDate == null || startDate == null) return;

        String leaveType = leaveTypeCombo.getSelectedItem().toString();
        LocalDate oneYearFromStart = startDate.plusYears(1);
        LocalDate twoYearsFromStart = startDate.plusYears(2);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        if (!endDate.isAfter(today)) {
            JOptionPane.showMessageDialog(this, "Cannot choose past or current dates for leave.", "Error", JOptionPane.ERROR_MESSAGE);
            endDateChooser.setDate(null);
        } else if (endDate.isBefore(startDate)) {
            JOptionPane.showMessageDialog(this, "End date cannot be before start date.", "Error", JOptionPane.ERROR_MESSAGE);
            endDateChooser.setDate(null);
        } else if (endDate.isAfter(oneYearFromStart) && !leaveType.equals("Vacation")) {
            JOptionPane.showMessageDialog(this, "Cannot choose leave more than 1 year in advance unless it's Vacation Leave.", "Error", JOptionPane.ERROR_MESSAGE);
            endDateChooser.setDate(null);
        } else if (endDate.isAfter(twoYearsFromStart)) {
            JOptionPane.showMessageDialog(this, "Cannot choose leave more than 2 years in advance.", "Error", JOptionPane.ERROR_MESSAGE);
            endDateChooser.setDate(null);
        } else if (leaveType.equals("Vacation") && daysBetween > 21) {
            JOptionPane.showMessageDialog(this, "Vacation Leave cannot be more than 3 weeks apart.", "Error", JOptionPane.ERROR_MESSAGE);
            endDateChooser.setDate(null);
        } else if (leaveType.equals("Sick") && daysBetween > 60) {
            JOptionPane.showMessageDialog(this, "Sick Leave cannot be more than 2 months apart.", "Error", JOptionPane.ERROR_MESSAGE);
            endDateChooser.setDate(null);
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

        backButton1 = new buttons.grayButton();
        backButton = new buttons.whiteButton();
        leaveTypeCombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        startDateChooser = new com.toedter.calendar.JDateChooser();
        endDateChooser = new com.toedter.calendar.JDateChooser();
        submitButton = new buttons.redButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MotorPH Payroll System");
        setMaximumSize(new java.awt.Dimension(960, 540));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backButton1.setText("Back");
        backButton1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        backButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(backButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 420, 150, -1));

        backButton.setText("Back");
        backButton.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        getContentPane().add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 110, 40));

        leaveTypeCombo.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        leaveTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----", "Vacation", "Sick" }));
        getContentPane().add(leaveTypeCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 210, 190, -1));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Leave Type");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 180, -1, -1));

        jLabel4.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("End Date");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 320, -1, -1));

        jLabel5.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Start Date");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 250, -1, -1));
        getContentPane().add(startDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 190, -1));
        getContentPane().add(endDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, 190, -1));

        submitButton.setText("Submit");
        submitButton.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(submitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 420, 150, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Leave Request.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton1ActionPerformed
        // TODO add your handling code here:
        new EmployeeLeave(this.employeeData).setVisible(true);
        dispose();
    }//GEN-LAST:event_backButton1ActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new EmployeeLeave(this.employeeData).setVisible(true);
        dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        String leaveType = leaveTypeCombo.getSelectedItem().toString();
        LocalDate startDate = convertToLocalDate(startDateChooser);
        LocalDate endDate = convertToLocalDate(endDateChooser);

        if (leaveType.equals("----") || startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields before submitting.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (endDate.isBefore(startDate)) {
            JOptionPane.showMessageDialog(this, "End date cannot be before start date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeNumber = Integer.parseInt(employeeData[0]);

        if (existingRequest != null) {
            // **Regenerate the new LeaveRequestNumber and update the request**
            String oldLeaveRequestNumber = existingRequest.generateLeaveRequestNumber(existingRequest.getStartDate(), existingRequest.getEndDate());
            boolean success = existingRequest.updateLeaveRequest(oldLeaveRequestNumber, startDate, endDate, leaveType);

            if (success) {
                new EmployeeLeave(employeeData).setVisible(true);
                dispose();
            }
        } else {
            // **Submit a new leave request**
            LeaveRequest newLeaveRequest = new LeaveRequest(employeeNumber, leaveType, startDate, endDate, "Pending");
            newLeaveRequest.submitLeaveRequest();
            new EmployeeLeave(employeeData).setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_submitButtonActionPerformed

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
            java.util.logging.Logger.getLogger(EmployeeLeaveRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeLeaveRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeLeaveRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeLeaveRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeLeaveRequest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private buttons.whiteButton backButton;
    private buttons.grayButton backButton1;
    private com.toedter.calendar.JDateChooser endDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox<String> leaveTypeCombo;
    private com.toedter.calendar.JDateChooser startDateChooser;
    private buttons.redButton submitButton;
    // End of variables declaration//GEN-END:variables
}
