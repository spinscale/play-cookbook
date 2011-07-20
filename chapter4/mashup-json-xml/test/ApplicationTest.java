import org.junit.*;

import play.Logger;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

	public String expectedResult = "foo:first|bar:second\n";
		
    @Test
    public void testThatParametersWork() {
        Response response = POST("/thing?thing.foo=first&thing.bar=second");
        assertIsOk(response);
        assertContentType("text/plain", response);
        assertContentMatch(expectedResult, response);
    }
    
    @Test
    public void testThatXmlWorks() {
    	String xml = "<thing><foo>first</foo><bar>second</bar></thing>";
    	Response response = POST("/thing", "application/xml", xml);
    	assertIsOk(response);
    	assertContentType("text/plain", response);
    	Logger.info("GOT CONTENT %s", response.out.toString());
    	assertContentMatch(expectedResult, response);
    }
    
    @Test
    public void testThatJsonWorks() {
    	String json = "{ thing : { \"foo\" : \"first\", \"bar\" : \"second\" } }";
    	Response response = POST("/thing", "application/json", json);
    	assertIsOk(response);
    	assertContentType("text/plain", response);
    	Logger.info("GOT CONTENT JSON%s", response.out.toString());
    	assertContentMatch(expectedResult, response);
    }
    
}