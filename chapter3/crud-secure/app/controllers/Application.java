package controllers;

import java.util.List;

import models.Product;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
    	List<Product> products = Product.find("order by createdAt desc").fetch(10);
        render(products);
    }

}