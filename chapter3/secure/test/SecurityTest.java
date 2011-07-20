import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class SecurityTest extends FunctionalTest {

    @Test
    public void testThatIndexPageNeedsLogin() {
        Response response = GET("/");
        assertStatus(302, response);
        assertLocationRedirect("/login", response);
    }
    
    @Test
    public void testThatUserCanLogin() {
    	loginAs("user");
    	Response response = GET("/");
    	assertContentMatch("Logged in as user", response);
    }

    @Test
    public void testThatUserCannotAccessAdminPage() {
    	loginAs("user");
        Response response = GET("/admin");
        assertStatus(403, response);
    }

    @Test
    public void testThatAdminAccessAdminPage() {
    	loginAs("admin");
    	Response response = GET("/admin");
        assertStatus(302, response);
    }
    
    private void assertLocationRedirect(String location, Response resp) {
    	assertHeaderEquals("Location", "http://localhost"+location, resp);    	
    }
    
    private void loginAs(String user) {
    	Response response = POST("/login?username=" + user + "&password=secret");
    	assertStatus(302, response);
    	assertLocationRedirect("/", response);
    }
}