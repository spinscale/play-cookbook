package models;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import play.db.jpa.Model;

@Entity
@XmlRootElement(name="thing")
@XmlAccessorType(XmlAccessType.FIELD)
public class Thing extends Model {
	public String foo;
	public String bar;
	
	public String toString() {
		return "foo " + foo + " / bar " + bar;
	}
}
