/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.Bumataysse_program;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Windows10 Pro
 */
public class Crudski extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Crudski.class.getName());

    /**
     * Creates new form Crudski
     */
    public Crudski() {
    initComponents();

    btn_add.addActionListener(e -> addData());
    btn_update.addActionListener(e -> updateData());
    btn_delete.addActionListener(e -> deleteData());
    CLEAR.addActionListener(e -> clearSelection());

    loadData();
}
    private void loadData() {

    DefaultTableModel model =
        (DefaultTableModel) jTable1.getModel();

    model.setRowCount(0);

    String sql = "SELECT ID, [NAME], STOCK FROM PARTS";

    try (Connection con = MsConnectAccess.conn()) {

        if (con == null) {
            return;
        }

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int stock = rs.getInt("STOCK");

                model.addRow(new Object[]{
                    id,
                    name,
                    stock
                });
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
            this,
            "Error loading data: " + e.getMessage()
        );
    }
}


// =========================
// ADD
// =========================
private void addData() {

    String name = JOptionPane.showInputDialog(
        this,
        "Enter part name:"
    );

    if (name == null || name.trim().isEmpty()) {
        return;
    }

    String stockInput = JOptionPane.showInputDialog(
        this,
        "Enter stock quantity:"
    );

    if (stockInput == null || stockInput.trim().isEmpty()) {
        return;
    }

    try {

        int stock = Integer.parseInt(stockInput.trim());

        if (stock < 0) {
            JOptionPane.showMessageDialog(
                this, "Stock cannot be negative."
            );
            return;
        }

        String sql =
            "INSERT INTO PARTS ([NAME], STOCK) VALUES (?, ?)";

        try (Connection con = MsConnectAccess.conn()) {

            if (con == null) {
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, name.trim());
                pst.setInt(2, stock);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                    this,
                    "Part added successfully!"
                );

                loadData();
            }
        }

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
            this,
            "Stock must be a number."
        );

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
            this,
            "Error adding part: " + e.getMessage()
        );
    }
}


// =========================
// UPDATE
// =========================
private void updateData() {

    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
            this,
            "Please select a part to update."
        );
        return;
    }

    Object id = jTable1.getValueAt(selectedRow, 0);

    String oldName =
        jTable1.getValueAt(selectedRow, 1).toString();

    String oldStock =
        jTable1.getValueAt(selectedRow, 2).toString();

    String name = JOptionPane.showInputDialog(
        this,
        "Enter new part name:",
        oldName
    );

    if (name == null || name.trim().isEmpty()) {
        return;
    }

    String stockInput = JOptionPane.showInputDialog(
        this,
        "Enter new stock quantity:",
        oldStock
    );

    if (stockInput == null || stockInput.trim().isEmpty()) {
        return;
    }

    try {

        int stock = Integer.parseInt(stockInput.trim());

        if (stock < 0) {
            JOptionPane.showMessageDialog(
                this, "Stock cannot be negative."
            );
            return;
        }

        String sql =
            "UPDATE PARTS SET [NAME] = ?, STOCK = ? WHERE ID = ?";

        try (Connection con = MsConnectAccess.conn()) {

            if (con == null) {
                return;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, name.trim());
                pst.setInt(2, stock);
                pst.setObject(3, id);

                int result = pst.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Part updated successfully!"
                    );

                    loadData();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Part not found."
                    );
                }
            }
        }

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
            this,
            "Stock must be a number."
        );

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
            this,
            "Error updating part: " + e.getMessage()
        );
    }
}


// =========================
// DELETE
// =========================
private void deleteData() {

    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
            this,
            "Please select a part to delete."
        );
        return;
    }

    Object id = jTable1.getValueAt(selectedRow, 0);

    String name =
        jTable1.getValueAt(selectedRow, 1).toString();

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete " + name + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    String sql = "DELETE FROM PARTS WHERE ID = ?";

    try (Connection con = MsConnectAccess.conn()) {

        if (con == null) {
            return;
        }

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setObject(1, id);

            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Part deleted successfully!"
                );

                loadData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Part not found."
                );
            }
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
            this,
            "Error deleting part: " + e.getMessage()
        );
    }
}


// =========================
// CLEAR SELECTION
// =========================
private void clearSelection() {
    jTable1.clearSelection();
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_add = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        CLEAR = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_add.setText("ADD");
        getContentPane().add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 105, 38));

        btn_update.setText("UPDATE");
        getContentPane().add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 105, 37));

        btn_delete.setText("DELETE");
        getContentPane().add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 105, 31));

        CLEAR.setText("CLEAR");
        getContentPane().add(CLEAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 105, 34));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "NAME", "STOCK"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, -1, 300));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("J-WORKS CLASSIC");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 240, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Crudski().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CLEAR;
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    
}
