import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;

public class LoginApp extends JFrame implements ActionListener {

    private static final String USERNAME_LABEL_TEXT = "Username:";
    private static final String PASSWORD_LABEL_TEXT = "Password:";
    private static final String TOP_BAR_TEXT = "Login to Take the Quiz";

    JLabel usernameLabel, passwordLabel, topBarLabel, securityQuestionLabel, securityAnswerLabel;
    JTextField usernameField, securityAnswerField;
    JPasswordField passwordField;
    JCheckBox showPasswordCheckbox;
    JButton loginButton, forgotPasswordButton;

    User[] users = {
        new User("user1", "password1".toCharArray(), "What is your pet's name?", "Fluffy"),
        new User("a", "b".toCharArray(), "Where were you born?", "CityX"),
        new User("user2", "password2".toCharArray(), "What is your favorite color?", "Blue")
    };

    // Forgot Password components
    private ForgotPasswordApp forgotPasswordApp;

    public LoginApp() {
        setTitle("Login");
        setLayout(new BorderLayout(20, 20));

        // Top bar with title
        topBarLabel = new JLabel(TOP_BAR_TEXT, SwingConstants.CENTER);
        topBarLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBarLabel.setForeground(Color.WHITE);
        topBarLabel.setBackground(new Color(102, 102, 102));
        topBarLabel.setOpaque(true);
        add(topBarLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel(USERNAME_LABEL_TEXT));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel(PASSWORD_LABEL_TEXT));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        showPasswordCheckbox = new JCheckBox("Show Password");
        inputPanel.add(new JPanel());
        inputPanel.add(showPasswordCheckbox);

        // Forgot Password button on the right side
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.addActionListener(this);
        inputPanel.add(new JPanel());
        inputPanel.add(forgotPasswordButton);

        add(inputPanel, BorderLayout.CENTER);

        // Login button at the bottom
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Styling changes
        inputPanel.setBackground(Color.WHITE);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);
        usernameField.setFont(inputFont);
        passwordField.setFont(inputFont);
        inputPanel.setFont(inputFont);

        showPasswordCheckbox.addActionListener(this);
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        showPasswordCheckbox.setForeground(Color.BLACK);

        getContentPane().setBackground(Color.WHITE);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            loginButton.setEnabled(false);
            new LoginWorker().execute();
        } else if (e.getSource() == showPasswordCheckbox) {
            passwordField.setEchoChar(showPasswordCheckbox.isSelected() ? '\0' : '*');
        } else if (e.getSource() == forgotPasswordButton) {
            handleForgotPassword();
        }
    }

    private void handleForgotPassword() {
        // Check if username exists
        String username = usernameField.getText();
        User user = findUserByUsername(username);

        if (user != null) {
            // Display the ForgotPasswordApp window
            if (forgotPasswordApp != null) {
                forgotPasswordApp.dispose();
            }
            forgotPasswordApp = new ForgotPasswordApp(user);
        } else {
            showErrorMessage("Username not found.");
        }
    }

    private User findUserByUsername(String username) {
        for (User u : users) {
            if (u.username.equals(username)) {
                return u;
            }
        }
        return null;
    }

    private class LoginWorker extends SwingWorker<Boolean, Void> {

        @Override
        protected Boolean doInBackground() throws Exception {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();

            for (User user : users) {
                if (user.username.equals(username) && Arrays.equals(user.password, passwordChars)) {
                    return true; // Successful login
                }
            }

            return false; // Unsuccessful login
        }

        @Override
        protected void done() {
            try {
                boolean loginSuccess = get();

                if (loginSuccess) {
                    dispose();
                    OnlineTest testApp = new OnlineTest("Online Test of Java for " + usernameField.getText());
                    testApp.setVisible(true);
                } else {
                    showErrorMessage("Invalid username or password");
                    Arrays.fill(passwordField.getPassword(), ' ');
                    passwordField.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loginButton.setEnabled(true);
            }
        }
    }

    private void showErrorMessage(String message) {
        // Display error message in the error label
        topBarLabel.setText(message);
        topBarLabel.setBackground(new Color(255, 77, 77));
        topBarLabel.setOpaque(true);

        // Reset the error message after a delay
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topBarLabel.setText(TOP_BAR_TEXT);
                topBarLabel.setBackground(new Color(102, 102, 102));
                topBarLabel.setOpaque(true);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String s[]) {
        SwingUtilities.invokeLater(() -> {
            new LoginApp().setVisible(true);
        });
    }
}
