package models;

import java.text.MessageFormat;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class OrderItem extends Model {

	@Required public String itemId;
	public Boolean hazardous;
	public Boolean bulk;
	public Boolean toxic;
	public Integer piecesIncluded;
	
	public String toString() {
		return MessageFormat.format("{0}/{1}/{2}/{3}/{4}", itemId, piecesIncluded, bulk, toxic, hazardous);
	}
}
