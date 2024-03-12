import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.stage.Stage;
   /*------------------------------------------------- UpdateLimits -----
    |  class UpdateLimits
    |
    |  Purpose: This class contains methods of the View limit page
    |                                                   
    *-------------------------------------------------------------------*/
public class UpdateLimits implements Initializable {
    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";

    private static String UpdateWithdrawalLimit = "UPDATE User SET  withdrawlimit= ?  WHERE userID = ?";
    private static String UpdateTransferLimit = "UPDATE User SET  transferLimit= ?  WHERE userID = ?";

    @FXML
    Text Withdrawlimit;

    @FXML
    Text TransferLimit;

    @FXML
    Button UpdateLimitsbutton;
    @FXML
    TextField submittransfer;
    @FXML
    TextField submitwithdraw;
    @FXML
    Button backtoMain;

    @FXML
    Label limiterror;

    java.util.Date transactionDate;
    DataSingleton data = DataSingleton.getInstance();

    public void initialize(URL url, ResourceBundle resourceBundle) {

        String UsernameEntered = data.getUserName();

        User newUser = new User(UsernameEntered);

        newUser.readDB();

        Transactions tr = new Transactions(newUser.getUserID());
       
        Withdrawlimit.setText(String.valueOf(newUser.getWithdrawLimit()));
        TransferLimit.setText((String.valueOf(newUser.getTransferLimit())));

    }
   /*------------------------------------------------- UpdateLimits -----
    |  Method updatelimits
    |
    |  Purpose: This method is linked a javafx button,this method would update the user withdraw and transfer limit
    |           based on the new limits that had been inputted 
    |                                                   
    *-------------------------------------------------------------------*/
    @FXML
    public void updatelimits(ActionEvent Event) throws ClassNotFoundException, SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);

            PreparedStatement UpdateDailyTransferLimit = connection.prepareStatement(UpdateTransferLimit);
            PreparedStatement UpdateDailyWithdrawalLimit = connection.prepareStatement(UpdateWithdrawalLimit);

            try {
                int withdrawalLimit = Integer.parseInt(submitwithdraw.getText());
                UpdateDailyWithdrawalLimit.setInt(2, data.getUserID());
                UpdateDailyWithdrawalLimit.setInt(1, withdrawalLimit);
             
                UpdateDailyWithdrawalLimit.executeUpdate();
            } catch (NumberFormatException e) {
               limiterror.setText("Invalid withdrawal limit input: " + submitwithdraw.getText());
                
            }

            try {
                int transferLimit = Integer.parseInt(submittransfer.getText());
                UpdateDailyTransferLimit.setInt(2, data.getUserID());
                UpdateDailyTransferLimit.setInt(1, transferLimit);
                UpdateDailyTransferLimit.executeUpdate();
          
            } catch (NumberFormatException e) {
                limiterror.setText("Invalid transfer limit input: " + submittransfer.getText());
               
            }
        limiterror.setText("Successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error occurred while executing query: " + UpdateWithdrawalLimit);
            e.printStackTrace();
        }

    }
   /*------------------------------------------------- UpdateLimits -----
    |  Method backtoMain
    |
    |  Purpose: To load and display the FXMl file of MainMenu 
    |                                                   
    *-------------------------------------------------------------------*/
    public void backtoMain(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        // scene.getStylesheets().add(css);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

    }
   /*------------------------------------------------- UpdateLimits -----
    |  Method backtosettings
    |
    |  Purpose: To load and display the FXMl file of settings 
    |                                                   
    *-------------------------------------------------------------------*/
    public void backtosettings(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        // scene.getStylesheets().add(css);
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();

    }

   /*------------------------------------------------- UpdateLimits -----
    |  Method logout
    |
    |  Purpose: To load and display the FXMl file of LoginPage 
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
