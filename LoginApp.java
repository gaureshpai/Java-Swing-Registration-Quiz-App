import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class LoginApp extends JFrame implements ActionListener {

    private static final String USERNAME_LABEL_TEXT = "Username:";
    private static final String PASSWORD_LABEL_TEXT = "Password:";
    private static final String TOP_BAR_TEXT = "Login to Take the Quiz";

    JLabel usernameLabel, passwordLabel, topBarLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPasswordCheckbox;
    JButton loginButton, forgotPasswordButton, registerButton;

    User[] users;

    private ForgotPasswordApp forgotPasswordApp;

    public LoginApp(User[] users) {
        setTitle("Login");
        setLayout(new BorderLayout(20, 20));
        this.users = users;

        topBarLabel = new JLabel(TOP_BAR_TEXT, SwingConstants.CENTER);
        topBarLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBarLabel.setForeground(Color.WHITE);
        topBarLabel.setBackground(new Color(102, 102, 102));
        topBarLabel.setOpaque(true);
        add(topBarLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
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

        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.addActionListener(this);
        inputPanel.add(new JPanel());
        inputPanel.add(forgotPasswordButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        inputPanel.add(new JPanel());
        inputPanel.add(registerButton);

        add(inputPanel, BorderLayout.CENTER);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

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

            new LoginWorker(usernameField.getText()).execute();
        } else if (e.getSource() == showPasswordCheckbox) {
            passwordField.setEchoChar(showPasswordCheckbox.isSelected() ? '\0' : '*');
        } else if (e.getSource() == forgotPasswordButton) {
            handleForgotPassword();
        } else if (e.getSource() == registerButton) {
            handleRegistration();
        }
    }

    private void handleForgotPassword() {

        String username = usernameField.getText();
        User user = findUserByUsername(username);

        if (user != null) {
            if (forgotPasswordApp != null) {
                forgotPasswordApp.dispose();
            }
            forgotPasswordApp = new ForgotPasswordApp(users);
        } else {
            showErrorMessage("Username not found.");
        }
    }

    private void handleRegistration() {
        RegistrationWindow registrationWindow = new RegistrationWindow();
        registrationWindow.setVisible(true);
    }

    private User findUserByUsername(String username) {
        if (users == null || users.length == 0) {
            return null;
        }
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    private class LoginWorker extends SwingWorker<Boolean, Void> {
        private String username;

        public LoginWorker(String username) {
            this.username = username;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();

            for (User user : users) {
                if (user.getUsername().equals(username) && Arrays.equals(user.getPassword(), passwordChars)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        protected void done() {
            try {
                boolean loginSuccess = get();

                if (loginSuccess) {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        OnlineTest testApp = new OnlineTest("Online Test of Java for " + username);
                        testApp.setVisible(true);
                    });
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
        topBarLabel.setText(message);
        topBarLabel.setBackground(new Color(255, 77, 77));
        topBarLabel.setOpaque(true);

        javax.swing.Timer timer = new javax.swing.Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topBarLabel.setText(TOP_BAR_TEXT);
                topBarLabel.setBackground(new Color(102, 102, 102));
                topBarLabel.setOpaque(true);
                ((javax.swing.Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private class RegistrationWindow extends JFrame implements ActionListener {

        JTextField usernameField, securityQuestionField, securityAnswerField;
        JPasswordField passwordField;
        JButton registerUserButton;

        public RegistrationWindow() {
            setTitle("Registration");
            setLayout(new GridLayout(5, 2, 10, 10));

            add(new JLabel("Username:"));
            usernameField = new JTextField();
            add(usernameField);

            add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            add(passwordField);

            add(new JLabel("Security Question:"));
            securityQuestionField = new JTextField();
            add(securityQuestionField);

            add(new JLabel("Security Answer:"));
            securityAnswerField = new JTextField();
            add(securityAnswerField);

            registerUserButton = new JButton("Register");
            registerUserButton.addActionListener(this);
            add(registerUserButton);

            setSize(300, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == registerUserButton) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String securityQuestion = securityQuestionField.getText();
                String securityAnswer = securityAnswerField.getText();

                User newUser = new User(username, password, securityQuestion, securityAnswer);
                users = Arrays.copyOf(users, users.length + 1);
                users[users.length - 1] = newUser;

                UserDatabase.saveUsers(users);
                dispose();
            }
        }
    }

    public static void main(String s[]) {
        User[] users = UserDatabase.loadUsers();
        new LoginApp(users).setVisible(true);
    }
}
