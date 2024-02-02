// File: User.java

class User {
    String username;
    char[] password;
    String securityQuestion;
    String securityAnswer;

    public User(String username, char[] password, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
}
