import org.junit.Test;

import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class UserRightTest extends FunctionalTest {

    @Test
    public void testSecretsWork() {
    	login("user", "user");
    	Response response = GET("/secret");
        assertIsOk(response);
        assertContentEquals("This is secret", response);
    }

    @Test
    public void testSecretsAreDeniedForUnknownUser() {
    	Response response = GET("/secret");
        assertStatus(404, response);
    }

    @Test
    public void testSuperSecretsAreAllowedForAdmin() {
    	login("admin", "admin");
    	Response response = GET("/top-secret");
        assertIsOk(response);
        assertContentEquals("This is top secret", response);
    }

    @Test
    public void testSecretsAreDeniedForUser() {
    	login("user", "user");
    	Response response = GET("/top-secret");
        assertStatus(403, response);
    }

    private void login(String user, String pass) {
    	String data = "username=" + user + "&password=" + pass;
    	Response response = POST("/login", 
    			APPLICATION_X_WWW_FORM_URLENCODED, data);
    	assertIsOk(response);
    }
}