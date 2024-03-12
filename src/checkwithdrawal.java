/*------------------------------------------------- checkwithdrawal -----
|  Class checkwithdrawal
|
|  Purpose: To check if there is still withdrawlimit for the user to withdraw,If not we will return a exception
|
|   
|
*-------------------------------------------------------------------*/
public class checkwithdrawal extends Exception {
    private float amount;

    public checkwithdrawal(float amount) {
        this.amount = amount;
    }

    public double getAmount() {

        float amount = (this.amount) * -1;
        return amount;
    }
}
