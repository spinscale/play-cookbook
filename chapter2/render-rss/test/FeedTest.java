import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.InputSource;

import play.mvc.Http.Response;
import play.test.FunctionalTest;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;

public class FeedTest extends FunctionalTest {

    @Test
    public void testThatRss10Works() throws Exception {
        Response response = GET("/feed/posts.rss");
        assertIsOk(response);
        assertContentType("application/rss+xml", response);
        assertCharset("utf-8", response);
        SyndFeed feed = getFeed(response);
        assertEquals("rss_1.0", feed.getFeedType());
    }

    @Test
    public void testThatRss20Works() throws Exception {
        Response response = GET("/feed/posts.rss2");
        assertIsOk(response);
        assertContentType("application/rss+xml", response);
        assertCharset("utf-8", response);
        SyndFeed feed = getFeed(response);
        assertEquals("rss_2.0", feed.getFeedType());
    }

    @Test
    public void testThatAtomWorks() throws Exception {
        Response response = GET("/feed/posts.atom");
        assertIsOk(response);
        assertContentType("application/atom+xml", response);
        assertCharset("utf-8", response);
        SyndFeed feed = getFeed(response);
        assertEquals("atom_0.3", feed.getFeedType());
    }
    
    private SyndFeed getFeed(Response response) throws Exception {
        SyndFeedInput input = new SyndFeedInput();
        InputSource s = new InputSource(IOUtils.toInputStream(getContent(response))); 
        return input.build(s);
    }

}