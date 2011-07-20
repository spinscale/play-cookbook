package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	public String username;
	public String password;
	@ManyToMany
	public Set<Right> rights;
	
	public boolean hasRight(String name) {
		Right r = Right.find("byName", name).first();
		return rights.contains(r);
	}
}
