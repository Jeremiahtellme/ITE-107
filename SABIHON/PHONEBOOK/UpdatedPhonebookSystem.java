
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PhonebookSystem extends JFrame {

    private JTextField nameField, phoneField, groupField;
    private JTextArea contactArea;
    private JComboBox<String> groupComboBox;
    private JButton addButton, deleteButton, searchButton, updateButton, displayButton, addGroupButton, deleteGroupButton;
    private Map<String, List<Contact>> phonebook;

    private static final String DATA_FILE = "phonebook_data.txt";

    public PhonebookSystem() {
        setTitle("Phonebook System with Groups");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout(10, 10));

        phonebook = new HashMap<>();
        loadDataFromFile();

        add(createInputPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(createTextAreaPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(173, 216, 230)); // Soft teal background
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add or Search Contact"));

        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        groupField = new JTextField(15);

        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel groupLabel = new JLabel("Group:");

        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        groupLabel.setFont(new Font("Arial", Font.BOLD, 14));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        inputPanel.add(groupLabel);
        inputPanel.add(groupField);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setBackground(new Color(240, 128, 128)); // Light coral background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addButton = createStyledButton("Add");
        deleteButton = createStyledButton("Delete");
        searchButton = createStyledButton("Search");
        updateButton = createStyledButton("Update");
        displayButton = createStyledButton("Display");
        addGroupButton = createStyledButton("Add Group");
        deleteGroupButton = createStyledButton("Delete Group");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(addGroupButton);
        buttonPanel.add(deleteGroupButton);

        // Add Action Listeners
        addButton.addActionListener(e -> addContact());
        deleteButton.addActionListener(e -> deleteContact());
        searchButton.addActionListener(e -> searchContact());
        updateButton.addActionListener(e -> updateContact());
        displayButton.addActionListener(e -> displayContacts());
        addGroupButton.addActionListener(e -> addGroup());
        deleteGroupButton.addActionListener(e -> deleteGroup());

        return buttonPanel;
    }

    private JPanel createTextAreaPanel() {
        JPanel textAreaPanel = new JPanel(new BorderLayout());
        textAreaPanel.setBackground(new Color(152, 251, 152)); // Pale green background
        textAreaPanel.setBorder(BorderFactory.createTitledBorder("Contacts"));

        contactArea = new JTextArea(12, 20);
        contactArea.setEditable(false);
        contactArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        contactArea.setBackground(new Color(245, 245, 220)); // Light beige for text area background
        contactArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scrollPane = new JScrollPane(contactArea);
        textAreaPanel.add(scrollPane, BorderLayout.CENTER);

        return textAreaPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return button;
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String group = groupField.getText();

        if (name.isEmpty() || phone.isEmpty() || group.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        phonebook.putIfAbsent(group, new ArrayList<>());
        phonebook.get(group).add(new Contact(name, phone));
        contactArea.append("Added: " + name + " to group " + group + "\n");
        saveDataToFile();
    }

    private void deleteContact() {
        String name = nameField.getText();
        String group = groupField.getText();

        if (group.isEmpty() || name.isEmpty() || !phonebook.containsKey(group)) {
            JOptionPane.showMessageDialog(this, "Invalid group or name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        phonebook.get(group).removeIf(contact -> contact.getName().equals(name));
        contactArea.append("Deleted: " + name + " from group " + group + "\n");
        saveDataToFile();
    }

    private void searchContact() {
        String name = nameField.getText();
        String group = groupField.getText();

        if (group.isEmpty() || name.isEmpty() || !phonebook.containsKey(group)) {
            JOptionPane.showMessageDialog(this, "Invalid group or name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Contact contact : phonebook.get(group)) {
            if (contact.getName().equals(name)) {
                JOptionPane.showMessageDialog(this, "Found: " + contact, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Contact not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String group = groupField.getText();

        if (group.isEmpty() || name.isEmpty() || !phonebook.containsKey(group)) {
            JOptionPane.showMessageDialog(this, "Invalid group or name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Contact contact : phonebook.get(group)) {
            if (contact.getName().equals(name)) {
                contact.setPhone(phone);
                contactArea.append("Updated: " + name + " in group " + group + "\n");
                saveDataToFile();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Contact not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void displayContacts() {
        contactArea.setText("");
        for (Map.Entry<String, List<Contact>> entry : phonebook.entrySet()) {
            contactArea.append("Group: " + entry.getKey() + "\n");
            for (Contact contact : entry.getValue()) {
                contactArea.append(contact + "\n");
            }
        }
    }

    private void addGroup() {
        String group = groupField.getText();
        if (group.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Group name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        phonebook.putIfAbsent(group, new ArrayList<>());
        contactArea.append("Group added: " + group + "\n");
        saveDataToFile();
    }

    private void deleteGroup() {
        String group = groupField.getText();
        if (group.isEmpty() || !phonebook.containsKey(group)) {
            JOptionPane.showMessageDialog(this, "Invalid group name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        phonebook.remove(group);
        contactArea.append("Group deleted: " + group + "\n");
        saveDataToFile();
    }

    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, List<Contact>> entry : phonebook.entrySet()) {
                writer.println("Group: " + entry.getKey());
                for (Contact contact : entry.getValue()) {
                    writer.println(contact.getName() + "," + contact.getPhone());
                }
                writer.println(); // Empty line between groups
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
            // File might not exist on first run, which is fine
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PhonebookSystem::new);
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

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String toString() {
            return name + " - " + phone;
        }
    }
}
