import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;

public class App extends Application {

    /*------------------------------------------------- App -----
    |  Method start
    |
    |  Purpose:  To load and display the FXMl file of LoginPage 
    |
    |
    *-------------------------------------------------------------------*/
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        // Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/LoginPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Login");
            String css = this.getClass().getResource("atm.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
