package utils;

import play.classloading.enhancers.ControllersEnhancer.ControllerSupport;

public class EtagCacheCalculator implements ControllerSupport {

	public String calculate(String str) {
		return String.valueOf(str.hashCode());
	}
}
