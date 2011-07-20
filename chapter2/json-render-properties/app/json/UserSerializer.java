package json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import models.SuperSecretData;
import models.User;
import play.mvc.Router;
import annotations.NoJsonExport;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class UserSerializer implements JsonSerializer<User> {
	
	public JsonElement serialize(User user, Type type,
			JsonSerializationContext context) {
		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new LocalExclusionStrategy())
			.create();
		
		JsonElement elem = gson.toJsonTree(user);
		elem.getAsJsonObject().addProperty("uri", createUriForUser(user.id));
		return elem;
	}
	
	private String createUriForUser(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		return Router.reverse("Application.showUser", map).url;
	}


	public static class LocalExclusionStrategy implements ExclusionStrategy {
		public boolean shouldSkipClass(Class<?> clazz) {
			return clazz == SuperSecretData.class;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(NoJsonExport.class) != null;
		}
	}
}