package controllers;

import java.util.List;

import models.OrderItem;
import models.OrderItemPojo;
import play.Logger;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
    	List<OrderItem> items = OrderItem.findAll();
        render(items);
    }
    
    public static void createOrderJpa(OrderItem item) {
    	Logger.error("PArams: " + params);
    	Logger.error("VAL: " + validation.errors());
    	Logger.error("item: " + item);
       	if (validation.hasErrors()) {
    		render("@index");
    	}
       	item.save();
    	renderText(item.toString());
    }
    
    public static void createOrder(OrderItemPojo item) {
    	Logger.error("PArams: " + params);
    	Logger.error("VAL: " + validation.errors());
    	Logger.error("item: " + item);
       	if (validation.hasErrors()) {
    		render("@index");
    	}
    	renderText(item.toString());
    }

}