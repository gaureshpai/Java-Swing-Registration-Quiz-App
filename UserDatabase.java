import java.io.*;
import java.util.*;

public class UserDatabase {
    private static final String FILENAME = "users.txt";

    public static User[] loadUsers() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[0];
                    char[] password = parts[1].toCharArray();
                    String securityQuestion = parts[2];
                    String securityAnswer = parts[3];
                    userList.add(new User(username, password, securityQuestion, securityAnswer));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: users.txt file not found.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading users: " + e.getMessage());
        }
        return userList.toArray(new User[0]);
    }

    public static void saveUsers(User[] users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            for (User user : users) {
                writer.println(user.getUsername() + "," + String.valueOf(user.getPassword()) + "," + user.getSecurityQuestion() + "," + user.getSecurityAnswer());
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}
