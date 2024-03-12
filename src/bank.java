import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class bank {
    private int bankID;
    private String bankName;
    private Float exchange;
    private String currency;
    private static final String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static final String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static final String MYSQL_DB_USER = "root";
    private static final String MYSQL_DB_USER_PASSWORD = "root";

    private static final String BankQuery = "SELECT * FROM bank";

    bank() {

    }

    bank(int bankID, String bankName, Float exchange, String currency) {
        this.bankID = bankID;
        this.bankName = bankName;
        this.exchange = exchange;
        this.currency = currency;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Float getExchange() {
        return exchange;
    }

    public void setExchange(Float exchange) {
        this.exchange = exchange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /*------------------------------------------------- Bank -----
    |  Method readBankDB
    |
    |  Purpose: To call the database and load all the contents of the banks in the database
    |           this includes the bankName,exchangeRate,Currency Type.
    |
    |   Output: The readBankDB method would then output all the contents into a Arraylist of bank
    |
    *-------------------------------------------------------------------*/

    public ArrayList<bank> readBankDB() {
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);

            PreparedStatement retrieveBankInfoSQL = connection.prepareStatement(BankQuery);
            ResultSet bankinfoRS = retrieveBankInfoSQL.executeQuery();
            ArrayList<bank> banks = new ArrayList<bank>();
            int i = 0;
            while (bankinfoRS.next()) {
                banks.add(new bank(i + 1, bankinfoRS.getString("BankName"), bankinfoRS.getFloat("exchangeRate"),
                        bankinfoRS.getString("currency")));
            }
            return banks;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing query: " + BankQuery);
            e.printStackTrace();
            return null; // or return an empty ArrayList instead
        }
        return null; // this line is not necessary since the method already has a return statement
    }
}