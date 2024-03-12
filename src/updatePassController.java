
/**
 * updatePassController
 */

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.*;
   /*------------------------------------------------- updatePassController -----
    |  class updatePassController
    |
    |  Purpose: This class contains methods of the Change Pass page
    |                                                   
    *-------------------------------------------------------------------*/
public class updatePassController {

    @FXML
    TextField username;
    @FXML
    PasswordField oldpass;
    @FXML
    PasswordField newpass;
    @FXML
    private Text errorMessage;
    @FXML
    private Button submit;
    @FXML
    Label error;
    @FXML
    Button backtosetting;

    DataSingleton data = DataSingleton.getInstance();

    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";
   /*------------------------------------------------- updatePassController -----
    |  Method UpdatePassword
    |
    |  Purpose: This method is linked to a javafx button,this method would first called the authenticate method
    |           if the password is correct the method would then update the new password based on user input in the database
    |                                                   
    *-------------------------------------------------------------------*/
    public void UpdatePassword(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        String enteredPassword = oldpass.getText();
        String newPassword = newpass.getText();

        if (authenticate(data.getUserName(), enteredPassword)) {
            // Successful login
            try {
                int newPin = Integer.parseInt(newPassword);
                ChangePassword(newPassword);
                error.setText("Password Changed!");
            } catch (NumberFormatException e) {
                // Password is not an integer
                error.setText("New password must be an integer!");
            }
        } else {
            // Failed login
            error.setText("Wrong password!");
        }
    }
    /*------------------------------------------------- updatePassController -----
    |  Method authenticate
    |
    |  Purpose: This method takes in the user input of username and password, using the inputs we will go into the database to search for the user
    |           if both user and password returns then we would return success(boolean True) else we would return false
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public boolean authenticate(String username, String password) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            int pin = Integer.parseInt(password);
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            conn = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD);
            stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND pin = ?");
            stmt.setString(1, username);
            stmt.setInt(2, pin);
            rs = stmt.executeQuery();
            success = rs.next();
        } catch (NumberFormatException e) {
            // The password is not an integer
            success = false;
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }
   /*------------------------------------------------- updatePassController -----
    |  Method ChangePassword
    |
    |  Purpose: This method takes in the password and updates the database password field according to the userName
    |           using the datasingleton class
    |                                                   
    *-------------------------------------------------------------------*/
    public void ChangePassword(String password) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean check = true;
        Console cons = System.console();
        boolean success = false;
        int pin;
        ResultSet rs = null;

        pin = Integer.parseInt(new String(password));
        try {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            conn = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD);
            stmt = conn.prepareStatement("UPDATE user SET PIN = ? WHERE Username = ?");
            stmt.setInt(1, pin);
            stmt.setString(2, data.getUserName());
            stmt.executeUpdate();
        } catch (SQLException SqlE) {
            System.out.println(SqlE);
        }

    }
   /*------------------------------------------------- updatePassController -----
    |  Method backtosettings
    |
    |  Purpose:  To load and display the FXMl file of settings 
    |
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

   /*------------------------------------------------- updatePassController -----
    |  Method logout
    |
    |  Purpose:  To load and display the FXMl file of LoginPage 
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
