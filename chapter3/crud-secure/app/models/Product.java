package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
public class Product extends Model {

	public String name;
	@Lob
	public String description;
	
	@ManyToOne
	public Merchant merchant;
	
	@Temporal(TemporalType.DATE)
	public Date createdAt;
	
	public String toString() {
		return name;
	}
}
