package models;

import javax.persistence.Entity;

import play.modules.search.Field;
import play.modules.search.Indexed;
import play.modules.searchhelp.IndexedModel;

@Entity
@Indexed
public class User extends IndexedModel {

	@Field
	public String name;
	@Field
	public String descr;
}
