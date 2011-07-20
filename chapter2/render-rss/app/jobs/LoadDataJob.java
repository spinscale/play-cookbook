package jobs;

import java.util.Date;

import models.Post;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class LoadDataJob extends Job {

	// Create random posts
	public void doJob() {
		for (int i = 0 ; i < 100 ; i++) {
			Post post = new Post();
			post.author = "Alexander Reelsen";
			post.title = RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(50));
			post.content = RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(500));
			post.createdAt = new Date(new Date().getTime() + RandomUtils.nextInt(Integer.MAX_VALUE));
			post.save();
		}
	}
}
