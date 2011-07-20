package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	public String name;
	@Email
	public String email;
	
	public Boolean active;
}
