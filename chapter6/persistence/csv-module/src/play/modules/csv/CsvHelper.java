package play.modules.csv;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.Play;
import sun.security.action.GetLongAction;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CsvHelper {

	private static ConcurrentHashMap<Class, AtomicLong> ids = new ConcurrentHashMap<Class, AtomicLong>();
	private static ConcurrentHashMap<Class, ReentrantLock> locks = new ConcurrentHashMap<Class, ReentrantLock>();
	private static ConcurrentHashMap<Class, CsvHelper> helpers = new ConcurrentHashMap<Class, CsvHelper>();
	private static final char separator = '\t';
	private Class clazz;
	private File dataFile;
	
	private CsvHelper(Class clazz) {
		this.clazz = clazz;
		File dir = new File(Play.configuration.getProperty("csv.path", "/tmp"));
		this.dataFile = new File(dir, clazz.getSimpleName() + ".csv");
		
		locks.put(clazz, new ReentrantLock());
		ids.put(clazz, getMaxId());
	}

	public static CsvHelper getCsvHelper(Class clazz) {
		if (!helpers.containsKey(clazz)) {
			helpers.put(clazz, new CsvHelper(clazz));
		}
		return helpers.get(clazz);
	}

	public static void clean() {
		helpers.clear();
		locks.clear();
		ids.clear();
	}

	public <T> List<T> findByExample(Map<String, String> fieldMap, int limit, int offset) {
		List<T> results = new ArrayList<T>();
		try {
			Map<String, Integer> fieldPositions = new HashMap<String, Integer>();
			fieldPositions.put("id", 0);
			for (int i = 0 ; i < clazz.getFields().length ; i++) {
				String fieldName = clazz.getFields()[i].getName();
				if (!fieldName.equals("id")) {
					fieldPositions.put(fieldName, i+1); // Always add one due to the id field in the array
				}
			}
			
			List<String[]> entries = getEntriesFromFile();
			int countOffset = 0;
			int countLimit = 0;
			for (String[] entry : entries) {
				boolean isMatched = true;
				// Check every defined field in fieldMap, if it matches
				for (Entry<String, String> queryField : fieldMap.entrySet()) {
					Integer fieldPosition = fieldPositions.get(queryField.getKey());
					String entryFieldContent = entry[fieldPosition];
					
					if (!entryFieldContent.equals(queryField.getValue())) {
						isMatched = false;
					}
				}
				
				if (isMatched) {
					countOffset++;
					// though we matched, we are not in specified range, and simply go on
					if (offset >= countOffset) {
						continue;
					}
					
					results.add((T) createObjectFromArray(entry));
					
					countLimit++;
					if (limit > 0 && countLimit >= limit) {
						return results;
					}
				}
			}
		} catch (Exception e) {
			Logger.error(e, "Error in csv helper");
		}
		
		return results;
	}

	public <T extends CsvModel> void delete(T model) {
		if (model._key() == null) {
			return;
		}

		try {
			File out = File.createTempFile(model.getClass().getSimpleName(), null);
			CSVWriter writer = new CSVWriter(new FileWriter(out), separator);

			List<String[]> entries = getEntriesFromFile();
			for (String[] fields : entries) {
				if (!fields[0].equals(model._key().toString())) {
					writer.writeNext(fields);
				}
			}

			writer.close();
			moveDataFile(out);
		} catch (Exception e) {
			Logger.error(e, "Error in csv helper");
		}
	}

	public void deleteAll() {
		getLock();
		FileUtils.deleteQuietly(dataFile);
		ids.get(clazz).set(0);
		releaseLock();
	}

	public <T extends CsvModel> T findById(Long id) {
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("id", id.toString());
		List<T> results = findByExample(fieldMap, 1, 0);
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	public synchronized <T extends CsvModel> T save(T model) {
		try {
			File out = File.createTempFile(model.getClass().getSimpleName(), null);
			CSVWriter writer = new CSVWriter(new FileWriter(out), separator);

			if (dataFile.exists()) {
				List<String[]> myEntries = getEntriesFromFile();

				for (String[] fields : myEntries) {
					// update it
					if (fields[0].equals(model._key())) {
						String[] arrayObject = createArrayFromObject(model, model._key().toString());
						if (arrayObject != null) {
							writer.writeNext(arrayObject);
						}
					} else {
						writer.writeNext(fields);
					}
				}
			}

			// insert it if it was not updated...
			if (model._key() == null) {
				Long id = getNewId();
				String[] arrayObject = createArrayFromObject(model, id.toString());
				writer.writeNext(arrayObject);
				model.id = id;
			}

			writer.close();

			moveDataFile(out);
		} catch (Exception e) {
			Logger.error(e, "Error in csv helper");
		}

		return model;
	}

	private List<String[]> getEntriesFromFile() throws IOException {
		if (!dataFile.exists()) {
			return new ArrayList<String[]>();
		}
		CSVReader reader = new CSVReader(new FileReader(dataFile), separator);
		List<String[]> entries = reader.readAll();
		reader.close();
		return entries;
	}

	private <T extends CsvModel> String[] createArrayFromObject(T model, String id) throws IllegalArgumentException, IllegalAccessException {
		LinkedHashMap result = new LinkedHashMap<String, String>();

		// Insert the id first
		result.put("id", id);

		for (Field field : model.getClass().getFields()) {
			String fieldName = field.getName();

			if (fieldName.equals("id")) {
				continue;
			}

			// If is is a CsvModel field write special notation
			if (CsvModel.class.isAssignableFrom(field.getType())) {
				String name = field.getType().getSimpleName();
				Object modelObj = field.get(model);
				if (modelObj != null) {
					CsvModel csvModel = (CsvModel) modelObj;
					if (csvModel._key() != null) {
						String key = csvModel._key().toString();
						result.put(fieldName, "#" + name + "#" + key);
					}
				}
			} else {
				String content = (String) field.get(model);
				result.put(fieldName, content);
			}
		}

		return (String[]) result.values().toArray(new String[]{});
	}

	private <T extends CsvModel> T createObjectFromArray(String[] obj) throws InstantiationException, IllegalAccessException {
		T model = (T) clazz.newInstance();
		model.id = Long.valueOf(obj[0]);

		for (int i = 1; i < obj.length; i++) {
			Object value = obj[i];
			Field field = model.getClass().getFields()[i-1]; // the id is private and does not count as field

			if (CsvModel.class.isAssignableFrom(field.getType())) {
				String entity = value.toString().split("#")[1];
				Long entityId = Long.valueOf(value.toString().split("#")[2]);
				Class modelClazz = Play.classes.getApplicationClass("models." + entity).javaClass;
				value = getCsvHelper(modelClazz).findById(entityId);
			}

			field.set(model, value);
		}

		return model;
	}

	private void getLock() {
		// Get or create lock
		if (!locks.containsKey(clazz)) {
			locks.put(clazz, new ReentrantLock());
		}
		ReentrantLock lock = locks.get(clazz);
		lock.lock();
	}
	
	private void releaseLock() {
		if (locks.get(clazz).isLocked()) {
			locks.get(clazz).unlock();
		}
	}
	
	private synchronized void moveDataFile(File file) {
		getLock();

		FileUtils.deleteQuietly(dataFile);
		try {
			FileUtils.moveFile(file, dataFile);
		} catch (IOException e) {
			Logger.error(e, "Error in csv helper");
		} finally {
			releaseLock();
		}
	}

	private Long getNewId() {
		return ids.get(clazz).addAndGet(1);
	}

	private AtomicLong getMaxId() {
		AtomicLong result = new AtomicLong(0);
		try {
			for (String[] entry : getEntriesFromFile()) {
				Long val = Long.valueOf(entry[0]);
				if (val > result.get()) {
					result.set(val);
				}
			}
		} catch (Exception e) {
			Logger.error(e, "Error in csv helper");
		}
		return result;
	}


}
