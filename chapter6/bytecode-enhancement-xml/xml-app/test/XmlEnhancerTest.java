import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import models.Thing;

import org.junit.Test;

import play.test.UnitTest;

public class XmlEnhancerTest extends UnitTest {

    @Test
    public void testThingEntity() {
    	XmlRootElement xmlRootElem = Thing.class.getAnnotation(XmlRootElement.class);
    	assertNotNull(xmlRootElem);
    	assertEquals("thing", xmlRootElem.name());
    	
    	XmlAccessorType anno = Thing.class.getAnnotation(XmlAccessorType.class);
    	assertNotNull(anno);
    	assertEquals(XmlAccessType.FIELD, anno.value());
    }

}
