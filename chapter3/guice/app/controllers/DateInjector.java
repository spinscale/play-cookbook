package controllers;

import java.util.Date;

import play.classloading.enhancers.ControllersEnhancer.ControllerSupport;

public class DateInjector implements ControllerSupport {

	public Date getDate() {
		return new Date();
	}
}
