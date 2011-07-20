package controllers;

import play.*;

import static render.RenderXml.*;

import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static void thing(Thing thing) {
		renderText("foo:"+thing.foo+"|bar:"+thing.bar+"\n");
	}
	
	public static void thingXml(Thing thing) {
		renderXML(thing);
	}
	
}