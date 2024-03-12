
/**
 * transactionhistorycontroller
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/*------------------------------------------------- transactionhistorycontroller -----
|  Class transactionhistorycontroller
|
|  Purpose: This class contains methods of the Transacation History page.This class also implements Initializable
|           a interface for javafx where once the page loads up everything within the initilize method would automatically be called
|
|                                                   
*-------------------------------------------------------------------*/
public class transactionhistorycontroller extends testemail implements Initializable {

    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";

    private static String SQL_QUERY = "SELECT User.UserID ,Transaction.TransactionNo,User.username, Transaction.Remarks, Transaction.CheqNo, Transaction.TransactionDate, Transaction.TransactionType, "
            +
            "Transaction.AccountType, Transaction.FromUserName, Transaction.ToUserName, Transaction.Amount FROM User JOIN Transaction ON User.UserID = Transaction.UserID WHERE User.UserID = ?";
    private static String getUserID = "Select * FROM User WHERE username = ?";

    DataSingleton data = DataSingleton.getInstance();

    private int userId;

    @FXML
    private TableView<TransactionTempValues> TransHistoryView;
    @FXML
    private TableColumn<TransactionTempValues, Integer> TransactionNo;
    @FXML
    private TableColumn<TransactionTempValues, Integer> UserID;
    @FXML
    private TableColumn<TransactionTempValues, String> Remarks;
    @FXML
    private TableColumn<TransactionTempValues, String> AccountType;
    @FXML
    private TableColumn<TransactionTempValues, Integer> CheqNo;
    @FXML
    private TableColumn<TransactionTempValues, String> Type;
    @FXML
    private TableColumn<TransactionTempValues, String> From;
    @FXML
    private TableColumn<TransactionTempValues, String> To;
    @FXML
    private TableColumn<TransactionTempValues, String> Date;
    @FXML
    private TableColumn<TransactionTempValues, Integer> Amount;
    @FXML
    private TextField filertrans;

    ObservableList<TransactionTempValues> Searchlist = FXCollections.observableArrayList();
    @FXML
    Button transferback;

    /*------------------------------------------------- transactionhistorycontroller -----
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
        stage.setScene(scene);
        stage.show();

    }

    /*------------------------------------------------- transactionhistorycontroller -----
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

    /*------------------------------------------------- transactionhistorycontroller -----
    |  Method initialize
    |
    |  Purpose: Automatically calls the ReadTranscationDB method when the page loads up
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReadTranscationDB();

    }

    /*------------------------------------------------- transactionhistorycontroller -----
    |  Method ReadTranscationDB
    |
    |  Purpose: Based on the username that was set using the DataSingleton class, this method would call the database and 
    |           find out all the different data (UserID,TransactionNo,CheqNo,Amount,remarks,FromUserName,ToUserName,TransactionDate)
    |           we would then set it all as a object of the class TransactionTempValues and then set it into the obeservable list to
    |           be called in the javafx tableview
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void ReadTranscationDB() {
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            PreparedStatement getUserIDStatement = connection.prepareStatement(getUserID);
            PreparedStatement retrieveUserInfoSQL = connection.prepareStatement(SQL_QUERY);

            getUserIDStatement.setString(1, data.getUserName());
            ResultSet result = getUserIDStatement.executeQuery();

            if (result.next()) {
                userId = result.getInt("UserID");

            }

            retrieveUserInfoSQL.setInt(1, userId);

            ResultSet userinfoRS = retrieveUserInfoSQL.executeQuery();
            int i = 0;
            // print Transcation history

            while (userinfoRS.next()) {

                String queryaccType = " ";
                String querytype = " ";

                Integer queryUserId = userinfoRS.getInt("User.UserID");
                Integer queryTransNo = userinfoRS.getInt("TransactionNo");
                Integer queryCheqNo = userinfoRS.getInt("CheqNo");
                Integer queryAmount = userinfoRS.getInt("Amount");
                String queryRemark = userinfoRS.getString("remarks");
                String queryFrom = userinfoRS.getString("FromUserName");
                String queryTo = userinfoRS.getString("ToUserName");
                String queryDate = userinfoRS.getString("TransactionDate");

                if (userinfoRS.getInt("TransactionType") == 1) {
                    querytype = "Deposit ";
                } else if (userinfoRS.getInt("TransactionType") == 2) {
                    querytype = "Withdraw";
                } else {
                    querytype = "Transfer";
                }
                if (userinfoRS.getInt("AccountType") == 1) {
                    queryaccType = "Savings";
                } else {
                    queryaccType = "Current";
                }
                TransactionTempValues transaction = new TransactionTempValues(queryTransNo, queryUserId, queryRemark,
                        queryCheqNo, queryDate, querytype, queryaccType, queryFrom, queryTo, queryAmount);
                Searchlist.add(transaction);
                TransHistoryView.setItems(Searchlist);

                UserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
                Remarks.setCellValueFactory(new PropertyValueFactory<>("remark"));
                TransactionNo.setCellValueFactory(new PropertyValueFactory<>("TransactionNo"));
                CheqNo.setCellValueFactory(new PropertyValueFactory<>("CheqNo"));
                Type.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
                AccountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
                From.setCellValueFactory(new PropertyValueFactory<>("FromUserName"));
                To.setCellValueFactory(new PropertyValueFactory<>("ToUserName"));
                Date.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            }

            /*------------------------------------------------- transactionhistorycontroller -----
            |  FilteredList
            |
            |  Purpose: we would call the searchlist with all the values retrieved from the database
            |           and put it as a filtered list so that the input that was given at a javafx textfield
            |           would be used as the filter for the tableview
            |
            |                                                   
            *-------------------------------------------------------------------*/
            // initialist filterlist
            FilteredList<TransactionTempValues> filteredData = new FilteredList<>(Searchlist, b -> true);

            filertrans.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(TransactionTempValues -> {

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();
                    if (TransactionTempValues.getAccountType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }

                    else if (TransactionTempValues.getTransactionDate().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else if (TransactionTempValues.getRemark().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    }

                    else if (TransactionTempValues.getTransactionType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else if (TransactionTempValues.getFromUserName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else if (TransactionTempValues.getToUserName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else
                        return false;

                });
            });
            SortedList<TransactionTempValues> sortData = new SortedList<>(filteredData);

            sortData.comparatorProperty().bind(TransHistoryView.comparatorProperty());
            TransHistoryView.setItems(sortData);

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error occured while executing query: " + SQL_QUERY);
            e.printStackTrace();
        }

    }

    /*------------------------------------------------- transactionhistorycontroller -----
    |  Method saveToTextFile,saveToCSV
    |
    |  Purpose: Both methods are linked to a javafx button,where we are calling a file/io and outputing the tableview contents
    |           into either a txt or csv file
    |                                                   
    *-------------------------------------------------------------------*/
    @FXML
    public void saveToTextFile(ActionEvent event) {
        try {
            FileWriter writer = new FileWriter("TransactionHistory.txt");
            for (Object item : TransHistoryView.getItems()) {
                writer.write(item.toString() + "\n");
            }
            writer.close();

            sendemail("TransactionHistory.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void saveToCSV(ActionEvent event) {
        try {
            FileWriter writer = new FileWriter("TransactionHistory.csv");
            for (Object item : TransHistoryView.getItems()) {
                writer.write(item.toString() + "\n");
            }
            writer.close();
            sendemail("TransactionHistory.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendemail() {

    }

}