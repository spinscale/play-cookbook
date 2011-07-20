package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import static pdf.RenderPDF.renderPDF;;

public class Application extends Controller {

    public static void index() {
    	User user = new User();
        renderPDF(user);
    }
}