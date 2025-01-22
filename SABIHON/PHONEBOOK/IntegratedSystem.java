import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class IntegratedSystem extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PhonebookSystem::new);
    }

    static class PhonebookSystem extends JFrame {
        private JTextField nameField, phoneField, groupField;
        private JTextArea contactArea;
        private JButton addButton, deleteButton, searchButton, updateButton, displayButton, addGroupButton, deleteGroupButton, payrollButton;
        private Map<String, List<Contact>> phonebook;

        private static final String DATA_FILE = "phonebook_data.txt";

        public PhonebookSystem() {
            setTitle("Phonebook System");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLayout(new BorderLayout(10, 10));

            phonebook = new HashMap<>();
            loadDataFromFile();

            add(createInputPanel(), BorderLayout.NORTH);
            add(createButtonPanel(), BorderLayout.CENTER);
            add(createTextAreaPanel(), BorderLayout.SOUTH);

            setVisible(true);
        }

        private JPanel createInputPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            panel.setBorder(BorderFactory.createTitledBorder("Contact Details"));

            nameField = new JTextField(15);
            phoneField = new JTextField(15);
            groupField = new JTextField(15);

            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Phone:"));
            panel.add(phoneField);
            panel.add(new JLabel("Group:"));
            panel.add(groupField);

            return panel;
        }

        private JPanel createButtonPanel() {
            JPanel panel = new JPanel(new FlowLayout());

            addButton = new JButton("Add Contact");
            deleteButton = new JButton("Delete Contact");
            searchButton = new JButton("Search Contact");
            updateButton = new JButton("Update Contact");
            displayButton = new JButton("Display All Contacts");
            addGroupButton = new JButton("Add Group");
            deleteGroupButton = new JButton("Delete Group");
            payrollButton = new JButton("Open Payroll System");

            panel.add(addButton);
            panel.add(deleteButton);
            panel.add(searchButton);
            panel.add(updateButton);
            panel.add(displayButton);
            panel.add(addGroupButton);
            panel.add(deleteGroupButton);
            panel.add(payrollButton);

            addButton.addActionListener(e -> addContact());
            deleteButton.addActionListener(e -> deleteContact());
            searchButton.addActionListener(e -> searchContact());
            updateButton.addActionListener(e -> updateContact());
            displayButton.addActionListener(e -> displayContacts());
            addGroupButton.addActionListener(e -> addGroup());
            deleteGroupButton.addActionListener(e -> deleteGroup());
            payrollButton.addActionListener(e -> openPayrollSystem());

            return panel;
        }

        private JPanel createTextAreaPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            contactArea = new JTextArea(15, 50);
            contactArea.setEditable(false);
            panel.add(new JScrollPane(contactArea), BorderLayout.CENTER);
            return panel;
        }

        private void addContact() {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String group = groupField.getText();

            if (name.isEmpty() || phone.isEmpty() || group.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            phonebook.putIfAbsent(group, new ArrayList<>());
            phonebook.get(group).add(new Contact(name, phone));
            JOptionPane.showMessageDialog(this, "Contact added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            saveDataToFile();
        }

        private void deleteContact() {
            String name = nameField.getText();
            String group = groupField.getText();

            if (group.isEmpty() || name.isEmpty() || !phonebook.containsKey(group)) {
                JOptionPane.showMessageDialog(this, "Invalid group or contact.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            phonebook.get(group).removeIf(contact -> contact.getName().equals(name));
            JOptionPane.showMessageDialog(this, "Contact deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            saveDataToFile();
        }

        private void searchContact() {
            String name = nameField.getText();
            String group = groupField.getText();

            if (group.isEmpty() || name.isEmpty() || !phonebook.containsKey(group)) {
                JOptionPane.showMessageDialog(this, "Invalid group or contact.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Contact contact : phonebook.get(group)) {
                if (contact.getName().equals(name)) {
                    JOptionPane.showMessageDialog(this, "Found: " + contact, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Contact not found.", "Search Result", JOptionPane.ERROR_MESSAGE);
        }

        private void updateContact() {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String group = groupField.getText();

            if (group.isEmpty() || name.isEmpty() || !phonebook.containsKey(group)) {
                JOptionPane.showMessageDialog(this, "Invalid group or contact.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Contact contact : phonebook.get(group)) {
                if (contact.getName().equals(name)) {
                    contact.setPhone(phone);
                    JOptionPane.showMessageDialog(this, "Contact updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    saveDataToFile();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Contact not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        private void displayContacts() {
            contactArea.setText("");
            phonebook.forEach((group, contacts) -> {
                contactArea.append("Group: " + group + "\n");
                contacts.forEach(contact -> contactArea.append(contact + "\n"));
                contactArea.append("\n");
            });
        }

        private void addGroup() {
            String group = groupField.getText();
            if (group.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Group name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            phonebook.putIfAbsent(group, new ArrayList<>());
            JOptionPane.showMessageDialog(this, "Group added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            saveDataToFile();
        }

        private void deleteGroup() {
            String group = groupField.getText();
            if (group.isEmpty() || !phonebook.containsKey(group)) {
                JOptionPane.showMessageDialog(this, "Invalid group.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            phonebook.remove(group);
            JOptionPane.showMessageDialog(this, "Group deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            saveDataToFile();
        }

        private void openPayrollSystem() {
            new PayrollSystem(phonebook).setVisible(true);
        }

        private void saveDataToFile() {
            try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
                for (Map.Entry<String, List<Contact>> entry : phonebook.entrySet()) {
                    writer.println("Group: " + entry.getKey());
                    for (Contact contact : entry.getValue()) {
                        writer.println(contact.getName() + "," + contact.getPhone());
                    }
                    writer.println();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadDataFromFile() {
            try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
                String line;
                String currentGroup = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Group: ")) {
                        currentGroup = line.substring(7);
                        phonebook.putIfAbsent(currentGroup, new ArrayList<>());
                    } else if (!line.isBlank() && currentGroup != null) {
                        String[] parts = line.split(",");
                        phonebook.get(currentGroup).add(new Contact(parts[0], parts[1]));
                    }
                }
            } catch (IOException e) {
                // File might not exist on the first run, which is fine
            }
        }

        static class Contact {
            private String name;
            private String phone;

            public Contact(String name, String phone) {
                this.name = name;
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            @Override
            public String toString() {
                return name + " - " + phone;
            }
        }
    }

    static class PayrollSystem extends JFrame {
        private Map<String, List<PhonebookSystem.Contact>> phonebook;
        private JButton openCalculatorButton;

        public PayrollSystem(Map<String, List<PhonebookSystem.Contact>> phonebook) {
            this.phonebook = phonebook;
            setTitle("Payroll System");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);

            openCalculatorButton = new JButton("Open Calculator");
            openCalculatorButton.addActionListener(e -> new Calculator());

            add(openCalculatorButton, BorderLayout.CENTER);
        }
    }

    static class Calculator extends JFrame {
        private JTextField inputField;
        private JButton addButton, subtractButton, multiplyButton, divideButton, clearButton;
        private JTextArea historyArea;

        public Calculator() {
            setTitle("Calculator");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 400);

            inputField = new JTextField(15);
            JPanel panel = new JPanel();
            panel.add(inputField);

            addButton = new JButton("+");
            subtractButton = new JButton("-");
            multiplyButton = new JButton("*");
            divideButton = new JButton("/");
            clearButton = new JButton("C");

            panel.add(addButton);
            panel.add(subtractButton);
            panel.add(multiplyButton);
            panel.add(divideButton);
            panel.add(clearButton);

            historyArea = new JTextArea(10, 30);
            historyArea.setEditable(false);

            add(panel, BorderLayout.NORTH);
            add(new JScrollPane(historyArea), BorderLayout.CENTER);

            addButton.addActionListener(e -> performCalculation('+'));
            subtractButton.addActionListener(e -> performCalculation('-'));
            multiplyButton.addActionListener(e -> performCalculation('*'));
            divideButton.addActionListener(e -> performCalculation('/'));
            clearButton.addActionListener(e -> clearHistory());

            setVisible(true);
        }

        private void performCalculation(char operator) {
            String input = inputField.getText();
            try {
                String[] parts = input.split(" ");
                double operand1 = Double.parseDouble(parts[0]);
                double operand2 = Double.parseDouble(parts[1]);
                double result = 0;

                switch (operator) {
                    case '+': result = operand1 + operand2; break;
                    case '-': result = operand1 - operand2; break;
                    case '*': result = operand1 * operand2; break;
                    case '/': result = operand1 / operand2; break;
                }

                historyArea.append(input + " " + operator + " = " + result + "\n");
                inputField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input. Use format: number1 number2", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void clearHistory() {
            historyArea.setText("");
        }
    }
}
