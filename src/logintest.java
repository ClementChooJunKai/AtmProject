
import static org.junit.Assert.*;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/*------------------------------------------------- logintest -----
|  Class logintest
|
|  Purpose: This is the unit testing for the login page
|
|   
|
*-------------------------------------------------------------------*/

public class logintest {

    private main main;

    /*------------------------------------------------- logintest -----
    |  Method testAuthenticateSuccess
    |
    |  Purpose: This unit testing test whether the username and password exist in the database
    |
    |  Method testAuthenticateFailure,testAuthenticateInvalidUsername 
    |   
    |  Purpose: This unit testing test whether a wrong input would work
    |                                                   
    *-------------------------------------------------------------------*/
    @Before
    public void setUp() {
        main = new main();

    }

    @Test
    public void testAuthenticateSuccess() throws SQLException, ClassNotFoundException {
        assertTrue(main.authenticate("stupid", "1234"));
    }

    @Test
    public void testAuthenticateFailure() throws SQLException, ClassNotFoundException {
        assertFalse(main.authenticate("john", "5678"));
    }

    @Test
    public void testAuthenticateInvalidUsername() throws SQLException, ClassNotFoundException {
        assertFalse(main.authenticate("invalidUsername", "1234"));
    }

}
