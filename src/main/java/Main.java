import Controller.SocialMediaController;
import DAO.AccountDAO;
import Service.SocialMediaService;
import Util.ConnectionUtil;
import Model.Account;
import io.javalin.Javalin;

import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    static AccountDAO dao;
    static AccountDAO mockDAO;
    static SocialMediaService service;

    static void setUpTest() {
        dao = new AccountDAO();
        mockDAO = Mockito.mock(AccountDAO.class);
        service = new SocialMediaService(mockDAO);
    }

    static void testRegisterUser() {
        // Test user registration
        Account newAcc = new Account("username", "pass");
        Account persistedAccount = new Account(2, "username", "password");
        Mockito.when(mockDAO.addUser(newAcc)).thenReturn(persistedAccount);
        Mockito.when(mockDAO.exists(newAcc.username)).thenReturn(false);
        Account actualAccount = service.registerAccount(newAcc);

        if (actualAccount != null) {
            System.out.println(actualAccount.getAccount_id());
        } else {
            System.out.println("null");
        }
    }

    static void testRegForReal() {
        ConnectionUtil.resetTestDatabase();
        Account newAcc = new Account("username", "pass");

        try {
            Account actualAccount = dao.addUser(newAcc);

            if (actualAccount != null) {
                System.out.println(actualAccount.getAccount_id());
            } else {
                System.out.println("null");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Woah");
        }
    }


    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        //Javalin app = controller.startAPI();
        //app.start(8080);

        testRegForReal();
    }
}
