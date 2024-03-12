import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/*------------------------------------------------- User -----
|  Class User
|
|  Purpose: This class contains methods and allows the creation of the user object
|
|                                                   
*-------------------------------------------------------------------*/
public class User {
    private int UserID;
    private int PIN;
    private String Username;
    private static String Fullname;
    private boolean admin;
    private String BankName;
    private float BalanceCurrent;
    private float BalanceSavings;
    private int AccountNoCurrent;
    private int AccountNoSavings;
    private static int withdrawLimit;
    private static int transferLimit;
    private boolean newStatus;
    private int BankID;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String Email;

    public int getBankID() {
        return BankID;
    }

    public void setBankID(int bankID) {
        BankID = bankID;
    }

    User(String user) {

        Username = user;

    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int pIN) {
        PIN = pIN;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public static String getFullname() {
        return Fullname;
    }

    public static void setFullname(String fullname) {
        Fullname = fullname;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public float getBalanceCurrent() {
        return BalanceCurrent;
    }

    public void setBalanceCurrent(float balanceCurrent) {
        BalanceCurrent = balanceCurrent;
    }

    public float getBalanceSavings() {
        return BalanceSavings;
    }

    public void setBalanceSavings(float balanceSavings) {
        BalanceSavings = balanceSavings;
    }

    public static int getWithdrawLimit() {
        return withdrawLimit;
    }

    public static void setWithdrawLimit(int withdrawLimit) {
        User.withdrawLimit = withdrawLimit;
    }

    public static int getTransferLimit() {
        return transferLimit;
    }

    public static void setTransferLimit(int transferLimit) {
        User.transferLimit = transferLimit;
    }

    public boolean isNewStatus() {
        return newStatus;
    }

    public void setNewStatus(boolean newStatus) {
        this.newStatus = newStatus;
    }

    public int getAccountNoCurrent() {
        return AccountNoCurrent;
    }

    public void setAccountNoCurrent(int accountNoCurrent) {
        AccountNoCurrent = accountNoCurrent;
    }

    public int getAccountNoSavings() {
        return AccountNoSavings;
    }

    public void setAccountNoSavings(int accountNoSavings) {
        AccountNoSavings = accountNoSavings;
    }

    private static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    // Getting DataBase Name
    private static String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static String MYSQL_DB_USER = "root";
    private static String MYSQL_DB_USER_PASSWORD = "root";

    private static String UserMainmenu = "Select * FROM User WHERE username = ?";

    /*------------------------------------------------- User -----
    |  Method readDB
    |
    |  Purpose: The readDB methods retrieve all the fields in the database based on the username and using the setter method
    |           to set all the fields.
    |
    |                                                   
    *-------------------------------------------------------------------*/
    public void readDB() {
        try (Connection connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_DB_USER, MYSQL_DB_USER_PASSWORD)) {
            Class.forName(MYSQL_JDBC_DRIVER_CLASS);
            PreparedStatement retrieveUserInfo = connection.prepareStatement(UserMainmenu);

            retrieveUserInfo.setString(1, getUsername());

            ResultSet userinfoRS = retrieveUserInfo.executeQuery();
            while (userinfoRS.next()) {
                Fullname = userinfoRS.getString("fullName");
                admin = userinfoRS.getBoolean("admin");

                BalanceSavings = userinfoRS.getFloat("BalanceSavings");
                BalanceCurrent = userinfoRS.getFloat("BalanceCurrent");
                withdrawLimit = userinfoRS.getInt("withdrawLimit");
                transferLimit = userinfoRS.getInt("transferLimit");
                UserID = userinfoRS.getInt("UserID");
                BankID = userinfoRS.getInt("bankID");
                AccountNoCurrent = userinfoRS.getInt("AccountNoCurrent");
                AccountNoSavings = userinfoRS.getInt("AccountNoSavings");
                Email = userinfoRS.getString("Email");
                setAdmin(admin);
                setBalanceCurrent(BalanceCurrent);
                setBalanceSavings(BalanceSavings);
                setWithdrawLimit(withdrawLimit);
                setEmail(Email);
                setTransferLimit(transferLimit);
                setFullname(Fullname);
                setUserID(UserID);
                setBankID(BankID);
                setAccountNoCurrent(AccountNoCurrent);
                setAccountNoSavings(AccountNoSavings);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver class not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error occured while executing query: " + UserMainmenu);
            e.printStackTrace();
        }

    }

}
