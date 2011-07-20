package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class SuperSecretData extends Model {
	public String secret = "foo";
}
