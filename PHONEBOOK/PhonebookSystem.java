private JPanel createInputPanel() {
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    inputPanel.setBackground(new Color(173, 216, 230)); // Soft teal background
    inputPanel.setBorder(BorderFactory.createTitledBorder("Add or Search Contact"));

    nameField = new JTextField(15);
    phoneField = new JTextField(15);
    JLabel nameLabel = new JLabel("Name:");
    JLabel phoneLabel = new JLabel("Phone:");

    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));

    inputPanel.add(nameLabel);
    inputPanel.add(nameField);
    inputPanel.add(phoneLabel);
    inputPanel.add(phoneField);
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

    buttonPanel.add(addButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(searchButton);
    buttonPanel.add(updateButton);
    buttonPanel.add(displayButton);

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
