import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Transactiontest {

    private Connection connection;

    // Creating User,Transaction
    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        // setup a real Connection object
        String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
        String MYSQL_DB_USER = "root";
        String MYSQL_DB_USER_PASSWORD = "root";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD);
        // Adding of new account to be used
        String insertUser = "insert into user(userID,pin,username,fullname,admin,BalanceSavings,BalanceCurrent,newUserStatus,withdrawLimit,transferLimit,BankID) VALUES (7,123,'bitch','abc',false,100,200,false,1000,1000,4)";
        PreparedStatement insertUserSql = connection.prepareStatement(insertUser);
        insertUserSql.executeUpdate();

        // Adding new Transaction to be Used
        String insertTransaction = " INSERT INTO Transaction(UserId,Remarks,CheqNo,TransactionDate,TransactionType,AccountType,FromUserName,ToUserName,Amount) values(7,'Nil',1,'2023-03-22',1,1,'stupid','dog',1000);";
        PreparedStatement insertTransactionsql = connection.prepareStatement(insertTransaction);
        insertTransactionsql.executeUpdate();

    }

    // Testing Base SQL
    @Test
    public void testBase() throws SQLException {

        // assert that the User was inserted into the database
        String selectTransaction = "SELECT * FROM user WHERE userid = ?";
        PreparedStatement selectTransactionSQL = connection.prepareStatement(selectTransaction);
        selectTransactionSQL.setInt(1, 7);
        ResultSet resultSet = selectTransactionSQL.executeQuery();
        assertTrue(resultSet.next());

        String updatebalance = "UPDATE User SET  BalanceSavings= 100,BalanceCurrent=100 WHERE userID = 7";
        PreparedStatement UpdateBalanceSql = connection.prepareStatement(updatebalance);
        UpdateBalanceSql.executeUpdate();

        // assert that the balance was updated
        String selectBalance = "SELECT BalanceSavings,BalanceCurrent FROM user WHERE userid = 7 ";
        PreparedStatement selectBalanceSQL = connection.prepareStatement(selectBalance);

        resultSet = selectBalanceSQL.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(resultSet.getFloat("BalanceSavings"), 100.0f, 0.001f);
        assertEquals(resultSet.getFloat("BalanceCurrent"), 100.0f, 0.001f);
    }

    // Testing Withdraw Limit SQL
    @Test
    public void testWithdrawLimit() throws SQLException {

        // Updating the limits
        String UpdateWithdrawalLimit = "UPDATE User SET  withdrawlimit= 5000,transferLimit = 3000  WHERE userID = 7";
        PreparedStatement UpdateWithdrawLimitSql = connection.prepareStatement(UpdateWithdrawalLimit);
        UpdateWithdrawLimitSql.executeUpdate();

        // assert that the limits was updated
        String selectLimit = "SELECT withdrawlimit,transferLimit FROM user WHERE userid = 7 ";
        PreparedStatement selectLimitSQL = connection.prepareStatement(selectLimit);
        ResultSet resultSet = selectLimitSQL.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(resultSet.getFloat("withdrawlimit"), 5000.0f, 0.001f);
        assertEquals(resultSet.getFloat("transferLimit"), 3000.0f, 0.001f);
    }

    // Testing Transaction SQL
    @Test
    public void testTransaction() throws SQLException {
        // assert that the transaction was inserted into the database
        String selectTransaction = "SELECT * FROM transaction WHERE userid = 7";
        PreparedStatement selectTransactionSQL = connection.prepareStatement(selectTransaction);
        ResultSet resultSet = selectTransactionSQL.executeQuery();
        assertTrue(resultSet.next());

    }

    // testing GetUser Method from Transactions and Calling for BalanceSavings
    @Test
    public void testGetUserSavings() throws SQLException {
        float[] arr = Transactions.getUser(1, 7);
        assertEquals(100.0f, arr[0], 0.01f);
        assertEquals(1000.0f, arr[1], 0.01f);
        assertEquals(1000.0f, arr[2], 0.01f);
    }

    @Test
    public void testGetUserCurrent() throws SQLException {
        float[] arr = Transactions.getUser(2, 7);
        assertEquals(200.0f, arr[0], 0.01f);
        assertEquals(1000.0f, arr[1], 0.01f);
        assertEquals(1000.0f, arr[2], 0.01f);
    }

    @Test
    public void testTransacationHistorySql() throws SQLException {
        String DisplayTransHistory = "SELECT User.UserID ,User.username, Transaction.Remarks, Transaction.CheqNo, Transaction.TransactionDate, Transaction.TransactionType, "
                + "Transaction.AccountType, Transaction.FromUserName, Transaction.ToUserName, Transaction.Amount FROM User JOIN Transaction ON User.UserID = Transaction.UserID WHERE User.UserID = 7";
        PreparedStatement DisplayTransHistorySql = connection.prepareStatement(DisplayTransHistory);
        ResultSet resultSet = DisplayTransHistorySql.executeQuery();
        assertTrue(resultSet.next());
        // Testing inner join of 2 tables from sql
        assertEquals(resultSet.getFloat("User.UserID"), 7, 0);
        assertEquals(resultSet.getString("User.username"), "bitch");
        assertEquals(resultSet.getString("Transaction.Remarks"), "Nil");
        assertEquals(resultSet.getString("Transaction.FromUserName"), "stupid");
        assertEquals(resultSet.getString("Transaction.ToUserName"), "dog");
        assertEquals(resultSet.getFloat("Transaction.TransactionType"), 1, 0);
        assertEquals(resultSet.getFloat("Transaction.AccountType"), 1, 0);
        assertEquals(resultSet.getFloat("Transaction.Amount"), 1000, 0);
        assertNotEquals(resultSet.getFloat("Transaction.CheqNo"), 7, 0);
    }

    // Deleting Everything that was created
    @After
    public void cleanup() throws SQLException {
        // delete test data from the database
        String deleteUser = "DELETE FROM user WHERE userid = 7";
        String deleteTransaction = "DELETE FROM TRANSACTION WHERE USERID= 7";
        PreparedStatement deleteUserSQL = connection.prepareStatement(deleteUser);
        PreparedStatement deleteTransSQL = connection.prepareStatement(deleteTransaction);
        deleteTransSQL.executeUpdate();
        deleteUserSQL.executeUpdate();
    }
}
