import java.sql.*;
import javax.swing.JOptionPane;

public class ATM {
    private Connection connection;

    public ATM() {
        // Get the singleton instance of DatabaseConnection
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public Account getAccount(String accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                String pin = rs.getString("pin");
                return new Account(accountNumber, balance, pin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAccount(Account account) {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, account.getBalance());
            stmt.setString(2, account.getAccountNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        // Logic to handle logout
        JOptionPane.showMessageDialog(null, "Logged out successfully.");
        DatabaseConnection.getInstance().closeConnection();  // Close the connection
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        ATMInterface atmInterface = new ATMInterface(atm);
        atmInterface.setVisible(true);
    }
}
