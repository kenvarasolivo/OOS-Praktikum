package ui;

import Bank.Exceptions.TransactionAttributeException;
import javafx.application.Application;
import javafx.stage.Stage;
import Bank.PrivateBank;

public class FxApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws TransactionAttributeException {
        SceneLoader.setStage(primaryStage);
        SceneLoader.loadMainPage(new PrivateBank("MyBank", 0.2, 0.5));
        primaryStage.setTitle("PrivateBank");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
