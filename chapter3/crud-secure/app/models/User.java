package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class User extends Model {

	@Column(unique=true)
	public String user;
	public String password;
	public Boolean isAdmin;
	
	public void setPassword(String clear) {
		password = Crypto.passwordHash(clear);
	}
	
	public String toString() {
		return user;
	}
}
