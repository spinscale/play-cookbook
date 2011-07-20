package jobs;

import java.util.HashMap;
import java.util.Map;

import play.inject.BeanSource;
import play.inject.Injector;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.EtagCacheCalculator;

@OnApplicationStart
public class InjectionJob extends Job implements BeanSource {
	
	private static final Map<Class, Object> clazzMap = new HashMap<Class, Object>();

	public void doJob() {
		clazzMap.put(EtagCacheCalculator.class, new EtagCacheCalculator());
		Injector.inject(this);
	}
	
	public <T> T getBeanOfType(Class<T> clazz) {
		return (T) clazzMap.get(clazz);
	}
}
