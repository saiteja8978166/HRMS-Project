import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.println("MySQL Connected Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}