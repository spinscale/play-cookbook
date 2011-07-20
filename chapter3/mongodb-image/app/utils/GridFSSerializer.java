package utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.mvc.Router;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.mongodb.gridfs.GridFSDBFile;

public class GridFSSerializer implements JsonSerializer<GridFSDBFile> {

	@Override
	public JsonElement serialize(GridFSDBFile file, Type type,
			JsonSerializationContext ctx) {
		String url = createUriForFile(file);
		JsonObject obj = new JsonObject();
		obj.addProperty("thumb", url);
		obj.addProperty("large", url);
		obj.addProperty("title", (String)file.get("title"));
		obj.addProperty("link", url);

		return obj;
	}

	private String createUriForFile(GridFSDBFile file) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", file.getId().toString());
		return Router.getFullUrl("Application.showImage", map);
	}
}
