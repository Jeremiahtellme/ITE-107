package calculator;

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

    public Calculator() {
        setTitle("Simple Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());

        textDisplay = new JTextField();
        textDisplay.setEditable(false);
        textDisplay.setBackground(Color.BLACK);
        textDisplay.setForeground(Color.GREEN);
        textDisplay.setFont(new Font("Arial", Font.BOLD, 20));
        add(textDisplay, BorderLayout.NORTH);

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new GridLayout(5, 4));
        buttonGroup.setBackground(Color.DARK_GRAY);

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C"
        };

        for (String button : buttons) {
            JButton b = new JButton(button);
            b.setBackground(Color.LIGHT_GRAY);
            b.setForeground(Color.BLACK);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            b.addActionListener(this);
            buttonGroup.add(b);
        }

        add(buttonGroup, BorderLayout.CENTER);

        JPanel history = new JPanel();
        history.setLayout(new BorderLayout());
        history.setBackground(Color.DARK_GRAY);

        historyDisplay = new JTextArea(10, 20);
        historyDisplay.setEditable(false);
        historyDisplay.setText("History:\n");
        historyDisplay.setBackground(Color.BLACK);
        historyDisplay.setForeground(Color.WHITE);
        historyDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(historyDisplay);
        history.add(scrollPane, BorderLayout.CENTER);

        add(history, BorderLayout.WEST);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String calcuInput = e.getActionCommand();

        if (textDisplay.getText().length() <= 0 && calcuInput.equals("0")) {
            return;
        }

        if (calcuInput.equals("C")) {
            input1 = 0;
            input2 = 0;
            textDisplay.setText("");
        } else if (Character.isDigit(calcuInput.charAt(0)) || calcuInput.equals(".")) {
            if (done) {
                textDisplay.setText("");
                done = false;
            }

            if (calcuInput.equals(".") && textDisplay.getText().contains(".")) {
                return;
            }

            textDisplay.setText(textDisplay.getText() + calcuInput);
        } else if (calcuInput.equals("=")) {
            if (textDisplay.getText().isEmpty()) {
                return;
            }

            try {
                input2 = Double.parseDouble(textDisplay.getText());
                calculate();
                textDisplay.setText(String.valueOf(resultingValue));
                done = true;

                String history = historyRecorder(
                    String.valueOf(input1) + " " +
                    operator + " " +
                    String.valueOf(input2) + " = " +
                    String.valueOf(resultingValue)
                );
                historyDisplay.setText("History:\n" + history);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            if (textDisplay.getText().isEmpty()) {
                return;
            }

            operator = calcuInput;
            input1 = Double.parseDouble(textDisplay.getText());
            textDisplay.setText("");
            done = false;
        }
    }

    public void calculate() {
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
                if (input2 == 0) {
                    textDisplay.setText("Error: Div by 0");
                    resultingValue = 0;
                    return;
                }
                resultingValue = input1 / input2;
                break;
        }
    }

    public String historyRecorder(String record) throws IOException {
        File file = new File("calculator_history.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder history = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            history.append(line).append(System.lineSeparator());
        }

        history.append(record);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(history.toString());

        reader.close();
        writer.close();

        return history.toString();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("calculator_history.txt");
        if (!file.exists()) {
            file.createNewFile();
        } else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("");
            writer.close();
        }

        new Calculator();
    }
}
