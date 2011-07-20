package controllers;

import java.util.Date;
import java.util.UUID;

import play.modules.router.Any;
import play.modules.router.Post;
import play.modules.router.Put;
import play.modules.router.ServeStatic;
import play.modules.router.StaticRoutes;
import play.mvc.Controller;

@StaticRoutes({
	@ServeStatic(value="/public/", directory="public")
})
public class Application extends Controller {

	@Put(value="/", priority=2)
    public static void hiddenIndex() {
        renderText("Secret news here");
    }

	@Any(value="/", priority=100)
    public static void index() {
        forbidden("Reserved for administrator");
    }

    @Post("/ticket")
    public static void getTicket(String username, String password) {
    	String uuid = UUID.randomUUID().toString();
    	renderJSON(uuid);
    }
}
