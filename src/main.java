import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

/*------------------------------------------------- main -----
  |  Class main
  |
  |  Purpose: This contains all the methods and all FXML variable of the main menu page
  |
  |                                                   
  *-------------------------------------------------------------------*/

public class main {
    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";
    public int generatedOTP;
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    private Text errorMessage;

    @FXML
    private Button submit;

    @FXML
    Button resetPassword;

    @FXML
    Button sendEmail;

    @FXML
    TextField OTP;
    @FXML
    PasswordField newpass;
    @FXML
    TextField resetUsername;

    @FXML
    Button transferback;

    @FXML
    Label error;

    private Parent root;
    private Scene scene;
    private Stage stage;

    DataSingleton data = DataSingleton.getInstance();

    /*------------------------------------------------- main -----
    |  Method handleLoginButtonAction
    |
    |  Purpose: This method is linked to the javafx button,where it would call the authenticate method and if the authenticate method is successful
    |           we will set the username to the DataSingleton class as a global variable and at the same time load the MainMenu Page while if its 
    |           unsuccessful then ask tell the user to reinput.
    |
    |                                                   
    *-------------------------------------------------------------------*/
    @FXML
    public void handleLoginButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if (authenticate(enteredUsername, enteredPassword)) {
            // Successful login

            data.setUserName(enteredUsername);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("FxmlFiles/MainMenu.fxml"));
            root = loader.load();

            scene = new Scene(root, 800, 600);
            String css = this.getClass().getResource("atm.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            // Failed login
            errorMessage.setText("Invalid username or password");
        }

    }

    /*------------------------------------------------- main -----
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
        boolean success = false;
        int pin = 0;
        ResultSet rs = null;

        try {
            pin = Integer.parseInt(password);
        } catch (NumberFormatException e) {
            // Password is not an integer, set error message and return false

            return false;
        }

        try {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            conn = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD);
            stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND pin = ?");
            stmt.setString(1, username);
            stmt.setInt(2, pin);
            rs = stmt.executeQuery();

            if (rs.next()) {
                success = true;
            }
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
  /*------------------------------------------------- main -----
    |  Method switchToResetPass
    |
    |  Purpose: To load and display the FXMl file of ResetPass 
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToResetPass(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/ResetPass.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Reset Password");
        stage.setScene(scene);
        stage.show();
    }
/*------------------------------------------------- main -----
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
/*------------------------------------------------- main -----
    |  Method switchToLogin
    |
    |  Purpose: To load and display the FXMl file of LoginPage 
    |
    |                                                   
    *-------------------------------------------------------------------*/

    public void switchToLogin(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
/*------------------------------------------------- main -----
    |  Method resetPassword
    |
    |  Purpose: This method is linked to a javafx button,the method will take in the username of the user and the otp generated by the sendemail method
    |           if otp matches then based on the new password the user set we would update the password in the database.
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void resetPassword(ActionEvent Event) throws IOException, ClassNotFoundException, SQLException {
        Connection conn = null;

        PreparedStatement stmt = null;

        String enteredOTP = OTP.getText();
        String username = resetUsername.getText();
     
        if (enteredOTP.equals(String.valueOf(generatedOTP))) {

            int pin;
            String enterpassword = newpass.getText();

            pin = Integer.parseInt(new String(enterpassword));
            try {
                Class.forName(MYSQL_JDBC_DRIVER_CLASS);
                conn = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD);
                stmt = conn.prepareStatement("UPDATE user SET PIN = ? WHERE Username = ?");
                stmt.setInt(1, pin);
                stmt.setString(2, username);
                stmt.executeUpdate();
                error.setText("Password change successfully");
            } catch (SQLException SqlE) {
                System.out.println(SqlE);
            }
        }
    }
/*------------------------------------------------- main -----
    |  Method SendEmail
    |
    |  Purpose: This method is linked to a javafx button,the method will take in the username of the user and goes into the database 
    |           to retrieve the email.Using the email the method would then call a api to send a email to the user's email address
    |           with the otp that was generated.
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void SendEmail(ActionEvent Event) throws IOException, ClassNotFoundException {

        Connection conn = null;
        PreparedStatement get = null;

        ResultSet rs = null;

        String username = resetUsername.getText();
        try {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            conn = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD);
            get = conn.prepareStatement("SELECT * FROM user WHERE username = ? ");
            get.setString(1, username);

            rs = get.executeQuery();

            while (rs.next()) {
                data.setEmail((rs.getString("Email")));
            }
        } catch (SQLException SqlE) {
            System.out.println(SqlE);
        }

        Random r = new Random();
        generatedOTP = r.nextInt((999999 - 100000) + 1) + 1000;

        String to = data.getEmail();
        if (to == null) {
            error.setText("Cannot find user");
        } else {
        String senderName = "Easy ATM";
        String senderEmail = "oopeasyatm@gmail.com";
        String password = "qhbxkemulplsythv";

        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, senderName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Easy ATM One Time Password");
            String messageContent = "<html><body><p style=\"font-size:20px;\">Your 6 digit OTP is: "
                    + String.valueOf(generatedOTP) + "</p></body></html>";
            message.setContent(messageContent, "text/html; charset=utf-8");
            // message.setText("Your 6 digit OTP is: " + String.valueOf(ranNo));

            Transport.send(message);
            error.setText("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        resetPassword.setDisable(false);
    }

}
}
