package play.modules.searchhelp;

import java.util.Collections;
import java.util.List;

import play.Play;
import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SearchHelperPlugin extends PlayPlugin {

	private SearchHelperEnhancer enhancer = new SearchHelperEnhancer();

	@Override
	public void enhance(ApplicationClass applicationClass) throws Exception {
		enhancer.enhanceThisClass(applicationClass);
	}


	@Override
	public JsonObject getJsonStatus() {
		JsonObject obj = new JsonObject();
		List<ApplicationClass> classes = Play.classes.getAssignableClasses(IndexedModel.class);

		for (ApplicationClass applicationClass : classes) {
			if (isIndexed(applicationClass)) {
				List<String> fieldList = getIndexedFields(applicationClass);
				JsonArray fields = new JsonArray();

				for (String field :fieldList) {
					fields.add(new JsonPrimitive(field));
				}

				obj.add(applicationClass.name, fields);
			}
		}

		return obj;
	}


	@Override
	public String getStatus() {
		String output = "SearchHelperPlugin:\n~~~~~~~~~~~~~~~~~~~\n";

		List<ApplicationClass> classes = Play.classes.getAssignableClasses(IndexedModel.class);

		for (ApplicationClass applicationClass : classes) {
			if (isIndexed(applicationClass)) {
				List<String> fieldList = getIndexedFields(applicationClass);
				output += "Entity " + applicationClass.name + ": " + fieldList +  "\n";
			}
		}

		return output;
	}

	private List<String> getIndexedFields(ApplicationClass applicationClass) {
		try {
			Class clazz = applicationClass.javaClass;
			List<String> fieldList = (List<String>) clazz.getMethod("getIndexedFields").invoke(null);
			return fieldList;
		} catch (Exception e) {}
		
		return Collections.emptyList();
	}
	
	private boolean isIndexed(ApplicationClass applicationClass) {
		try {
			Class clazz = applicationClass.javaClass;
			Boolean isIndexed = (Boolean) clazz.getMethod("isIndexed").invoke(null);
			return isIndexed;
		} catch (Exception e) {}
		
		return false;
	}
}
