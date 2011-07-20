package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class Registration extends Model {

	public String uuid;
	@OneToOne
	public User user;
	public Date createdAt = new Date();
}
