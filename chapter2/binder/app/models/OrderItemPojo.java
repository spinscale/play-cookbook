package models;

import java.text.MessageFormat;

import play.data.validation.Required;

public class OrderItemPojo {

	@Required public String itemId;
	public Boolean hazardous;
	public Boolean bulk;
	public Boolean toxic;
	public Integer piecesIncluded;
	
	public String toString() {
		return MessageFormat.format("{0}/{1}/{2}/{3}/{4}", itemId, piecesIncluded, bulk, toxic, hazardous);
	}
}