import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import play.mvc.Http;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.FunctionalTest;
import play.utils.Utils;

public class CachingTest extends FunctionalTest {

    @Test
    public void testThatCachingPagePartsWork() {
        Response response = GET("/");
        String cachedTime = getCachedTime(response);
        assertEquals(getUncachedTime(response), cachedTime);

        response = GET("/");
        String newCachedTime = getCachedTime(response);
        assertNotSame(getUncachedTime(response), newCachedTime);
        assertEquals(cachedTime, newCachedTime);
    }
    
    @Test
    public void testThatCachingWholePageWorks() throws Exception {
    	Response response = GET("/cacheFor");
    	String content = getContent(response);
    	response = GET("/cacheFor");
    	assertEquals(content, getContent(response));
    	Thread.sleep(6000);
    	response = GET("/cacheFor");
    	assertNotSame(content, getContent(response));
    }
    
    @Test
    public void testThatCachingHeadersAreSet() {
    	Response response = GET("/proxyCache");
    	assertIsOk(response);
    	assertHeaderEquals("Cache-Control", "max-age=3600", response);
    }
    
    @Test
    public void testThatEtagCachingWorks() {
    	Response response = GET("/etagCache/123");
    	assertIsOk(response);
    	assertContentEquals("Learn to use etags, dumbass!", response);

    	Request request = newRequest();
    	
    	String etag = String.valueOf("123".hashCode());
    	Header noneMatchHeader =  new Header("if-none-match", etag);
    	request.headers.put("if-none-match", noneMatchHeader);
    	
    	DateTime ago = new DateTime().minusHours(12);
    	String agoStr = Utils.getHttpDateFormatter().format(ago.toDate());
    	Header modifiedHeader = new Header("if-modified-since", agoStr);
    	request.headers.put("if-modified-since", modifiedHeader);
    	
    	response = GET(request, "/etagCache/123");
    	assertStatus(304, response);
    }

    
    private String getUncachedTime(Response response) {
    	return getTime(response, 0);
    }
    
    private String getCachedTime(Response response) {
    	return getTime(response, 1);
    }
    
    private String getTime(Response response, int pos) {
    	assertIsOk(response);
    	String content = getContent(response);
    	return content.split("\n")[pos];
    }
}


