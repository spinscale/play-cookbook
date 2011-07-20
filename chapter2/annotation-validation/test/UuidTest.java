import java.util.UUID;

import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class UuidTest extends FunctionalTest {

    @Test
    public void testThatValidUuidWorks() {
    	String uuid = UUID.randomUUID().toString();
        Response response = GET("/" + uuid);
        assertIsOk(response);
        assertContentEquals(uuid + " is valid", response);
    }

    @Test
    public void testThatInvalidUuidWorksNot() {
        Response response = GET("/absolutely-No-UUID");
        assertStatus(500, response);
    }

}