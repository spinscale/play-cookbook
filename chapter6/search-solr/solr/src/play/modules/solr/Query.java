package play.modules.solr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import play.Logger;
import play.data.binding.Binder;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.exceptions.UnexpectedException;

public class Query {

	private SolrQuery query;
	private SolrServer server;
	private Class clazz;

	public <T extends Model> Query(String queryString, Class<T> clazz) {
		query = new SolrQuery();
		query.setFilterQueries("searchClass:" + clazz.getName());
		query.setQuery(queryString);
		this.server = SolrPlugin.getSearchServer();
		this.clazz = clazz;
	}
	
	public Query limit(int limit) {
		query.setRows(limit);
		return this;
	}
	
	public Query start(int start) {
		query.setStart(start);
		return this;
	}
	
	public List<String> fetchIds() {
		query.setFields("id");
		SolrDocumentList results = getResponse();
		List<String> ids = new ArrayList(results.size());
		for (SolrDocument doc : results) {
			String entityId = doc.getFieldValue("id").toString().split(":")[1];
			ids.add(entityId);
		}
		
		return ids;
	}
	
	public <T extends Model> List<T> fetch() {
		List<T> result = new ArrayList<T>();
		
		List<String> ids = fetchIds();
		for (String id : ids) {
			Object objectId = getIdValueFromIndex(clazz, id);
			result.add((T) JPA.em().find(clazz, objectId));
		}

		return result;
	}
	
	private SolrDocumentList getResponse() {
		try {
			QueryResponse rp = server.query(query);
			return rp.getResults();
		} catch (SolrServerException e) {
			Logger.error(e, "Error on solr query: %s", e.getMessage());
		}
		
		return new SolrDocumentList();
	}
	
    private Object getIdValueFromIndex(Class<?> clazz, String indexValue) {
        java.lang.reflect.Field field = getIdField(clazz);
        Class<?> parameter = field.getType();
        try {
            return Binder.directBind(indexValue, parameter);
        } catch (Exception e) {
            throw new UnexpectedException("Could not convert the ID from index to corresponding type", e);
        }
    }

    private java.lang.reflect.Field getIdField(Class<?> clazz) {
        for (java.lang.reflect.Field field : clazz.getFields()) {
            if (field.getAnnotation(Id.class) != null) {
                return field;
            }
        }
        throw new RuntimeException("Your class " + clazz.getName()
                        + " is annotated with javax.persistence.Id but the field Id was not found");
    }


}
