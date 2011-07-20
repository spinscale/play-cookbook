package jobs;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;

import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

@OnApplicationStart
public class Log4jWatcherJob extends Job {

	public void doJob() {
        String log4jPath = Play.configuration.getProperty("application.log.path", "/log4j.xml");
        VirtualFile file = VirtualFile.open(log4jPath);
        if (!file.exists()) { // try again with the .properties
            log4jPath = Play.configuration.getProperty("application.log.path", "/log4j.properties");
            file = VirtualFile.open(log4jPath);
        }
        
        if (file.exists()) {
        	PropertyConfigurator.configureAndWatch(file.relativePath());
        }
	}
}
