import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgotPasswordApp extends JFrame implements ActionListener {

    private JLabel securityQuestionLabel;
    private JTextField securityAnswerField;
    private JButton retrievePasswordButton;

    private User[] users;

    public ForgotPasswordApp(User[] users) {
        setTitle("Forgot Password");
        setLayout(new BorderLayout(20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Randomly select a user for security question (you might want to implement a better selection logic)
        int randomIndex = (int) (Math.random() * users.length);
        User user = users[randomIndex];

        // Display security question
        securityQuestionLabel = new JLabel("Security Question: " + user.getSecurityQuestion());
        mainPanel.add(securityQuestionLabel, BorderLayout.NORTH);

        securityAnswerField = new JTextField();
        mainPanel.add(securityAnswerField, BorderLayout.CENTER);

        retrievePasswordButton = new JButton("Retrieve Password");
        retrievePasswordButton.addActionListener(this);
        mainPanel.add(retrievePasswordButton, BorderLayout.SOUTH);

        this.users = users; // Save the users array

        add(mainPanel);

        pack(); // Auto-size the window to fit components
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == retrievePasswordButton) {
            retrievePassword();
        }
    }

    private void retrievePassword() {
        // Retrieve the user's password based on the entered security answer
        String answer = securityAnswerField.getText();

        for (User user : users) {
            if (answer.equals(user.getSecurityAnswer())) {
                JOptionPane.showMessageDialog(this, "Your password is: " + new String(user.getPassword()));
                dispose(); // Close the window after retrieving the password
                return; // Exit the method after finding the user
            }
        }

        // If the loop completes without finding a matching user
        JOptionPane.showMessageDialog(this, "Incorrect answer to security question or user not found.");
    }

    public static void main(String[] args) {
        // For testing the ForgotPasswordApp independently
        User[] testUsers = { new User("testuser1", "testpassword1".toCharArray(), "What is your favorite color?", "Green"),
                             new User("testuser2", "testpassword2".toCharArray(), "What is your pet's name?", "Rex"),
                             new User("testuser3", "testpassword3".toCharArray(), "Where were you born?", "New York") };
        SwingUtilities.invokeLater(() -> {
            new ForgotPasswordApp(testUsers);
        });
    }
}
