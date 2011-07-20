import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import models.Car;
import models.User;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import play.modules.csv.CsvHelper;
import play.modules.csv.CsvQuery;
import play.test.Fixtures;
import play.test.UnitTest;

public class CsvTest extends UnitTest {

	private Car c;
	
	@Before
	public void cleanUp() {
		Fixtures.deleteAllModels();
		CsvHelper.clean();
		Fixtures.loadModels("car-data.yml");
		c = Car.findById(1L);
	}
    
	@Test
    public void saveOneEntity() throws Exception {
    	String data = FileUtils.readFileToString(new File("/tmp/Car.csv"));
    	assertEquals("\"1\"\t\"BMW\"\t\"320\"\n", data);
    }

	@Test
	public void saveTwoEntities() throws Exception {
		Car c = new Car();
		c.brand = "VW";
		c.type = "Jetta";
		c.save();
		
		String data = FileUtils.readFileToString(new File("/tmp/Car.csv"));
		String expected = "\"1\"\t\"BMW\"\t\"320\"\n\"2\"\t\"VW\"\t\"Jetta\"\n";
		assertEquals(expected, data);
	}
	
	@Test
	public void saveEntityWithLinkToAnother() throws Exception {
		User u = new User();
		u.name = "alex";
		u.currentCar = c;
		u.save();
		
		String data = FileUtils.readFileToString(new File("/tmp/User.csv"));
		String expected = "\"1\"\t\"alex\"\t\"#Car#1\"\n";
		assertEquals(expected, data);

	}
	
	@Test
	public void deleteSimpleEntity() throws IOException {
		c.delete();
		
		Car c = new Car();
		c.brand = "VW";
		c.type = "Jetta";
		c.save();
		
		String data = FileUtils.readFileToString(new File("/tmp/Car.csv"));
		String expected = "\"2\"\t\"VW\"\t\"Jetta\"\n";
		assertEquals(expected, data);
	}
	
	@Test
	public void deleteOneOfTwoEntities() throws IOException {
		c.delete();
		String data = FileUtils.readFileToString(new File("/tmp/Car.csv"));
		assertEquals("", data);
	}
	
	@Test
	public void readSimpleEntityById() {
		Car car = Car.findById(1L);
		assertValidCar(car, "BMW", "320");
	}
	
	@Test
	public void readComplexEntityWithOtherEntites() {
		User u = new User();
		u.name = "alex";
		u.currentCar = c;
		u.save();
		
		u = User.findById(1L);
		assertNotNull(u);
		assertEquals("alex", u.name);
		assertValidCar(u.currentCar, "BMW", "320");
	}
	
	@Test
	public void checkThatFinderWorks() {
		Car car = Car.find("byBrand", "BMW").first();
		assertValidCar(car, "BMW", "320");
		
		List<Car> cars = Car.find("byBrandAndType", "BMW", "320").fetch();
		assertValidCar(cars.get(0), "BMW", "320");
		
		car = Car.find("byTypeAndBrand", "320", "BMW").first();
		assertValidCar(car, "BMW", "320");
	}
	
	@Test
	public void checkThatLimitAndOffsetWorks() throws Exception {
		new Car("VW", "Passat").save();
		new Car("VW", "Golf").save();
		new Car("VW", "Polo").save();
		new Car("VW", "Phaeton").save();
		new Car("VW", "Scirocco").save();
		new Car("Loremo", "GT").save();
		
		List<Car> cars = Car.find("byBrand", "VW").fetch();
		assertEquals(5, cars.size());
		assertValidCar(cars.get(0), "VW", "Passat");
		assertValidCar(cars.get(1), "VW", "Golf");
		assertValidCar(cars.get(2), "VW", "Polo");
		assertValidCar(cars.get(3), "VW", "Phaeton");
		assertValidCar(cars.get(4), "VW", "Scirocco");
		
		cars = Car.find("byBrand", "VW").fetch(0, 1);
		assertEquals(4, cars.size());
		assertValidCar(cars.get(0), "VW", "Golf");
		assertValidCar(cars.get(1), "VW", "Polo");
		assertValidCar(cars.get(2), "VW", "Phaeton");
		assertValidCar(cars.get(3), "VW", "Scirocco");
		
		cars = Car.find("byBrand", "VW").fetch(3, 0);
		assertEquals(3, cars.size());
		assertValidCar(cars.get(0), "VW", "Passat");
		assertValidCar(cars.get(1), "VW", "Golf");
		assertValidCar(cars.get(2), "VW", "Polo");
		
		cars = Car.find("byBrand", "VW").fetch(4, 2);
		assertEquals(3, cars.size());
		assertValidCar(cars.get(0), "VW", "Polo");
		assertValidCar(cars.get(1), "VW", "Phaeton");
		assertValidCar(cars.get(2), "VW", "Scirocco");
	}
	
	@Test
	public void checkThatFindingAllWorks() {
		new Car("VW", "Passat").save();
		new Car("VW", "Golf").save();
		Map<String, String> emptyMap = Collections.emptyMap();
		CsvQuery query = new CsvQuery(Car.class, emptyMap);
		List<Car> cars = query.fetch();
		assertEquals(3, cars.size());
		assertValidCar(cars.get(0), "BMW", "320");
		assertValidCar(cars.get(1), "VW", "Passat");
		assertValidCar(cars.get(2), "VW", "Golf");
	}

	private void assertValidCar(Car car, String expectedBrand, String expectedType) {
		assertNotNull(car);
		assertEquals(expectedBrand, car.brand);
		assertEquals(expectedType, car.type);
	}
}
