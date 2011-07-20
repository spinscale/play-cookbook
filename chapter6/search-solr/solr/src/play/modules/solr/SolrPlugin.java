package play.modules.solr;

import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.db.jpa.Model;

public class SolrPlugin extends PlayPlugin {

	/*
	 * <requestHandler name="/update/javabin" class="solr.BinaryUpdateRequestHandler" />
	 */

	private SolrEnhancer enhancer = new SolrEnhancer();
	
	@Override
    public void enhance(ApplicationClass applicationClass) throws Exception {
		enhancer.enhanceThisClass(applicationClass);
    }

	@Override
	public void onApplicationStart() {
		Logger.info("Solr plugin started");
	}


	@Override
	public void onApplicationStop() {
		Logger.info("Solr plugin stopped");
	}

	@Override
	public void onEvent(String message, Object context) {
		if (!StringUtils.startsWith(message, "JPASupport.")) {
			return;
		}
		
		try {
			Model model = (Model) context;
			String entityId = model.getClass().getName() + ":" + model.getId().toString();
			
			SolrServer server = getSearchServer();
			server.deleteById(entityId);
			
//			if ("JPASupport.objectPersisted".equals(message) || "JPASupport.objectUpdated".equals(message)) {
			if ("JPASupport.objectUpdated".equals(message)) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", entityId);
				doc.addField("searchClass", model.getClass().getName());
				
				for (java.lang.reflect.Field field : context.getClass().getFields()) {
					String fieldName = field.getName();
					Field annot = field.getAnnotation(Field.class);
					if (annot == null) {
						continue;
					}
					
					String annotationValue = annot.value();
					if (annotationValue != null && !"#default".equals(annotationValue)) {
						fieldName = annotationValue;
					}
					
					doc.addField(fieldName, field.get(context));
				}
				server.add(doc);
				Logger.info("Added entity %s with fields %s on event %s", model.getClass().getSimpleName(), doc.getFieldNames() ,message);
			}
			server.commit();
		} catch (Exception e) {
			Logger.error(e, "Problem updating entity %s on event %s with error %s", context, message, e.getMessage());
		}
	}
	
	public static SolrServer getSearchServer() {
		String url = Play.configuration.getProperty("solr.server", "http://localhost:8983/solr");
		CommonsHttpSolrServer server = null;
		try {
			server = new CommonsHttpSolrServer( url );
			server.setRequestWriter(new BinaryRequestWriter());
		} catch (MalformedURLException e) {
			Logger.error(e, "Problem creating solr server object: %s", e.getMessage());
		}
		return server;
	}
}
