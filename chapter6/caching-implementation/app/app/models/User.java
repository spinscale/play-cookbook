package models;

import java.util.Date;

import javax.persistence.Entity;

import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	public User(String name) {
		this.name = name;
	}

	public User() {
	}

	public String name;
	public Boolean b;
	public boolean c;
	public Integer i;
	public int j;
	public long l;
	public Long k;

	@Required
	@As("dd/MM/yyyy")
	public Date birth;

	@Email
	public String email;

	public String toString() {
		return name;
	}

	public static String yip() {
		return "YIP";
	}

}
