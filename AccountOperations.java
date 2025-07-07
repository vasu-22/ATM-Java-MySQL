import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountOperations extends JFrame {
    private ATM atm;
    private String accountNumber;
    private JTextField amountField;
    private JLabel balanceLabel;
    private JTextArea operationHistory;

    public AccountOperations(ATM atm, String accountNumber) {
        this.atm = atm;
        this.accountNumber = accountNumber;
        setTitle("Account Operations");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        // Load and display account balance
        loadAccountDetails();
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel balanceTitle = new JLabel("Balance:");
        balanceTitle.setBounds(10, 20, 80, 25);
        panel.add(balanceTitle);

        balanceLabel = new JLabel();
        balanceLabel.setBounds(100, 20, 165, 25);
        panel.add(balanceLabel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(10, 50, 80, 25);
        panel.add(amountLabel);

        amountField = new JTextField(20);
        amountField.setBounds(100, 50, 165, 25);
        panel.add(amountField);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(10, 80, 100, 25);
        panel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(120, 80, 100, 25);
        panel.add(withdrawButton);

        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(230, 80, 100, 25);
        panel.add(transferButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 110, 100, 25);
        panel.add(logoutButton);

        operationHistory = new JTextArea();
        operationHistory.setBounds(10, 140, 360, 120);
        operationHistory.setEditable(false);
        panel.add(operationHistory);

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performDeposit();
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performWithdraw();
            }
        });

        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performTransfer();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atm.logout();
                new ATMInterface(atm).setVisible(true);
                dispose();
            }
        });
    }

    private void loadAccountDetails() {
        Account account = atm.getAccount(accountNumber);
        if (account != null) {
            balanceLabel.setText(String.format("$%.2f", account.getBalance()));
        }
    }

    private void performDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.");
                return;
            }

            Account account = atm.getAccount(accountNumber);
            if (account != null) {
                account.setBalance(account.getBalance() + amount);
                atm.updateAccount(account);
                loadAccountDetails();
                operationHistory.append(String.format("Deposited: $%.2f\n", amount));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void performWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.");
                return;
            }

            Account account = atm.getAccount(accountNumber);
            if (account != null) {
                if (account.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds.");
                    return;
                }
                account.setBalance(account.getBalance() - amount);
                atm.updateAccount(account);
                loadAccountDetails();
                operationHistory.append(String.format("Withdrew: $%.2f\n", amount));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void performTransfer() {
        String targetAccountNumber = JOptionPane.showInputDialog(this, "Enter target account number:");
        if (targetAccountNumber == null || targetAccountNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Target account number cannot be empty.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.");
                return;
            }

            Account sourceAccount = atm.getAccount(accountNumber);
            Account targetAccount = atm.getAccount(targetAccountNumber);
            if (sourceAccount != null && targetAccount != null) {
                if (sourceAccount.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds.");
                    return;
                }

                sourceAccount.setBalance(sourceAccount.getBalance() - amount);
                targetAccount.setBalance(targetAccount.getBalance() + amount);
                atm.updateAccount(sourceAccount);
                atm.updateAccount(targetAccount);
                loadAccountDetails();
                operationHistory.append(String.format("Transferred: $%.2f to %s\n", amount, targetAccountNumber));
            } else {
                JOptionPane.showMessageDialog(this, "Target account not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }
}
