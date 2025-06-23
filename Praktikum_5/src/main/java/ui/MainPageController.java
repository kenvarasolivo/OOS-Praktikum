package ui;

import Bank.Exceptions.TransactionAttributeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import Bank.PrivateBank;

public class MainPageController {
    @FXML private ListView<String> accountListView; //ui
    private ObservableList<String> accounts; //data source
    private PrivateBank privateBank;

    public void setPrivateBank(PrivateBank privateBank) {
        this.privateBank = privateBank;
        initializeData();
    }

    private void initializeData() {
        accounts = FXCollections.observableArrayList(privateBank.getAllAccounts());
        accountListView.setItems(accounts);

        // Set up the context menu programmatically
        ContextMenu contextMenu = new ContextMenu();
        MenuItem selectItem = new MenuItem("Auswählen");
        MenuItem deleteItem = new MenuItem("Löschen");

        selectItem.setOnAction(event -> onSelectAccount());
        deleteItem.setOnAction(event -> onDeleteAccount());
        contextMenu.getItems().addAll(selectItem, deleteItem);

        accountListView.setContextMenu(contextMenu);
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void onCreateAccount() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Create a New Account");
        dialog.setContentText("Account Name:");

        dialog.showAndWait().ifPresent(accountName -> { //Optional<string> method
            try {
                privateBank.createAccount(accountName);
                accounts.add(accountName);
            } catch (Exception e) {
                showError("Error", e.getMessage());
            }
        });
    }

    @FXML
    public void onDeleteAccount() {
        String selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showError("Error", "No account selected!");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the account?", ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                privateBank.deleteAccount(selectedAccount);
                accounts.remove(selectedAccount);
            } catch (Exception e) {
                showError("Error", e.getMessage());
            }
        }
    }

    @FXML
    public void onSelectAccount() {
        String selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showError("Error", "No account selected!");
            return;
        }
        SceneLoader.loadAccountPage(selectedAccount, privateBank);
    }

    @FXML
    public void onExit() {
        // Close the application
        System.exit(0);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
