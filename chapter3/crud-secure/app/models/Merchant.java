package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Merchant extends Model {

	public String name;
	
	public String toString() {
		return name;
	}
}
