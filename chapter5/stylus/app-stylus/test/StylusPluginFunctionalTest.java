import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import play.Play;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class StylusPluginFunctionalTest extends FunctionalTest {

	// If this would work, this would be the way to go for the test
	// However public resources are not accessible in test mode
//    @Test
//    public void testThatIndexPageWorks() throws Exception {
//        Response response = GET("/public/test.styl");
//        assertIsOk(response);
//        assertContentType("text/html", response);
//        assertCharset("utf-8", response);
//        
//        String content = FileUtils.readFileToString(Play.getFile("public/test.styl"));
//        assertNotSame(content, getContent(response));
//        System.out.println("FOOOOOOO " + getContent(response));
//        assertTrue(getContent(response).length() > 0);
//    }
    
}