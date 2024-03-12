
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;

public class MainSceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /*------------------------------------------------- MainSceneController -----
    |  Method switchToScene
    |
    |  Purpose: To load and display the FXMl file of MainScene 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- MainSceneController -----
    |  Method switchToScene2
    |
    |  Purpose: To load and display the FXMl file of LoginPage 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToScene2(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void btnOKClicked(ActionEvent event) {

    }

}
