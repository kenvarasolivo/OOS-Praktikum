package ui;

import Bank.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import Bank.Exceptions.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountPageController {
    @FXML private Label accountNameLabel;
    @FXML private Label balanceLabel;
    @FXML private ListView<Transaction> transactionListView;
    private ObservableList<Transaction> transactions;
    private PrivateBank privateBank;
    private String accountName;

    public void setAccountDetails(String accountName, PrivateBank privateBank) {
        this.accountName = accountName;
        this.privateBank = privateBank;
        initializeData();
    }

    private void initializeData() {
        accountNameLabel.setText(accountName);
        refresh();
    }

    private void refresh() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactions(accountName));
        transactionListView.setItems(transactions);
        balanceLabel.setText("Balance: " + privateBank.getAccountBalance(accountName));
    }

    @FXML
    public void initialize() {
        // Create the ContextMenu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Löschen");
        deleteItem.setOnAction(event -> onDeleteTransaction());

        contextMenu.getItems().add(deleteItem);
        transactionListView.setContextMenu(contextMenu);
    }

    @FXML
    public void onDeleteTransaction() {
        Transaction selectedTransaction = transactionListView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showError("Error", "No transaction selected!");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this transaction?", ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                privateBank.removeTransaction(accountName, selectedTransaction);
                refresh();
            } catch (Exception e) {
                showError("Error", e.getMessage());
            }
        }
    }

    @FXML
    public void onAddTransaction() {
        // Create a dialog for adding a transaction
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");

        // Create buttons
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create UI elements
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Payment", "Transfer");
        typeComboBox.setValue(null); // Default value
        typeComboBox.setPromptText("Please select a type");

        // Additional fields for Payment type
        TextField incomingInterestField = new TextField();
        incomingInterestField.setPromptText("Incoming Interest (0 - 1)");
        incomingInterestField.setDisable(true);  // Disable initially

        TextField outgoingInterestField = new TextField();
        outgoingInterestField.setPromptText("Outgoing Interest (0 - 1)");
        outgoingInterestField.setDisable(true);  // Disable initially

        // Additional fields for Transfer type
        TextField senderField = new TextField();
        senderField.setPromptText("Sender");
        senderField.setDisable(true);  // Disable initially

        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient");
        recipientField.setDisable(true);  // Disable initially

        // Enable fields based on the selected transaction type
        typeComboBox.setOnAction(event -> {
            boolean isPayment = "Payment".equals(typeComboBox.getValue());
            incomingInterestField.setDisable(!isPayment);
            outgoingInterestField.setDisable(!isPayment);
            senderField.setDisable(isPayment);
            recipientField.setDisable(isPayment);
        });

        VBox vbox = new VBox(10); //10 pixels vertical spacing
        vbox.getChildren().addAll(
                new Label("Amount:"), amountField,
                new Label("Description:"), descriptionField,
                new Label("Type:"), typeComboBox,
                incomingInterestField,
                outgoingInterestField,
                senderField,
                recipientField
        );

        dialog.getDialogPane().setContent(vbox);

        // Set the result converter for the dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    String description = descriptionField.getText();
                    String type = typeComboBox.getValue();
                    Transaction transaction = null;

                    if ("Payment".equals(type)) {
                        double incomingInterest = Double.parseDouble(incomingInterestField.getText());
                        double outgoingInterest = Double.parseDouble(outgoingInterestField.getText());
                        transaction = new Payment(getCurrentDate(), amount, description, incomingInterest, outgoingInterest);
                    } else if ("Transfer".equals(type)) {
                        String sender = senderField.getText();
                        String recipient = recipientField.getText();

                        // Check if the account is the sender or recipient
                        if (accountName.equals(sender)) {
                            // Outgoing transfer
                            transaction = new OutgoingTransfer(getCurrentDate(), amount, description, sender, recipient);
                        } else if (accountName.equals(recipient)) {
                            // Incoming transfer
                            transaction = new IncomingTransfer(getCurrentDate(), amount, description, sender, recipient);
                        } else {
                            // If the sender/recipient doesn't match the account name, display an error
                            showError("Invalid Transaction", "Sender/Recipient does not match the current account.");
                            return null;
                        }
                    }

                    return transaction;
                } catch (NumberFormatException | TransactionAttributeException | TransactionAlreadyExistException e) {
                    showError("Invalid Input", "Please enter valid values for all fields.");
                    return null;
                }
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(transaction -> {
            try {
                // Add the transaction to the account (assuming you have a method for this in PrivateBank)
                privateBank.addTransaction(accountName, transaction);  // You might need to adjust this method call
                refresh();  // Refresh the UI (transaction list, balance, etc.)
            } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException |
                     IOException e) {
                showError("Transaction Error", "An error occurred while adding the transaction.");
                e.printStackTrace();
            }
        });
    }

    // Helper method to get the current date (replace with your actual method if needed)
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(new Date());
    }

    @FXML
    public void onBack() {
        SceneLoader.loadMainPage(privateBank);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void onSortAscending() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsSorted(accountName, true));
        transactionListView.setItems(transactions);
    }

    @FXML
    public void onSortDescending() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsSorted(accountName, false));
        transactionListView.setItems(transactions);
    }

    @FXML
    public void onShowPositive() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsByType(accountName, true));
        transactionListView.setItems(transactions);
    }

    @FXML
    public void onShowNegative() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsByType(accountName, false));
        transactionListView.setItems(transactions);
    }

}