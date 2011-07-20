package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class User extends Model {

	@Required
	@Column(unique=true)
	public String name;
	@Required
	public String password;
	public String quote;
	
	public String toString() {
		return name;
	}
}
