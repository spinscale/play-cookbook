package controllers;

import play.mvc.With;
import models.Merchant;
import controllers.CRUD.For;

@With(Secure.class)
public class Merchants extends CRUD {

	@Check("admin")
    public static void delete(String id) {
    	CRUD.delete(id);
    }
	
	@Check("user")
	public static void create() throws Exception {
		CRUD.create();
	}
}
