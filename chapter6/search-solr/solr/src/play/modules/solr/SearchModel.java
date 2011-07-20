package play.modules.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.beans.Field;

import play.db.jpa.Model;

public class SearchModel extends Model {

	public static Query search(String query, String ... values) {
        throw new UnsupportedOperationException("Check your configuration. Bytecode enhancement did not happen");
	}
	
	protected static Query search(Class clazz, String query, String ... values) {
		StringBuilder sb = new StringBuilder();
		if (query.startsWith("by")) {
			query = query.replaceAll("^by", "");
		}
		String fieldNames[] = query.split("And");
		
		for (int i = 0 ; i < fieldNames.length; i++) {
			String fieldStr = fieldNames[i];
			String value = values[i];
			
			String fieldName = StringUtils.uncapitalize(fieldStr);
			String solrFieldName = getSolrFieldName(fieldName, clazz);
			
			sb.append(solrFieldName);
			sb.append(":");
			sb.append(value);
			
			if (i < fieldNames.length-1) {
				sb.append(" AND ");
			}
		}
		
		return new Query(sb.toString(), clazz);
	}
	
	private static String getSolrFieldName(String fieldName, Class clazz) {
		try {
			java.lang.reflect.Field field = clazz.getField(fieldName);
			Field annot = field.getAnnotation(Field.class);
			if (annot != null && !annot.value().equals("#default")) {
				return annot.value();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldName;
	}
}
