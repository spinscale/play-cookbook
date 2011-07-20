package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void create(Car car) {
    	car = car.save();
    	renderText("Car with id " + car.id + " and values " + car.toString());
    }
}