// File: ForgotPasswordApp.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgotPasswordApp extends JFrame implements ActionListener {

    private JLabel securityQuestionLabel;
    private JTextField securityAnswerField;
    private JButton retrievePasswordButton;

    private User user;

    public ForgotPasswordApp(User user) {
        this.user = user;

        setTitle("Forgot Password");
        setLayout(new BorderLayout(20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Display security question
        securityQuestionLabel = new JLabel("Security Question: " + user.securityQuestion);
        mainPanel.add(securityQuestionLabel, BorderLayout.NORTH);

        securityAnswerField = new JTextField();
        mainPanel.add(securityAnswerField, BorderLayout.CENTER);

        retrievePasswordButton = new JButton("Retrieve Password");
        retrievePasswordButton.addActionListener(this);
        mainPanel.add(retrievePasswordButton, BorderLayout.SOUTH);

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
        String answer = securityAnswerField.getText();

        if (answer.equals(user.securityAnswer)) {
            JOptionPane.showMessageDialog(this, "Your password is: " + new String(user.password));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect answer to security question.");
        }
    }

    public static void main(String[] args) {
        // For testing the ForgotPasswordApp independently
        User testUser = new User("testuser", "testpassword".toCharArray(), "What is your favorite color?", "Green");
        SwingUtilities.invokeLater(() -> {
            new ForgotPasswordApp(testUser);
        });
    }
}
