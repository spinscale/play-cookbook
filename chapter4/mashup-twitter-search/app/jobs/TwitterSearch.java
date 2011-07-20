package jobs;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import models.SearchResult;
import play.Play;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Every("10min")
public class TwitterSearch extends Job {

	public void doJob() {
		String url = Play.configuration.getProperty("twitter.query");
		
		if (url == null) {
			return;
		}
		
		JsonElement element = WS.url(url).get().getJson();
		
		if (!element.isJsonObject()) {
			return;
		}
		
		JsonObject jsonObj = (JsonObject) element;

		Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").create();
		
		Type collectionType = new TypeToken<Collection<SearchResult>>(){}.getType();
		Collection<SearchResult> search = gson.fromJson(jsonObj.get("results"), collectionType);
		search = removeDuplicates(search);

		Cache.set("twitterSearch", search);
	}

	private Collection<SearchResult> removeDuplicates(Collection<SearchResult> search) {
		Collection<SearchResult> nonduplicateSearches = new LinkedHashSet();
		Set<String> contents = new HashSet();
		
		for (SearchResult searchResult : search) {
			if (!contents.contains(searchResult.text)) {
				nonduplicateSearches.add(searchResult);
				contents.add(searchResult.text);
			}
		}

		return nonduplicateSearches;
	}
}
