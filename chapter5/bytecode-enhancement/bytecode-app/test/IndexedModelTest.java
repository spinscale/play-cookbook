import models.Order;
import models.User;

import org.junit.Test;

import play.test.UnitTest;

public class IndexedModelTest extends UnitTest {

    @Test
    public void testThatUserIsIndexed() {
    	assertTrue(User.isIndexed());
    	assertTrue(User.getIndexedFields().contains("name"));
    	assertTrue(User.getIndexedFields().contains("descr"));
    	assertEquals(2, User.getIndexedFields().size());
    }

    @Test
    public void testThatOrderIndexDoesNotExist() {
    	assertFalse(Order.isIndexed());
    	assertEquals(0, Order.getIndexedFields().size());
    }
}
