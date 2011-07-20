package models;

import javax.persistence.Entity;

import org.apache.solr.client.solrj.beans.Field;

import play.modules.solr.SearchModel;

@Entity
public class User extends SearchModel {

	@Field
	public String name;
	@Field
	public String shortDescription;
	@Field("tw_s")
	public String twitter;

	public Car car;
}
