/*------------------------------------------------- DataSingleton -----
|  Class DataSingleton
|
|  Purpose: This class is to set BankID,UserID,Username,Email as global variable accross the whole project
|           we can call the class again and use the get method to use it at any class
|
|   
|
*-------------------------------------------------------------------*/

public class DataSingleton {

    private static final DataSingleton instance = new DataSingleton();
    private int bankID;
    private int UserID;
    private String userName;
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return instance;
    }

}
