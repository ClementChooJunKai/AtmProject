import java.util.*;

import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*------------------------------------------------- Transactions -----
 |  class Transactions
 |
 |  Purpose: This class contains the withdraw,deposit and transfer methods where most of the 
 |           calculation and basic functions are written here
 |                                                   
 *-------------------------------------------------------------------*/
public class Transactions extends api implements Initializable {

    private int TransactionNo;
    private static int userID;
    private String Username;
    private String remark;
    private int CheqNo;
    java.util.Date transactionDate = new Date();
    private int transactionType;
    private int accountType;
    private String FromUserName;
    private String ToUserName;
    private Float amount;
    private int TransfereeUID;
    private int TransfereeBankID;
    private String transfereeUname;
    int DepositChoice;
    private int WithdrawChoice;
    private int TransferFromChoice;
    private int TransferToChoice;

    public int getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(int transactionNo) {
        TransactionNo = transactionNo;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCheqNo() {
        return CheqNo;
    }

    public void setCheqNo(int cheqNo) {
        CheqNo = cheqNo;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public Float getAmount() {
        return amount;
    }

    public Transactions(int transactionNo, String username, String remark, int cheqNo, Date transactionDate,
            int transactionType, int accountType, String fromUserName, String toUserName, Float amount) {
        TransactionNo = transactionNo;
        Username = username;
        this.remark = remark;
        CheqNo = cheqNo;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.accountType = accountType;
        FromUserName = fromUserName;
        ToUserName = toUserName;
        this.amount = amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Transactions() {

    }

    // constructors
    public Transactions(int UID) {
        userID = UID;

    }

    public Transactions(int UID, int TransactionType) {
        userID = UID;
        this.transactionType = TransactionType;
    }

    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";
    // Inserting New Transaction Queries
    private static String insertTransaction = "INSERT INTO Transaction(UserId,Remarks,CheqNo,TransactionDate,TransactionType,AccountType,FromUserName,ToUserName,Amount)"
            + "values(?,?,?,?,?,?,?,?,?);";
    // deposit queries
    private static String getBalance = "Select  * FROM User WHERE UserID = ?;";
    private static String updateBalanceSavings = "UPDATE User SET  BalanceSavings= ?  WHERE userID = ?";
    private static String updateBalanceCurrent = "UPDATE User SET  BalanceCurrent= ?  WHERE userID = ?";
    // Checking Withdrawal Limit queries
    private static String checkWithdrawalLimit = "UPDATE User INNER JOIN (SELECT userID, MAX(TransactionDate) AS max_last_withdraw_date FROM Transaction"
            +
            " GROUP BY userID ORDER BY max_last_withdraw_date DESC LIMIT 1) AS t ON user.userID = t.userID SET User.withdrawlimit = 10000"
            +
            " WHERE User.userID = ? AND User.withdrawlimit < 10000 AND t.max_last_withdraw_date < CURDATE()";
    private static String checkTransferLimit = "UPDATE User INNER JOIN (SELECT userID, MAX(TransactionDate) AS max_last_withdraw_date FROM Transaction"
            +
            " GROUP BY userID ORDER BY max_last_withdraw_date DESC LIMIT 1) AS t ON user.userID = t.userID SET User.transferlimit = 10000"
            +
            " WHERE User.userID = ? AND User.transferlimit < 10000 AND t.max_last_withdraw_date < CURDATE()";
    // updating withdrawal limit
    private static String UpdateWithdrawalLimit = "UPDATE User SET  withdrawlimit= ?  WHERE userID = ?";
    private static String UpdateTransferLimit = "UPDATE User SET  transferLimit= ?  WHERE userID = ?";

    // display limits
    private static String viewLimits = "SELECT withdrawLimit, transferLimit FROM User WHERE userID = ?";
    private static String getUserID = "Select * FROM User WHERE username = ?";

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public Label errorlbl;
    @FXML
    public Label errorlbl2;
    @FXML
    public Label transferlbl;
    @FXML
    public TextField depositAmt;

    @FXML
    public TextField depositremark;
    @FXML
    public TextField depositCheqNo;
    @FXML
    public Button DepositSubmit;
    @FXML
    public TextField withdrawAmount;
    @FXML
    public TextField withdrawToSOC;
    @FXML
    public Button submitWithdraw;
    @FXML
    public TextField transfereeName;
    @FXML
    public TextField transferFromSOC;
    @FXML
    public TextField transferToSOC;
    @FXML
    public TextField transferAmount;
    @FXML
    public TextField transferRemarks;
    @FXML
    public Button submitTransfer;
    @FXML
    public RadioButton DepositToSav, WithdrawToCurr, DepositToCurr, WithdrawToSav;
    @FXML
    public RadioButton TransferFromSav, TransferFromCurr, TransferToSav, TransferToCurr;
    @FXML
    public Button transferback;

    DataSingleton data = DataSingleton.getInstance();

    /*------------------------------------------------- Deposit -----
    |  Method Deposit
    |
    |  Purpose: This method takes in the userID using the datasingleton class and based on the user inputs
    |           updates either the balance current or the savings.The methods also calls for another sql
    |           to insert into the transaction table to save a new transaction everytime a deposit happens
    |                                                   
    *-------------------------------------------------------------------*/
    @FXML
    void Deposit(ActionEvent event) {
        java.sql.Date sqlDate = new java.sql.Date(transactionDate.getTime());

        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            PreparedStatement insertTransactionSQL = connection.prepareStatement(insertTransaction);
            PreparedStatement BalanceCurrent = connection.prepareStatement(updateBalanceCurrent);
            PreparedStatement BalanceSavings = connection.prepareStatement(updateBalanceSavings);

            // set user id
            insertTransactionSQL.setInt(1, data.getUserID());

            // input deposit amount
            try {
                amount = Float.parseFloat(depositAmt.getText());

                if (amount < 0) {
                    throw new IllegalArgumentException("Deposit amount must be a positive value.");
                }
                if (amount > 1000000) {
                    throw new IllegalArgumentException("Deposit amount is too high.");
                }
                insertTransactionSQL.setFloat(9, amount);

                int accountType = DepositChoice;
                // savings
                if (accountType == 1) {
                    insertTransactionSQL.setInt(6, 1);
                    float getUser[] = getUser(accountType, data.getUserID());
                    float finalBalance = amount + getUser[0];
                    BalanceSavings.setFloat(1, finalBalance);
                    BalanceSavings.setInt(2, data.getUserID());
                    BalanceSavings.executeUpdate();
                }
                // current
                else if (accountType == 2) {
                    insertTransactionSQL.setInt(6, 2);
                    float getUser[] = getUser(accountType, data.getUserID());
                    float finalBalance = amount + getUser[0];
                    BalanceCurrent.setFloat(1, finalBalance);
                    BalanceCurrent.setInt(2, data.getUserID());
                    BalanceCurrent.executeUpdate();
                }
                CheqNo = Integer.parseInt(depositCheqNo.getText());
                // enter Remarks
                remark = depositremark.getText();
                insertTransactionSQL.setString(2, remark);
                insertTransactionSQL.setInt(3, CheqNo);

                // set current date
                insertTransactionSQL.setDate(4, sqlDate);

                // set transaction type
                insertTransactionSQL.setInt(5, 1);

                // set from user
                insertTransactionSQL.setString(7, data.getUserName());

                // set to user
                insertTransactionSQL.setString(8, "NIL");

                insertTransactionSQL.executeUpdate();

            } catch (NumberFormatException e) {
                errorlbl.setText("String not allowed for deposit amount or cheque number");
                return;
            } catch (IllegalArgumentException e) {
                errorlbl.setText(e.getMessage());
                return;
            }

        } catch (

        ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();
            errorlbl.setText("Error: Database driver not found");
        } catch (SQLException e) {
            System.out.println("Error occurred while executing query: " + insertTransaction);
            e.printStackTrace();
            errorlbl.setText("Error: Failed to execute query");
        }
        errorlbl.setText("Success!");
    }

    /*------------------------------------------------- Transactions -----
    |  Method getUser
    |
    |  Purpose: This method takes in the userID and accounttype and based on the inputs calls the database
    |           to retrieve either the balancesavings or balancecurrent and also retrieveing the withdrawlimit 
    |           and transferlimit.The method would then store the values in a array and return the array to be used.
    |                                                   
    *-------------------------------------------------------------------*/
    public static float[] getUser(int accountType, int userid) {
        float[] arr = { 0, 0, 0 };
        float FinalBalance = 0;
        float withdrawlimit = 0;
        float transferlimit = 0;
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            PreparedStatement getbalance = connection.prepareStatement(getBalance);

            getbalance.setInt(1, userid);
            ResultSet getB = getbalance.executeQuery();

            String ac = "";
            // get savings or current balance
            if (accountType == 1) {
                ac = "Savings";
            } else
                ac = "Current";

            while (getB.next()) {
                FinalBalance = getB.getFloat("Balance" + ac);
                withdrawlimit = getB.getFloat("withdrawlimit");
                transferlimit = getB.getFloat("transferLimit");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error occured while executing query: " + getBalance);
            e.printStackTrace();
        }
        arr[0] = FinalBalance;
        arr[1] = withdrawlimit;
        arr[2] = transferlimit;

        return arr;

    }

    public void getDepositOption(ActionEvent event) {

        if (DepositToSav.isSelected()) {
            DepositChoice = 1;
        } else if (DepositToCurr.isSelected()) {
            DepositChoice = 2;
        }
    }

    /*------------------------------------------------- Transactions -----
    |  Method withdraw
    |
    |  Purpose: This method takes in the userID using the datasingleton class and based on the user inputs
    |           checks against the withdrawal limits, if its within the withdrawal limits then based on the amount
    |           the user input updates the balance current or savings while also inserting a new entry to the 
    |           transaction table.
    |                                                   
    *-------------------------------------------------------------------*/
    @FXML
    private void withdraw(ActionEvent event) throws ClassNotFoundException, SQLException, IOException, checkwithdrawal {

        java.sql.Date sqlDate = new java.sql.Date(transactionDate.getTime());
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            // read balances Db
            PreparedStatement BalanceCurrent = connection.prepareStatement(updateBalanceCurrent);
            PreparedStatement BalanceSavings = connection.prepareStatement(updateBalanceSavings);

            PreparedStatement insertTransactionSQL = connection.prepareStatement(insertTransaction);
            // check withdrawal limit DB
            PreparedStatement CheckDailyWithdrawalLimit = connection.prepareStatement(checkWithdrawalLimit);
            // update witdrawal DB
            PreparedStatement UpdateWithdrawalLimitSQL = connection.prepareStatement(UpdateWithdrawalLimit);
            CheckDailyWithdrawalLimit.setInt(1, data.getUserID());
            CheckDailyWithdrawalLimit.executeUpdate();

            float finalBalance = 0;
            float newlimit = 0;
            int accountType = 0;
            // set user id
            insertTransactionSQL.setInt(1, data.getUserID());

            try {
                amount = Float.parseFloat(withdrawAmount.getText());
                if (amount < 0) {
                    throw new IllegalArgumentException("Withdrawal amount must be a positive value.");
                }
                insertTransactionSQL.setFloat(9, amount);

                float getUser[] = getUser(accountType, data.getUserID());
                newlimit = getUser[1] - amount;

                if (newlimit < 0) {
                    throw new IllegalArgumentException("Withdrawal amount over withdraw limit by" + -newlimit);
                }

                // input account type

                accountType = WithdrawChoice; //
                getUser = getUser(accountType, data.getUserID());
                finalBalance = getUser[0] - amount;

                if (finalBalance < 0) {
                    throw new checkwithdrawal(finalBalance);
                }

                // set amount
                insertTransactionSQL.setFloat(9, amount);
                // Savings
                if (accountType == 1) {
                    insertTransactionSQL.setInt(6, 1);
                    BalanceSavings.setFloat(1, finalBalance);
                    BalanceSavings.setInt(2, data.getUserID());
                    BalanceSavings.executeUpdate();
                }
                // current
                else if (accountType == 2) {
                    insertTransactionSQL.setInt(6, 2);
                    BalanceCurrent.setFloat(1, finalBalance);
                    BalanceCurrent.setInt(2, data.getUserID());
                    BalanceCurrent.executeUpdate();

                }

                UpdateWithdrawalLimitSQL.setFloat(1, newlimit);
                UpdateWithdrawalLimitSQL.setInt(2, data.getUserID());
                UpdateWithdrawalLimitSQL.executeUpdate();

                // no Remarks
                insertTransactionSQL.setString(2, "NIL");

                // NO Cheq No
                insertTransactionSQL.setInt(3, 0);

                // set current date
                insertTransactionSQL.setDate(4, sqlDate);

                // set transcation type
                insertTransactionSQL.setInt(5, 2);

                // set from user
                insertTransactionSQL.setString(7, data.getUserName());

                // set to user
                insertTransactionSQL.setString(8, "NIL");
                // execute transcation
                insertTransactionSQL.executeUpdate();

                errorlbl2.setText("Success!");

            } catch (NumberFormatException e) {
                errorlbl2.setText("Please Input Values ");
                return;
            } catch (IllegalArgumentException e) {
                errorlbl2.setText(e.getMessage());
                return;
            } catch (checkwithdrawal e) {
                errorlbl2.setText("Withdrawal over the limit " + e.getAmount());
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error occured while executing query: " + checkWithdrawalLimit);
            e.printStackTrace();
        }
    }

    public void getWithdrawOption(ActionEvent event) {

        if (WithdrawToSav.isSelected()) {
            WithdrawChoice = 1;
        } else if (WithdrawToCurr.isSelected()) {
            WithdrawChoice = 2;
        }
    }

    /*------------------------------------------------- Transactions -----
    |  Method switchToMainScene
    |
    |  Purpose: To load and display the FXMl file of MainMenu 
    |                                                   
    *-------------------------------------------------------------------*/
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("atm.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- Transactions -----
    |  Method Transfer
    |
    |  Purpose: This method takes in the userID using the datasingleton class and based on the user inputs
    |           checks against the withdrawal limits, if its within the withdrawal limits then it would check
    |           the transferlimit if it passes as well then we would deduct the amount from the user
    |           while at the same time deposit the new amount to the transferee and also inserting a new transaction
    |           for both users
    |                                                   
    *-------------------------------------------------------------------*/
    @FXML
    private void Transfer(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        java.sql.Date sqlDate = new java.sql.Date(transactionDate.getTime());
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);

            PreparedStatement insertTransactionSQL = connection.prepareStatement(insertTransaction);
            PreparedStatement DepositInsertTransactionSQL = connection.prepareStatement(insertTransaction);
            PreparedStatement CheckDailyTransferLimit = connection.prepareStatement(checkTransferLimit);
            PreparedStatement BalanceCurrent = connection.prepareStatement(updateBalanceCurrent);
            PreparedStatement BalanceSavings = connection.prepareStatement(updateBalanceSavings);
            PreparedStatement updateTransferLimitSQL = connection.prepareStatement(UpdateTransferLimit);
            CheckDailyTransferLimit.setInt(1, data.getUserID());
            CheckDailyTransferLimit.executeUpdate();

            float finalBalance = 0;
            float newlimit = 0;
            int accountType = 0;

            // set user id

            PreparedStatement getUserIDStatement = connection.prepareStatement(getUserID);

            transfereeUname = transfereeName.getText();
            getUserIDStatement.setString(1, transfereeUname);
            ResultSet result = getUserIDStatement.executeQuery();
            if (result.next()) {
                TransfereeUID = result.getInt("UserID");
                TransfereeBankID = result.getInt("bankID");

                // Enter transfer amount
                try {
                    amount = Float.parseFloat(transferAmount.getText());
                    if (amount < 0) {
                        throw new IllegalArgumentException("Transfer amount must be a positive value.");
                    }
                    insertTransactionSQL.setFloat(9, amount);

                    accountType = TransferFromChoice;
                    // Check transfer limit
                    float getUser[] = getUser(accountType, data.getUserID()); // return all user limits
                    newlimit = getUser[2] - amount; // transferlimit - amount see if enough

                    if (newlimit < 0) {
                        throw new IllegalArgumentException("Over Transfer limit by " + (-newlimit));
                    }

                    // Check balance
                    finalBalance = getUser[0] - amount; // check new balance after minusing amount to see if got enough
                                                        // balance

                    if (finalBalance < 0) {
                        throw new IllegalArgumentException("Transfer over the limit" + finalBalance);
                    } else {
                        transferlbl.setText("Balance After Transfer: " + finalBalance);
                    }

                    // set amount
                    insertTransactionSQL.setFloat(9, amount);
                    DepositInsertTransactionSQL.setFloat(9, amount);
                    // Savings
                    if (accountType == 1) {
                        insertTransactionSQL.setInt(6, 1);
                        BalanceSavings.setFloat(1, finalBalance);
                        BalanceSavings.setInt(2, userID);
                        BalanceSavings.executeUpdate();
                    }
                    // current
                    else if (accountType == 2) {
                        insertTransactionSQL.setInt(6, 2);
                        BalanceCurrent.setFloat(1, finalBalance);
                        BalanceCurrent.setInt(2, data.getUserID());
                        BalanceCurrent.executeUpdate();

                    }
                    accountType = TransferToChoice;
                    if (accountType == 1) {
                        DepositInsertTransactionSQL.setInt(6, 1);

                        getUser = getUser(accountType, TransfereeUID);
                        float conversionrate = getCurrency(data.getBankID(), TransfereeBankID);
                        float DepositBalance = amount * conversionrate;
                        DepositBalance = DepositBalance + getUser[0];

                        BalanceSavings.setFloat(1, DepositBalance);
                        BalanceSavings.setInt(2, TransfereeUID);
                        BalanceSavings.executeUpdate();
                    }
                    // current
                    else if (accountType == 2) {
                        DepositInsertTransactionSQL.setInt(6, 2);
                        getUser = getUser(accountType, TransfereeUID);

                        float conversionrate = getCurrency(data.getBankID(), TransfereeBankID);
                        float DepositBalance = amount * conversionrate;

                        DepositBalance = DepositBalance + getUser[0];

                        BalanceCurrent.setFloat(1, DepositBalance);
                        BalanceCurrent.setInt(2, TransfereeUID);
                        BalanceCurrent.executeUpdate();

                    }

                    updateTransferLimitSQL.setFloat(1, newlimit);
                    updateTransferLimitSQL.setInt(2, data.getUserID());
                    updateTransferLimitSQL.executeUpdate();

                    // no Remarks

                    remark = transferRemarks.getText();
                    insertTransactionSQL.setInt(1, data.getUserID());
                    insertTransactionSQL.setString(2, remark);

                    // NO Cheq No
                    insertTransactionSQL.setInt(3, 0);

                    // set current date
                    insertTransactionSQL.setDate(4, sqlDate);

                    // set transcation type
                    insertTransactionSQL.setInt(5, getTransactionType());

                    // set from user
                    insertTransactionSQL.setString(7, data.getUserName());
                    // set from user
                    insertTransactionSQL.setString(8, transfereeUname);

                    // execute transcation
                    insertTransactionSQL.executeUpdate();

                    DepositInsertTransactionSQL.setInt(1, TransfereeUID);
                    DepositInsertTransactionSQL.setString(2, remark);
                    DepositInsertTransactionSQL.setInt(3, 0);
                    DepositInsertTransactionSQL.setDate(4, sqlDate);
                    DepositInsertTransactionSQL.setInt(5, 3);
                    DepositInsertTransactionSQL.setString(7, data.getUserName());
                    DepositInsertTransactionSQL.setString(8, "NIL");
                    DepositInsertTransactionSQL.executeUpdate();
                } catch (NumberFormatException e) {
                    transferlbl.setText("Error: Transfer amount must be a number.");
                    return;
                } catch (IllegalArgumentException e) {
                    transferlbl.setText(e.getMessage());
                    return;
                }
                transferlbl.setText("Transfer Success!");
            } else {
                transferlbl.setText("Username not found. Please try again.");
            }
        }

        catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error occured while executing query: " + checkWithdrawalLimit);
            e.printStackTrace();
        }

    }

    public void getTransferFromChoice(ActionEvent event) {

        if (TransferFromSav.isSelected()) {
            TransferFromChoice = 1;
        } else if (TransferFromCurr.isSelected()) {
            TransferFromChoice = 2;
        }
    }

    /*------------------------------------------------- Transactions -----
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

    public void getTransferToChoice(ActionEvent event) {

        if (TransferToSav.isSelected()) {
            TransferToChoice = 1;
        } else if (TransferToCurr.isSelected()) {
            TransferToChoice = 2;
        }
    }

}
