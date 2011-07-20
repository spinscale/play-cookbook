package models;

import java.util.Date;
import javax.persistence.Entity;
import play.data.validation.Max;
import play.db.jpa.Model;

@Entity
public class Tweet extends Model {
	@Max(140) public String content;
	public Date postedAt;
	public User user;
}
