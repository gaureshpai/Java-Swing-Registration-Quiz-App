public class User {
    private String username;
    private char[] password;
    private String securityQuestion;
    private String securityAnswer;

    public User(String username, char[] password, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }
}
