package controllers;

import play.modules.spring.Spring;
import play.mvc.Controller;
import spring.EncryptionService;

public class Application extends Controller {

    public static void decrypt() {
    	EncryptionService encService = Spring.getBeanOfType(EncryptionService.class);
        renderText(encService.decrypt(params.get("text")));
    }

    public static void encrypt() {
    	EncryptionService encService = Spring.getBeanOfType(EncryptionService.class);
        renderText(encService.encrypt(params.get("text")));
    }

}