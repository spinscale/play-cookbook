package controllers;

import models.Customer;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
    	Customer customer = new Customer();
        render(customer);
    }

}