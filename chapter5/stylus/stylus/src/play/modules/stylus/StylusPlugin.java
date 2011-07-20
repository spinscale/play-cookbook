package play.modules.stylus;

import java.io.PrintStream;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.cache.Cache;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.vfs.VirtualFile;

public class StylusPlugin extends PlayPlugin {

	StylusCompiler compiler = new StylusCompiler();
	
	@Override
	public void onApplicationStart() {
		Logger.info("Loading stylus plugin");
	}
	
    @Override
    public boolean serveStatic(VirtualFile file, Request request, Response response) {
    	String fileEnding = Play.configuration.getProperty("stylus.suffix", "styl");
    	if(file.getName().endsWith("." + fileEnding)) {
    		response.contentType = "text/css";
    		response.status = 200;
    		try {
    			String key = "stylusPlugin-" + file.getName();
    			String css = Cache.get(key, String.class);
    			if (css == null) {
    				css = compiler.compile(file.getRealFile());
    			}
                
                // Cache in prod mode
                if(Play.mode == Play.Mode.PROD) {
                	Cache.add(key, css, "1h");
                    response.cacheFor(Play.configuration.getProperty("http.cacheControl", "3600") + "s");
                }
                response.print(css);
            } catch(Exception e) {
                response.status = 500;
                response.print("Stylus processing failed\n");
                if (Play.mode == Play.Mode.DEV) {
                	e.printStackTrace(new PrintStream(response.out));
                } else {
                	Logger.error(e, "Problem processing stylus file");
                }
            }
            return true;
        }

        return false;
    }

}
