import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class PayrollSystem extends JFrame {
    private JTextField txtEmployeeID, txtName, txtHourlyRate, txtHoursWorked;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<String> payrollRecords = new ArrayList<>();
    private static final String FILE_NAME = "payroll.txt";

    public PayrollSystem() {
        // Set up JFrame
        setTitle("Payroll System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up colors
        Color primaryColor = new Color(0, 128, 128); // Modern teal
        Color secondaryColor = new Color(245, 245, 220); // Warm beige
        Color accentColor = new Color(255, 165, 0); // Bright orange
        Color buttonColor = new Color(154, 205, 50); // Soft yellow-green

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.setBackground(secondaryColor);
        inputPanel.add(new JLabel("Employee ID:"));
        txtEmployeeID = new JTextField();
        inputPanel.add(txtEmployeeID);

        inputPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Hourly Rate:"));
        txtHourlyRate = new JTextField();
        inputPanel.add(txtHourlyRate);

        inputPanel.add(new JLabel("Hours Worked:"));
        txtHoursWorked = new JTextField();
        inputPanel.add(txtHoursWorked);

        JButton btnAddEmployee = new JButton("Add Employee");
        styleButton(btnAddEmployee, buttonColor);
        inputPanel.add(btnAddEmployee);

        JButton btnCalculatePay = new JButton("Calculate Pay");
        styleButton(btnCalculatePay, accentColor);
        inputPanel.add(btnCalculatePay);

        add(inputPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(primaryColor);

        tableModel = new DefaultTableModel(new String[]{"Employee ID", "Name", "Hourly Rate", "Hours Worked", "Gross Pay", "Net Pay"}, 0);
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(secondaryColor);

        JButton btnSaveRecord = new JButton("Save Record");
        styleButton(btnSaveRecord, buttonColor);
        buttonsPanel.add(btnSaveRecord);

        JButton btnDisplayRecords = new JButton("Display Records");
        styleButton(btnDisplayRecords, accentColor);
        buttonsPanel.add(btnDisplayRecords);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Load existing records
        loadRecords();

        // Add Employee Action
        btnAddEmployee.addActionListener(e -> addEmployee());

        // Calculate Pay Action
        btnCalculatePay.addActionListener(e -> calculatePay());

        // Save Record Action
        btnSaveRecord.addActionListener(e -> saveRecords());

        // Display Records Action
        btnDisplayRecords.addActionListener(e -> displayRecords());
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void addEmployee() {
        String id = txtEmployeeID.getText();
        String name = txtName.getText();
        String hourlyRateStr = txtHourlyRate.getText();
        String hoursWorkedStr = txtHoursWorked.getText();

        if (id.isEmpty() || name.isEmpty() || hourlyRateStr.isEmpty() || hoursWorkedStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for duplicate ID
        for (String record : payrollRecords) {
            if (record.startsWith(id + ",")) {
                JOptionPane.showMessageDialog(this, "Employee ID must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            double hourlyRate = Double.parseDouble(hourlyRateStr);
            double hoursWorked = Double.parseDouble(hoursWorkedStr);

            // Calculate Gross Pay and Net Pay
            double grossPay = hourlyRate * hoursWorked;
            double netPay = grossPay * 0.8; // Simplified deduction (20% tax)

            String record = String.format("%s,%s,%.2f,%.2f,%.2f,%.2f", id, name, hourlyRate, hoursWorked, grossPay, netPay);
            payrollRecords.add(record);
            tableModel.addRow(new Object[]{id, name, hourlyRate, hoursWorked, grossPay, netPay});
            JOptionPane.showMessageDialog(this, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculatePay() {
        String id = txtEmployeeID.getText();
        for (String record : payrollRecords) {
            if (record.startsWith(id + ",")) {
                String[] parts = record.split(",");
                double hourlyRate = Double.parseDouble(parts[2]);
                double hoursWorked = Double.parseDouble(parts[3]);
                double grossPay = hourlyRate * hoursWorked;
                double netPay = grossPay * 0.8; // Simplified deduction (20% tax)

                String output = String.format("Gross Pay: %.2f, Net Pay: %.2f", grossPay, netPay);
                JOptionPane.showMessageDialog(this, output, "Pay Calculation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void saveRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String record : payrollRecords) {
                writer.write(record);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Records saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving records.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRecords() {
        tableModel.setRowCount(0); // Clear table
        for (String record : payrollRecords) {
            String[] parts = record.split(",");
            tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]});
        }
    }

    private void loadRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                payrollRecords.add(line);
                String[] parts = line.split(",");
                tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]});
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No existing records found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PayrollSystem().setVisible(true));
    }
}
