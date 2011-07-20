import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class EncryptionTest extends FunctionalTest {

    @Test
    public void testThatDecryptionWorks() {
        Response response = GET("/decrypt?text=foo");
        assertIsOk(response);
        assertContentEquals("Doof", response);
    }

    @Test
    public void testThatEncryptionWorks() {
        Response response = GET("/encrypt?text=oof");
        assertIsOk(response);
        assertContentEquals("Efoo", response);
    }

}