import java.util.HashMap;

import models.User;

import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;

import com.google.gson.Gson;

public class JsonRenderTest extends FunctionalTest {

    @Test
    public void testThatJsonRenderingWorks() {
        Response response = GET("/user/1");
        assertIsOk(response);
        
        User user = new Gson().fromJson(getContent(response), User.class);
        assertNotNull(user);
        assertNull(user.password);
        assertNull(user.secrets);
        assertEquals(user.login, "alex");
        assertEquals(user.address.city, "Munich");
        assertContentMatch("\"uri\":\"/user/1\"", response);
    }
}