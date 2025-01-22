import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField textDisplay;
    private JTextArea historyDisplay;

    private double input1, input2, resultingValue;
    private String operator;
    private boolean done;

    private final String HISTORY_FILE = "history.txt";

    public Calculator() {
        setTitle("Enhanced Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        initializeMenu();
        initializeDisplay();
        initializeButtons();
        initializeHistoryPanel();

        setVisible(true);
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        menuBar.add(menu);

        JMenuItem computeMenuItem = new JMenuItem("Compute");
        computeMenuItem.addActionListener(e -> selectCompute());
        menu.add(computeMenuItem);

        JMenuItem viewHistoryMenuItem = new JMenuItem("View History");
        viewHistoryMenuItem.addActionListener(e -> viewHistory());
        menu.add(viewHistoryMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        menu.add(exitMenuItem);

        setJMenuBar(menuBar);
    }

    private void initializeDisplay() {
        textDisplay = new JTextField();
        textDisplay.setEditable(false);
        textDisplay.setBackground(Color.DARK_GRAY);
        textDisplay.setForeground(Color.CYAN);
        textDisplay.setFont(new Font("Consolas", Font.BOLD, 24));
        textDisplay.setHorizontalAlignment(JTextField.RIGHT);
        textDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(textDisplay, BorderLayout.NORTH);
    }

    private void initializeButtons() {
        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new GridLayout(5, 4, 5, 5));
        buttonGroup.setBackground(Color.BLACK);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C"
        };

        for (String button : buttons) {
            JButton b = new JButton(button);
            b.setBackground(new Color(50, 50, 50));
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Arial", Font.BOLD, 20));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            b.addActionListener(this);
            buttonGroup.add(b);
        }

        buttonGroup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buttonGroup, BorderLayout.CENTER);
    }

    private void initializeHistoryPanel() {
        historyDisplay = new JTextArea(15, 20);
        historyDisplay.setEditable(false);
        historyDisplay.setText("History:\n");
        historyDisplay.setBackground(new Color(30, 30, 30));
        historyDisplay.setForeground(Color.LIGHT_GRAY);
        historyDisplay.setFont(new Font("Monospaced", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(historyDisplay);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "History", 0, 0, new Font("Arial", Font.BOLD, 14), Color.LIGHT_GRAY));
        scrollPane.setBackground(Color.BLACK);

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.setBackground(Color.BLACK);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        add(historyPanel, BorderLayout.EAST);
    }

    private void selectCompute() {
        JOptionPane.showMessageDialog(this, "Use the buttons to perform a calculation.", "Compute", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewHistory() {
        try {
            StringBuilder historyContent = new StringBuilder("History:\n");
            try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    historyContent.append(line).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, historyContent.toString(), "View History", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Unable to load history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("C")) {
            resetCalculator();
        } else if (Character.isDigit(actionCommand.charAt(0)) || actionCommand.equals(".")) {
            handleNumberInput(actionCommand);
        } else if (actionCommand.equals("=")) {
            performCalculation();
        } else { // Operators
            handleOperator(actionCommand);
        }
    }

    private void resetCalculator() {
        input1 = 0;
        input2 = 0;
        textDisplay.setText("");
        done = false;
    }

    private void handleNumberInput(String input) {
        if (done) {
            textDisplay.setText("");
            done = false;
        }
        if (input.equals(".") && textDisplay.getText().contains(".")) {
            return;
        }
        textDisplay.setText(textDisplay.getText() + input);
    }

    private void handleOperator(String op) {
        if (!textDisplay.getText().isEmpty()) {
            input1 = Double.parseDouble(textDisplay.getText());
            operator = op;
            textDisplay.setText("");
        }
    }

    private void performCalculation() {
        if (textDisplay.getText().isEmpty()) {
            return;
        }
        try {
            input2 = Double.parseDouble(textDisplay.getText());
            calculate();
            textDisplay.setText(String.valueOf(resultingValue));
            done = true;

            String historyRecord = input1 + " " + operator + " " + input2 + " = " + resultingValue;
            appendToHistory(historyRecord);
            historyDisplay.append(historyRecord + "\n");
        } catch (NumberFormatException ex) {
            textDisplay.setText("Error");
        }
    }

    private void calculate() {
        switch (operator) {
            case "+":
                resultingValue = input1 + input2;
                break;
            case "-":
                resultingValue = input1 - input2;
                break;
            case "*":
                resultingValue = input1 * input2;
                break;
            case "/":
                resultingValue = input2 != 0 ? input1 / input2 : Double.NaN;
                if (Double.isNaN(resultingValue)) {
                    textDisplay.setText("Error");
                }
                break;
            default:
                textDisplay.setText("Error");
                break;
        }
    }

    private void appendToHistory(String record) {
        try (FileWriter writer = new FileWriter(HISTORY_FILE, true)) {
            writer.write(record + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
