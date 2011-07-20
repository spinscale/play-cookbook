package controllers;

import models.Thing;
import play.mvc.Controller;
import static play.modules.api.RenderXml.*;

public class Application extends Controller {

	public static void thing(Thing thing) {
		renderText("foo:"+thing.foo+"|bar:"+thing.bar+"\n");
	}
	
	public static void thingXml(Thing thing) {
		renderXML(thing);
	}

}