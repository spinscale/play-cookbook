package play.modules.csv;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.transaction.NotSupportedException;

import org.apache.commons.lang.StringUtils;

import play.db.Model;

public abstract class CsvModel implements Model {

	public Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Object getId() {
		return id;
	}
	
	@Override
	public Object _key() {
		return getId();
	}

	@Override
	public void _save() {
		save();
	}

	@Override
	public void _delete() {
		delete();
	}
	
	public void delete() {
		CsvHelper helper = CsvHelper.getCsvHelper(this.getClass());
		helper.delete(this);
	}
	
	public <T extends CsvModel> T save() {
		CsvHelper helper = CsvHelper.getCsvHelper(this.getClass());
		return (T) helper.save(this);
	}

	public static <T extends CsvModel> T findById(Long id) {
		throw new UnsupportedOperationException("No bytecode enhancement?");
	}

	public static <T extends CsvModel> CsvQuery find(String query, Object ... fields) {
		throw new UnsupportedOperationException("No bytecode enhancement?");
	}

	protected static <T extends CsvModel> CsvQuery find(Class<T> clazz, String query, Object ... fields) {
		String[] fieldNames = query.replaceAll("^by", "").split("And");
		Map<String, String> fieldMap = new LinkedHashMap<String, String>();
		try {
			for (int i = 0 ; i < fields.length ; i++) {
				String fieldName = StringUtils.uncapitalize(fieldNames[i]);
				if (CsvModel.class.isAssignableFrom(clazz.getField(fieldName).getType())) {
					CsvModel model = (CsvModel) fields[i];
					String id = "#" + fields[i].getClass().getSimpleName() + "#" + model._key();
					fieldMap.put(fieldName, id);
				} else {
					fieldMap.put(fieldName, fields[i].toString());
				}
			}
			
			return new CsvQuery(clazz, fieldMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected static <T extends CsvModel> T findById(Class<T> clazz, Long id) {
		CsvHelper helper = CsvHelper.getCsvHelper(clazz);
		return (T) helper.findById(id);
	}
}
