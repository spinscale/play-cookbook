package play.modules.searchhelp;

import java.util.Collections;
import java.util.List;

import play.db.jpa.Model;

public abstract class IndexedModel extends Model {

	public static Boolean isIndexed() {
		return false;
	}
	
	public static List<String> getIndexedFields() {
		return Collections.emptyList();
	}
}
