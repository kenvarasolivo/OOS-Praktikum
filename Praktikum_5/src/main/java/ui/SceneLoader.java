package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Bank.PrivateBank;

public class SceneLoader {
    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void loadMainPage(PrivateBank privateBank) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/mainpage.fxml"));
            Parent root = loader.load();
            MainPageController controller = loader.getController();
            controller.setPrivateBank(privateBank); // Parameter nachträglich übergeben
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAccountPage(String accountName, PrivateBank privateBank) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/accountpage.fxml"));
            Parent root = loader.load();
            AccountPageController controller = loader.getController();
            controller.setAccountDetails(accountName, privateBank); // Parameter nachträglich übergeben
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
