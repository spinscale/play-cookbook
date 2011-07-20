import java.io.IOException;
import java.util.List;

import models.Car;
import models.User;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import play.db.jpa.JPABase;
import play.db.jpa.Model;
import play.modules.solr.Query;
import play.test.Fixtures;
import play.test.UnitTest;

public class SolrSearchTest extends UnitTest {

	CommonsHttpSolrServer server;
	
	@Before
	public void setup() throws Exception {
		Fixtures.deleteAllModels();
		
		server = new CommonsHttpSolrServer("http://localhost:8983/solr");
		server.setRequestWriter(new BinaryRequestWriter());
		clearSolrServerIndex();
		
		Fixtures.loadModels("test-data.yml");
	}
	
	private void clearSolrServerIndex() throws Exception {
		server.deleteByQuery( "*:*" );
		server.commit();
	}

    @Test
    public void simpleUserTest() throws Exception {
    	SolrQuery query = new SolrQuery();
		query.setQuery("name:alex");
		QueryResponse rp = server.query(query);
		
		SolrDocumentList results = rp.getResults();
    	assertEquals(1, results.size());
    	assertEquals("alex", results.get(0).getFieldValue("name"));
    	
    	User u = User.find("byName", "alex").first();
    	assertEquals(u.getClass().getName() + ":" + u.id.toString(), results.get(0).getFieldValue("id"));
    }
    
    @Test
    public void testCarsWithDynamicSetup() throws Exception {
    	Car c = Car.find("byBrand", "BMW").first();
    	assertNotNull(c);
    	
    	SolrQuery query = new SolrQuery();
		query.setQuery("brand_s:BMW");
		QueryResponse rp = server.query(query);
		
		SolrDocumentList results = rp.getResults();
    	assertEquals(1, results.size());
    	assertEquals("BMW", results.get(0).getFieldValue("brand_s"));
    	assertEquals(c.getClass().getName() + ":" + c.id.toString(), results.get(0).getFieldValue("id"));
    }
    
    @Test
    public void testQuery() {
    	List<User> users = new Query("name:alex", User.class).fetch();
    	assertEquals(1, users.size());
    	assertEquals("alex", users.get(0).name);
    }

    @Test
    public void testQueryResultIds() {
    	User u = User.find("byName", "alex").first();
    	List<String> users = new Query("name:alex", User.class).fetchIds();
    	assertEquals(u.id.toString(), users.get(0));
    }
	
	@Test
	public void ensureDeletionWorks() throws Exception {
		List<Car> cars = Car.findAll();
		for (Car car : cars) {
			car.delete();
		}
		assertEquals(0, new Query("brand_s:*", Car.class).fetchIds().size());
	}

    @Test
    public void testPagination() throws Exception {
    	Car.deleteAll();
    	clearSolrServerIndex();
    	
    	Fixtures.loadModels("test-data-many-cars.yml");
    	List<Model> result = new Query("brand_s:*", Car.class).limit(10).start(10).fetch();
    	assertEquals(3, result.size());
    }
    
    @Test
    public void testOffset() throws Exception {
    	Car.deleteAll();
    	clearSolrServerIndex();
    	
    	Fixtures.loadModels("test-data-many-cars.yml");
    	List<Model> result = new Query("brand_s:*", Car.class).limit(5).start(0).fetch();
    	assertEquals(5, result.size());
    	result = new Query("brand_s:*", Car.class).limit(5).start(5).fetch();
    	assertEquals(5, result.size());
    	result = new Query("brand_s:*", Car.class).limit(5).start(10).fetch();
    	assertEquals(3, result.size());
    }
    
    @Test
    public void testEnhancedSearchCapability() {
    	assertEquals(1, Car.search("byBrandAndType", "BMW", "320").fetchIds().size());
    	
    	List<User> users = User.search("byNameAndTwitter", "a*ex", "spinscale*").fetch();
    	User user = users.get(0);
    	User u1 = User.find("byName", "alex").first();
    	assertEquals(user.id, u1.id);
    }
}
