package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Address extends Model {
	public String street;
	public String city;
	public String zip;
}
