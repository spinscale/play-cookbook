package models;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.EqualsBuilder;

import play.db.jpa.Model;

@Entity
public class Right extends Model {
	
	@Column(unique=true)
	public String name;
}
