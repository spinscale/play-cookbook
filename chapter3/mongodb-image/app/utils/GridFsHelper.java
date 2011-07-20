package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;

import play.Play;
import play.modules.morphia.MorphiaPlugin;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFsHelper {

	public static GridFSDBFile getFile(String id) {
		GridFSDBFile file = getGridFS().findOne(new ObjectId(id));
		return file;
	}
	
	public static List<GridFSDBFile> getFiles() {
		return getGridFS().find(new BasicDBObject());
	}

	public static void storeFile(String title, File image) throws IOException {
		GridFS fs = getGridFS();
		fs.remove(image.getName()); // delete the old file
		GridFSInputFile gridFile = fs.createFile(image);
		gridFile.save();
		gridFile.setContentType("image/" + FilenameUtils.getExtension(image.getName()));
		gridFile.setFilename(image.getName());
		gridFile.put("title", title);
		gridFile.save();
	}

	private static GridFS getGridFS() {
		String collection = Play.configuration.getProperty("morphia.db.collection.upload", "uploads");
		GridFS fs = new GridFS(MorphiaPlugin.ds().getDB(), collection);
		return fs;
	}
}
