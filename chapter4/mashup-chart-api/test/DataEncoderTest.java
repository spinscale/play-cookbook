import static org.hamcrest.core.Is.is;
import googlechart.DataEncoder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import play.test.UnitTest;

public class DataEncoderTest extends UnitTest {

	@Test
	public void simpleTest() {
		assertThat(DataEncoder.encode(getValuesAsList(0), 2), is("A0"));
		assertThat(DataEncoder.encode(getValuesAsList(0), 4096), is("A0"));
	}
	
	@Test
	public void otherTest() {
		assertThat(DataEncoder.encode(getValuesAsList(3), 3), is("AA"));
	}
	
	@Ignore
	@Test
	public void testThatEncoderWorks() {
		assertThat(DataEncoder.encode(getValuesAsList(0,3,6,9,12,15,18,21,24,27), 30), is("e:"));
	}
	
	private List<Number> getValuesAsList(Number ... numbers) {
		List<Number> numberList = new ArrayList();
		for (Number number : numbers) {
			numberList.add(number);
		}
		return numberList;
	}
}
