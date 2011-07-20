package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class Post extends Model {

	public String author;
	public String title;
	public Date createdAt;
	public String content;
	
	public static List<Post> findLatest() {
		return Post.findLatest(20);
	}
	
	public static List<Post> findLatest(int limit) {
		return Post.find("order by createdAt DESC").fetch(limit);
	}
}
