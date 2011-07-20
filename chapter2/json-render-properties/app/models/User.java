package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.google.gson.annotations.SerializedName;

import annotations.NoJsonExport;

import play.data.binding.NoBinding;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	@SerializedName("userLogin")
	public String login;
	@NoJsonExport
	public String password;
	@ManyToOne
	public Address address;
	@OneToOne
	public SuperSecretData secrets;
	
	public String toString() {
		return id + "/" +  login;
	}
}
