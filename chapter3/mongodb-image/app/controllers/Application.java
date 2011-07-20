package controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.mvc.Controller;
import utils.GridFSSerializer;
import utils.GridFsHelper;

import com.mongodb.gridfs.GridFSDBFile;

public class Application extends Controller {

	public static void index() {
		render();
	}
	
	public static void getImages() {
		List<GridFSDBFile> files = GridFsHelper.getFiles();
		Map map = new HashMap();
		map.put("items", files);
		renderJSON(map, new GridFSSerializer());
	}

	public static void storeImage(File image, String description) {
		notFoundIfNull(image);
		try {
			GridFsHelper.storeFile(description, image);
		} catch (IOException e) {
			flash("uploadError", e.getMessage());
		}
		index();
	}

	public static void showImage(String id) {
		GridFSDBFile file = GridFsHelper.getFile(id);
		notFoundIfNull(file);
		renderBinary(file.getInputStream(), file.getFilename(), 
				file.getLength(), file.getContentType(), true);
	}
}