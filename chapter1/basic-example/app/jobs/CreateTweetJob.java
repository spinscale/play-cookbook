package jobs;

import java.util.Calendar;

import models.Tweet;
import models.User;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("1mn")
public class CreateTweetJob extends Job {

	public void doJob() {
		User user = User.find("byLogin", "alr").first();
		Tweet t = new Tweet();
		t.user = user;
		t.content = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(140));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, RandomUtils.nextInt(10000));
		t.postedAt = cal.getTime(); 
		t.save();
		Logger.debug("Created tweet at: %s", t.postedAt);
	}
}
