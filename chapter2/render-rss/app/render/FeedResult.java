package render;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Post;
import play.Play;
import play.exceptions.UnexpectedException;
import play.mvc.Router;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Result;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

public class FeedResult extends Result {

	private List<Post> posts;
	private String format;
	
	public FeedResult(String format, List<Post> posts) {
		this.posts = posts;
		this.format = format;
	}
	
	public static void renderFeedRss(List<Post> posts) {
		throw new FeedResult("rss", posts);
	}

	public static void renderFeedRss2(List<Post> posts) {
		throw new FeedResult("rss2", posts);
	}

	public static void renderFeedAtom(List<Post> posts) {
		throw new FeedResult("atom", posts);
	}

	public void apply(Request request, Response response) {
        try {
    		SyndFeed feed = new SyndFeedImpl();
    		feed.setAuthor(Play.configuration.getProperty("rss.author"));
    		feed.setTitle(Play.configuration.getProperty("rss.title"));
    		feed.setDescription(Play.configuration.getProperty("rss.description"));
    		feed.setLink(getFeedLink());

    		List<SyndEntry> entries = new ArrayList<SyndEntry>();
    		for (Post post : posts) {
    			String url = createUrl("Application.showPost", "id", post.id.toString());
    			SyndEntry entry = createEntry(post.title, url, post.content, post.createdAt);
    			entries.add(entry);
    		}
    		
    		feed.setEntries(entries);
    		
    		feed.setFeedType(getFeedType());
    		setContentType(response);
    		
    		SyndFeedOutput output = new SyndFeedOutput();
    		String rss = output.outputString(feed);
            response.out.write(rss.getBytes("utf-8"));
		} catch (Exception e) {
            throw new UnexpectedException(e);
		}	
	}
	
	private SyndEntry createEntry (String title, String link, String description,
			Date createDate) {
		SyndEntry entry = new SyndEntryImpl();
		entry.setTitle(title);
		entry.setLink(link);
		entry.setPublishedDate(createDate);
		
		SyndContent entryDescription = new SyndContentImpl();
		entryDescription.setType("text/html");
		entryDescription.setValue(description);
		
		entry.setDescription(entryDescription);
		
		return entry;
	}
	
	private void setContentType(Response response) {
		if ("rss2".equals(format) || "rss".equals(format)) {
			response.contentType = "application/rss+xml; charset=utf-8";
		} else {
			response.contentType = "application/atom+xml; charset=utf-8";
		}
	}

	private String getFeedType() {
		if ("rss2".equals(format)) {
			return "rss_2.0";
		} else if ("rss".equals(format)) {
			return "rss_1.0";
		}
		return "atom_0.3";
	}
	
	private String getFeedLink(){
		if ("rss2".equals(format)) {
			return Router.getFullUrl("Application.renderRss2");
		} else if ("rss".equals(format)) {
			return Router.getFullUrl("Application.renderRss");
		}
		return Router.getFullUrl("Application.renderAtom");
	}

	private String createUrl(String controller, String key, String value) {
		Map args = new HashMap();
		args.put(key, value);
		return Router.getFullUrl(controller, args);
	}
}
