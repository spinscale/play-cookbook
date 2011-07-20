package controllers;

import play.*;
import play.mvc.*;

import java.net.URL;
import java.util.*;

import org.apache.log4j.PropertyConfigurator;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

}