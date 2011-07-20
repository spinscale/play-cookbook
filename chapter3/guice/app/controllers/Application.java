package controllers;

import guice.EncryptionService;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Controller;

public class Application extends Controller {

	@Inject
	private static DateInjector dater;
	
	@Inject
	private static EncryptionService encService;
	
    public static void decrypt() {
        renderText(encService.decrypt(params.get("text")));
    }

    public static void encrypt() {
        renderText(encService.encrypt(params.get("text")));
    }
    
    public static void date() {
        renderText(dater.getDate());
    }
}