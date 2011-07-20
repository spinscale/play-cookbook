package models;

import java.math.BigDecimal;

import javax.persistence.Entity;

import play.modules.searchhelp.IndexedModel;

@Entity(name="orders")
public class Order extends IndexedModel {

	public String title;
	public BigDecimal amount;
}
