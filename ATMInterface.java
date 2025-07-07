import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMInterface extends JFrame {
    private ATM atm;
    private JTextField accountNumberField;
    private JPasswordField pinField;

    public ATMInterface(ATM atm) {
        this.atm = atm;
        setTitle("ATM Interface");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setBounds(10, 20, 120, 25);
        panel.add(accountNumberLabel);

        accountNumberField = new JTextField(20);
        accountNumberField.setBounds(140, 20, 165, 25);
        panel.add(accountNumberField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(10, 50, 80, 25);
        panel.add(pinLabel);

        pinField = new JPasswordField(20);
        pinField.setBounds(140, 50, 165, 25);
        panel.add(pinField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String accountNumber = accountNumberField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (accountNumber.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter account number and PIN.");
            return;
        }

        Account account = atm.getAccount(accountNumber);
        if (account != null && pin.equals(account.getPin())) {
            new AccountOperations(atm, accountNumber).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid account number or PIN.");
        }
    }
}
