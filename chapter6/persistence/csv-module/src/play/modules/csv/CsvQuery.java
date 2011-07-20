package play.modules.csv;

import java.util.List;
import java.util.Map;

public class CsvQuery {

	private int limit = 0;
	private int offset = 0;
	private CsvHelper helper;
	private Map<String, String> fieldMap;

	public CsvQuery(Class clazz, Map<String, String> fieldMap) {
		this.helper = CsvHelper.getCsvHelper(clazz);
		this.fieldMap = fieldMap;
	}

	public CsvQuery limit (int limit) {
		this.limit = limit;
		return this;
	}

	public CsvQuery offset (int offset) {
		this.offset = offset;
		return this;
	}

	public <T extends CsvModel> T first() {
		List<T> results = fetch(1,0);
		if (results.size() > 0) {
			return (T) fetch(1, 0).get(0);
		}
		return null;
	}

	public <T> List<T> fetch() {
		return fetch(limit, offset);
	}

	public <T> List<T> fetch(int limit, int offset) {
		return helper.findByExample(fieldMap, limit, offset);
	}

}
