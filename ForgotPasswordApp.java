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

        int randomIndex = (int) (Math.random() * users.length);
        User user = users[randomIndex];

        securityQuestionLabel = new JLabel("Security Question: " + user.getSecurityQuestion());
        mainPanel.add(securityQuestionLabel, BorderLayout.NORTH);

        securityAnswerField = new JTextField();
        mainPanel.add(securityAnswerField, BorderLayout.CENTER);

        retrievePasswordButton = new JButton("Retrieve Password");
        retrievePasswordButton.addActionListener(this);
        mainPanel.add(retrievePasswordButton, BorderLayout.SOUTH);

        this.users = users;

        add(mainPanel);

        pack();
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

        for (User user : users) {
            if (answer.equals(user.getSecurityAnswer())) {
                JOptionPane.showMessageDialog(this, "Your password is: " + new String(user.getPassword()));
                dispose(); 
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Incorrect answer to security question or user not found.");
    }

    public static void main(String[] args) {

        User[] testUsers = { new User("testuser1", "testpassword1".toCharArray(), "What is your favorite color?", "Green"),
                             new User("testuser2", "testpassword2".toCharArray(), "What is your pet's name?", "Rex"),
                             new User("testuser3", "testpassword3".toCharArray(), "Where were you born?", "New York") };
        SwingUtilities.invokeLater(() -> {
            new ForgotPasswordApp(testUsers);
        });
    }
}
