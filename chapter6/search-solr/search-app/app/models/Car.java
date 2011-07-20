package models;

import javax.persistence.Entity;

import org.apache.solr.client.solrj.beans.Field;

import play.modules.solr.SearchModel;

@Entity
public class Car extends SearchModel {

	@Field("brand_s")
	public String brand;
	@Field("type_s")
	public String type;
}
