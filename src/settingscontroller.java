import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * settingscontroller
 */

public class settingscontroller {

    @FXML
    Button backtoMain;
    @FXML
    Button changepass;
    @FXML
    Button ViewLimit;

    /*------------------------------------------------- settingscontroller -----
    |  Method switchToMainScene
    |
    |  Purpose: To load and display the FXMl file of MainMenu 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /*------------------------------------------------- settingscontroller -----
    |  Method switchToChangePass
    |
    |  Purpose: To load and display the FXMl file of ChangePass 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToChangePass(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/ChangePass.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Change Password");
        stage.setScene(scene);
        stage.show();
    }

    /*------------------------------------------------- settingscontroller -----
    |  Method switchToSceneViewLimit
    |
    |  Purpose: To load and display the FXMl file of ViewLimit 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToSceneViewLimit(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/ViewLimit.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("View Limits");
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- settingscontroller -----
    |  Method logout
    |
    |  Purpose: To load and display the FXMl file of LoginPage 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void logout(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

}