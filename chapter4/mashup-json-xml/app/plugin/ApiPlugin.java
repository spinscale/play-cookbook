package plugin;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.mvc.Http.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class ApiPlugin extends PlayPlugin {
	
	public static JAXBContext jc;
	private Gson gson;

	public void onLoad() {
		Logger.info("ApiPlugin loaded");
		try {
			List<ApplicationClass> applicationClasses = Play.classes.getAnnotatedClasses(XmlRootElement.class);
			List<Class> classes = new ArrayList<Class>();
			for (ApplicationClass applicationClass : applicationClasses) {
				classes.add(applicationClass.javaClass);
			}
			jc = JAXBContext.newInstance(classes.toArray(new Class[]{}));
		} catch (JAXBException e) {
			Logger.error(e, "Problem initializing jaxb context: %s", e.getMessage());
		}
		gson = new GsonBuilder().create();				

	}
	
	public Object bind(String name, Class clazz, Type type, Annotation[] annotations, Map<String, String[]> params) {
		String contentType = Request.current().contentType;

		if ("application/json".equals(contentType)) {
			return getJson(clazz, name);
		} else if ("application/xml".equals(contentType)) {
			return getXml(clazz);
		}

		return null;
	}
	
	private Object getXml(Class	clazz) {
		try {
			if (clazz.getAnnotation(XmlRootElement.class) != null) {
				Unmarshaller um = jc.createUnmarshaller();
				StringReader sr = new StringReader(Request.current().params.get("body"));
				return um.unmarshal(sr);
			}
		} catch (JAXBException e) {
			Logger.error("Problem rendering XML: %s", e.getMessage());
		}
		return null;
	}
	
	private Object getJson(Class clazz, String name) {
		try {
			String body = Request.current().params.get("body");
			JsonElement jsonElem = new JsonParser().parse(body);
			if (jsonElem.isJsonObject()) {
				JsonObject json = (JsonObject) jsonElem;
				
				if (json.has(name)) {
					JsonObject from = json.getAsJsonObject(name);
					Object result = gson.fromJson(from, clazz);
					return result;
				}
			}
		} catch (Exception e) {
			Logger.error("Problem rendering JSON: %s", e.getMessage());
		}
		return null;
	}

}
