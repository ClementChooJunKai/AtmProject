
/*------------------------------------------------- TransactionTempValues -----
 |  class TransactionTempValues
 |
 |  Purpose: This class contains the getter and setter methods to be used for the transactionhistory tableview
 |                                                   
 *-------------------------------------------------------------------*/

public class TransactionTempValues {

    private int TransactionNo;
    private int userID;
    private String Username;

    private String remark;
    private int CheqNo;
    private String transactionDate;
    private String transactionType;
    private String accountType;
    private String FromUserName;
    private String ToUserName;
    private int amount;

    public TransactionTempValues(int transactionNo, int userID, String remark, int cheqNo, String transactionDate,
            String transactionType, String accountType, String fromUserName, String toUserName, int amount) {
        TransactionNo = transactionNo;
        this.userID = userID;
        this.remark = remark;
        CheqNo = cheqNo;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.accountType = accountType;
        FromUserName = fromUserName;
        ToUserName = toUserName;
        this.amount = amount;
    }

    public String toString() {
        return "TransactionNo='" + TransactionNo + '\'' + ", UserID ='" + userID + '\'' + ", Remark =" + remark + '\''
                + ", CheqNo=" + CheqNo + '\'' + ", transactionDate=" + transactionDate + '\'' + ", transactionType="
                + transactionType + '\'' + ", FromUserName=" + FromUserName + '\'' + ", ToUserName=" + ToUserName + '\''
                + ", amount=" + amount + '\'' + ", accountType=" + accountType;
    }

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

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
