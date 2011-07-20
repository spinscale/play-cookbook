package play.modules.api;

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
	private XmlEnhancer enhancer = new XmlEnhancer();

	@Override
	public void onApplicationStart() {
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
		Logger.info("ApiPlugin loaded");
	}
	
    public void enhance(ApplicationClass applicationClass) throws Exception {
    	enhancer.enhanceThisClass(applicationClass);
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
			Unmarshaller um = jc.createUnmarshaller();

			if (clazz.getAnnotation(XmlRootElement.class) != null) {
				String body;
				if (Request.current().params._contains("body")) {
					body = Request.current().params.get("body");
				} else {
					body = IOUtils.toString(Request.current().body);
				}
				StringReader sr = new StringReader(body);
				return um.unmarshal(sr);
			}
		} catch (Exception e) {
			Logger.error(e, "Problem rendering XML: %s", e.getMessage());
		}
		return null;
	}
	
	private Object getJson(Class clazz, String name) {
		try {
			String data = IOUtils.toString(Request.current().body);
			JsonElement jsonElem = new JsonParser().parse(data);
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
