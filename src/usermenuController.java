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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/*------------------------------------------------- usermenuController -----
|  Class usermenuController
|
|  Purpose: This class contains methods in the MainMenu
|
|                                                   
*-------------------------------------------------------------------*/
public class usermenuController extends api implements Initializable {
    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";
    private static String SQL_QUERY = "Select * from User";

    private static String checkWithdrawalLimit = "UPDATE User INNER JOIN (SELECT userID, MAX(TransactionDate) AS max_last_withdraw_date FROM Transaction"
            +
            " GROUP BY userID ORDER BY max_last_withdraw_date DESC ) AS t ON user.UserID = t.UserID SET User.withdrawlimit = 10000"
            +
            " WHERE User.UserID = ? AND User.withdrawlimit < 10000 AND t.max_last_withdraw_date < CURDATE()";
    private static String checkTransferLimit = "UPDATE User INNER JOIN (SELECT UserID, MAX(TransactionDate) AS max_last_withdraw_date FROM Transaction"
            +
            " GROUP BY userID ORDER BY max_last_withdraw_date DESC ) AS t ON user.UserID = t.UserID SET User.transferlimit = 10000"
            +
            " WHERE User.UserID = ? AND User.transferlimit < 10000 AND t.max_last_withdraw_date < CURDATE()";
    private static String Fullname;
    private static String Username;
    private static int userID;
    private static int Pin;
    private static float BalanceSavings;
    private static float BalanceCurrent;
    private static int withdrawLimit;
    private static int transferLimit;
    private static boolean admin;
    private static Console cons;
    private static String remark;
    private static int bankID;
    private static int AccountNoSavings;
    private static int AccountNoCurrent;
    private static String Email;
    @FXML
    private static TextField username;
    @FXML
    Label welcome;
    @FXML
    Text bankname;
    @FXML
    Text balancesaving;
    @FXML
    Text balancecurrent;
    @FXML
    Text accountnosavings;
    @FXML
    Text accountnocurrent;
    @FXML
    Button transactionhistory;
    @FXML
    Button withdraw;
    @FXML
    Button deposit;
    @FXML
    Button settings;
    @FXML
    Button exit;

    DataSingleton data = DataSingleton.getInstance();

    /*------------------------------------------------- usermenuController -----
    |  Method initialize
    |
    |  Purpose: This method would automatically retrieve the User infomation based on the username given once
    |           the page loads up.It would then print all relavant information on the main menu page
    |
    |                                                   
    *-------------------------------------------------------------------*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcome.setText("Welcome " + data.getUserName());

        String UsernameEntered = data.getUserName();

        User newUser = new User(UsernameEntered);

        newUser.readDB();

        Transactions tr = new Transactions(newUser.getUserID());

        // Getting New User Object values
        remark = tr.getRemark();
        Username = tr.getUsername();
        userID = newUser.getUserID();
        Fullname = newUser.getFullname();
        BalanceSavings = newUser.getBalanceSavings();
        BalanceCurrent = newUser.getBalanceCurrent();
        withdrawLimit = newUser.getWithdrawLimit();
        transferLimit = newUser.getTransferLimit();
        bankID = newUser.getBankID();
        AccountNoCurrent = newUser.getAccountNoCurrent();
        AccountNoSavings = newUser.getAccountNoSavings();
        Email = newUser.getEmail();
        // setting bankID and userID to data singleton

        data.setBankID(bankID);
        data.setEmail(Email);
        data.setUserID(userID);

        try {
            updatelimits();
        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
        // Printing Bank Details on Main menu
        userMainMenu(tr, Fullname, BalanceCurrent, BalanceSavings, withdrawLimit, transferLimit);

    }

    /*------------------------------------------------- usermenuController -----
    |  Method updatelimits
    |
    |  Purpose: This method calls the sql for checktransferlimits and checkwithdrawlimits to check if a day had passed  
    |           since the last transaction and will auto refresh the withdrawal and transfer limit if a day or more had passed.
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void updatelimits() throws SQLException, ClassNotFoundException {
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);

            PreparedStatement CheckDailyTransferLimit = connection.prepareStatement(checkTransferLimit);
            PreparedStatement CheckDailyWithdrawalLimit = connection.prepareStatement(checkWithdrawalLimit);

            CheckDailyWithdrawalLimit.setInt(1, data.getUserID());

            CheckDailyWithdrawalLimit.executeUpdate();
            CheckDailyTransferLimit.setInt(1, data.getUserID());
            CheckDailyTransferLimit.executeUpdate();

        }
    }

    /*------------------------------------------------- usermenuController -----
    |  Method userMainMenu
    |
    |  Purpose: This method prints all the inputs that were retrieve from the databased on the main menu
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void userMainMenu(Transactions tr, String fullname, float BalanceCurrent, float BalanceSavings,
            int withdrawLimit, int transferLimit) {

        bank newBank = new bank();
        ArrayList<bank> banks = new ArrayList<>();
        banks = newBank.readBankDB();

        bank selectedBank = banks.get(bankID);

        bankname.setText(selectedBank.getBankName());

        welcome.setText("Welcome " + fullname + "!");

        accountnocurrent.setText(String.valueOf(AccountNoCurrent));
        accountnosavings.setText(String.valueOf(AccountNoSavings));
        System.out.println(selectedBank.getBankID());
        switch (data.getBankID()) {

            case (1): {
                balancecurrent.setText("SGD $" + BalanceSavings);
                balancesaving.setText("SGD $" + BalanceCurrent);
                break;
            }
            case (2): {
                balancecurrent.setText("CNY $" + BalanceSavings);
                balancesaving.setText("CNY $" + BalanceCurrent);
                break;
            }
            case (3): {
                balancecurrent.setText("MYR $" + BalanceSavings);
                balancesaving.setText("MYR $" + BalanceCurrent);
                break;
            }
            case (4): {
                balancecurrent.setText("AUD $" + BalanceSavings);
                balancesaving.setText("AUD $" + BalanceCurrent);
                break;
            }

        }

    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    /*------------------------------------------------- usermenuController -----
    |  Method switchToSceneTransactionHistory
    |
    |  Purpose:  To load and display the FXMl file of Transactionhistory 
    |
    |
    *-------------------------------------------------------------------*/
    public void switchToSceneTransactionHistory(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/Transactionhistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Transaction History");
        stage.setScene(scene);
        stage.show();
    }

    /*------------------------------------------------- usermenuController -----
    |  Method switchToSettings
    |
    |  Purpose:  To load and display the FXMl file of settings 
    |
    |
    *-------------------------------------------------------------------*/
    public void switchToSettings(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- usermenuController -----
    |  Method logout
    |
    |  Purpose:  To load and display the FXMl file of LoginPage 
    |
    |
    *-------------------------------------------------------------------*/
    public void logout(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /*------------------------------------------------- usermenuController -----
    |  Method switchToSceneDeposit
    |
    |  Purpose:  To load and display the FXMl file of Deposit 
    |
    |
    *-------------------------------------------------------------------*/
    public void switchToSceneDeposit(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/Deposit.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Deposit");
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- usermenuController -----
    |  Method switchToSceneWithdraw
    |
    |  Purpose:  To load and display the FXMl file of Withdraw 
    |
    |
    *-------------------------------------------------------------------*/
    public void switchToSceneWithdraw(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/Withdraw.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Withdraw");
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- usermenuController -----
    |  Method switchToSceneTransfer
    |
    |  Purpose:  To load and display the FXMl file of Transfer 
    |
    |
    *-------------------------------------------------------------------*/
    public void switchToSceneTransfer(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/Transfer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Transfer");
        stage.setScene(scene);
        stage.show();

    }

}
